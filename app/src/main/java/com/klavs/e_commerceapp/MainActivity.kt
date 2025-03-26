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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.klavs.e_commerceapp.components.BottomNavigationBar
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.routes.Account
import com.klavs.e_commerceapp.routes.CreateOrder
import com.klavs.e_commerceapp.routes.LogIn
import com.klavs.e_commerceapp.routes.Orders
import com.klavs.e_commerceapp.routes.ProductDetails
import com.klavs.e_commerceapp.routes.Register
import com.klavs.e_commerceapp.routes.ShoppingCartTop
import com.klavs.e_commerceapp.routes._ShoppingCart
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.routes._Menu
import com.klavs.e_commerceapp.routes._Search
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.view.cart.ShoppingCart
import com.klavs.e_commerceapp.view.Home
import com.klavs.e_commerceapp.view.menu.orders.Orders
import com.klavs.e_commerceapp.view.ProductDetails
import com.klavs.e_commerceapp.view.menu.Menu
import com.klavs.e_commerceapp.view.Search
import com.klavs.e_commerceapp.view.cart.create_order.CreateOrder
import com.klavs.e_commerceapp.view.login.Login
import com.klavs.e_commerceapp.view.login.Register
import com.klavs.e_commerceapp.view.menu.Account
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import com.klavs.e_commerceapp.viewmodel.HomeViewModel
import com.klavs.e_commerceapp.viewmodel.OrderViewModel
import com.klavs.e_commerceapp.viewmodel.ProductViewModel
import com.klavs.e_commerceapp.viewmodel.UserViewModel
import kotlinx.serialization.json.Json
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
    val account by globalViewModel.account.collectAsStateWithLifecycle()
    val cartResource by globalViewModel.cart.collectAsStateWithLifecycle()
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
            BottomNavigationBar(
                navController = navController,
                cartSize = (cartResource as? Resource.Success)?.data?.cartItems?.size ?: 0
            )
        }
    }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(
                bottom = if (bottomBarIsVisible)
                    innerPadding.calculateBottomPadding() else 0.dp
            ),
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
                    ShoppingCart(globalViewModel = globalViewModel, navController = navController)
                }
            }
            composable<_Menu> {
                LaunchedEffect(Unit) { bottomBarIsVisible = true }
                Menu(
                    navController = navController,
                    globalViewModel = globalViewModel
                )
            }
            composable<Orders> { backStackEntry ->
                LaunchedEffect(Unit) { bottomBarIsVisible = false }
                val viewModel = backStackEntry.sharedViewModel<OrderViewModel>(navController)
                Orders(
                    navController = navController,
                    orderViewModel = viewModel,
                    token = account?.token
                )
            }



            composable<ProductDetails> { backStackEntry ->
                LaunchedEffect(Unit) { bottomBarIsVisible = false }
                val productDetails = backStackEntry.toRoute<ProductDetails>()
                val viewModel = koinViewModel<ProductViewModel>()
                ProductDetails(
                    id = productDetails.id,
                    globalViewModel = globalViewModel,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable<LogIn> { backStackEntry ->
                LaunchedEffect(Unit) { bottomBarIsVisible = false }
                val viewModel = backStackEntry.sharedViewModel<UserViewModel>(navController)
                Login(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable<Register> { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<UserViewModel>(navController)
                Register(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable<CreateOrder> { backStackEntry ->
                if (account != null) {
                    LaunchedEffect(Unit) { bottomBarIsVisible = false }
                    val viewModel = backStackEntry.sharedViewModel<CartViewModel>(navController)
                    val cart =
                        Json.decodeFromString<Cart>(backStackEntry.toRoute<CreateOrder>().cart)
                    CreateOrder(
                        navController = navController,
                        cartViewModel = viewModel,
                        globalViewModel = globalViewModel,
                        account = account!!,
                        cart = cart
                    )
                }
            }
            composable<Account>{backStackEntry ->
                account?.let {
                    LaunchedEffect(Unit) { bottomBarIsVisible = false }
                    Account(
                        navController = navController,
                        account = it
                    )
                }
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