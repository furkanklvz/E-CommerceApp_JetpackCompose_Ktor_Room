package com.klavs.e_commerceapp.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.components.StarRating
import com.klavs.e_commerceapp.components.StarRatingSize
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.entity.CartItem
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import com.klavs.e_commerceapp.viewmodel.ProductViewModel

@Composable
fun ProductDetails(
    id: Int,
    viewModel: ProductViewModel,
    globalViewModel: GlobalViewModel,
    navController: NavHostController
) {
    val productResource by viewModel.product.collectAsStateWithLifecycle()
    val cartResource by globalViewModel.cart.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (!productResource.isSuccess()) {
            viewModel.getProduct(id)
        }
    }

    ProductDetailsContent(
        productResource = productResource,
        navController = navController,
        cart = (cartResource as? Resource.Success)?.data,
        add = {
            globalViewModel.addToCart(
                productId = it,
            )
        },
        remove = { productId, quantity ->
            globalViewModel.deleteCartItem(
                productId = productId,
                quantity = quantity
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ProductDetailsContent(
    productResource: Resource<Product>,
    cart: Cart?,
    add: (productId: Int) -> Unit,
    remove: (productId: Int, quantity: Int) -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "favourite"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "share"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            when (productResource) {
                is Resource.Error -> {
                    LaunchedEffect(Unit) {
                        productResource.throwable.printStackTrace()
                    }
                }

                Resource.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                }

                is Resource.Success -> {
                    val product = productResource.data
                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .background(Color.White)
                            ) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(fixImageUrl(product.imageUrl))
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = product.name,
                                    modifier = Modifier.matchParentSize(),
                                    onError = {
                                        Log.e("coil error", it.result.throwable.message?:"")
                                    },
                                    contentScale = ContentScale.Fit,
                                    error = {
                                        Icon(
                                            imageVector = Icons.Rounded.ErrorOutline,
                                            contentDescription = "error",
                                            modifier = Modifier.background(Color.LightGray)
                                        )
                                    }
                                )
                            }
                            product.rating?.let {
                                Box(modifier = Modifier.padding(10.dp)) {
                                    StarRating(
                                        raters = product.raters,
                                        rating = product.rating,
                                        size = StarRatingSize.Large
                                    )
                                }
                            }
                            Text(
                                product.name,
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        "Product Description",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        product.description,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                        FlexibleBottomAppBar {
                            Text("${product.price} ₺", fontWeight = FontWeight.Bold)
                            val productIsInCart =
                                cart != null && cart.cartItems.any { it.productId == product.productId }
                            if (productIsInCart) {
                                Row {
                                    IconButton(
                                        onClick = {
                                            remove(
                                                product.productId,
                                                cart!!.cartItems.find { it.productId == product.productId }?.quantity?:0
                                            )
                                        },
                                        modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DeleteOutline,
                                            contentDescription = "remove",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = {
                                                remove(product.productId, 1)
                                            },
                                            modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Remove,
                                                contentDescription = "sub",
                                                modifier = Modifier.background(
                                                    MaterialTheme.colorScheme.primaryContainer,
                                                    RoundedCornerShape(7.dp),
                                                ),
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        Text(
                                            "${
                                                cart!!.cartItems.find {
                                                    it.productId == product.productId
                                                }?.quantity?:"err"
                                            }",
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            onClick = {add(product.productId)},
                                            modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = "add",
                                                modifier = Modifier.background(
                                                    MaterialTheme.colorScheme.primaryContainer,
                                                    RoundedCornerShape(7.dp),
                                                ),
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            } else {
                                Button(
                                    onClick = {add(product.productId)},
                                    elevation = ButtonDefaults.elevatedButtonElevation()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AddShoppingCart,
                                            contentDescription = "add shopping cart"
                                        )
                                        Text("Add to Cart")
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
private fun ProductDetailsPreview() {
    val product = Product(
        productId = 1420,
        description = "AsyaAsyaAsyaAsya\nAsyaAsya\nAsyaAsyaAsyaAsyaAsyaAsyaAsyaAsya\n" +
                "AsyaAsya\n" +
                "AsyaAsyaAsyaAsyaAsyaAsyaAsyaAsya\n" +
                "AsyaAsya\n" +
                "AsyaAsyaAsyaAsyaAsyaAsyaAsyaAsya\n" +
                "AsyaAsya\n" +
                "AsyaAsyaAsyaAsyaAsyaAsyaAsyaAsya\n" +
                "AsyaAsya\n" +
                "AsyaAsyaAsyaAsyaAsyaAsyaAsyaAsya\n" +
                "AsyaAsya\n" +
                "AsyaAsyaAsyaAsya",
        imageUrl = null,
        isActive = true,
        name = "Enrique",
        price = 56.954,
        stock = 9665,
        rating = 3.5f,
        raters = 5566
    )

    ECommerceAppTheme {
        ProductDetailsContent(
            Resource.Success(product), navController = rememberNavController(),
            cart = Cart(
                cartId = 1,
                customerId = "1",
                cartItems = listOf(
                    CartItem(
                        productId = 1420,
                        productName = "TODO()",
                        productPrice = 20.0,
                        quantity = 2
                    )
                )
            ),
            add = {  },
            remove = { _, _ -> }
        )
    }
}