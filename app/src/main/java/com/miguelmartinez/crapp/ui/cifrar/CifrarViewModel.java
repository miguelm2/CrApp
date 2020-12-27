package com.miguelmartinez.crapp.ui.cifrar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CifrarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CifrarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}