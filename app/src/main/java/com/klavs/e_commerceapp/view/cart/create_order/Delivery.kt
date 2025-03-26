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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme

@Composable
fun DeliveryOptions(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    addressLine: String,
    onAddressLineChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    scrollToPage: (Int) -> Unit
) {
    var phoneError by remember { mutableStateOf(false) }
    var addressLineError by remember { mutableStateOf(false) }
    var cityError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var surnameError by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = name,
            label = { Text("Name") },
            isError = nameError,
            supportingText = {
                if (nameError) {
                    Text("Please enter a valid name.")
                }
            },
            onValueChange = {
                onNameChange(it)
                nameError = false
            })
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = surname,
            label = { Text("Surname") },
            isError = surnameError,
            supportingText = {
                if (surnameError) {
                    Text("Please enter a valid surname.")
                }
            },
            onValueChange = {
                onSurnameChange(it)
                surnameError = false
            })
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = phone,
            label = { Text("Phone") },
            isError = phoneError,
            supportingText = {
                if (phoneError) {
                    Text("Please enter a valid phone number.")
                }
            },
            prefix = { Text("+90 ") },
            onValueChange = {
                if (it.isDigitsOnly()) {
                    onPhoneChange(it)
                    phoneError = false
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = email,
            label = { Text("E-Mail") },
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text("Please enter a valid e-mail address.")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            onValueChange = {
                onEmailChange(it)
                emailError = false

            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = city,
            supportingText = {
                if (cityError) {
                    Text("Please enter a valid city.")
                }
            },
            label = { Text("City") },
            isError = cityError,
            onValueChange = {
                onCityChange(it)
                cityError = false
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = addressLine,
            supportingText = {
                if (addressLineError) {
                    Text("Please enter a valid address.")
                }
            },
            label = { Text("Address Line") },
            isError = addressLineError,
            onValueChange = {
                onAddressLineChange(it)
                addressLineError = false
            }
        )
        Button(
            onClick = {
                nameError = name.isBlank()
                emailError = email.isBlank()
                surnameError = surname.isBlank()
                phoneError = phone.isBlank()
                cityError = city.isBlank()
                addressLineError =
                    addressLine.isBlank()
                if (!nameError
                    && !phoneError
                    && !cityError
                    && !addressLineError
                    && !surnameError
                    && !emailError
                ) {
                    scrollToPage(1)
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
                Text("Go to Payment")
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "go the payment"
                )
            }
        }
    }
}

@Preview
@Composable
private fun DeliveryPreview() {
    ECommerceAppTheme {
        DeliveryOptions(
            name = "TODO()",
            onNameChange = {},
            phone = "TODO()",
            onPhoneChange = { },
            addressLine = "TODO()",
            onAddressLineChange = { },
            city = "TODO()",
            onCityChange = { },
            surname = "TODO()",
            onSurnameChange = {},
            email = "TODO()",
            onEmailChange = {}
        ) { }
    }
}