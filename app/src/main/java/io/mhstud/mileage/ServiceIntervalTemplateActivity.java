
package io.mhstud.mileage;

import io.mhstud.mileage.dao.Dao;
import io.mhstud.mileage.dao.ServiceIntervalTemplate;
import io.mhstud.mileage.exceptions.InvalidFieldException;
import io.mhstud.mileage.provider.FillUpsProvider;
import io.mhstud.mileage.provider.tables.ServiceIntervalTemplatesTable;
import io.mhstud.mileage.views.CursorSpinner;
import io.mhstud.mileage.views.DateDelta;
import io.mhstud.mileage.views.DistanceDelta;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

public class ServiceIntervalTemplateActivity extends BaseFormActivity {

    protected final ServiceIntervalTemplate mTemplate = new ServiceIntervalTemplate(
            new ContentValues());

    protected EditText mTitle;

    protected EditText mDescription;

    protected DistanceDelta mDistance;

    protected DateDelta mDuration;

    protected CursorSpinner mVehicleTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.service_interval_template);
    }

    @Override
    protected Dao getDao() {
        return mTemplate;
    }

    @Override
    protected String[] getProjectionArray() {
        return ServiceIntervalTemplatesTable.PROJECTION;
    }

    @Override
    protected Uri getUri(long id) {
        return ContentUris.withAppendedId(
                Uri.withAppendedPath(FillUpsProvider.BASE_URI, ServiceIntervalTemplatesTable.URI),
                id);
    }

    @Override
    protected void initUI() {
        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mDistance = (DistanceDelta) findViewById(R.id.distance);
        mDuration = (DateDelta) findViewById(R.id.duration);
        mVehicleTypes = (CursorSpinner) findViewById(R.id.types);
    }

    @Override
    protected void populateUI() {
        mTitle.setText(mTemplate.getTitle());
        mDescription.setText(mTemplate.getDescription());
        mDistance.setDelta(mTemplate.getDistance());
        mDuration.setDelta(mTemplate.getDuration());
    }

    @Override
    protected void setFields() throws InvalidFieldException {
        String title = mTitle.getText().toString();
        if (title.length() == 0) {
            throw new InvalidFieldException(mTitle, R.string.error_invalid_interval_title);
        }
        mTemplate.setTitle(title);

        mTemplate.setDescription(mDescription.getText().toString());

        long distance = mDistance.getDelta();
        if (distance <= 0) {
            throw new InvalidFieldException(mDistance.getEditField(),
                    R.string.error_invalid_interval_distance);
        }
        mTemplate.setDistance(distance);

        long duration = mDuration.getDelta();
        if (duration <= 0) {
            throw new InvalidFieldException(mDuration.getEditField(),
                    R.string.error_invalid_interval_duration);
        }
        mTemplate.setDuration(mDuration.getDelta());

        mTemplate.setVehicleTypeId(mVehicleTypes.getSelectedItemId());
    }

    @Override
    protected int getCreateString() {
        return R.string.add_service_interval_template;
    }
}
