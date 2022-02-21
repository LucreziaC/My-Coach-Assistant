package com.lucreziacarena.mycoachassistant.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.lucreziacarena.mycoachassistant.R

@ExperimentalMaterial3Api
@Composable
fun TopAppBar(
    screenName: String? = null,
    navController: NavController? = null,
    route: String? = null,
    picture: String? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    backNavigation: Boolean = false
) {
    MediumTopAppBar(
        title = {
            Text(
                text = screenName ?: "",
                modifier = Modifier.padding(bottom = 0.dp),
                maxLines = 2
            )
        },
        navigationIcon = {
            if(backNavigation) {
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
            }
        },
        actions = {
            if (picture !=null) {
                Image(
                    painter = rememberImagePainter(
                        picture,
                        onExecute = ImagePainter.ExecuteCallback { _, _ -> true },
                        builder = {
                            crossfade(false)
                            placeholder(R.drawable.face_placeholder)
                        }
                    ),
                    "athlete image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    alignment = Alignment.Center

                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            scrolledContainerColor = TopAppBarDefaults.mediumTopAppBarColors()
                .containerColor(
                    scrollFraction = 0f
                ).value
        ),

    )
}
