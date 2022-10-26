package ru.rs0x29a.sample.notification.adapter.sms.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NotificationToAdapter(
  @JsonProperty("to")
  var to: String,
  @JsonProperty("subject")
  var subject: String,
  @JsonProperty("text")
  var text: String,
)
