package com.mvcion.proximitydetectionapp.ui.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.ScannerService;
import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceTools;
import com.mvcion.proximitydetectionapp.databinding.FragmentScannerBinding;

import java.text.MessageFormat;

public class ScannerFragment extends Fragment {

    private final String SCANNER_ITERATION_PATTERN = "Scanner iteration: {0}";
    private final String UNIQUE_DEVICES_TOTAL_PATTERN = "Unique devices total: {0}";
    private final String UPDATE_FREQUENCY_PATTERN = "Update frequency: {0}s";

    private long processingWindowNanos;
    private long reportDelayMillis;
    private int scannerMode;
    private int callbackType;
    private int matchMode;
    private int numOfMatches;

    private ScannerViewModel scannerViewModel;
    private FragmentScannerBinding binding;

    private void fetchScannerPreferences(Context context) {
        processingWindowNanos = PreferencesFacade.getProcessingWindowNanos(context);
        reportDelayMillis = PreferencesFacade.getReportDelayMillis(context);
        scannerMode = PreferencesFacade.getScannerMode(context);
        callbackType = PreferencesFacade.getCallbackType(context);
        matchMode = PreferencesFacade.getMatchMode(context);
        numOfMatches = PreferencesFacade.getNumOfMatches(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scannerViewModel =
                new ViewModelProvider(this).get(ScannerViewModel.class);

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProgressBar progressBar = binding.scannerProgressBarRunning;
        progressBar.setVisibility(View.INVISIBLE);

        SwitchCompat switchCompat = binding.scannerSwitchCompatRunning;

        if (ServiceTools.isServiceRunning(inflater.getContext(), ScannerService.class)) {
            progressBar.setVisibility(View.VISIBLE);
            switchCompat.setChecked(true);
        }

        IntentFilter intentFilter = new IntentFilter("ScannerService");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ScannerService")) {

                    int devicesNearbyNum = intent.getIntExtra("devicesNearbyNum", 0);
                    int scannerIteration = intent.getIntExtra("scannerIteration", 0);
                    int allUniqueDevicesNum = intent.getIntExtra("allUniqueDevicesNum", 0);
                    String[] proximityInfos = intent.getStringArrayListExtra("proximityReport").toArray(new String[0]);

                    TextView nearbyDevicesCounterTextView = binding.scannerTextViewNearbyDevicesCounter;
                    TextView scannerIterationTextView = binding.scannerTextViewScannerIteration;
                    TextView uniqueDevicesTotalTextView = binding.scannerTextViewUniqueDevices;
                    ListView devicesListView = binding.proximityDetectionListView;
                    TextView updateFrequencyTextView = binding.scannerTextViewUpdateFrequency;

                    nearbyDevicesCounterTextView.setText(MessageFormat.format("{0}", devicesNearbyNum));
                    scannerIterationTextView.setText(MessageFormat.format(
                            SCANNER_ITERATION_PATTERN, scannerIteration
                    ));
                    uniqueDevicesTotalTextView.setText(MessageFormat.format(
                            UNIQUE_DEVICES_TOTAL_PATTERN, allUniqueDevicesNum
                    ));
                    updateFrequencyTextView.setText(MessageFormat.format(
                            UPDATE_FREQUENCY_PATTERN, processingWindowNanos / 1_000_000_000.0
                    ));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            inflater.getContext(),
                            android.R.layout.simple_list_item_1,
                            proximityInfos
                    );
                    devicesListView.setAdapter(adapter);
                }
            }
        };

        requireActivity().registerReceiver(receiver, intentFilter);

        switchCompat.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                progressBar.setVisibility(View.VISIBLE);

                fetchScannerPreferences(inflater.getContext());
                requireActivity()
                        .startService(
                                new Intent(getActivity(), ScannerService.class)
                                        .putExtra("processingWindowNanos", processingWindowNanos)
                                        .putExtra("reportDelayMillis", reportDelayMillis)
                                        .putExtra("scannerMode", scannerMode)
                                        .putExtra("callbackType", callbackType)
                                        .putExtra("matchMode", matchMode)
                                        .putExtra("numOfMatches", numOfMatches)
                        );
            } else {
                requireActivity()
                        .stopService(new Intent(getActivity(), ScannerService.class));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}