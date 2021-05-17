package com.mvcion.proximitydetectionapp.ui.scanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.common.PreferencesFacade;
import com.mvcion.proximitydetectionapp.databinding.FragmentScannerBinding;

public class ScannerFragment extends Fragment {

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

//        final TextView textView = binding.textScanner;
//        scannerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}