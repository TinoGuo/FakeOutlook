package me.tino.fakeoutlook.annotation

/**
 * the sealed class to bound type of AgendaSubEvent
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 21:44.
 */
sealed class EventType {

    object COFFEE : EventType()

    object DINNER : EventType()
}