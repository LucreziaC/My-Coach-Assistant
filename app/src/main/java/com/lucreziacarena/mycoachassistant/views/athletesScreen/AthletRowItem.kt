package com.lucreziacarena.mycoachassistant.views.athletesScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.ui.components.loadPicture

@Composable
fun AthleteRowItem(athlet: AthleteModel, onClick: () -> Unit = {}) {
    ConstraintLayout(modifier = Modifier.padding(10.dp).fillMaxWidth().clickable { onClick() })
    {
        val (image, fullname, goNextIcon) = createRefs()
        val loadImage = loadPicture(athlet.picture, placeholder = Icons.Default.Face)
        loadImage.value?.asImageBitmap()?.let {
            Image(
                it,
                "athlete image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(0.dp).padding(end = 25.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
        Text(
            athlet.name + " " + athlet.surname,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.constrainAs(fullname) {
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
                start.linkTo(image.end)
                width = Dimension.fillToConstraints
            }
        )

        Icon(
            Icons.Filled.NavigateNext,
            "arrow icon",
            modifier = Modifier
                .padding(end = 10.dp)
                .constrainAs(goNextIcon) {
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}
