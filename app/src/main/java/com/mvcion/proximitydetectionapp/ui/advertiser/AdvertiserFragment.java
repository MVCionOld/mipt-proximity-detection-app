package com.mvcion.proximitydetectionapp.ui.advertiser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.mvcion.proximitydetectionapp.databinding.FragmentAdvertiserBinding;
import com.mvcion.proximitydetectionapp.services.AdvertiserService;
import com.mvcion.proximitydetectionapp.services.config.AdvertiserServiceConfig;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class AdvertiserFragment extends Fragment {

    private AdvertiserServiceConfig config = new AdvertiserServiceConfig();
    private FragmentAdvertiserBinding binding;

    private Thread fetchAdvertiserPreferences;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        observeConfigChanges(inflater);

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
                AdvertiserServiceConfig currentConfig = config.clone();
                Log.d("AdvertiserFragment", currentConfig.toString());
                requireActivity().startService(new Intent(getActivity(), AdvertiserService.class)
                        .putExtra("advertiserMode", currentConfig.getAdvertiserMode().get())
                        .putExtra("advertiserTxPower", currentConfig.getAdvertiserTxPower().get())
                );
            } else {
                requireActivity()
                        .stopService(new Intent(getActivity(), AdvertiserService.class));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        ImageView infoButton = binding.advertiserImageViewInfo;
        infoButton.setOnClickListener(new View.OnClickListener() {

            boolean isOnTouchListenerSet = false;
            boolean isPoppedUp = false;

            @SuppressLint("InflateParams")
            final View popupView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.popup_window_advertiser, null);

            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

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
                            .findViewById(R.id.advertiser__popupwindow__textview)
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

        binding = null;
        fetchAdvertiserPreferences.interrupt();

        try {
            fetchAdvertiserPreferences.join();
        } catch (InterruptedException exception) {
            Log.e("AdvertiserFragment", exception.toString());
        }
    }

    private void observeConfigChanges(@NonNull LayoutInflater inflater) {
        fetchAdvertiserPreferences = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Context context = inflater.getContext();
                    config.getAdvertiserMode().set(PreferencesFacade.getAdvertiseMode(context));
                    config.getAdvertiserTxPower().set(PreferencesFacade.getAdvertiseTxPower(context));
                    Thread.sleep(1_000L);
                }
            } catch (InterruptedException exception) {
                Log.e("AdvertiserFragment", exception.toString());
            }
        });
        fetchAdvertiserPreferences.start();
    }
}