package ru.rs0x29a.sample.notification.route.infrastructure

import io.swagger.v3.oas.annotations.Operation
import org.apache.camel.ProducerTemplate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.rs0x29a.sample.notification.route.domain.NotificationHeader
import ru.rs0x29a.sample.notification.route.domain.NotificationCreateRequest

@RequestMapping("/notification")
@RestController
class NotificationController(
  private val producerTemplate: ProducerTemplate
) {

  @Operation(
    operationId = "create-notification",
    summary = "Создание задачи на отправку уведомления",
    description = "Создание задачи на отправку уведомления в формате JSON в очередь SQS",
    method = "createNotification",
    tags = ["notification-create"]
  )
  @PostMapping("create")
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun createNotification(@RequestBody request: NotificationCreateRequest) {
    producerTemplate.asyncRequestBodyAndHeader(
      "direct:notification",
      request,
      NotificationHeader.NOTIFICATION_TYPE_HEADER_NAME.value,
      request.type
    )
  }
}
