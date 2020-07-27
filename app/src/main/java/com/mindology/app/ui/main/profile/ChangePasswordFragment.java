package com.mindology.app.ui.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;

public class ChangePasswordFragment extends BaseFragment {

    private static final String TAG = "ChangePasswordFragment";

    private ChangePasswordViewModel viewModel;

    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: GroupedInspections. " + this);

        this.view = view;

        viewModel = ViewModelProviders.of(this, providerFactory).get(ChangePasswordViewModel.class);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

}




















