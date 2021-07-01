package com.mvcion.proximitydetectionapp.ui.scanner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;
import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceTools;
import com.mvcion.proximitydetectionapp.databinding.FragmentScannerBinding;
import com.mvcion.proximitydetectionapp.services.ScannerService;

import java.text.MessageFormat;

public class ScannerFragment extends Fragment {

    private final String SCANNER_ITERATION_PATTERN = "Scanner iteration: {0}";
    private final String UNIQUE_DEVICES_TOTAL_PATTERN = "Unique devices total: {0}";
    private final String UPDATE_FREQUENCY_PATTERN = "Update frequency: {0}{1}";
    private int scannerMode = DefaultPreferences.getAdvertiseModeValue();
    private int matchMode = DefaultPreferences.getScanMatchModeValue();
    private int numOfMatches = DefaultPreferences.getScanNumOfMatchesValue();
    private long processingWindowNanos = DefaultPreferences.getScanProcessingWindowNanosValue();
    private FragmentScannerBinding binding;
    private BroadcastReceiver receiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("ScannerFragment", MessageFormat.format("scannerMode: {0}", scannerMode));
        Log.d("ScannerFragment", MessageFormat.format("matchMode: {0}", matchMode));
        Log.d("ScannerFragment", MessageFormat.format("numOfMatches: {0}", numOfMatches));
        Log.d("ScannerFragment", MessageFormat.format("processingWindowNanos: {0}", processingWindowNanos));

        Thread fetchScannerPreferences = new Thread(() -> {
            Context context = inflater.getContext();
            scannerMode = PreferencesFacade.getScanMode(context);
            matchMode = PreferencesFacade.getScanMatchMode(context);
            numOfMatches = PreferencesFacade.getScanNumOfMatches(context);
            processingWindowNanos = PreferencesFacade.getScanProcessingWindowNanos(context);
        });
        fetchScannerPreferences.start();

        ScannerViewModel scannerViewModel = new ViewModelProvider(this)
                .get(ScannerViewModel.class);

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
        receiver = new BroadcastReceiver() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ScannerService")) {

                    int devicesNearbyNum = intent
                            .getIntExtra("devicesNearbyNum", 0);
                    int scannerIteration = intent
                            .getIntExtra("scannerIteration", 0);
                    int allUniqueDevicesNum = intent
                            .getIntExtra("allUniqueDevicesNum", 0);
                    String[] proximityInfos = intent
                            .getStringArrayListExtra("proximityReport").toArray(new String[0]);

                    TextView nearbyDevicesCounterTextView = binding.scannerTextViewNearbyDevicesCounter;
                    TextView scannerIterationTextView = binding.scannerTextViewScannerIteration;
                    TextView uniqueDevicesTotalTextView = binding.scannerTextViewUniqueDevices;
                    ListView devicesListView = binding.proximityDetectionListView;
                    TextView updateFrequencyTextView = binding.scannerTextViewUpdateFrequency;

                    nearbyDevicesCounterTextView.setText(String.format("%d", devicesNearbyNum));
                    scannerIterationTextView.setText(MessageFormat.format(
                            SCANNER_ITERATION_PATTERN, scannerIteration
                    ));
                    uniqueDevicesTotalTextView.setText(MessageFormat.format(
                            UNIQUE_DEVICES_TOTAL_PATTERN, allUniqueDevicesNum
                    ));
                    updateFrequencyTextView.setText(MessageFormat.format(
                            UPDATE_FREQUENCY_PATTERN,
                            processingWindowNanos / 1_000_000_000.0, "s"
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
                requireActivity().registerReceiver(receiver, intentFilter);
                progressBar.setVisibility(View.VISIBLE);

                try {
                    fetchScannerPreferences.join();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                requireActivity()
                        .startService(
                                new Intent(getActivity(), ScannerService.class)
                                        .putExtra("scannerMode", scannerMode)
                                        .putExtra("matchMode", matchMode)
                                        .putExtra("numOfMatches", numOfMatches)
                                        .putExtra("processingWindowNanos", processingWindowNanos)
                        );
            } else {
                requireActivity().unregisterReceiver(receiver);

                progressBar.setVisibility(View.INVISIBLE);

                requireActivity()
                        .stopService(
                                new Intent(
                                        getActivity(),
                                        ScannerService.class
                                )
                        );

                TextView nearbyDevicesCounterTextView = binding.scannerTextViewNearbyDevicesCounter;
                TextView scannerIterationTextView = binding.scannerTextViewScannerIteration;
                TextView uniqueDevicesTotalTextView = binding.scannerTextViewUniqueDevices;
                ListView devicesListView = binding.proximityDetectionListView;
                TextView updateFrequencyTextView = binding.scannerTextViewUpdateFrequency;

                nearbyDevicesCounterTextView.setText("0");
                scannerIterationTextView.setText(MessageFormat.format(
                        SCANNER_ITERATION_PATTERN, 0
                ));
                uniqueDevicesTotalTextView.setText(MessageFormat.format(
                        UNIQUE_DEVICES_TOTAL_PATTERN, 0
                ));
                updateFrequencyTextView.setText(MessageFormat.format(
                        UPDATE_FREQUENCY_PATTERN, "-", ""
                ));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        new String[]{}
                );
                devicesListView.setAdapter(adapter);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        try {
            requireActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {}
        super.onDestroyView();
        binding = null;
    }
}