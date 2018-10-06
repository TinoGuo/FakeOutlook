package me.tino.fakeoutlook.model

import me.tino.fakeoutlook.annotation.EventType
import java.util.*

/**
 * the data class of the agenda event
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 22:09.
 */
data class AgendaSubEvent(
    val dayItem: DayItem,
    val hour: Int = 0,
    val min: Int = 0,
    val type: EventType? = null,
    val lastTime: Int = 0,
    val location: String? = null,
    val title: String? = null,
    var weatherSectionInfo: List<WeatherSectionInfo>? = null
) {
    val anyEvent: Boolean
        get() = title?.isNotEmpty() == true

    companion object {
        //build in location array.
        private val INTERNAL_LOCATIONS = arrayOf("Ruby's Coffee", "Gimme Coffee", null)
        //build in title array
        private val INTERNAL_TITLE = arrayOf("Team Dinner", "Coffee with Ada", "Meeting Review")

        /**
         * create a empty event
         * @param dayItem DayItem   the related date
         * @return AgendaSubEvent   without any event
         */
        fun createEmptyEvent(dayItem: DayItem): AgendaSubEvent {
            return AgendaSubEvent(dayItem)
        }

        /**
         * crate a build in event
         * @param dayItem DayItem   the related date
         * @param random Random     random generate
         * @return AgendaSubEvent   with build in property event
         */
        fun createRandomEvent(dayItem: DayItem, random: Random = Random()): AgendaSubEvent {
            return AgendaSubEvent(
                dayItem,
                random.nextInt(24),
                random.nextInt(60),
                if (random.nextBoolean()) EventType.COFFEE else EventType.DINNER,
                random.nextInt(2) + 1,
                INTERNAL_LOCATIONS[random.nextInt(INTERNAL_LOCATIONS.size)],
                INTERNAL_TITLE[random.nextInt(INTERNAL_TITLE.size)]
            )
        }
    }
}