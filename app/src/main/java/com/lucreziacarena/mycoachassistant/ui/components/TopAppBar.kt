package com.lucreziacarena.mycoachassistant.ui.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@ExperimentalMaterial3Api
@Composable
fun TopAppBar(
    screenName: String? = null,
    navController: NavController? = null,
    route: String? = null,
    loading: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    MediumTopAppBar(
        title = {
            Text(
                text = screenName ?: "",
                modifier = Modifier.padding(bottom = 0.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (route != null) {
                    navController?.navigate(route)
                } else {
                    navController?.popBackStack()
                }
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "",
                )
            }
        },
        actions = {
            if (loading)
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(30.dp).size(25.dp)
                )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            scrolledContainerColor = TopAppBarDefaults.mediumTopAppBarColors()
                .containerColor(
                    scrollFraction = 0f
                ).value
        )

    )
}
