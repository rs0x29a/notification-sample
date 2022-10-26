package ru.rs0x29a.sample.notification.route.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.StringWriter
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.springframework.stereotype.Component
import ru.rs0x29a.sample.notification.route.domain.NotificationCreateRequest
import ru.rs0x29a.sample.notification.route.domain.NotificationHeader
import ru.rs0x29a.sample.notification.route.domain.NotificationToAdapter
import ru.rs0x29a.sample.notification.route.domain.NotificationType

@Component
class NotificationRoute : RouteBuilder() {

  override fun configure() {
    from("direct:notification")
      .routeId("route-notification")
      .process(TemplateSelectProcessor())
      .process {
        it.`in`.headers = mapOf<String, Any>(
          SQS_ATTRIBUTE_NOTIFICATION_TYPE to it.`in`.getHeader(NotificationHeader.NOTIFICATION_TYPE_HEADER_NAME.value).toString()
        )
        it.`in`.body = ObjectMapper().writeValueAsString(it.`in`.body)
      }
      .log("Putting a task on a notification queue -> \${headers} : \${body}")
      .to(SQS_URI_NOTIFICATION)

    from(SQS_URI_NOTIFICATION)
      .routeId("route-notification-create")
      .log("Notification found -> \${headers} : \${body}")
      .choice()
      .`when`(header("NotificationType").isEqualTo(NotificationType.SMS))
      .process { it.`in`.body = ObjectMapper().writeValueAsString(it.`in`.body) }
      .log("Notification send -> SMS: \${body}")
      .to(SQS_URI_NOTIFICATION_SMS)
      .otherwise()
      .log("Unknown notification type")
      .end()
  }

  class TemplateSelectProcessor : Processor {
    override fun process(exchange: Exchange) {
      val notification = exchange.getIn().getBody(NotificationCreateRequest::class.java)

      val adapter: NotificationToAdapter = when (notification.type) {
        NotificationType.SMS -> {
          NotificationToAdapter(
            notification.phone,
            SUBJECT_SMS,
            getMessageFromTemplate(notification)
          )
        }
      }

      exchange.`in`.body = adapter
    }

    private fun getMessageFromTemplate(notification: NotificationCreateRequest): String {
      val velocityEngine = VelocityEngine()
      velocityEngine.setProperty(Velocity.RESOURCE_LOADERS, "class")
      velocityEngine.setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
      velocityEngine.init()

      val context = VelocityContext()
      context.put(ENTRY_MARK_IN_TEMPLATE, notification)

      val writer = StringWriter()

      val template = when (notification.type) {
        NotificationType.SMS -> velocityEngine.getTemplate(PATH_TEMPLATE_SMS)
      }
      template.merge(context, writer)

      return writer.toString().trim()
    }
  }

  companion object {
    const val SQS_URI_NOTIFICATION = "aws2-sqs://arn:aws:sqs:eu-west-1:239120060208:notification?messageAttributeNames=All"
    const val SQS_URI_NOTIFICATION_SMS = "aws2-sqs://arn:aws:sqs:eu-west-1:239120060208:notification-sms"

    const val SQS_ATTRIBUTE_NOTIFICATION_TYPE = "NotificationType"

    const val PATH_TEMPLATE_SMS = "templates/SMS.vm"
    const val ENTRY_MARK_IN_TEMPLATE = "root"

    const val SUBJECT_SMS = "qib.group"
  }
}
