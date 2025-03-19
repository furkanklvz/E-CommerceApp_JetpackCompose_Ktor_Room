package com.klavs.e_commerceapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.helper.fixImageUrl
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetails(
    id: Int,
    viewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    navController: NavHostController
) {
    val productResource by viewModel.product.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!productResource.isSuccess()) {
            viewModel.getProduct(id)
        }
    }

    ProductDetailsContent(
        productResource = productResource,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsContent(
    productResource: Resource<Product>,
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

                Resource.Idle -> {
                    Text("idle")
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                                StarRating(raters = product.raters,
                                    rating = product.rating,
                                    size = StarRatingSize.Large) }
                        }
                        Text(product.name,
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
private fun ProductDetailsPreview() {
    val product = Product(
        productId = 1420,
        description = "Asya",
        imageUrl = null,
        isActive = true,
        name = "Enrique",
        price = 56.954,
        stock = 9665,
        rating = 3.5f,
        raters = 5566
    )

    ECommerceAppTheme {
        ProductDetailsContent(Resource.Success(product), navController = rememberNavController())
    }
}