package com.teleserv.calculator

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

class TipCalculatorTests {
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val expected: String = NumberFormat.getCurrencyInstance().format(2.0)
        val actual = calculateTip(10.0, 20.0, false)

        assertEquals(expected, actual)
    }

    @Test
    fun calculateTip_20PercentWithRoundup() {
        val expected: String = NumberFormat.getCurrencyInstance().format(1.0)
        val actual = calculateTip(10.0, 8.0, true)

        assertEquals(expected, actual)
    }
}