package dev.realogs.robustbatteryservice

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        startStopService()
    }

    private fun startStopService() {
        if (!isMyServiceRunning(MyService::class.java)) {
            Toast.makeText(this, "Battery Service is Running", Toast.LENGTH_SHORT).show()
            startService(Intent(this, MyService::class.java))
        }
    }

    private fun isMyServiceRunning(mClass: Class<MyService>): Boolean {

        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (mClass.name.equals(service.service.className)) {
                return true
            }
        }
        return false
    }
}

class BatteryStatsService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null){
            return
        }
        if(context == null){
            return
        }

        val action: String? = intent.action
        if(action == null)
            return

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


        //Battery Charging Status
        var status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            charging_status = "Charging"
            binding.tvStatusValueCharging.visibility = View.VISIBLE
            binding.tvStatusValueDischarging.visibility = View.INVISIBLE

            binding.batteryCharging.visibility = View.VISIBLE
            binding.batteryVeryLow.visibility = View.INVISIBLE

            if (level <= 15) {
                binding.batteryVeryLow.visibility = View.VISIBLE
                binding.batteryVeryLow.setImageResource(R.drawable.battery_very_low)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)


            } else if (level >= 15 && level < 35) {

                binding.batteryLow.visibility = View.VISIBLE
                binding.batteryLow.setImageResource(R.drawable.battery_low)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE

            } else if (level >= 35 && level < 80) {

                binding.batteryMedium.visibility = View.VISIBLE
                binding.batteryMedium.setImageResource(R.drawable.battery_medium)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE

            } else if (level >= 80) {
                binding.batteryFull.visibility = View.VISIBLE
                binding.batteryFull.setImageResource(R.drawable.battery_full)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE

            }

            var chargePlug = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                power_source = "USB"
                binding.imgPowerUsb.setImageResource(R.drawable.ic_usb_white_24dp)
                binding.imgPowerAc.setImageResource(R.drawable.ic_power_plug_grey600_24dp)
                binding.imgPowerDischarging.setImageResource(R.drawable.ic_battery_50_grey600_24dp)
                binding.imgPowerWireless.setImageResource(R.drawable.ic_access_point_grey600_24dp)
            }
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                power_source = "AC Source"
                binding.imgPowerUsb.setImageResource(R.drawable.ic_usb_grey600_24dp)
                binding.imgPowerAc.setImageResource(R.drawable.ic_power_plug_white_24dp)
                binding.imgPowerDischarging.setImageResource(R.drawable.ic_battery_50_grey600_24dp)
                binding.imgPowerWireless.setImageResource(R.drawable.ic_access_point_grey600_24dp)
            }
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                power_source = "Wireless"
                binding.imgPowerUsb.setImageResource(R.drawable.ic_usb_grey600_24dp)
                binding.imgPowerAc.setImageResource(R.drawable.ic_power_plug_grey600_24dp)
                binding.imgPowerDischarging.setImageResource(R.drawable.ic_battery_50_grey600_24dp)
                binding.imgPowerWireless.setImageResource(R.drawable.ic_access_point_white_24dp)
            }


        }

        if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            charging_status = "Discharging"

            binding.tvStatusValueCharging.visibility = View.INVISIBLE
            binding.tvStatusValueDischarging.visibility = View.VISIBLE

            binding.imgPowerUsb.setImageResource(R.drawable.ic_usb_grey600_24dp)
            binding.imgPowerAc.setImageResource(R.drawable.ic_power_plug_grey600_24dp)
            binding.imgPowerDischarging.setImageResource(R.drawable.ic_battery_50_white_24dp)
            binding.imgPowerWireless.setImageResource(R.drawable.ic_access_point_grey600_24dp)

            if (level <= 15) {
                binding.batteryVeryLow.visibility = View.VISIBLE
                binding.batteryVeryLow.setImageResource(R.drawable.battery_very_low)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)
                binding.batteryCharging.visibility = View.INVISIBLE

            } else if (level >= 15 && level < 35) {

                binding.batteryLow.visibility = View.VISIBLE
                binding.batteryLow.setImageResource(R.drawable.battery_low)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE
                binding.batteryCharging.visibility = View.INVISIBLE

            } else if (level >= 35 && level < 80) {

                binding.batteryMedium.visibility = View.VISIBLE
                binding.batteryMedium.setImageResource(R.drawable.battery_medium)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryFull.setImageResource(R.drawable.battery_full_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE
                binding.batteryCharging.visibility = View.INVISIBLE

            } else if (level >= 80) {
                binding.batteryFull.visibility = View.VISIBLE
                binding.batteryFull.setImageResource(R.drawable.battery_full)
                binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
                binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
                binding.batteryVeryLow.visibility = View.INVISIBLE
                binding.batteryCharging.visibility = View.INVISIBLE


            }
        }

        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            charging_status = "Full"

            binding.batteryFull.visibility = View.VISIBLE
            binding.batteryFull.setImageResource(R.drawable.battery_full)
            binding.batteryMedium.setImageResource(R.drawable.battery_medium_grey)
            binding.batteryLow.setImageResource(R.drawable.battery_low_grey)
            binding.batteryVeryLow.visibility = View.INVISIBLE
        }

        if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            charging_status = "Unknown"

        }


        var voltage: Int = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)



        binding.tvBTPercent.setText(level.toString() + "%")
        binding.conditionValue.setText(battery_condition)
        binding.temperatureValue.setText(temperature_celcius.toString() + "°C")
        binding.tvVoltageValue.setText(voltage.toString() + "V")
        binding.progressBar.setProgress(level, true)

    }
}