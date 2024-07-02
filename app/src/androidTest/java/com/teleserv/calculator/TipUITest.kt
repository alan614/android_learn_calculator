package com.teleserv.calculator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.teleserv.calculator.ui.theme.CalculatorTheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            /*CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->*/
                    CalculatorApp(modifier = Modifier)
                /*}
            }*/
        }

        composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
        composeTestRule.onNodeWithText("Tip Rate").performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "No node with this text was found."
        )
    }
}