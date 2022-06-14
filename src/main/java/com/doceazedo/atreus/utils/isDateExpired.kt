package com.doceazedo.atreus.utils

import com.doceazedo.atreus.Atreus
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

val timezone: Long = Atreus.instance!!.config.getLong("timezone")

fun isDateExpired(dateString: String): Boolean {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date: Date = format.parse(dateString)
    val now = Date.from(Instant.now().minus(timezone, ChronoUnit.HOURS))
    return date.before(now)
}
