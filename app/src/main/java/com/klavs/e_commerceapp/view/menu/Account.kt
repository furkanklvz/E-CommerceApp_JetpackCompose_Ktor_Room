package com.klavs.e_commerceapp.view.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.klavs.e_commerceapp.data.model.entity.Account
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme

@Composable
fun Account(navController: NavHostController, account: Account) {
    AccountContent(
        account = account,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AccountContent(account: Account, navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                subtitle = { Text(account.userName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                titleHorizontalAlignment = Alignment.CenterHorizontally,
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledTonalIconButton(
                            onClick = {},
                            modifier = Modifier.size(IconButtonDefaults.largeContainerSize())
                        ) {
                            Text("${account.userName.first()}")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            Column {
                ListItem(
                    headlineContent = { Text("E- Mail") },
                    supportingContent = { Text(account.email) },
                )
                ListItem(
                    headlineContent = { Text("Full Name") },
                    supportingContent = { Text(account.fullName) },
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AccountContentPreview() {
    ECommerceAppTheme {
        AccountContent(
            account = Account(
                token = "TODO()",
                createdAt = 1,
                fullName = "TODO()",
                userName = "TODO()",
                email = "TODO()"
            ),
            navController = rememberNavController()
        )
    }
}