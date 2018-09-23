package me.tino.fakeoutlook.model

import me.tino.fakeoutlook.annotation.EventType
import java.util.*

/**
 * the data class of the agenda event
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 22:09.
 */
data class AgendaSubEvent(
    val id: String,
    val date: Date,
    val hour: Int,
    val min: Int,
    val type: EventType,
    val lastTime: Int,
    val location: String,
    val title: String?
) {
    val anyEvent: Boolean
        get() = title.isNullOrEmpty()
}