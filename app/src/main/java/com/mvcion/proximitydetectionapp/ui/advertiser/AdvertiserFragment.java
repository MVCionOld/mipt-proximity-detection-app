package com.mvcion.proximitydetectionapp.ui.advertiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;
import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceTools;
import com.mvcion.proximitydetectionapp.databinding.FragmentAdvertiserBinding;
import com.mvcion.proximitydetectionapp.services.AdvertiserService;

import java.text.MessageFormat;

public class AdvertiserFragment extends Fragment {

    private int advertiserMode = DefaultPreferences.getAdvertiseModeValue();
    private int advertiserTxPower = DefaultPreferences.getAdvertiseTxPowerValue();
    private FragmentAdvertiserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("AdvertiserFragment", MessageFormat.format("advertiserMode: {0}", advertiserMode));
        Log.d("AdvertiserFragment", MessageFormat.format("advertiserTxPower: {0}", advertiserTxPower));

        Thread fetchAdvertiserPreferences = new Thread(() -> {
            Context context = inflater.getContext();
            advertiserMode = PreferencesFacade.getAdvertiseMode(context);
            advertiserTxPower = PreferencesFacade.getAdvertiseTxPower(context);
        });
        fetchAdvertiserPreferences.start();

        AdvertiserViewModel advertiserViewModel = new ViewModelProvider(this)
                .get(AdvertiserViewModel.class);

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
                try {
                    fetchAdvertiserPreferences.join();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                requireActivity()
                        .startService(
                                new Intent(getActivity(), AdvertiserService.class)
                                    .putExtra("advertiserMode", advertiserMode)
                                    .putExtra("advertiserTxPower", advertiserTxPower)
                        );
            } else {
                requireActivity()
                        .stopService(new Intent(getActivity(), AdvertiserService.class));
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