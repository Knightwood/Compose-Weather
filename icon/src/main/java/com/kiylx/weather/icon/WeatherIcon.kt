package com.kiylx.weather.icon

import android.annotation.SuppressLint
import android.app.Application
import android.util.SparseArray
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object WeatherIcon {
    const val unKnowId = 999

    //save all icon
    private val sparseArray: SparseArray<Int> = SparseArray()
    private lateinit var ctx: Application

    fun init(ctx: Application) {
        this.ctx = ctx
    }

    /**
     * get mipmap ResId by code
     */
    @SuppressLint("DiscouragedApi")
    fun getResId(code: Int = unKnowId): Int {
        return sparseArray[code] ?: let {
            var tmp: Int = ctx.resources.getIdentifier(
                "u_$code",
                "mipmap",
                ctx.packageName
            )
            if (tmp == 0) {
                tmp = unKnowId
            }
            sparseArray[code] = tmp
            tmp
        }
    }

}

@Composable
@Preview
fun WithIconTextPreview() {
    WithIconText(
        icon = Icons.Default.AppSettingsAlt,
    )
}

@Composable
fun WithIconText(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
    icon: Any? = null,
    title: String = "title",
    text: String? = "描述文本",
) {
    Row(
        modifier = modifier.padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        icon?.let {
            Box(modifier = Modifier.padding(end = 8.dp)) {
                CircleIcon(model = icon)
            }
        }
        Column() {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            text?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun WeatherIcon(
    code: Int, modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.secondary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    backgroundShape: Shape = CircleShape,
    iconSize: Dp = 42.dp, onClickListener: () -> Unit = {}
) {
    val resId = WeatherIcon.getResId(code)
    ClickableParseIcon(
        modifier = modifier.size(iconSize),
        model = painterResource(id = resId),
        backgroundColor = backgroundColor,
        shape = backgroundShape,
        tint = tint,
        onClick = onClickListener
    )
}

@Composable
fun WeatherIconNoRound(
    code: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    iconSize: Dp = 48.dp,
    onClickListener: () -> Unit = {}
) {
    val resId = WeatherIcon.getResId(code)
    Icon(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier
            .clickable {
                onClickListener()
            }
            .padding(8.dp)
            .size(iconSize)
            .then(modifier),
        tint = tint,
    )
}


