package com.mvcion.proximitydetectionapp.ui.combined;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CombinedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CombinedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is combined fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}