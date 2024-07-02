package com.teleserv.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teleserv.calculator.ui.theme.CalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorApp(modifier = Modifier.padding(innerPadding))
                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(modifier: Modifier) {
    Calculator(modifier = modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center))
}

@Composable
fun Calculator(modifier: Modifier) {
    var amountInput by remember {
        mutableStateOf("")
    }

    /*var tipInput by remember {
        mutableDoubleStateOf(20.0)
    }*/

    var textTipInput by remember {
        mutableStateOf("")
    }
    val doubleTipAmount = textTipInput.toDoubleOrNull() ?: 20.0

    var isRoundedUp by remember {
        mutableStateOf(false)
    }

    val billAmount = amountInput.toDoubleOrNull() ?: 0.0
    //val tipAmount = calculateTip(billAmount, tipInput)
    val tipAmount = calculateTip(amount = billAmount, tipPercent = doubleTipAmount, isRoundedUp = isRoundedUp)


    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .align(Alignment.Start),
                //.padding(bottom = 16.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            amountInput = amountInput,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) { amountInput = it }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        EditNumberField(
            label = R.string.tip_rate,
            leadingIcon = R.drawable.percent,
            amountInput = textTipInput,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) { textTipInput = it }
        RoundUpTip(isChecked = isRoundedUp, modifier = modifier) { isRoundedUp = it }
        /*Row {
            TipRateField(tipRate = 5, isSelected = tipInput == 5.0) { tipInput = 5.0 }
            TipRateField(tipRate = 8, isSelected = tipInput == 8.0) { tipInput = 8.0 }
            TipRateField(tipRate = 10, isSelected = tipInput == 10.0) { tipInput = 10.0 }
            TipRateField(tipRate = 20, isSelected = tipInput == 20.0) { tipInput = 20.0 }
        }*/
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = stringResource(R.string.tip_amount, tipAmount),
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorTheme {
        //Greeting("Android")
        CalculatorApp(modifier = Modifier)
    }
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 10.0, isRoundedUp: Boolean = false): String {
    var tip = amount * (tipPercent / 100.0)
    if (isRoundedUp) {
        tip = kotlin.math.ceil(tip);
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    amountInput: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {

    TextField(
        label = { Text(text = stringResource(label)) },
        leadingIcon = { Image(painter = painterResource(id = leadingIcon), contentDescription = "") },
        singleLine = true,
        value = amountInput,
        onValueChange = onValueChange,
        modifier = modifier,
        //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun TipRateField(
    tipRate: Int,
    isSelected: Boolean = false,
    onClickAction: () -> Unit
) {
    val tipRateText = "$tipRate%"

    if (isSelected) {
        Button(
            onClick = onClickAction,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding = PaddingValues(horizontal = 1.dp),
        ) {
            Text(text = tipRateText)
        }
    } else {
        OutlinedButton(
            onClick = onClickAction,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding = PaddingValues(horizontal = 1.dp),
        ) {
            Text(text = tipRateText)
        }
    }
}

@Composable
fun RoundUpTip(isChecked: Boolean, modifier: Modifier, onSwitchChange: (Boolean) -> Unit) {
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            checked = isChecked,
            onCheckedChange = onSwitchChange,
            //modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
        )
    }
}