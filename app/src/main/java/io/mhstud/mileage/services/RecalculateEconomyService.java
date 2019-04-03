
package io.mhstud.mileage.services;

import io.mhstud.mileage.dao.Fillup;
import io.mhstud.mileage.dao.FillupSeries;
import io.mhstud.mileage.dao.Vehicle;
import io.mhstud.mileage.exceptions.InvalidFieldException;
import io.mhstud.mileage.math.Calculator;
import io.mhstud.mileage.provider.tables.FillupsTable;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class RecalculateEconomyService extends IntentService {
    private static final String TAG = "Mileage";

    public static final String CALCULATION_FINISHED =
            "io.mhstud.mileage.services.RecalculateEconomyService.CALCULATION_FINISHED";

    public static final String EXTRA_AVERAGE_ECONOMY = "average_economy";

    public static void run(Context context, Vehicle vehicle) {
        context.startService(new Intent(context, RecalculateEconomyService.class).putExtra(
                Vehicle._ID, vehicle.getId()));
    }

    public RecalculateEconomyService() {
        super(RecalculateEconomyService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long vehicleId = intent.getLongExtra(Vehicle._ID, -1);
        if (vehicleId == -1) {
            Log.d(TAG, "No vehicle ID");
            return;
        }

        Vehicle vehicle = Vehicle.loadById(this, vehicleId);
        String[] args = new String[] {
            String.valueOf(vehicle.getId())
        };

        String selection = Fillup.VEHICLE_ID + " = ?";

        Cursor cursor =
                getContentResolver().query(FillupsTable.BASE_URI, FillupsTable.PROJECTION,
                        selection, args, Fillup.ODOMETER + " asc");
        if (cursor.getCount() <= 1) {
            Log.d(TAG, "Not enough fillups to calculate economy");
            return;
        }

        Log.d(TAG, "Recalculating");
        // recalculate a whole bunch of shit
        FillupSeries series = new FillupSeries();

        while (cursor.moveToNext()) {
            Fillup fillup = new Fillup(cursor);
            series.add(fillup);

            if (fillup.hasPrevious()) {
                double economy = Calculator.fillupEconomy(vehicle, series);
                if (economy != fillup.getEconomy()) {
                    fillup.setEconomy(economy);
                }
            } else {
                fillup.setEconomy(0D);
            }

            try {
                fillup.saveIfChanged(this);
            } catch (InvalidFieldException e) {
                return;
            }
        }

        double avgEconomy = Calculator.averageEconomy(vehicle, series);

        cursor.close();

        sendBroadcast(new Intent(CALCULATION_FINISHED).setPackage(getPackageName()).putExtra(
                EXTRA_AVERAGE_ECONOMY, avgEconomy));
    }
}
