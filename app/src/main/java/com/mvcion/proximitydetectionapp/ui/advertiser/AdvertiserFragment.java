package com.mvcion.proximitydetectionapp.ui.advertiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.services.AdvertiserService;
import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceTools;
import com.mvcion.proximitydetectionapp.databinding.FragmentAdvertiserBinding;

public class AdvertiserFragment extends Fragment {

    private int advertiserMode;
    private int advertiserTxPower;
    private boolean isConnectable;

    private FragmentAdvertiserBinding binding;

    private void fetchAdvertiserPreferences(Context context) {
        advertiserMode = PreferencesFacade.getAdvertiserMode(context);
        advertiserTxPower = PreferencesFacade.getAdvertiserTxPower(context);
        isConnectable = PreferencesFacade.getAdvertiserIsConnectable(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdvertiserViewModel advertiserViewModel = new ViewModelProvider(this).get(AdvertiserViewModel.class);

        binding = FragmentAdvertiserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProgressBar progressBar = binding.advertiserProgressBarRunning;
        progressBar.setVisibility(View.INVISIBLE);

        SwitchCompat switchCompat = binding.advertiserSwitchCompatRunning;

        if (ServiceTools.isServiceRunning(inflater.getContext(), AdvertiserService.class)) {
            progressBar.setVisibility(View.VISIBLE);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                progressBar.setVisibility(View.VISIBLE);
                fetchAdvertiserPreferences(inflater.getContext());
                requireActivity()
                        .startService(
                                new Intent(getActivity(), AdvertiserService.class)
                                    .putExtra("advertiserMode", advertiserMode)
                                    .putExtra("advertiserTxPower", advertiserTxPower)
                                    .putExtra("isConnectable", isConnectable)
                        );
            } else {
                requireActivity()
                        .stopService(
                                new Intent(
                                        getActivity(),
                                        AdvertiserService.class
                                )
                        );
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