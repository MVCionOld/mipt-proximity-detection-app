package com.mvcion.proximitydetectionapp.ui.advertiser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdvertiserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdvertiserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is advertiser fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}