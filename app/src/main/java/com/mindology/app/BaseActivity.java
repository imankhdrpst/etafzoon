package com.mindology.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.ui.auth.AuthActivity;
import com.mindology.app.ui.auth.AuthResource;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    public SessionManager sessionManager;

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

        private void navLoginScreen(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void subscribeObservers(){
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<ClientUserDTO>>() {
            @Override
            public void onChanged(AuthResource<ClientUserDTO> userAuthResource) {
                if(userAuthResource != null){
                    switch (userAuthResource.status){
                        case LOADING:{
                            break;
                        }

                        case AUTHENTICATED:{
                            break;
                        }

                        case ERROR:{
                            break;
                        }

                        case NOT_AUTHENTICATED:{
                            navLoginScreen();
                            break;
                        }
                    }
                }
            }
        });
    }
}















