package com.mvcion.proximitydetectionapp.ui.combined;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.common.DBHelper;
import com.mvcion.proximitydetectionapp.databinding.FragmentCombinedBinding;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CombinedFragment extends Fragment {

    private CombinedViewModel combinedViewModel;
    private FragmentCombinedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        combinedViewModel =
                new ViewModelProvider(this).get(CombinedViewModel.class);

        binding = FragmentCombinedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBHelper dbHelper = new DBHelper(inflater.getContext());
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_SERVICE_ID, 42);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_DEVICE_MAC, "AA:AA:AA:AA");
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_DEVICE_NAME, "iphone");
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_PROCESSED_DTTM, String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z"))));
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_MIN_RSSI, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_MAX_RSSI, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_AVG_RSSI, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_MIN_TXPOWER, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_MAX_TXPOWER, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_AVG_TXPOWER, 0);
        contentValues.put(DBHelper.SCANNED_RECORDS_FIELD_PROCESSING_WINDOW_MILLIS, 5000);

        writableDatabase.insert(DBHelper.SCANNED_RECORDS_TABLE_NAME, null, contentValues);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.SCANNED_RECORDS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.SCANNED_RECORDS_FIELD_ID);
            int macIndex = cursor.getColumnIndex(DBHelper.SCANNED_RECORDS_FIELD_DEVICE_MAC);
            int nameIndex = cursor.getColumnIndex(DBHelper.SCANNED_RECORDS_FIELD_DEVICE_NAME);
            int processingDttmIndex = cursor.getColumnIndex(DBHelper.SCANNED_RECORDS_FIELD_PROCESSED_DTTM);
            Log.e("asa", MessageFormat.format(
                    "{0}) - [{1}] - {2} - {3}",
                    cursor.getInt(idIndex),
                    cursor.getString(processingDttmIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(macIndex)
            ));
        } else {
            Log.e("asa", "0 rows");
        }
        cursor.close();

        final TextView textView = binding.textCombined;
        combinedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}