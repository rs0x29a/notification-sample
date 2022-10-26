package ru.rs0x29a.sample.notification.adapter.sms.infrastructure

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JsonLibrary
import org.springframework.stereotype.Component
import ru.rs0x29a.sample.notification.adapter.sms.domain.NotificationToAdapter

@Component
class NotificationSmsAdapterRoute : RouteBuilder() {

  override fun configure() {
    from(SQS_URI_NOTIFICATION_SMS)
      .routeId("route-notification-sms-adapter")
      .log("Task received for notification processing : \${body}")
      .unmarshal().json(JsonLibrary.Jackson, NotificationToAdapter::class.java)
      .setHeader("CamelHttpMethod", constant("POST"))
      .setHeader("Content-Type", constant("application/json;charset=UTF-8"))
      .setHeader("X-Service-Key", constant(SMS_SERVICE_KEY))
      .setBody(
        simple(
          """
          {
            "sender": "${"\${body.subject}"}",
            "receiver": "${"\${body.to}"}",
            "text": "${"\${body.text}"}"
          }
          """.trimIndent()
        )
      )
      .to(SMS_URI)
  }

  companion object {
    const val SQS_URI_NOTIFICATION_SMS = "aws2-sqs://arn:aws:sqs:eu-west-1:239120060208:notification-sms"
    const val SMS_URI = "https://api.bytehand.com/v2/sms/messages"

    const val SMS_SERVICE_KEY = "l01LYAr50UAOPJawZdSpISGKKKOhxLNn"
  }
}
