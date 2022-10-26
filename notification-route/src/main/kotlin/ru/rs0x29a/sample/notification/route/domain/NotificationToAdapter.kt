package ru.rs0x29a.sample.notification.route.domain

data class NotificationToAdapter(
  val to: String,
  val subject: String,
  val text: String,
)
