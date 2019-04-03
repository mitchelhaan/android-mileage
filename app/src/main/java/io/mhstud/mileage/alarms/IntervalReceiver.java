
package io.mhstud.mileage.alarms;

import io.mhstud.mileage.dao.ServiceInterval;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IntervalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getExtras().getLong(ServiceInterval._ID, -1L);

        ServiceInterval interval = ServiceInterval.loadById(context, id);
        interval.raiseNotification(context);
    }
}
