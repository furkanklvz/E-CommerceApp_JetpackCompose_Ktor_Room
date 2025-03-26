package com.klavs.e_commerceapp.view.cart.create_order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.klavs.e_commerceapp.data.model.entity.Account
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.entity.CartItem
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderItem
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.routes.Orders
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@Composable
fun CreateOrder(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    account: Account,
    globalViewModel: GlobalViewModel,
    cart: Cart
) {

    val orderResource by cartViewModel.orderResource.collectAsStateWithLifecycle()

    CreateOrderContent(
        orderResource = orderResource,
        createOrder = { cartViewModel.createOrder(it, account.token) },
        navController = navController,
        cart = cart,
        reloadCart = { globalViewModel.getCart() },
        account = account
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CreateOrderContent(
    orderResource: Resource<OrderResponse>,
    createOrder: (CreateOrderRequest) -> Unit,
    reloadCart: () -> Unit,
    account: Account,
    navController: NavHostController,
    cart: Cart
) {
    var name by remember { mutableStateOf(runCatching { account.fullName.split(" ").dropLast(1).joinToString(" ") }.getOrElse { "" }) }
    var surname by remember { mutableStateOf(runCatching { account.fullName.split(" ").last() }.getOrElse { "" }) }
    var email by remember { mutableStateOf(account.email) }
    var phone by remember { mutableStateOf("") }
    var addressLine by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    var cardHolderName by remember { mutableStateOf("") }
    var cartNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf(TextFieldValue("")) }
    var cvc by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("Order")
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "navigate back"
                    )
                }
            }
        )
    }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (orderResource) {
                is Resource.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(orderResource.throwable.message ?: "Error")
                    }
                }

                Resource.Idle -> {
                    val stages = listOf(
                        "Delivery" to Icons.Rounded.DeliveryDining,
                        "Payment" to Icons.Rounded.Payment,
                        "Review" to Icons.Rounded.CheckCircleOutline
                    )
                    var selectedTabIndex by remember { mutableIntStateOf(0) }
                    Column(
                        Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val pagerState = rememberPagerState(
                            pageCount = { stages.size },
                            initialPage = selectedTabIndex
                        )
                        LaunchedEffect(pagerState.currentPage) {
                            selectedTabIndex = pagerState.currentPage
                        }
                        PrimaryTabRow(
                            selectedTabIndex = selectedTabIndex
                        ) {
                            stages.forEachIndexed { index, pair ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = {
                                        if (index < selectedTabIndex) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    },
                                    text = { Text(pair.first) },
                                    icon = {
                                        Icon(
                                            pair.second,
                                            contentDescription = pair.first
                                        )
                                    }
                                )
                            }
                        }
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = false
                        ) { currentPage ->
                            PageContent(
                                currentPage = currentPage,
                                scrollToPage = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(it)
                                    }
                                },
                                cart = cart,
                                createOrder = { createOrder(it) },
                                name = name,
                                onNameChange = { name = it },
                                phone = phone,
                                onPhoneChange = { phone = it },
                                addressLine = addressLine,
                                onAddressLineChange = { addressLine = it },
                                city = city,
                                onCityChange = { city = it },
                                cartNumber = cartNumber,
                                onCartNumberChange = { cartNumber = it },
                                expiryDate = expiryDate,
                                onExpiryDateChange = { expiryDate = it },
                                cvc = cvc,
                                onCvcChange = { cvc = it },
                                surname = surname,
                                onSurnameChange = { surname = it },
                                cardHolderName = cardHolderName,
                                onCardHolderNameChange = { cardHolderName = it },
                                email = email,
                                onEmailChange = { email = it }
                            )

                        }
                    }
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        reloadCart()
                    }
                    val order = orderResource.data
                    Column(Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "success",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(IconButtonDefaults.xLargeContainerSize())
                            )
                            Text(
                                "Your order confirmed.",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        HorizontalDivider()
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 30.dp)
                                .fillMaxWidth(0.95f)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text("Order Id:", fontWeight = FontWeight.Bold)
                                Text(order.orderId.toString())
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Delivery Address:", fontWeight = FontWeight.Bold)
                                Text(
                                    "${order.addresLine}, ${order.city}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Order Date:", fontWeight = FontWeight.Bold)
                                Text(
                                    "${order.orderDate}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(onClick = {
                                    navController.navigate(Orders) {
                                        val startDestination =
                                            navController.graph.startDestinationRoute
                                        startDestination?.let {
                                            popUpTo(startDestination) { inclusive = false }
                                        }
                                    }
                                }) { Text("Go to Orders") }
                                TextButton(onClick = {
                                    navController.navigate(_Home) {
                                        popUpTo(_Home) {
                                            inclusive = true
                                        }
                                    }
                                }) { Text("Go to Home") }
                            }
                        }
                    }
                }

                Resource.Unauthorized -> {
                    Text("Unauthorized")
                }
            }
        }
    }
}

