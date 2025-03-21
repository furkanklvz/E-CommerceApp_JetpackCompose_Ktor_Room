package com.klavs.e_commerceapp.view

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
import com.klavs.e_commerceapp.routes.LogIn
import com.klavs.e_commerceapp.routes.Orders
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch

sealed class ProfileMenuItem(val title: String, val icon: ImageVector, val onClick: () -> Unit) {
    data class Orders(val onClicked: () -> Unit) : ProfileMenuItem(
        title = "Orders",
        icon = Icons.Outlined.ShoppingBag,
        onClick = onClicked
    )

    data class LogIn(val onClicked: () -> Unit) : ProfileMenuItem(
        title = "Log In",
        icon = Icons.AutoMirrored.Rounded.Login,
        onClick = onClicked
    )

    data class LogOut(val onClicked: () -> Unit) : ProfileMenuItem(
        title = "Log Out",
        icon = Icons.AutoMirrored.Rounded.Logout,
        onClick = onClicked
    )
}

@Composable
fun Profile(navController: NavHostController, globalViewModel: GlobalViewModel) {
    val token by globalViewModel.token.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    ProfileContent(
        navigate = { navController.navigate(it) },
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
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    navigate: (Any) -> Unit,
    userSignedIn: Boolean,
    onLogOut: () -> Unit
) {
    val profileMenuItems = listOf(
        ProfileMenuItem.Orders(onClicked = {navigate(Orders)}),
        if (userSignedIn) ProfileMenuItem.LogOut(onClicked = onLogOut)
        else ProfileMenuItem.LogIn(onClicked = { navigate(LogIn) })
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            items(profileMenuItems) { menuItem ->
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
        ProfileContent(
            navigate = {},
            userSignedIn = false,
            onLogOut = {}
        )
    }
}