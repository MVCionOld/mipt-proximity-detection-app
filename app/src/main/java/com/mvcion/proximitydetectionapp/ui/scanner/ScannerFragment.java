package com.mvcion.proximitydetectionapp.ui.scanner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.R;
import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceTools;
import com.mvcion.proximitydetectionapp.databinding.FragmentScannerBinding;
import com.mvcion.proximitydetectionapp.services.ScannerService;
import com.mvcion.proximitydetectionapp.services.config.ScannerServiceConfig;

import java.text.MessageFormat;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ScannerFragment extends Fragment {

    private final String SCANNER_ITERATION_PATTERN = "Scanner iteration: {0}";
    private final String UNIQUE_DEVICES_TOTAL_PATTERN = "Unique devices total: {0}";

    private ScannerServiceConfig config = new ScannerServiceConfig();
    private FragmentScannerBinding binding;
    private BroadcastReceiver receiver;

    Thread fetchScannerPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        observeConfigChanges(inflater);

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

                    nearbyDevicesCounterTextView.setText(String.format("%d", devicesNearbyNum));
                    scannerIterationTextView.setText(MessageFormat.format(
                            SCANNER_ITERATION_PATTERN, scannerIteration
                    ));
                    uniqueDevicesTotalTextView.setText(MessageFormat.format(
                            UNIQUE_DEVICES_TOTAL_PATTERN, allUniqueDevicesNum
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

                Log.d("ScannerFragment", config.toString());

                requireActivity().startService(new Intent(getActivity(), ScannerService.class)
                        .putExtra("scannerMode", config.getScannerMode().get())
                        .putExtra("matchMode", config.getMatchMode().get())
                        .putExtra("numOfMatches", config.getNumOfMatches().get())
                        .putExtra("processingWindowNanos", config.getProcessingWindowNanos().get())
                );
            } else {
                requireActivity().unregisterReceiver(receiver);

                progressBar.setVisibility(View.INVISIBLE);

                requireActivity().stopService(new Intent(getActivity(), ScannerService.class));

                TextView nearbyDevicesCounterTextView = binding.scannerTextViewNearbyDevicesCounter;
                TextView scannerIterationTextView = binding.scannerTextViewScannerIteration;
                TextView uniqueDevicesTotalTextView = binding.scannerTextViewUniqueDevices;
                ListView devicesListView = binding.proximityDetectionListView;

                nearbyDevicesCounterTextView.setText("0");
                scannerIterationTextView.setText(MessageFormat.format(
                        SCANNER_ITERATION_PATTERN, 0
                ));
                uniqueDevicesTotalTextView.setText(MessageFormat.format(
                        UNIQUE_DEVICES_TOTAL_PATTERN, 0
                ));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        new String[]{}
                );
                devicesListView.setAdapter(adapter);
            }
        });

        ImageView infoButton = binding.scannerImageViewInfo;
        infoButton.setOnClickListener(new View.OnClickListener() {

            boolean isOnTouchListenerSet = false;
            boolean isPoppedUp = false;

            @SuppressLint("InflateParams")
            final View popupView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.popup_window_scanner, null);

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                if (!isOnTouchListenerSet) {
                    popupView.setOnTouchListener((touchView, event) -> {
                        popupWindow.dismiss();
                        return true;
                    });
                    isOnTouchListenerSet = true;
                }
                isPoppedUp = !isPoppedUp;
                if (isPoppedUp) {
                    ((TextView)popupWindow.getContentView()
                            .findViewById(R.id.scanner__popupwindow__textview)
                    ).setText(config.toString());
                    popupWindow.setElevation(20);
                    popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
                } else {
                    popupWindow.dismiss();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            requireActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {}

        binding = null;
        fetchScannerPreferences.interrupt();

        try {
            fetchScannerPreferences.join();
        } catch (InterruptedException exception) {
            Log.e("ScannerFragment", exception.toString());
        }
    }

    private void observeConfigChanges(@NonNull LayoutInflater inflater) {
        fetchScannerPreferences = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Context context = inflater.getContext();
                    config.getScannerMode().set(PreferencesFacade.getScanMode(context));
                    config.getMatchMode().set(PreferencesFacade.getScanMatchMode(context));
                    config.getNumOfMatches().set(PreferencesFacade.getScanNumOfMatches(context));
                    config.getProcessingWindowNanos().set(PreferencesFacade.getScanProcessingWindowNanos(context));
                    Thread.sleep(1_000L);
                }
            } catch (InterruptedException exception) {
                Log.e("ScannerFragment", exception.toString());
            }
        });
        fetchScannerPreferences.start();
    }
}