@Composable
fun PageContent(
    name: String,
    onNameChange: (String) -> Unit,
    cardHolderName: String,
    onCardHolderNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    addressLine: String,
    onAddressLineChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    cartNumber: String,
    onCartNumberChange: (String) -> Unit,
    expiryDate: TextFieldValue,
    onExpiryDateChange: (TextFieldValue) -> Unit,
    cvc: String,
    onCvcChange: (String) -> Unit,
    currentPage: Int,
    cart: Cart,
    createOrder: (CreateOrderRequest) -> Unit,
    scrollToPage: (Int) -> Unit
) {

    when (currentPage) {
        0 -> {
            DeliveryOptions(
                name = name,
                onNameChange = onNameChange,
                phone = phone,
                onPhoneChange = onPhoneChange,
                addressLine = addressLine,
                onAddressLineChange = onAddressLineChange,
                city = city,
                onCityChange = onCityChange,
                scrollToPage = scrollToPage,
                surname = surname,
                onSurnameChange = onSurnameChange,
                email = email,
                onEmailChange = onEmailChange
            )
        }

        1 -> {
            PaymentOptions(
                cartNumber = cartNumber,
                onCartNumberChange = onCartNumberChange,
                expiryDate = expiryDate,
                onExpiryDateChange = onExpiryDateChange,
                cvc = cvc,
                onCvcChange = onCvcChange,
                scrollToPage = scrollToPage,
                cardHolderName = cardHolderName,
                onCardHolderNameChange = onCardHolderNameChange
            )
        }

        2 -> {
            ReviewOptions(
                name = name,
                phone = phone,
                addressLine = addressLine,
                city = city,
                cardNumber = cartNumber,
                expireDate = expiryDate.text,
                cart = cart,
                createOrder = createOrder,
                surname = surname,
                cardHolderName = cardHolderName,
                cardCvc = cvc,
                email = email
            )
        }

        else -> {}
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreateOrderPreview() {
    val orderResponse = OrderResponse(
        addresLine = "Çekmeköy, Ekşioğlu",
        city = "İstanbul",
        customerId = "1",
        deliveryFee = 49.90,
        name = "Furkan",
        surname = "Kılavuz",
        orderDate = LocalDateTime(2024, 9, 12, 20, 30),
        orderId = 1,
        orderItems = listOf(
            OrderItem(
                orderId = 1,
                orderItemId = 1,
                price = 990.00,
                productId = 1,
                quantity = 5
            )
        ),
        orderStatus = 0,
        phone = "+555555",
        subTotal = 990.0
    )

    ECommerceAppTheme {
        CreateOrderContent(
            orderResource = Resource.Idle,
            createOrder = { },
            cart = Cart(
                cartId = 1,
                cartItems = listOf(
                    CartItem(
                        productId = 1,
                        productImageUrl = null,
                        productName = "TelefonTelefonTelefonTelefon",
                        productPrice = 17900.0,
                        quantity = 1
                    ),
                    CartItem(
                        productId = 2,
                        productImageUrl = null,
                        productName = "kulaklıkkulaklıkkulaklıkkulaklık",
                        productPrice = 3900.0,
                        quantity = 3
                    )
                ),
                customerId = "1"
            ),
            navController = rememberNavController(),
            reloadCart = {},
            account = Account(
                token = "Shelly",
                createdAt = 5450L,
                fullName = "mustafa ke mal ata türk",
                userName = "Clark",
                email = "Britt"
            )
        )
    }
}