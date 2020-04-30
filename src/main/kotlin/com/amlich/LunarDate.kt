package com.amlich

data class LunarDate(val year: Int, val month: Int, val day: Int, val leap: Boolean) {
    override operator fun equals(other: Any?): Boolean {
        val o = other as LunarDate
        return o.year == year && o.month == month && o.day == day && o.leap == leap
    }
}