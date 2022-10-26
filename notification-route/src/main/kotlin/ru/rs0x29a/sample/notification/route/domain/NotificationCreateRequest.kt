package ru.rs0x29a.sample.notification.route.domain

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "notification-create-request", description = "notification request")
data class NotificationCreateRequest(
  @field:Schema(
    required = true,
    description = "NotificationType",
    example = "SMS"
  )
  val type: NotificationType,

  @field:Schema(
    required = true,
    description = "email",
    example = "email"
  )
  val email: String,

  @field:Schema(
    required = true,
    description = "phone",
    example = "phone"
  )
  val phone: String,

  @field:Schema(
    required = false,
    description = "person",
    example = "person"
  )
  val person: Map<String, String>
)
