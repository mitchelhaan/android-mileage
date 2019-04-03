
package io.mhstud.mileage.charts;

import io.mhstud.mileage.dao.Fillup;

public abstract class LongitudeChart extends LineChart {
    @Override
    protected ChartGenerator createChartGenerator() {
        return new LineChartGenerator(this, getVehicle(), new String[] {
                Fillup.DATE,
                Fillup.LONGITUDE
        });
    }
}
