package com.lucreziacarena.mycoachassistant.views.sessionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.lucreziacarena.mycoachassistant.R
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesError
import com.lucreziacarena.mycoachassistant.ui.components.errorDialog
import com.lucreziacarena.mycoachassistant.utils.Utils
import com.lucreziacarena.mycoachassistant.views.athletesScreen.States
import java.text.DecimalFormat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(navController: NavController, athlete: AthleteModel, meters: Int) {
    val viewModel = hiltViewModel<SessionScreenViewModel>()
    val lapTimeList = remember { mutableListOf<String>() }
    val lapTimeMap =
        remember { mutableStateOf(mutableMapOf<Int, Long>()) } // map that match lap number and time to complete it
    val totalTime = remember { mutableStateOf(1L) }
    val numLap = remember { mutableStateOf(1) }
    val lastTimeRecorded = remember { mutableStateOf(0L) }
    val sessionIsClosed = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }
    val stopWatch = remember { StopWatch() }
    var pointList = remember { mutableStateOf<ArrayList<Point>>(ArrayList()) }
    var speedMax by remember { mutableStateOf(0L) }

    val showErrorDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

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
                lapTimeMap,
                totalTime,
                lastTimeRecorded
            )
        }
    ) {

        observeStates(viewModel.state, navController,loading, errorMessage, showErrorDialog)
        if(loading.value){
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if(showErrorDialog.value){
            errorDialog(showErrorDialog, errorMessage)
        }else {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (stopwatch, stats, chart) = createRefs()
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
                Row(
                    modifier = Modifier
                        .constrainAs(stats) {
                            top.linkTo(stopwatch.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Times List",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        //drawChart(timeToCompleteLapMap.value, pointList.value)
                        LazyColumn {
                            itemsIndexed(items = lapTimeList) { index, time: String ->
                                Text("Lap ${index + 1}: $time")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(50.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Stats",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        //drawChart(timeToCompleteLapMap.value, pointList.value)
                        val timesInMillis = mutableListOf<Long>()
                        val meter = 100
                        lapTimeMap.value.forEach { (key, value) -> timesInMillis.add(value) }
                        val df = DecimalFormat("###.##")

                        timesInMillis.map {
                            try {
                                val speed = meters / (it / 1000)
                                if (speed > speedMax)
                                    speedMax = (it / 1000)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        if (speedMax != null) {
                            info("Peak speed: ", "${speedMax.toDouble()} m/s")
                        }
                        //average time/lap
                        if (numLap.value > 0) {
                            val averageTimeLap = df.format((totalTime.value / 1000).toDouble() / numLap.value)

                            info("Average time/lap: ", averageTimeLap)
                        }
                        //average speed
                        val averageSpeed = df.format((numLap.value * meter) / (totalTime.value / 1000).toDouble())
                        info("Average speed: ", averageSpeed)
                    }
                }
                Spacer(Modifier.height(60.dp))

                //CHART
                if (sessionIsClosed.value) {
                    LineChart(pointList, Modifier
                        .height(300.dp)
                        .padding(20.dp)
                        .constrainAs(chart) {
                            top.linkTo(stats.bottom)
                            start.linkTo(stopwatch.start)
                            end.linkTo(stopwatch.end)
                        })
                    viewModel.send(SessionScreenEvent.OnCloseSession(athlete, numLap.value, speedMax))
                }
            }
        }
    }
}

@Composable
fun info(label: String, stat: String) {
    Row {
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
    lapTimeMap: MutableState<MutableMap<Int, Long>>,
    totalTime: MutableState<Long>,
    lastTimeRecorded: MutableState<Long>,
) {
    val items = listOf(
        BottomBarItem.Stop,
        BottomBarItem.Start,
        BottomBarItem.Lap
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
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
                label = { androidx.compose.material.Text(text = item.name, color = MaterialTheme.colorScheme.onPrimary) },
                alwaysShowLabel = true,
                selected = false,
                onClick = {
                    when (item) {
                        BottomBarItem.Lap -> {
                            numLap.value++
                            totalTime.value = getCurrentTime()
                            if (lapTimeMap.value.isNotEmpty() && lastTimeRecorded.value != 0L) {
                                val newTime = totalTime.value - lastTimeRecorded.value
                                // time spent to complete lap in a map with key = lap number and value = millisec
                                lapTimeMap.value[numLap.value] = newTime
                                //point for chart
                                pointList.value.add(
                                    Point(
                                        numLap.value.toFloat(),
                                        (newTime / 1000).toFloat() //in second
                                    )
                                )
                                // lap time in list
                                lapTimesList.add(Utils.formatTime(newTime))
                                lastTimeRecorded.value = totalTime.value
                            } else { // è il primo tempo registrato
                                lastTimeRecorded.value = totalTime.value
                                lapTimeMap.value[numLap.value] = totalTime.value
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
                            val newTime = totalTime.value - lastTimeRecorded.value
                            lapTimesList.add(Utils.formatTime(newTime))
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
fun LineChart(pointList: MutableState<ArrayList<Point>>, modifier: Modifier) {

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
        modifier = modifier
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


fun observeStates(
    state: MutableState<SessionState>,
    navController: NavController,
    showLoading: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    showErrorDialog: MutableState<Boolean>,
) {
    when (state.value) {
        is SessionState.Content -> {
            showLoading.value = false
            navController.popBackStack()

        }
        SessionState.Empty -> {
            showLoading.value = false
        }
        is SessionState.Error -> {
            when(val error = (state.value as SessionState.Error).error){
                is AthletesError.GenericError -> {
                    errorMessage.value = error.message ?: "Errore"
                    showErrorDialog.value = true
                }
                AthletesError.NoAthletesFound -> {}
            }
            showLoading.value = false


        }
        SessionState.Loading -> showLoading.value = true
    }
}
