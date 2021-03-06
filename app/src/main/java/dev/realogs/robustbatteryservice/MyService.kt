package dev.realogs.robustbatteryservice

import android.app.*
import android.content.*
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import dev.realogs.robustbatteryservice.Constants.CHANNEL_ID
import dev.realogs.robustbatteryservice.Constants.SERVICE_NOTIFICATION_ID

class MyService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_STICKY

    }

    private fun showNotification() {

        val batteryManager: BatteryManager = applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        var batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        var charging= ""

        if(batteryManager.isCharging){
            charging = "Charging"
        } else {
            charging = "Unplugged"
        }


        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)

        val notification = Notification
            .Builder(this, CHANNEL_ID)
            .setContentTitle("Battery Percentage: " + batteryLevel + "%")
            .setContentText("Charging Status: " + charging)
            .setSmallIcon(R.drawable.ic_baseline_battery_5_bar_24)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(SERVICE_NOTIFICATION_ID, notification)


    }



    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager!!.createNotificationChannel(serviceChannel)


        }
    }


}


