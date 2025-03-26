package com.klavs.e_commerceapp.view.menu.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.data.model.response.OrderItem
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.model.response.OrderStatus
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColumnScope.OrderDetails(order: OrderResponse, onProductClick: (Int) -> Unit) {
    val context = LocalContext.current
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Order Id: ")
            }
            append("${order.orderId}")
        },
        modifier = Modifier.padding(10.dp)
    )
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Date: ")
            }
            append("${order.orderDate.date}, ${order.orderDate.time}")
        },
        modifier = Modifier.padding(10.dp)
    )
    Card(
        modifier = Modifier.padding(15.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        order.orderItems.forEach { orderItem ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    headlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    supportingColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    trailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onProductClick(orderItem.productId) },
                headlineContent = {
                    Text(orderItem.productName)
                },
                supportingContent = {
                    Text(
                        text = buildAnnotatedString {
                            append("${orderItem.price} ₺")
                            if (orderItem.quantity > 1) {
                                append(" × ${orderItem.quantity}")
                            }
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                        contentDescription = "navigate"
                    )
                },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(6.dp))
                            .clip(RoundedCornerShape(6.dp))
                            .height(IconButtonDefaults.mediumContainerSize().height)
                            .aspectRatio(3 / 4f)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(fixImageUrl(orderItem.productImageUrl))
                                .crossfade(true)
                                .build(),
                            contentDescription = orderItem.productName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.matchParentSize(),
                            error = {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = "error",
                                    modifier = Modifier.background(Color.LightGray)
                                )
                            }
                        )
                    }
                }
            )
        }
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when (order.orderStatusValue) {
                OrderStatus.Pending -> {
                    Icon(
                        imageVector = Icons.Rounded.AccessTime,
                        contentDescription = "pending",
                        modifier = Modifier.size(IconButtonDefaults.xSmallIconSize)
                    )
                    Text(
                        "Pending",
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                OrderStatus.PurchaseFailed -> {
                    Icon(
                        imageVector = Icons.Rounded.Error,
                        contentDescription = "error",
                        modifier = Modifier.size(IconButtonDefaults.xSmallIconSize),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        "Purchase Failed",
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                OrderStatus.Approved -> {
                    Icon(
                        imageVector = Icons.Rounded.DeliveryDining,
                        contentDescription = "approved",
                        modifier = Modifier.size(IconButtonDefaults.xSmallIconSize),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Approved",
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                OrderStatus.Delivered -> {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = "delivered",
                        modifier = Modifier.size(IconButtonDefaults.xSmallIconSize),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Delivered",
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }

    Text(
        "Delivery Information",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(10.dp)
    )
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(order.name + " " + order.surname)
                    }
                    append(" - ")
                    append(
                        order.phone
                            .replaceRange(3, order.phone.length - 4, "****")
                    )
                }
            )
            Text(
                "${order.addresLine}, ${order.city}"
            )
        }
    }
    Text(
        "Price",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(10.dp)
    )
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sub Total:", fontWeight = FontWeight.Bold)
                Text("${order.subTotal} ₺", fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Delivery Fee:")
                Text("${order.deliveryFee} ₺")
            }
            HorizontalDivider(Modifier.padding(vertical = 9.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total:",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${order.subTotal + order.deliveryFee} ₺",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun OrderDetailsPreview() {
    ECommerceAppTheme {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            OrderDetails(
                order = OrderResponse(
                    deliveryFee = 49.90,
                    addresLine = "Ekşioğlu, 52. sokak, no: 8",
                    city = "İstanbul",
                    name = "Furkan",
                    surname = "Kılavuz",
                    orderDate = LocalDateTime(2024, 8, 21, 20, 16),
                    orderId = 1,
                    orderItems = listOf(
                        OrderItem(
                            orderId = 1,
                            orderItemId = 1,
                            price = 630.0,
                            productId = 1,
                            productImageUrl = null,
                            productName = "TODO()",
                            quantity = 2
                        ),
                        OrderItem(
                            orderId = 1,
                            orderItemId = 1,
                            price = 1000.0,
                            productId = 1,
                            productImageUrl = null,
                            productName = "TODO()2",
                            quantity = 1
                        ),
                        OrderItem(
                            orderId = 1,
                            orderItemId = 1,
                            price = 730.0,
                            productId = 1,
                            productImageUrl = null,
                            productName = "TODO()3",
                            quantity = 1
                        )
                    ),
                    orderStatus = 1,
                    phone = "905340586785",
                    subTotal = 2990.0
                ),
                onProductClick = {}
            )
        }
    }
}