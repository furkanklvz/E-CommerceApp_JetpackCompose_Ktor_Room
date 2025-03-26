package com.klavs.e_commerceapp.view.cart.create_order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun PaymentOptions(
    cartNumber: String,
    onCartNumberChange: (String) -> Unit,
    expiryDate: TextFieldValue,
    onExpiryDateChange: (TextFieldValue) -> Unit,
    cardHolderName: String,
    onCardHolderNameChange: (String) -> Unit,
    cvc: String,
    onCvcChange: (String) -> Unit,
    scrollToPage: (Int) -> Unit
) {
    var cardHolderNameError by remember { mutableStateOf(false) }
    var cardNumberError by remember { mutableStateOf(false) }
    var expiryDateError by remember { mutableStateOf(false) }
    var cvcError by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            isError = cardHolderNameError,
            supportingText = {
                if (cardHolderNameError) {
                    Text("Please enter a valid cart holder name.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = cardHolderName,
            label = { Text("Cart Holder Name") },
            onValueChange = {
                cardHolderNameError = false
                onCardHolderNameChange(it)
            }
        )
        OutlinedTextField(
            isError = cardNumberError,
            supportingText = {
                if (cardNumberError) {
                    Text("Please enter a valid cart number.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = cartNumber,
            label = { Text("Cart Number") },
            onValueChange = {
                if (it.isDigitsOnly()) {
                    cardNumberError = false
                    onCartNumberChange(it)
                }
            }
        )
        Row {
            OutlinedTextField(
                value = expiryDate,
                isError = expiryDateError,
                supportingText = {
                    if (expiryDateError) {
                        Text("Please enter a valid expiry date.")
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Expiry date"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = { Text("MM/YY") },
                onValueChange = { newValue ->
                    if (newValue.text.length <= 5 && (newValue.text.isDigitsOnly() || newValue.text.contains(
                            "/"
                        ))
                    ) {
                        expiryDateError = false
                        onExpiryDateChange(newValue)
                        if (newValue.text.length == 3 && !newValue.text.contains("/")
                        ) {
                            onExpiryDateChange(
                                newValue.copy(
                                    text = newValue.text
                                        .substring(0, 2) + "/"
                                            + newValue.text.substring(2),
                                    selection = TextRange(4)
                                )
                            )
                        }
                    }
                },
                label = { Text("Expiry Date") },
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            )
            OutlinedTextField(
                isError = cvcError,
                supportingText = {
                    if (cvcError) {
                        Text("Please enter a valid CVV.")
                    }
                },
                value = cvc,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        cvcError = false
                        onCvcChange(it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("CVV") },
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            )
        }
        Button(
            onClick = {
                cardHolderNameError = cardHolderName.isBlank()
                cardNumberError =
                    cartNumber.trim().length != 16
                expiryDateError =
                    expiryDate.text.length != 5
                cvcError = cvc.length != 3
                if (!cardNumberError && !expiryDateError && !cvcError && !cardHolderNameError) {
                    scrollToPage(2)
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("Review")
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "go the payment"
                )
            }
        }
    }
}