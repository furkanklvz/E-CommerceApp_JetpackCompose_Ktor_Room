package com.klavs.e_commerceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.data.model.response.OrderItem
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.model.response.OrderStatus
import com.klavs.e_commerceapp.extensions.format
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.OrderViewModel
import kotlinx.datetime.LocalDateTime

@Composable
fun Orders(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    token: String?
) {
    val ordersResource by orderViewModel.ordersResource.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        token?.let { orderViewModel.getOrders(it) }
    }

    OrdersContent(
        ordersResource = ordersResource,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersContent(
    ordersResource: Resource<List<OrderResponse>>,
    navController: NavHostController
) {
    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "navigate before"
                    )
                }
            },
            title = {
                Text("Orders")
            }
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (ordersResource) {
                is Resource.Error -> {
                    LaunchedEffect(Unit) {
                        ordersResource.throwable.printStackTrace()
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.TopCenter)
                            .padding(top = 30.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "Orders could not be loaded, please try again later.\n" +
                                    "Error Description: ${ordersResource.throwable.message}",
                            textAlign = TextAlign.Center
                        )

                    }
                }

                Resource.Idle -> {}
                Resource.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                }

                is Resource.Success -> {
                    val orders = ordersResource.data
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(orders) { order ->
                            OrderRow(order)
                        }
                    }
                }

                Resource.Unauthorized -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.TopCenter)
                            .padding(top = 30.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = "log in",
                        )
                        Text(
                            "Please log in.",
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OrderRow(order: OrderResponse) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier
            .padding(10.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(12.dp)
            ),
        headlineContent = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                userScrollEnabled = false
            ) {
                items(order.orderItems) { orderItem ->
                    Box(
                        modifier = Modifier
                            .height(IconButtonDefaults.mediumContainerSize().height)
                            .aspectRatio(3 / 4f)
                            .shadow(3.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(fixImageUrl(orderItem.productImageUrl))
                                .crossfade(true)
                                .build(),
                            contentDescription = orderItem.productName,
                            modifier = Modifier
                                .matchParentSize(),
                            contentScale = ContentScale.Fit,
                            error = {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = "error",
                                    modifier = Modifier.background(Color.Gray)
                                )
                            }
                        )
                    }
                }
            }
        },
        supportingContent = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
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
        },
        trailingContent = {
            Row {
                Column {
                    Text(order.orderDate.date.toString())
                    Text(
                        "${(order.subTotal + order.deliveryFee).format(2)} ₺",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "view",
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
        },
    )
}

@Preview(showSystemUi = true)
@Composable
private fun OrdersPreview() {
    val orders = listOf(
        OrderResponse(
            addresLine = "Keyonia",
            city = "Mose",
            customerId = "Kiesha",
            deliveryFee = 23.883,
            fullName = "Stephaie",
            orderDate = LocalDateTime.parse(
                input = "2024-5-12"
            ),
            orderId = 2850,
            orderItems = listOf(
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Telefon",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Ayakkabı",
                    quantity = 7945
                )
            ),
            orderStatus = 0,
            phone = "Christon",
            subTotal = 79.165
        ),
        OrderResponse(
            addresLine = "Beverly",
            city = "Ines",
            customerId = "Tarrance",
            deliveryFee = 75.152,
            fullName = "Colter",
            orderDate = LocalDateTime.parse(
                input = "2024-5-12"
            ),
            orderId = 6328,
            orderItems = listOf(
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Oyuncak",
                    quantity = 7945
                )
            ),
            orderStatus = 2,
            phone = "Lael",
            subTotal = 59.610
        ),
        OrderResponse(
            addresLine = "Emmalee",
            city = "Frederica",
            customerId = "Riley",
            deliveryFee = 62.704,
            fullName = "Shamia",
            orderDate = LocalDateTime.parse(
                input = "2024-5-12"
            ),
            orderId = 2821,
            orderItems = listOf(
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Mont",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Terlik",
                    quantity = 7945
                )
            ),
            orderStatus = 1,
            phone = "Canaan",
            subTotal = 23.801
        ),
        OrderResponse(
            addresLine = "Marisela",
            city = "Kimberlee",
            customerId = "Aleesha",
            deliveryFee = 19.118,
            fullName = "Khalilah",
            orderDate = LocalDateTime.parse(
                input = "2024-5-12"
            ),
            orderId = 9869,
            orderItems = listOf(
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Telefon",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Ayakkabı",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Klavye",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Mouse",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Mouse",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Mouse",
                    quantity = 7945
                )
            ),
            orderStatus = 3,
            phone = "Chaise",
            subTotal = 26.367
        ),
        OrderResponse(
            addresLine = "Rico",
            city = "Shant",
            customerId = "Chace",
            deliveryFee = 51.811,
            fullName = "Paulina",
            orderDate = LocalDateTime.parse(
                input = "2024-5-12"
            ),
            orderId = 5401,
            orderItems = listOf(
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Telefon",
                    quantity = 7945
                ),
                OrderItem(
                    orderId = 480,
                    orderItemId = 6582,
                    price = 46.676,
                    productId = 9340,
                    productImageUrl = null,
                    productName = "Ayakkabı",
                    quantity = 7945
                )
            ),
            orderStatus = 3,
            phone = "Jesus",
            subTotal = 43.011
        ),
    )

    ECommerceAppTheme {
        OrdersContent(
            ordersResource = Resource.Success(orders),
            navController = rememberNavController()
        )
    }
}