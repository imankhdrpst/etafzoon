package com.mindology.app.ui.auth;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindology.app.R;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.android.support.DaggerAppCompatActivity;

public class ForgetPasswordActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private ForgetPasswordViewModel viewModel;

    private TextInputEditText username;
    private TextInputLayout usernameLayout;
    private RelativeLayout progressBar;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Drawable logo;

    @Inject
    RequestManager requestManager;
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);


        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

        username = findViewById(R.id.user_id_input);
        usernameLayout = findViewById(R.id.user_id_input_layout);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.login_button).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(ForgetPasswordViewModel.class);

        subscribeObservers();

    }

    private void subscribeObservers() {
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<ClientUserDTO>>() {
            @Override
            public void onChanged(AuthResource<ClientUserDTO> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {

                        case LOADING: {
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED: {
                            showProgressBar(false);
                            onForgetPasswordDone();
                            break;
                        }

                        case ERROR: {
                            showProgressBar(false);
                            new SweetAlertDialog(ForgetPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(userAuthResource.message)
                                    .setConfirmText(getString(R.string.ok))
                                    .setCancelText(null)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .show();
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            showProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });
    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void onForgetPasswordDone() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button: {

                attemptForgetPassword();
                break;
            }
        }
    }

    private void attemptForgetPassword() {


        if (TextUtils.isEmpty(username.getText().toString())) {
            usernameLayout.setError(getString(R.string.error_username_empty));
        } else {
            usernameLayout.setError("");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
            usernameLayout.setError(getString(R.string.error_username_valid_email));
        } else {
            usernameLayout.setError("");
        }

//        if (TextUtils.isEmpty(usernameLayout.getError())) {
//            viewModel.forgetPassword(username.getText().toString());
//        }
    }
}
