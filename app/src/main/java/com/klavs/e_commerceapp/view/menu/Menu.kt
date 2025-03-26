package com.klavs.e_commerceapp.view.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.klavs.e_commerceapp.routes.Account
import com.klavs.e_commerceapp.routes.LogIn
import com.klavs.e_commerceapp.routes.Orders
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch

sealed class MenuItem(val title: String, val icon: ImageVector, val onClick: () -> Unit) {
    data class Orders(val onClicked: () -> Unit) : MenuItem(
        title = "Orders",
        icon = Icons.Outlined.ShoppingBag,
        onClick = onClicked
    )

    data class Account(val onClicked: () -> Unit) : MenuItem(
        title = "Account",
        icon = Icons.Rounded.AccountCircle,
        onClick = onClicked
    )

    data class LogIn(val onClicked: () -> Unit) : MenuItem(
        title = "Log In",
        icon = Icons.AutoMirrored.Rounded.Login,
        onClick = onClicked
    )

    data class LogOut(val onClicked: () -> Unit) : MenuItem(
        title = "Log Out",
        icon = Icons.AutoMirrored.Rounded.Logout,
        onClick = onClicked
    )
}

@Composable
fun Menu(navController: NavHostController, globalViewModel: GlobalViewModel) {
    val token by globalViewModel.account.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    MenuContent(
        userSignedIn = token != null,
        onLogOut = {
            scope.launch {
                try {
                    globalViewModel.logout()
                    navController.navigate(_Home) {
                        val startDestination = navController.graph.startDestinationId
                        popUpTo(startDestination) {
                            inclusive = true
                            saveState = false
                        }
                        restoreState = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuContent(
    navController: NavHostController,
    userSignedIn: Boolean,
    onLogOut: () -> Unit
) {
    val menuItems = if (userSignedIn) {
        listOf(
            MenuItem.Account(onClicked = { navController.navigate(Account) }),
            MenuItem.Orders(onClicked = { navController.navigate(Orders) }),
            MenuItem.LogOut(onClicked = onLogOut)
        )
    }else {
        listOf(
            MenuItem.LogIn(onClicked = { navController.navigate(LogIn) })
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Menu")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            items(menuItems) { menuItem ->
                MenuRow(title = menuItem.title, icon = menuItem.icon, onClick = menuItem.onClick)
            }
        }
    }
}

@Composable
private fun MenuRow(title: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = title)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                contentDescription = "go"
            )
        },
        headlineContent = {
            Text(title)
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ProfilePreview() {
    ECommerceAppTheme {
        MenuContent(
            userSignedIn = false,
            onLogOut = {},
            navController = rememberNavController()
        )
    }
}