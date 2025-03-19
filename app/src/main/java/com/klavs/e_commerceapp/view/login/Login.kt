package com.klavs.e_commerceapp.view.login

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.klavs.e_commerceapp.components.LoginTextField
import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import com.klavs.e_commerceapp.routes.Register
import com.klavs.e_commerceapp.routes._Home
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import com.klavs.e_commerceapp.util.Resource
import com.klavs.e_commerceapp.viewmodel.UserViewModel

@Composable
fun Login(viewModel: UserViewModel, navController: NavHostController) {
    val logInResource by viewModel.logInResource.collectAsStateWithLifecycle()
    LaunchedEffect(logInResource) {
        if (logInResource.isSuccess()){
            navController.navigate(_Home){
                val startDestinationId = navController.graph.startDestinationId
                popUpTo(startDestinationId) {
                    inclusive = true
                    saveState = false
                }
                restoreState = false
            }
        }
    }
    LoginContent(
        logInResource = logInResource,
        onLogIn = { viewModel.logIn(it) },
        onRegister = {navController.navigate(Register)}
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoginContent(
    logInResource: Resource<LogInResponse>,
    onLogIn: (LogInRequest) -> Unit,
    onRegister: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Log In")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LoginTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = "User Name",
                        leadingIcon = Icons.Rounded.Person
                    )
                    LoginTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = Icons.Rounded.Lock
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                FilledTonalButton(
                    onClick = {
                        if (userName.isNotBlank() && password.isNotBlank()) {
                            onLogIn(LogInRequest(userName, password))
                        }
                    },
                    modifier = Modifier.widthIn(180.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Log In")
                        Crossfade(logInResource.isLoading()) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(start = 5.dp)
                                        .size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    modifier = Modifier.padding(start = 5.dp),
                                    imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                                    contentDescription = "Log in"
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(vertical = 15.dp)
                )
                FilledTonalButton(
                    onClick = onRegister,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.widthIn(180.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text("Register")
                        Icon(
                            modifier = Modifier.padding(start = 5.dp),
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = "Log in"
                        )
                    }
                }
                if(logInResource.isError()) {
                    Text(
                        text = (logInResource as Resource.Error).throwable.message ?: "Unknown Error",
                        modifier = Modifier.padding(20.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    val logInResponse = LogInResponse(
        token = "token value",
        fullName = "Furkan Kılavuz",
        userName = "furkanklvz",
        email = "furkanklvz0@gmail.com"
    )
    ECommerceAppTheme {
        LoginContent(
            logInResource = Resource.Success(logInResponse),
            onLogIn = {},
            onRegister = {}
        )
    }
}