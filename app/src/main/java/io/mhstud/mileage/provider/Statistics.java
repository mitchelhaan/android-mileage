
package io.mhstud.mileage.provider;

import io.mhstud.mileage.ChartActivity;
import io.mhstud.mileage.R;
import io.mhstud.mileage.charts.AverageCostChart;
import io.mhstud.mileage.charts.AverageDistanceChart;
import io.mhstud.mileage.charts.AverageFuelEconomyChart;
import io.mhstud.mileage.charts.AveragePriceChart;
import io.mhstud.mileage.charts.AverageVolumeChart;
import io.mhstud.mileage.charts.BestFuelEconomyChart;
import io.mhstud.mileage.charts.LastMonthCostChart;
import io.mhstud.mileage.charts.LastYearCostChart;
import io.mhstud.mileage.charts.MaximumCostChart;
import io.mhstud.mileage.charts.MaximumDistanceChart;
import io.mhstud.mileage.charts.MaximumPriceChart;
import io.mhstud.mileage.charts.MaximumVolumeChart;
import io.mhstud.mileage.charts.MinimumCostChart;
import io.mhstud.mileage.charts.MinimumDistanceChart;
import io.mhstud.mileage.charts.MinimumPriceChart;
import io.mhstud.mileage.charts.MinimumVolumeChart;
import io.mhstud.mileage.charts.TotalCostChart;
import io.mhstud.mileage.charts.TotalVolumeChart;
import io.mhstud.mileage.charts.WorstFuelEconomyChart;
import io.mhstud.mileage.dao.Vehicle;
import io.mhstud.mileage.math.Calculator;

import android.content.Context;

import java.util.ArrayList;

// There be dragons in this code. Tread with caution.
public final class Statistics {
    public static final ArrayList<Statistic> STATISTICS = new ArrayList<Statistic>();

    public static final ArrayList<StatisticsGroup> GROUPS = new ArrayList<StatisticsGroup>();

    public static class EconomyStatistic extends Statistic {
        public EconomyStatistic(int label) {
            super(label);
        }

        private EconomyStatistic(String value, Class<? extends ChartActivity> chartClass, int label) {
            super(value, chartClass, label);
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return mFormatter.format(value) + " "
                    + Calculator.getEconomyUnitsAbbr(context, vehicle);
        }
    }

    public static final Statistic AVG_ECONOMY = new EconomyStatistic("avg_economy",
            AverageFuelEconomyChart.class, R.string.stat_avg_economy);

    public static final Statistic MIN_ECONOMY = new EconomyStatistic("min_economy",
            WorstFuelEconomyChart.class, R.string.stat_min_economy);

    public static final Statistic MAX_ECONOMY = new EconomyStatistic("max_economy",
            BestFuelEconomyChart.class, R.string.stat_max_economy);

    public static class DistanceStatistic extends Statistic {
        public DistanceStatistic(int label) {
            super(label);
        }

        private DistanceStatistic(String value, Class<? extends ChartActivity> chartClass, int label) {
            super(value, chartClass, label);
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return mFormatter.format(value) + " "
                    + Calculator.getDistanceUnitsAbbr(context, vehicle);
        }
    };

    public static final Statistic AVG_DISTANCE = new DistanceStatistic("avg_distance",
            AverageDistanceChart.class, R.string.stat_avg_distance);

    public static final Statistic MIN_DISTANCE = new DistanceStatistic("min_distance",
            MinimumDistanceChart.class, R.string.stat_min_distance);

    public static final Statistic MAX_DISTANCE = new DistanceStatistic("max_distance",
            MaximumDistanceChart.class, R.string.stat_max_distance);

    public static class CostStatistic extends Statistic {
        public CostStatistic(int label) {
            super(label);
        }

        private CostStatistic(String value, Class<? extends ChartActivity> chartClass, int label) {
            super(value, chartClass, label);
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return Calculator.getCurrencySymbol(vehicle) + mFormatter.format(value);
        }
    }

    public static class CostPerUnitStatistic extends CostStatistic {
        private final int mUnit;

        private CostPerUnitStatistic(String value, int label, int unit) {
            super(value, null, label);
            mUnit = unit;
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return context.getString(R.string.units_per, super.format(context, vehicle, value),
                    context.getString(mUnit));
        }
    }

    public static final Statistic AVG_COST = new CostStatistic("avg_cost", AverageCostChart.class,
            R.string.stat_avg_cost);

