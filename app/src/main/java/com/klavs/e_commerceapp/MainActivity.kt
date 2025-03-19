package com.klavs.e_commerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.klavs.e_commerceapp.components.BottomBar
import com.klavs.e_commerceapp.routes.LogIn
import com.klavs.e_commerceapp.routes.ProductDetails
import com.klavs.e_commerceapp.routes.Register
import com.klavs.e_commerceapp.routes.ShoppingCartTop
import com.klavs.e_commerceapp.routes._ShoppingCart
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.routes._Profile
import com.klavs.e_commerceapp.routes._Search
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.view.ShoppingCart
import com.klavs.e_commerceapp.view.Home
import com.klavs.e_commerceapp.view.ProductDetails
import com.klavs.e_commerceapp.view.Profile
import com.klavs.e_commerceapp.view.Search
import com.klavs.e_commerceapp.view.login.Login
import com.klavs.e_commerceapp.view.login.Register
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import com.klavs.e_commerceapp.viewmodel.HomeViewModel
import com.klavs.e_commerceapp.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceAppTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation(globalViewModel: GlobalViewModel = koinViewModel()) {
    val navController = rememberNavController()
    var bottomBarIsVisible by remember { mutableStateOf(true) }
    Scaffold(bottomBar = {
        AnimatedVisibility(
            bottomBarIsVisible,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            BottomBar(navController, globalViewModel)
        }
    }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            startDestination = _Home,
            navController = navController,
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
        ) {
            composable<_Home> { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<HomeViewModel>(navController)
                LaunchedEffect(Unit) { bottomBarIsVisible = true }
                Home(
                    viewModel = viewModel,
                    globalViewModel = globalViewModel,
                    navController = navController
                )
            }
            composable<_Search> {
                LaunchedEffect(Unit) { bottomBarIsVisible = true }
                Search()
            }
            navigation<ShoppingCartTop>(startDestination = _ShoppingCart) {
                composable<_ShoppingCart> { backStackEntry ->
                    LaunchedEffect(Unit) { bottomBarIsVisible = true }
                    val cartViewModel = backStackEntry.sharedViewModel<CartViewModel>(navController)
                    ShoppingCart(cartViewModel = cartViewModel, globalViewModel = globalViewModel)
                }
            }

            composable<_Profile> {
                LaunchedEffect(Unit) { bottomBarIsVisible = true }
                Profile(
                    navController = navController,
                    globalViewModel = globalViewModel
                )
            }
            composable<ProductDetails> { backStackEntry ->
                LaunchedEffect(Unit) { bottomBarIsVisible = false }
                val productDetails = backStackEntry.toRoute<ProductDetails>()
                ProductDetails(id = productDetails.id, navController = navController)
            }
            composable<LogIn> { backStackEntry ->
                LaunchedEffect(Unit) { bottomBarIsVisible = false }
                val viewModel = backStackEntry.sharedViewModel<UserViewModel>(navController)
                Login(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable<Register> {backStackEntry->
                val viewModel = backStackEntry.sharedViewModel<UserViewModel>(navController)
                Register(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val route = destination.parent?.route ?: return koinViewModel()
    val backStackEntry = remember(this) {
        navController.getBackStackEntry(route)
    }
    return koinViewModel(viewModelStoreOwner = backStackEntry)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ECommerceAppTheme {
        Navigation()
    }
}