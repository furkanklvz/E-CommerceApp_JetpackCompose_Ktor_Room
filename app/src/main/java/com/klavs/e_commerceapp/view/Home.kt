package com.klavs.e_commerceapp.view

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.klavs.e_commerceapp.R
import com.klavs.e_commerceapp.components.StarRating
import com.klavs.e_commerceapp.components.StarRatingSize
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.extensions.format
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.routes.ProductDetails
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import com.klavs.e_commerceapp.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(
    viewModel: HomeViewModel,
    globalViewModel: GlobalViewModel,
    navController: NavHostController
) {
    val productsResource by viewModel.products.collectAsStateWithLifecycle()
    HomeContent(
        productsResource = productsResource,
        navController = navController,
        addToCart = { id ->
            globalViewModel.addToCart(
                productId = id
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    productsResource: Resource<List<Product>>,
    navController: NavHostController,
    addToCart: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                }
            )
        }
    )  { innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            when (productsResource) {
                is Resource.Error -> {
                    LaunchedEffect(Unit) {
                        productsResource.throwable.printStackTrace()
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
                        Text("Products could not be loaded, please try again later.\n" +
                                "Error Description: ${productsResource.throwable.message}",
                            textAlign = TextAlign.Center)

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
                    val products = productsResource.data
                    LazyVerticalGrid(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        contentPadding = PaddingValues(5.dp),
                        columns = GridCells.Fixed(count = 3)
                    ) {
                        items(products) {
                            ProductCard(
                                product = it,
                                onProductClick = { navController.navigate(ProductDetails(it.productId)) },
                                addToCart = { addToCart(it.productId) }
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ProductCard(product: Product, onProductClick: () -> Unit, addToCart: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onProductClick, modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3 / 6f)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3 / 4f)
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CardDefaults.shape)
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .zIndex(2f)
                            .size(IconButtonDefaults.xSmallContainerSize())
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "favourite",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(IconButtonDefaults.xSmallIconSize)
                                .background(Color.White, CircleShape)
                                .padding(2.dp)
                        )
                    }
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(fixImageUrl(product.imageUrl))
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        onError = {
                            Log.d("coil error", it.result.throwable.stackTraceToString())
                        },
                        error = {
                            Icon(
                                imageVector = Icons.Outlined.Image, contentDescription = "error",
                                modifier = Modifier.background(Color.Black)
                            )
                        },
                        modifier = Modifier
                            .zIndex(1f)
                            .matchParentSize()
                    )
                }
                product.rating?.let { StarRating(it, product.raters, StarRatingSize.Small) }
                Text(
                    text = product.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scope = rememberCoroutineScope()
                var productJustAdded by remember { mutableStateOf(false) }
                val cartIcon: @Composable () -> Unit = {
                    Crossfade(targetState = productJustAdded) { added ->
                        if (added) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "added",
                                modifier = Modifier.size(IconButtonDefaults.xSmallIconSize)
                            )
                        } else {

                            Icon(
                                imageVector = Icons.Rounded.AddShoppingCart,
                                contentDescription = "add to tcart",
                                modifier = Modifier.size(IconButtonDefaults.xSmallIconSize)
                            )
                        }
                    }
                }
                Text(
                    text = "${product.price.format(2)} ₺",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .weight(1f)
                )
                IconButton(
                    enabled = !productJustAdded,
                    onClick = {
                        addToCart()
                        scope.launch {
                            productJustAdded = true
                            delay(1500)
                            productJustAdded = false
                        }
                    },
                    modifier = Modifier.size(IconButtonDefaults.xSmallContainerSize())
                ) {
                    cartIcon()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    val products = listOf(
        Product(
            productId = 1,
            rating = 3.4f,
            raters = 35,
            name = "Product Name",
            isActive = true,
            price = 90.0,
            stock = 200
        ),
        Product(
            productId = 2,
            rating = 4.1f,
            raters = 455,
            name = "Product Name",
            isActive = true,
            price = 900.0,
            stock = 200
        ),
        Product(
            productId = 3,
            name = "Product Name",
            isActive = true,
            price = 90.0,
            stock = 200
        ),
        Product(
            productId = 4,
            rating = 3.7f,
            raters = 351,
            name = "Product Name",
            isActive = true,
            price = 90.0,
            stock = 200
        ),
        Product(
            productId = 5,
            name = "Product Name",
            isActive = true,
            price = 90.0,
            stock = 200
        ),
        Product(
            productId = 6,
            rating = 2.4f,
            raters = 25,
            name = "Product NameProduct NameProduct NameProduct Name",
            isActive = true,
            price = 90.0,
            stock = 200
        )
    )

    ECommerceAppTheme {
        HomeContent(
            productsResource = Resource.Error(Exception("error description")),
            rememberNavController(),
            addToCart = {}
        )
    }
}