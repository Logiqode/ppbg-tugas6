package com.example.currencyconverter

import android.graphics.Color.rgb
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val usdToIdrRate = 16800.0
    private val idrToUsdRate = 1 / usdToIdrRate

    private val usdToJpyRate = 142.0
    private val jpyToUsdRate = 1 / usdToJpyRate

    private val idrToJpyRate = usdToJpyRate / usdToIdrRate
    private val jpyToIdrRate = 1 / idrToJpyRate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverter()
                }
            }
        }
    }

    @Composable
    fun CurrencyConverter() {
        var input by remember { mutableStateOf("") }
        var selectedConversion by remember { mutableStateOf("USD to IDR") }
        var result by remember { mutableStateOf("") }

        val options = listOf("USD to IDR", "IDR to USD", "USD to JPY", "JPY to USD", "IDR to JPY", "JPY to IDR")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Currency Converter",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = input,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        input = newValue
                    }
                },
                label = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuBox(
                options = options,
                selectedOption = selectedConversion,
                onOptionSelected = { selectedConversion = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = input.toDoubleOrNull()
                    result = if (amount != null) {
                        when (selectedConversion) {
                            "USD to IDR" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount * usdToIdrRate)
                                formatted
                            }
                            "IDR to USD" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale.US).format(amount * idrToUsdRate)
                                formatted
                            }
                            "USD to JPY" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale.JAPAN).format(amount * usdToJpyRate)
                                formatted
                            }
                            "JPY to USD" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale.US).format(amount * jpyToUsdRate)
                                formatted
                            }
                            "IDR to JPY" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale.JAPAN).format(amount * idrToJpyRate)
                                formatted
                            }
                            "JPY to IDR" -> {
                                val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount * jpyToIdrRate)
                                formatted
                            }
                            else -> "Invalid conversion"
                        }
                    } else {
                        "Invalid input"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(rgb(120,41,148))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Convert", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = result,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
                color = Color.DarkGray
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DropdownMenuBox(
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOption,
                onValueChange = {},
                label = { Text("Select conversion") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}