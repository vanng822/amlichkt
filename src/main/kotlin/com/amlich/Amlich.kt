package com.amlich

import java.time.ZoneId

var TimeZoneOffset = 7
const val VNTimeZoneName = "Asia/Ho_Chi_Minh"
val VNTimeZone = ZoneId.of(VNTimeZoneName)

fun today(): VNDate {
    return VNDate(TimeZoneOffset)
}