package com.chelseatroy.canary.data

import org.junit.Assert
import org.junit.Test

class PastimeProfileTest {
    @Test
    fun formatForView_putsPastimeClassInString_withEmptyDefault() {
        Assert.assertEquals(
            "EXERCISE",
            PastimeClass.formatForView(PastimeClass("EXERCISE").pastime))

        Assert.assertEquals(
            "Invalid pastime :(",
            PastimeClass.formatForView(PastimeClass("").pastime))
    }

    @Test
    fun pastimeClass_joinToString() {
        Assert.assertEquals(
            "LINE MOM, SMOKING",
            arrayListOf(PastimeClass("LINE MOM"), PastimeClass("SMOKING")).joinToString { it.pastime })

    }
}