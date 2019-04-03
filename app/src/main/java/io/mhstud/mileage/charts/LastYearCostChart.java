
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.math.Calculator;

public class LastYearCostChart extends IntervalCostChart {
    @Override
    protected final String getAxisTitle() {
        return getString(R.string.stat_last_year_cost);
    }

    @Override
    protected final long getInterval() {
        return Calculator.YEAR_MS;
    }
}
