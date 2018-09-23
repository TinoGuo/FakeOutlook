package me.tino.fakeoutlook.model

/**
 * the data class of the calendar
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 20:06.
 */
data class DayItem(
    val offset: Int,
    val month: String,
    private var _selected: Boolean
) {
    var selected: Boolean
        get() = _selected
        set(value) {
            _selected = value
        }
}