
package io.mhstud.mileage.charts;

import io.mhstud.mileage.dao.Fillup;

public abstract class CostChart extends LineChart {
    @Override
    protected ChartGenerator createChartGenerator() {
        return new LineChartGenerator(this, getVehicle(), new String[] {
                Fillup.DATE,
                Fillup.TOTAL_COST
        });
    }
}
