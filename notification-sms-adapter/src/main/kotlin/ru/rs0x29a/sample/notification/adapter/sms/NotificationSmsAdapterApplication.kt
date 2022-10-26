package ru.rs0x29a.sample.notification.adapter.sms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
  scanBasePackages = [
    "ru.rs0x29a.sample.notification.adapter.sms"
  ]
)
@ConfigurationPropertiesScan
class NotificationSmsAdapterApplication

fun main(args: Array<String>) {
  runApplication<NotificationSmsAdapterApplication>(*args)
}
