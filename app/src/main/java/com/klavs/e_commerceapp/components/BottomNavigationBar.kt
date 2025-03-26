package com.klavs.e_commerceapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.klavs.e_commerceapp.routes._ShoppingCart
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.routes._Menu
import com.klavs.e_commerceapp.routes._Search
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme


sealed class BottomBarItem(
    val route: Any,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomBarItem(_Home, "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Search :
        BottomBarItem(_Search, "Search", Icons.Rounded.Search, Icons.Rounded.Search)

    data object ShoppingCart :
        BottomBarItem(_ShoppingCart, "Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)

    data object Menu :
        BottomBarItem(_Menu, "Menu", Icons.Rounded.Menu, Icons.Rounded.Menu)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomNavigationBar(navController: NavHostController, cartSize: Int) {
    val items = listOf(
        BottomBarItem.Home,
        BottomBarItem.Search,
        BottomBarItem.ShoppingCart,
        BottomBarItem.Menu
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ShortNavigationBar{
        items.forEach { item ->
            val selected = navBackStackEntry?.destination?.hasRoute(item.route::class) == true
            ShortNavigationBarItem(
                iconPosition = NavigationItemIconPosition.Start,
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (item.route == _ShoppingCart) {
                        BadgedBox(
                            badge = {
                                if (cartSize != 0) {
                                    Badge {
                                        Text(cartSize.toString())
                                    }
                                }
                            }) {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
                label = null//{ Text(item.title) }
            )
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    ECommerceAppTheme {
        Scaffold(bottomBar = {
            BottomNavigationBar(
                rememberNavController(),
                cartSize = 1,
            )
        }) {
            Box(Modifier.padding(it))
        }
    }
}