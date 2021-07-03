package com.mvcion.proximitydetectionapp.ui.history;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.db.DBHelper;
import com.mvcion.proximitydetectionapp.dao.ProximityDetectionScanResultDao;
import com.mvcion.proximitydetectionapp.databinding.FragmentHistoryBinding;

import java.text.MessageFormat;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class HistoryFragment extends Fragment {

    private final String ROWS_COUNT_PATTERN = "Rows count: {0}";

    private FragmentHistoryBinding binding;

    private void setProximityDetectionHistoryList(@NonNull LayoutInflater inflater) {
        String[] proximityInfo = new String[0];
        {
            DBHelper dbHelper = new DBHelper(inflater.getContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(
                    DBHelper.SCANNED_RECORDS_TABLE_NAME,
                    null, null, null, null, null,
                    MessageFormat.format("{0} DESC", DBHelper.SCANNED_RECORDS_FIELD_ID)
            );
            if (cursor.moveToFirst()) {
                int it = 0;
                proximityInfo = new String[cursor.getCount()];
                do {
                    proximityInfo[it++] = ProximityDetectionScanResultDao.getDto(cursor).toString();
                } while (cursor.moveToNext());
            } else {
                Log.e("HistoryFragment", "0 rows");
            }
            cursor.close();
        }

        TextView nearbyDevicesCounterTextView = binding.historyTextViewHistorySize;
        nearbyDevicesCounterTextView.setText(MessageFormat.format(
                ROWS_COUNT_PATTERN, proximityInfo.length
        ));

        ListView devicesListView = binding.proximityDetectionHistoryListView;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                proximityInfo
        );
        devicesListView.setAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HistoryViewModel historyViewModel = new ViewModelProvider(this)
                .get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setProximityDetectionHistoryList(inflater);

        final Button button = binding.historyButtonClearHistory;
        button.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(inflater.getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(MessageFormat.format(
                    "DROP TABLE IF EXISTS {0};",
                    DBHelper.SCANNED_RECORDS_TABLE_NAME
            ));
            dbHelper.onCreate(db);

            setProximityDetectionHistoryList(inflater);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}