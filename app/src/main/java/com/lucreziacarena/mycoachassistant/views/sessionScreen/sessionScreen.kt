package com.lucreziacarena.mycoachassistant.views.sessionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.lucreziacarena.mycoachassistant.R
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.utils.Utils

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navController: NavController, athlete: AthleteModel) {
    val lapTimeList = remember { mutableListOf<String>() }
    val timeToCompleteLapMap =
        remember { mutableStateOf(mutableMapOf<Int, Long>()) } // map that match lap number and time to complete it
    val numLap = remember { mutableStateOf(0) }
    val stopWatch = remember { StopWatch() }
    var offset =  remember {  mutableStateOf( Offset(0f, 0f))}
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProfileToolbar(athlete, navController)
        },
        bottomBar = {
            ControlsBar(
                lapTimeList,
                stopWatch::start,
                stopWatch::stop,
                stopWatch::getTimOnLap,
                numLap,
                timeToCompleteLapMap,
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
        ) {
            StopWatchDisplay(
                formattedTime = stopWatch.formattedTime,
                modifier = Modifier.padding(10.dp)
            )
            Text(text = "Times List")
            //CHART
            drawChart(timeToCompleteLapMap.value, offset)
            LazyColumn {
                item {

                }
                items(items = lapTimeList ?: emptyList()) { time: String ->
                    Text(time)

                }
            }


        }
    }
}

@Composable
fun drawChart(map: MutableMap<Int, Long>, offset: MutableState<Offset>) {
    if (map.isEmpty()) {
        return
    }
    Canvas(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        //total number of lap
        val totalLap = map.size

        //distance between dots
        val lineDistance = size.width / (totalLap + 1)

        //canvasHeight
        val cHeight = size.height

        // Add some kind of a "Padding" for the initial point where the line starts.
        var currentLineDistance = 0F + lineDistance

        
        map.map { entry ->
            val newOffset = Offset(entry.key.toFloat(), (entry.value/1000).toFloat()) //time in sec
            drawLine(
                start = offset.value,
                end = newOffset,
                color = Color.Black
            )
            offset.value = newOffset

        }
    }
}

@Composable
fun StopWatchDisplay(
    formattedTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            formattedTime,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp,
            color = Color.Black
        )
        Spacer(Modifier.height(16.dp))

    }
}


@Composable
fun ProfileToolbar(athlete: AthleteModel, navController: NavController) {
    Surface(shadowElevation = 5.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp),
        ) {
            Image(
                Icons.Filled.ArrowBack,
                "arrow back icon",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { navController.popBackStack() }
            )

            Image(
                painter = rememberImagePainter(
                    athlete.picture,
                    onExecute = ImagePainter.ExecuteCallback { _, _ -> true },
                    builder = {
                        crossfade(false)
                        placeholder(R.drawable.face_placeholder)
                    }
                ),
                "athlete image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
            )
            Text(
                "${athlete.name} ${athlete.surname}'s session",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

sealed class BottomBarItem(var name: String, var icon: ImageVector?) {
    object Start : BottomBarItem("Start", Icons.Filled.PlayArrow)
    object Stop : BottomBarItem("Stop", Icons.Filled.Stop)
    object Lap : BottomBarItem("Lap", Icons.Filled.RestartAlt)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ControlsBar(
    lapTimesList: MutableList<String>,
    onstartClick: () -> Unit,
    onStopClick: () -> Unit,
    onLapClick: () -> Long,
    numLap: MutableState<Int>,
    timeToCompleteLapMap: MutableState<MutableMap<Int, Long>>,
) {
    val items = listOf(
        BottomBarItem.Stop,
        BottomBarItem.Start,
        BottomBarItem.Lap
    )
    NavigationBar(
        containerColor = androidx.compose.material.MaterialTheme.colors.primary,
        contentColor = Color.White,
        tonalElevation = 5.dp
    ) {
        val currentItem = BottomBarItem.Start

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    androidx.compose.material.Icon(
                        item.icon ?: Icons.Filled.Close,
                        contentDescription = item.name
                    )
                },
                label = { androidx.compose.material.Text(text = item.name) },
                alwaysShowLabel = true,
                selected = false,
                onClick = {
                    when (item) {
                        BottomBarItem.Lap -> {
                            numLap.value ++
                            val lapInMilli = onLapClick()
                            lapTimesList.add(Utils.formatTime(lapInMilli)) //add to times list current value
                            if (timeToCompleteLapMap.value.isNotEmpty() && timeToCompleteLapMap.value[numLap.value - 1] != null) {
                                timeToCompleteLapMap.value[numLap.value] =
                                    lapInMilli - timeToCompleteLapMap.value[numLap.value - 1]!! //insert time spent to complete last lap
                            } else {
                                timeToCompleteLapMap.value[numLap.value] = lapInMilli
                            }

                        }
                        BottomBarItem.Start -> {
                            onstartClick()
                            numLap.value = 1
                        }
                        BottomBarItem.Stop -> onStopClick()
                    }
                }
            )
        }

    }
}
