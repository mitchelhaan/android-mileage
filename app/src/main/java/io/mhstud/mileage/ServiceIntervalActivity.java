
package io.mhstud.mileage;

import io.mhstud.mileage.dao.Dao;
import io.mhstud.mileage.dao.Fillup;
import io.mhstud.mileage.dao.ServiceInterval;
import io.mhstud.mileage.dao.ServiceIntervalTemplate;
import io.mhstud.mileage.dao.Vehicle;
import io.mhstud.mileage.exceptions.InvalidFieldException;
import io.mhstud.mileage.provider.FillUpsProvider;
import io.mhstud.mileage.provider.tables.FillupsTable;
import io.mhstud.mileage.provider.tables.ServiceIntervalTemplatesTable;
import io.mhstud.mileage.provider.tables.ServiceIntervalsTable;
import io.mhstud.mileage.views.CursorSpinner;
import io.mhstud.mileage.views.DateButton;
import io.mhstud.mileage.views.DateDelta;
import io.mhstud.mileage.views.DistanceDelta;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

public class ServiceIntervalActivity extends BaseFormActivity {
    private final ServiceInterval mInterval = new ServiceInterval(new ContentValues());

    private CursorSpinner mVehicles;

    private CursorSpinner mIntervalTemplates;

    private EditText mTitle;

    private EditText mDescription;

    private DistanceDelta mDistance;

    private DateDelta mDuration;

    private EditText mOdometer;

    private DateButton mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.service_interval);
    }

    @Override
    protected Dao getDao() {
        return mInterval;
    }

    @Override
    protected String[] getProjectionArray() {
        return ServiceIntervalsTable.PROJECTION;
    }

    @Override
    protected Uri getUri(long id) {
        return ContentUris.withAppendedId(
                Uri.withAppendedPath(FillUpsProvider.BASE_URI, ServiceIntervalsTable.URI), id);
    }

    @Override
    protected void initUI() {
        mVehicles = (CursorSpinner) findViewById(R.id.vehicles);
        mIntervalTemplates = (CursorSpinner) findViewById(R.id.types);
        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mDistance = (DistanceDelta) findViewById(R.id.distance);
        mDuration = (DateDelta) findViewById(R.id.duration);
        mOdometer = (EditText) findViewById(R.id.odometer);
        mDate = (DateButton) findViewById(R.id.date);

        mVehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> list, View row, int position, long id) {
                filterTemplates(id);

                // update the odometer field
                if (!mInterval.isExistingObject()) {
                    String[] projection = new String[] {
                        Fillup.ODOMETER
                    };
                    String selection = Fillup.VEHICLE_ID + " = ?";
                    String[] args = new String[] {
                        String.valueOf(id)
                    };
                    Cursor fillupsCursor =
                            getContentResolver().query(FillupsTable.BASE_URI, projection,
                                    selection, args, Fillup.ODOMETER + " desc");
                    if (fillupsCursor.getCount() > 0) {
                        fillupsCursor.moveToFirst();
                        mOdometer.setText(fillupsCursor.getString(0));
                    }
                    fillupsCursor.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mIntervalTemplates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> list, View row, int position, long id) {
                Uri uri =
                        Uri.withAppendedPath(FillUpsProvider.BASE_URI,
                                ServiceIntervalTemplatesTable.URI);
                uri = ContentUris.withAppendedId(uri, id);
                Cursor intervalCursor =
                        getContentResolver().query(uri, ServiceIntervalTemplatesTable.PROJECTION,
                                null, null, null);
                ServiceIntervalTemplate template = new ServiceIntervalTemplate(intervalCursor);
                intervalCursor.close();

                // Overwrite everything that's on the form.
                mTitle.setText(template.getTitle());
                mDescription.setText(template.getDescription());
                mDistance.setDelta(template.getDistance());
                mDuration.setDelta(template.getDuration());
            }

            @Override
            public void onNothingSelected(AdapterView<?> list) {
            }
        });
    }

    private void filterTemplates(long id) {
        StringBuilder selection = new StringBuilder();
        selection.append(ServiceIntervalTemplate.VEHICLE_TYPE).append(" = ( select ")
                .append(Vehicle.VEHICLE_TYPE).append(" from vehicles where ").append(Vehicle._ID)
                .append(" =  ?)");
        String[] selectionArgs = new String[] {
            String.valueOf(id)
        };
        mIntervalTemplates.filter(selection.toString(), selectionArgs);
    }

    @Override
    protected void populateUI() {
        mTitle.setText(mInterval.getTitle());
        mDescription.setText(mInterval.getDescription());
        mDistance.setDelta(mInterval.getDistance());
        mDuration.setDelta(mInterval.getDuration());
        mOdometer.setText(String.valueOf(mInterval.getStartOdometer()));
        mDate.setDate(mInterval.getStartDate());
    }

    @Override
    protected void setFields() throws InvalidFieldException {
        String title = mTitle.getText().toString();
        if (title.length() == 0) {
            throw new InvalidFieldException(mTitle, R.string.error_invalid_interval_title);
        }
        mInterval.setTitle(title);

        mInterval.setDescription(mDescription.getText().toString());

        long duration = mDuration.getDelta();
        if (duration == 0) {
            throw new InvalidFieldException(mDescription, R.string.error_invalid_interval_duration);
        }
        mInterval.setDuration(duration);

        try {
            mInterval.setStartOdometer(Double.parseDouble(mOdometer.getText().toString()));
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(mOdometer, R.string.error_invalid_interval_odometer);
        }

        long timestamp = mDate.getTimestamp();
        if (timestamp == 0) {
            throw new InvalidFieldException(R.string.error_invalid_interval_timestamp);
        }
        mInterval.setStartDate(timestamp);

        long distance = mDistance.getDelta();
        if (distance == 0) {
            throw new InvalidFieldException(mDistance.getEditField(),
                    R.string.error_invalid_interval_distance);
        }
        mInterval.setDistance(mDistance.getDelta());

        mInterval.setVehicleId(mVehicles.getSelectedItemId());
    }

    @Override
    protected void saved() {
        mInterval.deleteAlarm(this);
        mInterval.scheduleAlarm(this, mInterval.getStartDate() + mInterval.getDuration());
        super.saved();
    }

    @Override
    protected int getCreateString() {
        return R.string.add_service_interval;
    }

    @Override
    public boolean canDelete() {
        Cursor count = managedQuery(ServiceIntervalTemplatesTable.BASE_URI, null, null, null, null);
        return count != null && count.getCount() > 1;
    }
}
