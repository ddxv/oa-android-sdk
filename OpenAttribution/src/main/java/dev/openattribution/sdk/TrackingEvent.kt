package dev.openattribution.sdk

data class TrackingEvent(
    val oaUid: String,
    val ifa: String?,
    val androidId: String,
    val eventId: String,
    val eventUid: String,
    val eventTime: Long,
    val customProperties: Map<String, Any>? = null
)

