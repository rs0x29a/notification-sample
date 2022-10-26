package ru.rs0x29a.sample.notification.route

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
  scanBasePackages = [
    "ru.rs0x29a.sample.notification.route"
  ]
)
@ConfigurationPropertiesScan
class NotificationRouteApplication

fun main(args: Array<String>) {
  runApplication<NotificationRouteApplication>(*args)
}
