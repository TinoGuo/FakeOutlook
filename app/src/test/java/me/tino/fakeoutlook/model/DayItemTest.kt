package me.tino.fakeoutlook.model

import org.junit.Assert
import org.junit.Test

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 October 07, 21:47.
 */
class DayItemTest {
    @Test
    fun equalTest() {
        val item1 = DayItem(1, 1, 2018, 0)
        val item2 = DayItem(1,2, 2018, 0)
        val item3 = item1.copy()

        Assert.assertNotEquals(item1, item2)
        Assert.assertEquals(item1, item3)
    }
}