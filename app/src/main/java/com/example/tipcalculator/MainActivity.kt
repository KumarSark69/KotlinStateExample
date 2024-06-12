package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Style
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   tipTimeLayoutPreview(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun tipTimeLayout(){
    Column(modifier = Modifier
        .statusBarsPadding()
        .padding(40.dp)
        .verticalScroll(rememberScrollState())
        .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        var amountInput by remember {
            mutableStateOf("")
        }
        var percentageInput by remember {
            mutableStateOf("")
        }
        var roundUp by remember { mutableStateOf(false) }



        val percentage=percentageInput.toDoubleOrNull() ?: 0.0
        val amount=amountInput.toDoubleOrNull() ?: 0.0

        val tip= calculateTip(amount=amount, tipPercentage = percentage,roundUp)
        Text(text= stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start))
        EditNumberField(value=amountInput, onValueChange = { amountInput = it },
            label = stringResource(id = R.string.bill_amount),
            labelIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
            ,modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())
        EditNumberField(value = percentageInput,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChange = {percentageInput=it},
            labelIcon = R.drawable.percent,
            label = stringResource(id = R.string.how_was_the_service),
             modifier = Modifier
                 .padding(bottom = 32.dp)
                 .fillMaxWidth())

        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(text = stringResource(id = R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(150.dp))
    }
}
private fun calculateTip(amount:Double,tipPercentage:Double=15.0, roundUp: Boolean):String{
    var tip=(tipPercentage/100)*amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}
@Composable
fun EditNumberField(value:String,
                    onValueChange: (String) -> Unit,
    label:String,
                    @DrawableRes labelIcon:Int,
    keyboardOptions: KeyboardOptions,modifier: Modifier=Modifier){
    TextField(value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = {Text(label)},
        leadingIcon = { Icon(painter = painterResource(id = labelIcon) , contentDescription =null)},
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}
@Composable
fun RoundTheTipRow(
    roundUp:Boolean,
    onRoundUpChanged:(Boolean)->Unit,
    modifier: Modifier=Modifier){
    Row (
        modifier= modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged,
            modifier= modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun tipTimeLayoutPreview(modifier: Modifier=Modifier) {
    TipCalculatorTheme {
        tipTimeLayout()
    }
}