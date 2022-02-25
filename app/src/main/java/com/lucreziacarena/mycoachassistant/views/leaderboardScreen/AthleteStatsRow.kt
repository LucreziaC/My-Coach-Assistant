package com.lucreziacarena.mycoachassistant.views.leaderboardScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.lucreziacarena.mycoachassistant.R
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.lucreziacarena.mycoachassistant.repository.models.AthleteStatsModel




@Composable
fun AthleteStatsRowItem(athlet: AthleteStatsModel, peakSpeed: Boolean, numLap:Boolean ) {
    ConstraintLayout(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
    )
    {
        val (image, fullname, goNextIcon) = createRefs()
        Image(
            painter = rememberImagePainter(
                athlet.picture,
                onExecute = ImagePainter.ExecuteCallback { _, _ -> true },
                builder = {
                    crossfade(false)
                    placeholder(R.drawable.face_placeholder)
                }
            ),
            "athlete image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(0.dp)
                .padding(end = 25.dp)
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Column(modifier = Modifier.constrainAs(fullname) {
            top.linkTo(image.top)
            bottom.linkTo(image.bottom)
            start.linkTo(image.end)
            width = Dimension.fillToConstraints
        }) {
            Text(
                athlet.name + " " + athlet.surname,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp

            )
            Text(
                "peak speed: ${athlet.peakSpeed}",
                fontWeight = if(peakSpeed) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                "numLap: ${athlet.numLap}",
                fontWeight = if(numLap) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,

            )
        }


    }
}
