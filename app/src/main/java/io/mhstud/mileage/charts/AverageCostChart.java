
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.dao.Vehicle;

import android.database.Cursor;

public class AverageCostChart extends CostChart {
    @Override
    protected String getAxisTitle() {
        return getString(R.string.stat_avg_cost);
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
