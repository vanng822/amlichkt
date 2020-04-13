package com.amlich

import java.time.LocalDateTime

var TimeZoneOffset = 7

fun today(): VNDate {
    return VNDate(LocalDateTime.now(), TimeZoneOffset)
}