package com.example.batteryalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.batteryalarm.ui.theme.BatteryAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatteryAlarmTheme {
                Scaffold  {
                    _ -> BatteryLevel()
                }
            }
        }
    }
}

@Composable
fun BatteryLevel() {

    var batteryLevel by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Effect to register the BroadcastReceiver
    LaunchedEffect(context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                batteryLevel = if (level != -1 && scale != -1) {
                    "${(level * 100) / scale}%"
                } else {
                    "Error"
                }
            }
        }
        context.registerReceiver(receiver, filter)
    }
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        if(batteryLevel >"20"){
            Image(painter = painterResource(id = R.drawable.battery_full),
                contentDescription = "Battery Greater Than 20 Percent",
                Modifier.size(350.dp))
        }
        else
            Image(painter = painterResource(id = R.drawable.battery_low),
                contentDescription = "Battery Less Than 20 Percent",
                Modifier.size(350.dp))
    }
}



@Preview(showSystemUi = true)
@Composable
private fun BatteryLevelPreview() {

    BatteryLevel()

}