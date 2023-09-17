package com.kiylx.weather.ui.page.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kiylx.weather.R
import com.kiylx.weather.icon.IconText
import com.kiylx.weather.icon.TwoText
import com.kiylx.weather.icon.WeatherIcon
import com.kiylx.weather.repo.bean.DailyEntity
import com.kiylx.weather.repo.bean.IndicesEntity
import java.util.function.Function
import java.util.stream.Collectors


@Composable
fun DailyWeatherInfo(stateHolder: WeatherPagerStateHolder) {
    val dailyState: State<DailyEntity> = stateHolder.dailyUiState.asDataFlow().collectAsState()
    val todayIndicesState =stateHolder.todayIndicesData.asDataFlow().collectAsState()

    LaunchedEffect(key1 = Unit) {
        stateHolder.getTodayIndices() //天气指数
    }

    Surface {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // todo 实时天气预警卡片
            // todo 当前的天气变化横向列表
            // 三天的天气状况列表
            DayWeather(stateHolder = stateHolder)
            // 空气质量信息和杂项
            val toDayWeatherState = stateHolder.threeDayWeatherData.asDataFlow().collectAsState()
            val toDayWeather = toDayWeatherState.value.data[0]
            //日出日落
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconText(
                    title = stringResource(id = R.string.sunrise),
                    icon = painterResource(id = com.kiylx.weather.icon.R.drawable.sun_rise),
                    text = toDayWeather.sunrise,
                )
                IconText(
                    title = stringResource(id = R.string.sunset),
                    icon = painterResource(id = com.kiylx.weather.icon.R.drawable.sun_set),
                    text = toDayWeather.sunset,
                )
            }

            //月相，月初月落
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TwoText(
                    title = stringResource(id = R.string.moonrise),
                    text = toDayWeather.moonrise,
                )
                IconText(
                    title = stringResource(id = R.string.moonPhase),
                    icon = painterResource(id = WeatherIcon.getResId(toDayWeather.moonPhaseIcon.toInt())),
                    text = toDayWeather.moonPhase
                )
                TwoText(
                    title = stringResource(id = R.string.moonset),
                    text = toDayWeather.moonset,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconText(
                    title = stringResource(id = R.string.pressure),
                    icon = painterResource(id = com.kiylx.weather.icon.R.drawable.pressure),
                    iconSize = 40.dp,
                    text = dailyState.value.data.pressure + " 百帕",
                )
                IconText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.uvIndex),
                    icon = painterResource(id = com.kiylx.weather.icon.R.drawable.ultraviolet),
                    iconSize = 40.dp,
                    text = toDayWeather.uvIndex
                )

            }
//            Row {
//                IconText(
//                    title = stringResource(id = R.string.vis),
//                    text = dailyState.value.data.vis + " 公里",
//                    icon = painterResource(id = com.kiylx.weather.icon.R.drawable.vis),
//                )
//            }

            //天气指数
            Surface {//运动指数1 洗车指数2 穿衣指数3 感冒指数9
                val indiectMap =todayIndicesState.value.data.stream().collect(Collectors.toMap(IndicesEntity.Daily::type,
                    Function.identity()))

                Card(modifier = Modifier.padding(8.dp)) {
                    Column {
                        Row {
                            IconText(
                                modifier = Modifier.weight(1f),
                                title = stringResource(id = R.string.sportIndex),
                                icon = painterResource(id = com.kiylx.weather.icon.R.drawable.sport),
                                iconSize = 40.dp,
                                text = indiectMap["1"]?.category?:"",
                                backgroundColor = Color.Transparent
                            )
                            IconText(
                                modifier = Modifier.weight(1f),
                                title = stringResource(id = R.string.wash_car_index),
                                icon = painterResource(id = com.kiylx.weather.icon.R.drawable.wash_car),
                                iconSize = 40.dp,
                                text = indiectMap["2"]?.category?:"",
                                backgroundColor = Color.Transparent
                            )
                        }
                        Row {
                            IconText(
                                modifier = Modifier.weight(1f),
                                title = stringResource(id = R.string.dress_index),
                                icon = painterResource(id = com.kiylx.weather.icon.R.drawable.wear_dress),
                                iconSize = 40.dp,
                                text = indiectMap["3"]?.category?:"",
                                backgroundColor = Color.Transparent

                            )
                            IconText(
                                modifier = Modifier.weight(1f),
                                title = stringResource(id = R.string.cold_index),
                                icon = painterResource(id = com.kiylx.weather.icon.R.drawable.cold),
                                iconSize = 40.dp,
                                text = indiectMap["9"]?.category?:"",
                                backgroundColor = Color.Transparent
                            )
                        }
                    }

                }
            }

        }
    }

}
