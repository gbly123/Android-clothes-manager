package com.example.redbook.viewModel;

import android.location.Address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RedBookViewModel extends ViewModel {

    private MutableLiveData<Address> addressLiveData;

    public RedBookViewModel() {
        addressLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Address> getAddressLiveData() {
        return addressLiveData;
    }

}
