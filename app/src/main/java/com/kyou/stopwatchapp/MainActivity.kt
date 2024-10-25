package com.kyou.stopwatchapp

import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Stopwatch()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("offset", offset)
    }
}


@Composable
fun Stopwatch() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 注意不要写成Chronometer(context = context)，不然会报错：Named arguments are not allowed for non-Kotlin functions
        // 因为Chronometer是一个Java写的class
        // 为什么不能写成 var stopwatch = Chronometer(context) ??
        var stopwatch: Chronometer? = null
        var running = false
        var offset: Long = 0L
//    val start = stopwatch?.start() // 有问题：Value of 'stopwatch?.start()' is always null
//    val stop = stopwatch?.stop() // 有问题：Value of 'stopwatch?.stop()' is always null
        // 参考：https://stackoverflow.com/questions/77442725/how-to-use-chronometer-in-kotlin-jetpack-compose-android

//        Row { stopwatch.format }


        AndroidView(
            // fatory要求输出的type是View。而Chronometer继承自TextView，TextView又继承于View
            // also函数的作用：先Chronometer(context)实例化一个对象，再把这个对象（it）assign给stopwatch这个
            // 变量。其实这一句话是干了两件事情。因为是在lambda函数里，你不能写成
            // context -> val stopwatch = Chronometer(context)
            factory = { context ->
                Chronometer(context).also {

                    stopwatch = it
                    // 利用TextView的性质来设置字体大小、粗细
                    stopwatch!!.setTextSize(40F)
                    stopwatch!!.setTypeface(null, Typeface.BOLD)
                    // 设置时间格式，由默认的MM:SS改成HH:MM:SS
//                    val t = System.currentTimeMillis() - it.getBase()
//                    val d = Date(t)
//                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
//                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
//                    it.setText(sdf.format(d))

//                    it.base = SystemClock.elapsedRealtime()
//                    it.setBase(SystemClock.elapsedRealtime())
//                    it.setFormat("%h%m%s")

//                    val t: Long = SystemClock.elapsedRealtime() - it.getBase()
//                    it.setText(DateFormat.format("kk:mm:ss", t))


                }
            },
            modifier = Modifier.padding(vertical = 32.dp)
        )
        // start
        TextButton(
            onClick = {
//                if (running)
                // setBase是让它重新归零，不然它会从app启动开始时刻计时（即打开app就开始计时了，你再点start可能显示的是5s了，而不是从0开始）
                stopwatch?.setBase(SystemClock.elapsedRealtime())
                stopwatch?.start()
            },
            border = BorderStroke(4.dp, Color.Transparent),
            modifier = Modifier.padding(vertical = 4.dp)
        ) { Text("START", fontSize = 26.sp, fontWeight = FontWeight.Bold) }
        // pause
        TextButton(
            onClick = { stopwatch?.stop() },
            border = BorderStroke(4.dp, Color.Transparent),
            modifier = Modifier.padding(vertical = 4.dp)
        ) { Text("PAUSE", fontSize = 26.sp, fontWeight = FontWeight.Bold) }
        // reset
        TextButton(
            onClick = {
                // setBase是让它重新归零，不然点了stop再点start后，是接着stop前的时间计时的
                stopwatch?.setBase(SystemClock.elapsedRealtime())
                // 加上这个stop后是为了阻止它归零后继续计时
                stopwatch?.stop()
            },
            border = BorderStroke(4.dp, Color.Transparent),
            modifier = Modifier.padding(vertical = 4.dp)
        ) { Text("RESET", fontSize = 26.sp, fontWeight = FontWeight.Bold) }
    }
}

@Preview
@Composable
fun StopwatchPreview() {
    Stopwatch()
}