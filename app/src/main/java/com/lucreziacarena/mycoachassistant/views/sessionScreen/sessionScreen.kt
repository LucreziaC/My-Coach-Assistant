package com.lucreziacarena.mycoachassistant.views.sessionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.lucreziacarena.mycoachassistant.R
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.utils.Utils

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navController: NavController, athlete: AthleteModel, meters: Int) {
    val lapTimeList = remember { mutableListOf<String>() }
    val timeToCompleteLapMilli =remember { mutableListOf(1L) } // map that match lap number and time to complete it
    val totalTime = remember { mutableStateOf(1L) }
    val numLap = remember { mutableStateOf(1) }
    val sessionIsClosed = remember { mutableStateOf(false) }
    val stopWatch = remember { StopWatch() }
    var pointList = remember { mutableStateOf<ArrayList<Point>>(ArrayList()) }
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
                sessionIsClosed,
                pointList,
                lapTimeList,
                stopWatch::start,
                stopWatch::stop,
                stopWatch::getTimOnLap,
                numLap,
                timeToCompleteLapMilli,
                totalTime
            )
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (stopwatch, timelist,stats) = createRefs()
            StopWatchDisplay(
                formattedTime = stopWatch.formattedTime,
                modifier = Modifier
                    .padding(10.dp)
                    .constrainAs(stopwatch) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Column(
                modifier = Modifier.constrainAs(timelist) {
                    top.linkTo(stopwatch.bottom)
                    end.linkTo(stats.start)
                    width = Dimension.fillToConstraints
                },

                ) {
                Text(text = "Times List", fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 4.dp))
                //drawChart(timeToCompleteLapMap.value, pointList.value)
                LazyColumn {
                    itemsIndexed(items = lapTimeList) { index, time: String ->
                        Text("Lap ${index + 1}: $time")
                    }
                }
            }
            Column( modifier = Modifier.constrainAs(stats) {
                top.linkTo(timelist.top)
                end.linkTo(parent.end)
                start.linkTo(timelist.start)
                width = Dimension.fillToConstraints


            } ){
                Text(text = "Stats", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                //drawChart(timeToCompleteLapMap.value, pointList.value)
                val speedMax = timeToCompleteLapMilli.map{time->
                    meters/time
                }.maxOrNull()
                if (speedMax != null) {
                    info("Peak speed: ", "${speedMax.toDouble()} m/s")
                }
                    //average time/lap
                if(numLap.value>0) {
                    val averageTimeLap = totalTime.value / numLap.value
                    info("Average time/lap: ", "${averageTimeLap.toDouble()}")
                }
                    //average speed
                    val averageSpeed = (numLap.value*meters)/totalTime.value
                    info("Average speed: ","${averageSpeed.toDouble()}")

            }

            //CHART
            /*if(draw.value)
                LineChart( pointList)*/


        }
    }
}

@Composable
fun info(label: String, stat: String) {
    Row{
        Text(label)
        Text(stat)
    }

}


@Composable
fun StopWatchDisplay(
    formattedTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
    sessionIsClosed: MutableState<Boolean>,
    pointList: MutableState<ArrayList<Point>>,
    lapTimesList: MutableList<String>,
    onstartClick: () -> Unit,
    onStopClick: () -> Unit,
    getCurrentTime: () -> Long,
    numLap: MutableState<Int>,
    timeToCompleteLapMap: MutableList<Long>,
    totalTime: MutableState<Long>,
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
                            numLap.value++
                            totalTime.value = getCurrentTime()
                            if (timeToCompleteLapMap.isNotEmpty() && timeToCompleteLapMap[numLap.value - 1] != null) {
                                // time spent to complete lap in a map with key = lap number and value = millisec
                                timeToCompleteLapMap[numLap.value] =
                                    totalTime.value - timeToCompleteLapMap[numLap.value - 1]!!
                                //point for chart
                                pointList.value.add(
                                    Point(
                                        numLap.value.toFloat(),
                                        ((totalTime.value - timeToCompleteLapMap[numLap.value - 1]) / 1000).toFloat()
                                    )
                                )
                                // lap time in list
                                lapTimesList.add(Utils.formatTime((totalTime.value - timeToCompleteLapMap[numLap.value - 1])))
                            } else {
                                timeToCompleteLapMap[numLap.value] = totalTime.value
                                pointList.value.add(Point(numLap.value.toFloat(), (totalTime.value / 1000).toFloat()))
                                lapTimesList.add(Utils.formatTime(totalTime.value))
                            }

                        }
                        BottomBarItem.Start -> {
                            onstartClick()
                            numLap.value = 0
                        }
                        BottomBarItem.Stop -> {
                            totalTime.value = getCurrentTime()
                            lapTimesList.add(Utils.formatTime((totalTime.value - timeToCompleteLapMap[numLap.value - 1])))
                            sessionIsClosed.value = true
                            onStopClick()
                        }
                    }
                }
            )
        }

    }
}

data class Point(val X: Float = 0f, val Y: Float = 0f)


@Composable
fun LineChart(pointList: MutableState<ArrayList<Point>>) {
    // Used to record the zoom size
    var scale by remember { mutableStateOf(1f) }
    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
        scale *= zoomChange
    }
    val point = pointList.value

    val path = Path()
    for ((index, item) in point.withIndex()) {
        val x = item.X * 50
        val y = (item.Y * 50) - 300
        if (index == 0) {
            path.moveTo(x, -y)
        } else {
            path.lineTo(x, -y)
        }
    }

    Canvas(
        modifier = Modifier
            .height(120.dp)
            .background(Color.White)
            // Monitor gesture scaling
            .graphicsLayer(
            ).transformable(state)
    ) {
        // draw  y Axis
        drawLine(
            start = Offset(10f, 300f),
            end = Offset(10f, 0f),
            color = Color.Black,
            strokeWidth = 2f
        )
        // x Axis
        drawLine(
            start = Offset(10f, 300f),
            end = Offset(510f, 300f),
            color = Color.Black,
            strokeWidth = 2f
        )
        // draw path
        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 2f)
        )
    }
}
