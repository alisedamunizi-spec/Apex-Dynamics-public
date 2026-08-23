package com.example.model

data class GameTime(
    val year: Long = 1970L,
    val month: Int = 1,
    val day: Int = 1,
    val hour: Int = 12,
    val minute: Int = 0,
    val second: Int = 0,
    val speed: GameSpeed = GameSpeed.X1,
    val currentEra: HistoricalEra = HistoricalEra.fromYear(1970L)
) {
    fun formatFullDateTime(): String {
        val yearStr = if (year < 0) "${-year} a.C." else if (year >= 10000) "+$year d.C." else "$year d.C."
        val timeStr = String.format("%02d:%02d:%02d", hour, minute, second)
        val dateStr = String.format("%02d/%02d", day, month)
        return "$dateStr/$yearStr • $timeStr"
    }

    fun formatYearOnly(): String {
        return if (year < 0) "${-year} a.C." else "$year d.C."
    }

    fun tick(deltaSeconds: Int = 1): GameTime {
        if (speed == GameSpeed.PAUSE) return this

        val multiplier = speed.multiplier
        var newSecond = second + (deltaSeconds * multiplier).toInt()
        var newMinute = minute
        var newHour = hour
        var newDay = day
        var newMonth = month
        var newYear = year

        // If speed is very high (e.g. >= 1000x), advance months or years directly
        if (multiplier >= 1000000L) {
            newYear += (multiplier / 100000L) * deltaSeconds
            return copy(
                year = newYear,
                currentEra = HistoricalEra.fromYear(newYear)
            )
        } else if (multiplier >= 10000L) {
            newYear += (multiplier / 1000L) * deltaSeconds
            return copy(
                year = newYear,
                currentEra = HistoricalEra.fromYear(newYear)
            )
        } else if (multiplier >= 500L) {
            newMonth += (multiplier / 100L).toInt() * deltaSeconds
            while (newMonth > 12) {
                newMonth -= 12
                newYear += 1
            }
            return copy(
                year = newYear,
                month = newMonth,
                currentEra = HistoricalEra.fromYear(newYear)
            )
        }

        while (newSecond >= 60) {
            newSecond -= 60
            newMinute += 1
        }
        while (newMinute >= 60) {
            newMinute -= 60
            newHour += 1
        }
        while (newHour >= 24) {
            newHour -= 24
            newDay += 1
        }
        while (newDay > 30) {
            newDay -= 30
            newMonth += 1
        }
        while (newMonth > 12) {
            newMonth -= 12
            newYear += 1
        }

        return copy(
            year = newYear,
            month = newMonth,
            day = newDay,
            hour = newHour,
            minute = newMinute,
            second = newSecond,
            currentEra = HistoricalEra.fromYear(newYear)
        )
    }
}
