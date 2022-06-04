package dev.realogs.robustbatteryservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import dev.realogs.robustbatteryservice.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var batteryReceiver: BatteryStatsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        batteryReceiver = BatteryStatsService()

        IntentFilter(Intent.ACTION_BATTERY_CHANGED).also {
            registerReceiver(batteryReceiver, it)
        }
    }
    }


class BatteryStatsService : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        var charging_status = ""
        var battery_condition = ""
        var power_source = "Unplugged"
        var level: Int = 0

        level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)!!

        //Battery Health Start
        var health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

        if (health == BatteryManager.BATTERY_HEALTH_COLD) {
            battery_condition = "Cold"
        }

        if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
            battery_condition = "Dead"
        }

        if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
            battery_condition = "Good"
        }

        if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
            battery_condition = "Over Heat"
        }

        if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
            battery_condition = "Over Voltage"
        }

        if (health == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
            battery_condition = "Unknown"
        }

        if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
            battery_condition = "Error"
        }

        binding.tvBatteryPercent.setText(level)
        binding.progressBar.setProgress(level, true)
    }

}