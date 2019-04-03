
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.dao.Vehicle;

import android.database.Cursor;

public class SouthChart extends LatitudeChart {
    @Override
    protected String getAxisTitle() {
        return getString(R.string.stat_south);
    }

    @Override
    protected void processCursor(LineChartGenerator generator, Cursor cursor, Vehicle vehicle) {
        int num = 0;
        double maximum_south = 10000;
        while (cursor.isAfterLast() == false) {
            if (generator.isCancelled()) {
                break;
            }
            double south = cursor.getDouble(1);
            if (south != 0) {
                if (south < maximum_south) {
                    maximum_south = south;
                }
                addPoint(cursor.getLong(0), maximum_south);
            }
            generator.update(num++);
            cursor.moveToNext();
        }
    }
}