    public static final Statistic MIN_COST = new CostStatistic("min_cost", MinimumCostChart.class,
            R.string.stat_min_cost);

    public static final Statistic MAX_COST = new CostStatistic("max_cost", MaximumCostChart.class,
            R.string.stat_max_cost);

    public static final Statistic TOTAL_COST = new CostStatistic("total_cost",
            TotalCostChart.class, R.string.stat_total_cost);

    public static final Statistic LAST_MONTH_COST = new CostStatistic("last_month_cost",
            LastMonthCostChart.class, R.string.stat_last_month_cost);

    public static final Statistic AVG_MONTHLY_COST = new CostPerUnitStatistic("monthly_cost",
            R.string.stat_avg_month_cost, R.string.per_month);

    public static final Statistic LAST_YEAR_COST = new CostStatistic("last_year_cost",
            LastYearCostChart.class, R.string.stat_last_year_cost);

    public static final Statistic AVG_YEARLY_COST = new CostPerUnitStatistic("yearly_cost",
            R.string.stat_avg_year_cost, R.string.per_year);

    public static class CostPerDistanceStatistic extends Statistic {
        private CostPerDistanceStatistic(String value, int label) {
            super(value, null, label);
        }

        @Override
        public String getLabel(Context context, Vehicle vehicle) {
            return context.getString(mLabel, Calculator.getDistanceUnitsAbbr(context, vehicle));
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            String cost = Calculator.getCurrencySymbol(vehicle) + mFormatter.format(value);
            String distance = Calculator.getDistanceUnitsAbbr(context, vehicle);
            return context.getString(R.string.units_per, cost, distance);
        }
    }

    public static final Statistic AVG_COST_PER_DISTANCE = new CostPerDistanceStatistic(
            "avg_cost_per_mi", R.string.stat_avg_cost_per_distance);

    public static final Statistic MIN_COST_PER_DISTANCE = new CostPerDistanceStatistic(
            "min_cost_per_mi", R.string.stat_min_cost_per_distance);

    public static final Statistic MAX_COST_PER_DISTANCE = new CostPerDistanceStatistic(
            "max_cost_per_mi", R.string.stat_max_cost_per_distance);

    public static class PriceStatistic extends Statistic {
        public PriceStatistic(int label) {
            super(label);
        }

        private PriceStatistic(String value, Class<? extends ChartActivity> chartClass, int label) {
            super(value, chartClass, label);
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return Calculator.getCurrencySymbol(vehicle) + mFormatter.format(value);
        }
    }

    public static final Statistic AVG_PRICE = new PriceStatistic("avg_price",
            AveragePriceChart.class, R.string.stat_avg_price);

    public static final Statistic MIN_PRICE = new PriceStatistic("min_price",
            MinimumPriceChart.class, R.string.stat_min_price);

    public static final Statistic MAX_PRICE = new PriceStatistic("max_price",
            MaximumPriceChart.class, R.string.stat_max_price);

    public static class FuelStatistic extends Statistic {
        public FuelStatistic(int label) {
            super(label);
        }

        private FuelStatistic(String value, Class<? extends ChartActivity> chartClass, int label) {
            super(value, chartClass, label);
        }

        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            return mFormatter.format(value) + " " + Calculator.getVolumeUnits(context, vehicle);
        }
    }

    public static final Statistic MIN_FUEL = new FuelStatistic("min_fuel",
            MinimumVolumeChart.class, R.string.stat_min_fuel);

    public static final Statistic MAX_FUEL = new FuelStatistic("max_fuel",
            MaximumVolumeChart.class, R.string.stat_max_fuel);

    public static final Statistic AVG_FUEL = new FuelStatistic("avg_fuel",
            AverageVolumeChart.class, R.string.stat_avg_fuel);

    public static final Statistic TOTAL_FUEL = new FuelStatistic("total_fuel",
            TotalVolumeChart.class, R.string.stat_total_fuel);

    public static final Statistic FUEL_PER_YEAR = new FuelStatistic("fuel_per_year", null,
            R.string.stat_fuel_per_year) {
        @Override
        public String format(Context context, Vehicle vehicle, double value) {
            String amount = super.format(context, vehicle, value);
            return context.getString(R.string.units_per, amount,
                    context.getString(R.string.per_year));
        }
    };

}
