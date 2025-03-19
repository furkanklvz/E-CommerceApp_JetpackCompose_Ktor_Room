package com.klavs.e_commerceapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Unspecified
) {
    TextField(
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation,
        label = label?.let { { Text(text = it) } },
        leadingIcon = leadingIcon?.let { { Icon(imageVector = it, contentDescription = null) } },
        trailingIcon = trailingIcon?.let { { Icon(imageVector = it, contentDescription = null) } },
        supportingText = supportingText?.let { { Text(text = it) } },
        isError = isError
    )
}

@Preview
@Composable
private fun LoginTextFieldPreview() {
    var value by remember { mutableStateOf("") }
    ECommerceAppTheme {
        Surface {
            Box(Modifier.fillMaxSize()) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    LoginTextField(
                        value = value,
                        onValueChange = { value = it },
                        label = "Label"
                    )
                }
            }
        }

    }
}