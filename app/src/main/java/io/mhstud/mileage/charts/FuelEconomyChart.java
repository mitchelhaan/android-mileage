
package io.mhstud.mileage.charts;

import io.mhstud.mileage.dao.Fillup;

public abstract class FuelEconomyChart extends LineChart {
    @Override
    protected final ChartGenerator createChartGenerator() {
        return new LineChartGenerator(this, getVehicle(), new String[] {
                Fillup.DATE,
                Fillup.ECONOMY
        });
    }
}
