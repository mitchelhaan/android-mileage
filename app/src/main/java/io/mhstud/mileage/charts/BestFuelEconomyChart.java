
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.dao.Vehicle;
import io.mhstud.mileage.math.Calculator;

import android.database.Cursor;

public class BestFuelEconomyChart extends FuelEconomyChart {
    @Override
    protected void processCursor(LineChartGenerator generator, Cursor cursor, Vehicle vehicle) {
        int num = 0;
        double best_fuel_economy = -100000;
        while (cursor.isAfterLast() == false) {
            if (generator.isCancelled()) {
                break;
            }
            if (num > 0) {
                double economy = cursor.getDouble(1);
                if (Calculator.isBetterEconomy(vehicle, economy, best_fuel_economy)) {
                    best_fuel_economy = economy;
                }
                addPoint(cursor.getLong(0), best_fuel_economy);
            }
            generator.update(num++);
            cursor.moveToNext();
        }
    }

    @Override
    protected String getAxisTitle() {
        return getString(R.string.stat_min_economy);
    }
}
