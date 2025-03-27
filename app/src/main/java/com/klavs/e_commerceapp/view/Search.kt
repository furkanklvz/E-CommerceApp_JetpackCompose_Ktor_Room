package com.klavs.e_commerceapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.klavs.e_commerceapp.ui.theme.ECommerceAppTheme
import kotlinx.coroutines.launch

@Composable
fun Search() {
    SearchContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchContent() {
    val searchBarState = rememberSearchBarState(
        initialValue = SearchBarValue.Expanded
    )
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            modifier = Modifier,
            placeholder = { Text("Search...") },
            leadingIcon = {
                if (searchBarState.currentValue == SearchBarValue.Expanded) {
                    IconButton(
                        onClick = { scope.launch { searchBarState.snapTo(0f) } }
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            /*TopSearchBar(
                shape = RoundedCornerShape(20.dp),
                scrollBehavior = scrollBehavior,
                state = searchBarState,
                inputField = inputField
            )*/
            TopAppBar(title = {},
                actions = {IconButton(onClick = {scope.launch { searchBarState.snapTo(1f) }}) {
                    Icon(imageVector = Icons.Rounded.Search,
                         contentDescription = null)
                } })
            ExpandedFullScreenSearchBar(
                state = searchBarState,
                inputField = inputField
            ) { }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchPreview() {
    ECommerceAppTheme {
        SearchContent()
    }
}