package com.kiylx.weather.ui.page.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kiylx.weather.R
import com.kiylx.weather.common.AllPrefs
import com.kiylx.weather.common.Route
import com.kiylx.weather.common.WindUnit
import com.kiylx.weather.common.tempUnit
import com.kiylx.weather.icon.WeatherIconNoRound
import com.kiylx.weather.repo.bean.DailyEntity
import com.kiylx.weather.ui.activitys.LocalNavController
import com.kiylx.weather.ui.page.component.IconLabel


/**
 * weather daily page header info
 */
@Composable
fun DailyWeatherHeaderPage(stateHolder: WeatherPagerStateHolder) {
    val state: State<DailyEntity> = stateHolder.dailyUiState.collectAsState()//页面数据
    val minutelyPrecipitationState =
        stateHolder.minutelyPrecipitationState.collectAsState()

    state.value.let { data ->
        val navController = LocalNavController.current
        val unit = tempUnit()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp, top = 8.dp)
        ) {
            //顶部信息
            Column(modifier = Modifier.fillMaxWidth()) {
                //天气信息
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    //icon and weather info
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            //天气图标
                            WeatherIconNoRound(
                                code = data.data.icon.toInt(),
                                iconSize = 90.dp,
                            )
                            //温度
                            Text(
                                text = data.data.temp + unit,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }

                        VerticalDivider(thickness = 2.dp)
                        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {

                            Text(
                                text = data.data.text,
                                style = MaterialTheme.typography.displayMedium
                            )

                            //体感温度
                            Text(
                                text = "${stringResource(id = R.string.feels_like_str)}: ${data.data.feelsLike}$unit",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = "${stringResource(id = R.string.relative_humidity)}: ${data.data.humidity}%",
                                modifier = Modifier.padding(vertical = 4.dp),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                    }
                    if (!stateHolder.showRainLineChart.value) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 4.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = minutelyPrecipitationState.value.summary,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.vis)}: ${data.data.vis}km",
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        val windSpeed = if (AllPrefs.windUnit == WindUnit.Km) {
                            "${data.data.windSpeed} ${
                                stringResource(
                                    id = R.string.wind_speed_unit_kmh
                                )
                            }"
                        } else {
                            "${data.data.windScale} ${
                                stringResource(
                                    id = R.string.wind_speed_unit_rating
                                )
                            }"
                        }
                        Text(
                            text = "${data.data.windDir}: $windSpeed",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                //格点天气或是其他的提示性信息
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (AllPrefs.gridWeather && AllPrefs.gpsAuto) {
                            //点击前往格点天气
                            IconLabel(
                                modifier = Modifier
                                    .align(Alignment.End),
                                painter = rememberVectorPainter(Icons.Filled.Info),
                                contentDescription = null,
                                text = "格点天气"
                            ) {
                                navController.navigate(Route.GRID_WEATHER)
                            }
                        }
                    }
                }
            }
        }
    }

}

