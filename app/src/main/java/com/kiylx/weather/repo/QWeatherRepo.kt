package com.kiylx.weather.repo

import com.kiylx.libx.http.retrofit.RetrofitHolder
import com.kiylx.weather.common.AllPrefs
import com.kiylx.weather.http.minutesToSeconds
import com.kiylx.weather.repo.api.Api
import com.kiylx.weather.repo.bean.DailyAirEntity
import com.kiylx.weather.repo.bean.DailyEntity
import com.kiylx.weather.repo.bean.DayAirEntity
import com.kiylx.weather.repo.bean.DayWeather
import com.kiylx.weather.repo.bean.HourWeatherEntity
import com.kiylx.weather.repo.bean.IndicesEntity
import com.kiylx.weather.repo.bean.LocationEntity
import com.kiylx.weather.repo.bean.LocationEntity.Companion.toLatLonStr
import com.kiylx.weather.repo.bean.MinutelyPrecipitationEntity
import com.kiylx.weather.repo.bean.WarningEntity
import com.kiylx.weather.ui.page.main.DayWeatherType

/**
 * 调用和风天气api，并在这里提供数据
 */
object QWeatherRepo {

    private val api by lazy {
        RetrofitHolder.getInstance()!!.createApi(Api::class.java)
    }

    //<editor-fold desc="格点天气">
    /**
     * 获取实时天气 会查询本地副本
     */
    suspend fun getDailyReport_Grid(
        location: LocationEntity,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DailyEntity {
        val cacheTime = if (noCache) null else AllPrefs.dailyInterval.minutesToSeconds()
        val res =
            api.getGridDaily(
                location.toLatLonStr(),
                lang,
                unit,
                cacheTime
            ).await()
        return res ?: DailyEntity()
    }

    /**
     * 获取24小时天气 会查询本地副本
     */
    suspend fun getDailyHourReport_Grid(
        location: LocationEntity,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): HourWeatherEntity {
        val cacheTime = if (noCache) null else AllPrefs.hourWeatherInterval.minutesToSeconds()
        val res =
            //默认位置且开启gps更新，需要使用经纬度获取格点数据
            api.getGridHourWeather(
                location.toLatLonStr(),
                lang,
                unit,
                cacheTime
            ).await()

        return res ?: HourWeatherEntity()
    }

    /**
     * 获取未来天气状况 会查询本地副本
     */
    suspend fun getDayReport_Grid(
        location: LocationEntity,
        type: Int = DayWeatherType.threeDayWeather,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DayWeather {
        val cacheTime = if (noCache) null else AllPrefs.dayWeatherInterval.minutesToSeconds()
        val res =
            when (type) {
                DayWeatherType.threeDayWeather -> {
                    api.getGridDayWeather3d(
                        location.toLatLonStr(),
                        lang, unit, cacheTime
                    )
                }

                DayWeatherType.sevenDayWeather -> {
                    api.getGridDayWeather7d(
                        location.toLatLonStr(),
                        lang, unit, cacheTime
                    )
                }

                DayWeatherType.fifteenDayWeather -> {
                    api.getDayWeather15d(
                        location.toLatLonStr(),
                        lang,
                        unit,
                        cacheTime
                    )
                }

                else -> {
                    throw IllegalArgumentException("illegal type")
                }
            }.await()
        return res ?: DayWeather()
    }
    //</editor-fold>

//<editor-fold desc="网络接口-非格点天气">
    /**
     * 获取实时天气 会查询本地副本
     */
    suspend fun getDailyReport(
        location: LocationEntity,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DailyEntity {
        val cacheTime = if (noCache) null else AllPrefs.dailyInterval.minutesToSeconds()
        val res = api.getDaily(location.id, lang, unit, cacheTime).await()
        return res ?: DailyEntity()
    }

    /**
     * 获取24小时天气 会查询本地副本
     */
    suspend fun getDailyHourReport(
        location: LocationEntity,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): HourWeatherEntity {
        val cacheTime = if (noCache) null else AllPrefs.hourWeatherInterval.minutesToSeconds()
        val res = api.getHourWeather(location.toLatLonStr(), lang, unit, cacheTime).await()
        return res ?: HourWeatherEntity()
    }

    /**
     * 获取天气预警 会查询本地副本
     */
    suspend fun getWarningNow(
        location: LocationEntity,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): WarningEntity {
        val cacheTime = if (noCache) null else AllPrefs.earlyWarningInterval.minutesToSeconds()
        val res = api.getWarningNow(location.toLatLonStr(), lang, cacheTime).await()
        return res ?: WarningEntity()
    }

    /**
     * 获取分钟级降水预报 会查询本地副本
     */
    suspend fun getMinutelyPrecipitation(
        location: LocationEntity,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): MinutelyPrecipitationEntity {
        val cacheTime = if (noCache) null else AllPrefs.weatherMinutelyInterval.minutesToSeconds()
        val res = api.getMinutelyPrecipitation(location.toLatLonStr(), lang, cacheTime).await()
        return res ?: MinutelyPrecipitationEntity()
    }

    /**
     * 获取未来天气状况 会查询本地副本
     */
    suspend fun getDayReport(
        location: LocationEntity,
        type: Int = DayWeatherType.threeDayWeather,
        unit: String = AllPrefs.unit,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DayWeather {
        val cacheTime = if (noCache) null else AllPrefs.dayWeatherInterval.minutesToSeconds()
        val res =
            when (type) {
                DayWeatherType.threeDayWeather -> {
                    api.getDayWeather3d(
                        location.toLatLonStr(),
                        lang, unit, cacheTime
                    )
                }

                DayWeatherType.sevenDayWeather -> {
                    api.getDayWeather7d(
                        location.toLatLonStr(),
                        lang, unit, cacheTime
                    )
                }

                DayWeatherType.fifteenDayWeather -> {
                    api.getDayWeather15d(
                        location.toLatLonStr(),
                        lang,
                        unit,
                        cacheTime
                    )
                }

                else -> {
                    throw IllegalArgumentException("illegal type")
                }
            }.await()
        return res ?: DayWeather()
    }


    /**
     * 获取实时空气质量 会查询本地副本
     */
    suspend fun getDailyAir(
        location: LocationEntity,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DailyAirEntity {
        val cacheTime = if (noCache) null else AllPrefs.weatherAirInterval.minutesToSeconds()
        val res = api.getDailyAir(location.toLatLonStr(), lang, cacheTime).await()
        return res ?: DailyAirEntity()
    }

    /**
     * 获取未来5天空气质量 会查询本地副本
     */
    suspend fun getDayAir(
        location: LocationEntity,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): DayAirEntity {
        val cacheTime = if (noCache) null else AllPrefs.weatherAirDayInterval.minutesToSeconds()
        val res = api.getDayAir(location.toLatLonStr(), lang, cacheTime).await()
        return res ?: DayAirEntity()
    }


    /**
     * 获取当天的天气指数 会查询本地副本
     */
    suspend fun getIndices1d(
        location: LocationEntity,
        type: String,
        lang: String = AllPrefs.lang,
        noCache: Boolean = false
    ): IndicesEntity {
        val cacheTime = if (noCache) null else AllPrefs.weatherIndicesInterval.minutesToSeconds()
        val res = api.getIndices1d(location.toLatLonStr(), lang, type, cacheTime).await()
        return res ?: IndicesEntity()
    }


//</editor-fold>

}