
package io.mhstud.mileage.charts;

import io.mhstud.mileage.R;
import io.mhstud.mileage.math.Calculator;

public class LastMonthCostChart extends IntervalCostChart {
    @Override
    protected final String getAxisTitle() {
        return getString(R.string.stat_last_month_cost);
    }

    @Override
    protected final long getInterval() {
        return Calculator.MONTH_MS;
    }
}
