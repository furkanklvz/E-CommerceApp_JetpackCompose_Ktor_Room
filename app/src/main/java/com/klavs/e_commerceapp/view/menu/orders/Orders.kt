package com.klavs.e_commerceapp.view.menu.orders

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
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
import com.klavs.e_commerceapp.data.model.response.PagedData
import com.klavs.e_commerceapp.extensions.format
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.routes.ProductDetails
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.OrderViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@Composable
fun Orders(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    token: String?
) {
    val ordersResource by orderViewModel.ordersResource.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        token?.let { orderViewModel.getOrders(token, 0, 1) }
    }

    OrdersContent(
        ordersResource = ordersResource,
        navController = navController,
        getOrders = { firstItemIndex, pageSize ->
            token?.let {
                orderViewModel.getOrders(
                    token = token,
                    firstItemIndex = firstItemIndex,
                    pageSize = pageSize
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersContent(
    ordersResource: Resource<PagedData<OrderResponse>>,
    getOrders: (firstItemIndex: Int, pageSize: Int) -> Unit,
    navController: NavHostController
) {
    val orders = remember { mutableStateListOf<OrderResponse>() }
    val scope = rememberCoroutineScope()
    var orderInBottomSheet by remember { mutableStateOf<OrderResponse?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var updatingListJob: Job? = null

    LaunchedEffect(ordersResource) {
        if (ordersResource is Resource.Success) {
            updatingListJob = launch {
                orders.addAll(ordersResource.data.data)
            }
        } else if (ordersResource is Resource.Error) {
            Log.e("orders", ordersResource.throwable.message.toString())
        }
    }

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
        orderInBottomSheet?.let {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            orderInBottomSheet = null
                        }
                    }
                },
                sheetState = sheetState
            ) {
                OrderDetails(
                    order = it,
                    onProductClick = { productId ->
                        navController.navigate(ProductDetails(productId))
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (orders.isEmpty()) {
                if (ordersResource.isLoading() || updatingListJob?.isCompleted != true) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                } else if (ordersResource.isSuccess()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlaylistRemove,
                            contentDescription = "no orders"
                        )
                        Text(
                            "You have not placed any orders yet.",
                            textAlign = TextAlign.Center
                        )
                        FilledTonalButton(onClick = {
                            navController.navigate(_Home) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                    saveState = false
                                }
                                restoreState = false
                            }
                        }) {
                            Text("Start Shopping Now")
                        }
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(orders) { order ->
                        OrderRow(
                            order = order,
                            onClick = { orderInBottomSheet = order }
                        )
                    }
                    if (orders.isNotEmpty() && ordersResource.isLoading()) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.padding(5.dp))
                        }
                    } else if (ordersResource is Resource.Success
                        && ordersResource.data.totalRecords > orders.size
                    ) {
                        item {
                            OutlinedIconButton(
                                onClick = {
                                    getOrders(
                                        ordersResource.data.lastItemIndex + 1,
                                        2
                                    )
                                },
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "get more"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OrderRow(order: OrderResponse, onClick: () -> Unit) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier
            .padding(10.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        headlineContent = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                userScrollEnabled = true
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
                    Text(buildAnnotatedString {
                        append(order.orderDate.date.toString())
                        append(", ")
                        append(order.orderDate.time.toString())
                    })
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
            name = "furkan",
            surname = "kılavuz",
            orderDate = LocalDateTime(2025, 8, 12, 20, 30),
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
            orderDate = LocalDateTime(2025, 6, 12, 20, 30),
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
            orderDate = LocalDateTime(2025, 5, 12, 20, 30),
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
            orderDate = LocalDateTime(2025, 4, 12, 20, 30),
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
            orderDate = LocalDateTime(2025, 3, 12, 20, 30),
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

    val pagedData = PagedData<OrderResponse>(
        pageSize = 3,
        totalRecords = 25,
        totalPages = 9,
        data = orders,
        lastItemIndex = 1
    )

    ECommerceAppTheme {
        OrdersContent(
            ordersResource = Resource.Success(pagedData),
            navController = rememberNavController(),
            getOrders = { _, _ -> }
        )
    }
}