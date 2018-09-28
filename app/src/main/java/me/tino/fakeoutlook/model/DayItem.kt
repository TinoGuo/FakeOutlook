package me.tino.fakeoutlook.model

/**
 * the data class of the calendar
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 20:06.
 */
data class DayItem(
    val dayOfMonth: Int,
    val month: Int,     //from 0 to 11
    val year: Int,
    val offsetFromMin: Int  //equal to the position in the calendar
) {
    override fun equals(other: Any?): Boolean {
        return when(other) {
            !is DayItem -> false
            else -> this === other ||
                    (dayOfMonth == other.dayOfMonth && month == other.month && year == other.year)
        }
    }

    override fun hashCode(): Int {
        var result = dayOfMonth
        result = 31 * result + month
        result = 31 * result + year
        result = 31 * result + offsetFromMin
        return result
    }
}