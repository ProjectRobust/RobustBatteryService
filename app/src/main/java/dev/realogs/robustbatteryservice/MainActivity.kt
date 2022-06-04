package dev.realogs.robustbatteryservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
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

        //Battery Temperature
        var temperature_celcius: Int =
            ((intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0))!!) / 10


        //Battery Charge Power Source
        var chargePlug = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            power_source = "USB"
        }
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            power_source = "AC Source"
        }
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            power_source = "Wireless"
        }

        //Battery Charging Status
        var status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            charging_status = "Charging"
            binding.batteryCharging.visibility = View.VISIBLE
            binding.batteryLow.visibility = View.INVISIBLE
        }

        if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            charging_status = "Discharging"
            binding.batteryCharging.visibility = View.INVISIBLE
            binding.batteryLow.visibility = View.INVISIBLE
        }

        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            charging_status = "Full"
            binding.batteryCharging.visibility = View.INVISIBLE
            binding.batteryLow.visibility = View.INVISIBLE
        }

        if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            charging_status = "Unknown"
            binding.batteryCharging.visibility = View.INVISIBLE
            binding.batteryLow.visibility = View.INVISIBLE
        }

        if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            charging_status = "Unplugged"
            binding.batteryCharging.visibility = View.INVISIBLE
            binding.batteryLow.visibility = View.VISIBLE
        }

        if (level != null) {
            if (level <= 15) {
                binding.batteryCharging.visibility = View.INVISIBLE
                binding.batteryLow.visibility = View.VISIBLE
            }

        }

        var voltage: Int = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)


        binding.tvBTPercent.setText(level.toString() + "%")
        binding.conditionValue.setText(battery_condition)
        binding.temperatureValue.setText(temperature_celcius.toString() + "°C")
        binding.tvStatusValue.setText(charging_status)
        binding.tvVoltageValue.setText(voltage.toString() + "V")

        binding.progressBar.setProgress(level, true)

    }

}