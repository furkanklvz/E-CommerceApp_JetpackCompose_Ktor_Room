package com.klavs.e_commerceapp.view.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderItem
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@Composable
fun CreateOrder(navController: NavHostController, cartViewModel: CartViewModel, token: String) {

    val orderResource by cartViewModel.orderResource.collectAsStateWithLifecycle()

    CreateOrderContent(
        orderResource = orderResource,
        createOrder = { cartViewModel.createOrder(it, token) },
        navController = navController
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateOrderContent(
    orderResource: Resource<OrderResponse>,
    createOrder: (CreateOrderRequest) -> Unit,
    navController: NavHostController
) {
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
                is Resource.Error -> {}
                Resource.Idle -> {
                    val stages = listOf(
                        "Delivery" to Icons.Rounded.DeliveryDining,
                        "Payment" to Icons.Rounded.Payment,
                        "Review" to Icons.Rounded.CheckCircleOutline
                    )
                    var selectedTabIndex by remember { mutableIntStateOf(1) }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val pagerState = rememberPagerState(
                            pageCount = { stages.size },
                            initialPage = 1
                        )
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
                                            selectedTabIndex = index
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
                        HorizontalPager(state = pagerState) { currentPage ->
                            PageContent(
                                currentPage = currentPage,
                                scrollToPage = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(it)
                                    }
                                    selectedTabIndex = it
                                }
                            )

                        }
                    }
                }

                Resource.Loading -> {}
                is Resource.Success -> {}
                Resource.Unauthorized -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageContent(
    currentPage: Int,
    scrollToPage: (Int) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var fullNameError by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf(false) }
    var addressLine by remember { mutableStateOf("") }
    var addressLineError by remember { mutableStateOf(false) }
    var city by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf(false) }

    var cartNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentPage) {
            0 -> {
                InfoTextField(
                    fullName, "Full Name",
                    isError = fullNameError,
                ) {
                    fullName = it
                    fullNameError = false
                }
                InfoTextField(
                    phone, "Phone",
                    isError = phoneError,
                    isPhone = true
                ) {
                    phone = it
                    phoneError = false
                }
                InfoTextField(
                    city, "City",
                    isError = cityError
                ) {
                    city = it
                    cityError = false
                }
                InfoTextField(
                    addressLine, "Address Line",
                    isError = addressLineError
                ) {
                    addressLine = it
                    addressLineError = false
                }
                Button(
                    onClick = {
                        fullNameError = fullName.isBlank()
                        phoneError = phone.isBlank()
                        cityError = city.isBlank()
                        addressLineError = addressLine.isBlank()
                        if (!fullNameError && !phoneError && !cityError && !addressLineError) {
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

            1 -> {
                InfoTextField(
                    value = cartNumber,
                    label = "Cart Number",
                    isError = false
                ) {
                    if (it.isDigitsOnly()) {
                        cartNumber = it
                    }
                }
                Row {
                    OutlinedTextField(
                        value = expiryDate,
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
                        onValueChange = {expiryDate = it},
                        label = { Text("Expiry Date") },
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("CVV") },
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)
                    )
                }
                Button(
                    onClick = {
                        scrollToPage(2)
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

            2 -> {
                Column {

                }
            }
        }
    }
}

@Composable
private fun InfoTextField(
    value: String,
    label: String,
    isError: Boolean,
    isPhone: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.95f),
        prefix = if (isPhone) {
            { Text("+90") }
        } else null,
        label = { Text(label) },
        value = value,
        isError = isError,
        supportingText = if (isError) {
            { Text("This field cannot be empty.") }
        } else null,
        onValueChange = { onValueChange(it) }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun CreateOrderPreview() {
    val orderResponse = OrderResponse(
        addresLine = "Çekmeköy, Ekşioğlu",
        city = "İstanbul",
        customerId = "1",
        deliveryFee = 49.90,
        fullName = "Furkan Kılavuz",
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
            navController = rememberNavController()
        )
    }
}