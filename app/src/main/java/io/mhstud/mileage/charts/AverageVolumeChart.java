
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.dao.Vehicle;

import android.database.Cursor;

public class AverageVolumeChart extends VolumeChart {
    @Override
    protected String getAxisTitle() {
        return getString(R.string.stat_avg_fuel);
    }

    @Override
    protected void processCursor(LineChartGenerator generator, Cursor cursor, Vehicle vehicle) {
        int num = 0;
        while (cursor.isAfterLast() == false) {
            if (generator.isCancelled()) {
                break;
            }
            addPoint(cursor.getLong(0), cursor.getDouble(1));
            generator.update(num++);
            cursor.moveToNext();
        }
    }
}
