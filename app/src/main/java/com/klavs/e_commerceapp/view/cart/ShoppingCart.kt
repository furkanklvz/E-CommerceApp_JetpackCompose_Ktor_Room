package com.klavs.e_commerceapp.view.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.RemoveShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.entity.CartItem
import com.klavs.e_commerceapp.extensions.format
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel

@Composable
fun ShoppingCart(cartViewModel: CartViewModel, globalViewModel: GlobalViewModel) {
    val cartResource by globalViewModel.cart.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (!cartResource.isSuccess()) {
            globalViewModel.listenToToken()
        }
    }
    ShoppingCartContent(
        cartResource = cartResource,
        increaseQuantity = { productId -> globalViewModel.addToCart(productId = productId) },
        decreaseQuantity = { productId, quantity ->
            globalViewModel.deleteCartItem(productId = productId, quantity = quantity)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingCartContent(
    cartResource: Resource<Cart>,
    increaseQuantity: (productId: Int) -> Unit,
    decreaseQuantity: (productId: Int, quantity: Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Shopping Cart")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            when (cartResource) {
                is Resource.Error -> {
                    LaunchedEffect(Unit) {
                        cartResource.throwable.printStackTrace()
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
                            imageVector = Icons.Rounded.RemoveShoppingCart,
                            contentDescription = "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "Your cart could not be loaded, please try again later.\n" +
                                    "Error Description: ${cartResource.throwable.localizedMessage}",
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
                    val cartItems = cartResource.data.cartItems
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            items(cartItems) { cartItem ->
                                CartItemRow(
                                    cartItem = cartItem,
                                    increaseQuantity = { increaseQuantity(cartItem.productId) },
                                    decreaseQuantity = { decreaseQuantity(cartItem.productId, it) }
                                )
                                if (cartItems.last() != cartItem) HorizontalDivider()
                            }
                        }
                        if (cartItems.isNotEmpty()) {
                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = {}) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text("Order Now")
                                    Icon(
                                        imageVector = Icons.Outlined.ShoppingBag,
                                        contentDescription = "get order"
                                    )
                                }
                            }
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
                            imageVector = Icons.Rounded.Person,
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
private fun CartItemRow(
    cartItem: CartItem,
    increaseQuantity: () -> Unit,
    decreaseQuantity: (quantity: Int) -> Unit
) {
    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Text(cartItem.productName)
        },
        leadingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(IconButtonDefaults.largeContainerSize())
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(fixImageUrl(cartItem.productImageUrl))
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentDescription = cartItem.productName,
                        error = {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = "error",
                                modifier = Modifier.background(Color.Gray)
                            )
                        }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { decreaseQuantity(1) },
                            modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Remove,
                                contentDescription = "decrease",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(IconButtonDefaults.xSmallIconSize)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceContainerHighest,
                                        RoundedCornerShape(7.dp)
                                    )
                            )
                        }
                        Text(
                            cartItem.quantity.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )

                        IconButton(
                            onClick = { increaseQuantity() },
                            modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "add",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(IconButtonDefaults.xSmallIconSize)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceContainerHighest,
                                        RoundedCornerShape(7.dp)
                                    )
                            )
                        }
                    }
                    IconButton(
                        onClick = { decreaseQuantity(cartItem.quantity) },
                        modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "remove",
                            modifier = Modifier.size(IconButtonDefaults.xSmallIconSize),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        trailingContent = {
            Text(
                (cartItem.productPrice * cartItem.quantity).format(2) + " ₺",
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun CartPreview() {
    val cart = Cart(
        cartId = 1,
        cartItems = listOf(
            CartItem(
                productId = 1,
                productPrice = 2500.0,
                quantity = 2,
                productName = "product 1"
            ),
            CartItem(
                productId = 12,
                productPrice = 2100.0,
                quantity = 1,
                productName = "product 2"
            ),
            CartItem(
                productId = 3,
                productPrice = 299.90,
                quantity = 1,
                productName = "product 3"
            ),
            CartItem(
                productId = 1,
                productPrice = 2500.0,
                quantity = 2,
                productName = "product 1"
            ),
            CartItem(
                productId = 12,
                productPrice = 2100.0,
                quantity = 1,
                productName = "product 2"
            ),
            CartItem(
                productId = 3,
                productPrice = 299.90,
                quantity = 1,
                productName = "product 3"
            ),
            CartItem(
                productId = 1,
                productPrice = 2500.0,
                quantity = 2,
                productName = "product 1"
            ),
            CartItem(
                productId = 12,
                productPrice = 2100.0,
                quantity = 1,
                productName = "product 2"
            ),
            CartItem(
                productId = 3,
                productPrice = 299.90,
                quantity = 1,
                productName = "product 3"
            )
        ),
        customerId = "1"
    )

    ECommerceAppTheme {
        ShoppingCartContent(
            cartResource = Resource.Success(cart),
            increaseQuantity = {},
            decreaseQuantity = { _, _ -> }
        )
    }
}