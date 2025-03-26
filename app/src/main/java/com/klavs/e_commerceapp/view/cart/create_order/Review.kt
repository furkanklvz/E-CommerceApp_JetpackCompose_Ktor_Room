package com.klavs.e_commerceapp.view.cart.create_order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReviewOptions(
    name: String,
    cardHolderName: String,
    surname: String,
    email: String,
    phone: String,
    addressLine: String,
    city: String,
    cardNumber: String,
    expireDate: String,
    cardCvc: String,
    cart: Cart,
    createOrder: (CreateOrderRequest) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(cart.cartItems) { cartItem ->
                ListItem(
                    modifier = Modifier.width(screenWidth * 0.75f),
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .height(IconButtonDefaults.mediumContainerSize().height)
                                .aspectRatio(3 / 4f)
                        ) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(fixImageUrl(cartItem.productImageUrl))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = cartItem.productName,
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Fit,
                                error = {
                                    Icon(
                                        imageVector = Icons.Rounded.ErrorOutline,
                                        modifier = Modifier.background(Color.Gray),
                                        contentDescription = "error"
                                    )
                                }
                            )
                        }
                    },
                    supportingContent = {
                        Text("${cartItem.productPrice} ₺  × ${cartItem.quantity} ")
                    },
                    headlineContent = {
                        Text(cartItem.productName)
                    }
                )
            }
        }
        HorizontalDivider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeliveryDining,
                    contentDescription = "Delivery",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "Delivery",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append("City: ")
                }
                withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    append(city)
                }
            }
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append("Delivery Address: ")
                }
                withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    append(addressLine)
                }
            }
            )
        }
        HorizontalDivider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Payment,
                    contentDescription = "Payment",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "Payment",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append("Cart Number: ")
                }

                withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    append("**** **** **** ${cardNumber.takeLast(4)}")
                }
            }
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append("Expiry Date: ")
                }

                withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    append(expireDate)
                }
            }
            )
        }
        HorizontalDivider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sub Total", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                Text(
                    "${cart.cartItems.sumOf { it.productPrice }} ₺",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Delivery Fee", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                Text(
                    "${cart.deliveryFee} ₺",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total Price",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    "${cart.deliveryFee + cart.cartItems.sumOf { it.productPrice }} ₺",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }

        Button(
            onClick = {
                createOrder(
                    CreateOrderRequest(
                        addresLine = addressLine,
                        city = city,
                        name = name,
                        phone = phone,
                        surname = surname,
                        cardHolderName = cardHolderName,
                        cardNumber = cardNumber,
                        cardExpireMonth = expireDate.split("/").first(),
                        cardExpireYear = expireDate.split("/").last(),
                        cardCvc = cardCvc,
                        email = email,
                    )
                )
            }, modifier = Modifier
                .padding(15.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Complete Order")
        }
    }
}

@Preview
@Composable
private fun ReviewOptionsPreview() {
    ECommerceAppTheme {
        ReviewOptions(
            name = "",
            phone = "TODO()",
            addressLine = "TODO()",
            city = "TODO()",
            cardNumber = "TODO()",
            expireDate = "TODO()",
            cart = Cart(
                cartId = 1,
                customerId = "TODO()"
            ),
            surname = "TODO()",
            cardHolderName = "TODO()",
            cardCvc = "TODO()",
            email = "TODO()"
        ) { }
    }
}