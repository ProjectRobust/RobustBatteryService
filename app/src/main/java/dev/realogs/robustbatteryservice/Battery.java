package dev.realogs.robustbatteryservice;

import android.content.Context;
import android.os.BatteryManager;

public class Battery {

    public static double getBatteryCurrentNowInAmperes(final Context context) {
        int value = 0;

        BatteryManager manager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        if (manager != null) {
            value = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        }

        value = (value != 0 && value != Integer.MIN_VALUE) ? value : 0;

        return ((double) value / 1000000)*(-1);
    }
}
