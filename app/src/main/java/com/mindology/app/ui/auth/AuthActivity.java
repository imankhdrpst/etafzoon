package com.mindology.app.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindology.app.R;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.ui.main.MainActivity;
import com.mindology.app.util.SoftInputAssist;
import com.mindology.app.viewmodels.ViewModelProviderFactory;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private AuthViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    private TextInputEditText inputPhoneNumber, inputActivationCode, inputName, inputFamily, inputAge;
    private AutoCompleteTextView inputCity;
    private RelativeLayout progressBar;
    private TextInputLayout layInputPhoneNumber, layInputActivationCode, layInputName, layInputFamily, layInputAge, layInputCity;
    private ExpandableLayout layExpandPhoneNumber, layExpandActivationCode, layExpandProfile;
    private int currentApiVersion;
    private LinearLayout layBannerActivationCode, layBannerProfile;
    private LinearLayout layBannerPhoneNumber;
    private MaterialButton btnNext, btnActivate, btnSaveProfile;
    private ImageView imgMindology;
    private ScrollView mainScrollView;
    private ImageView imgBtnBack;
    private TextView txtBannerActivationDescription;
    private TextView txtBannerActivationWrongNumber;
    private TextView txtActivationCodeDescription;
    private TextView txtResendCode;
    private boolean resendEnabled = false;

    private SoftInputAssist softInputAssist;

    private LoginState currentState;

    private String myPhoneNumber = "";
    private CountDownTimer resendTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_auth);

        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);

        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
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

        softInputAssist = new SoftInputAssist(this);

        mainScrollView = findViewById(R.id.main_scroll_view);

        progressBar = findViewById(R.id.progress_bar);

        imgBtnBack = findViewById(R.id.img_btn_back);

        imgBtnBack.setOnClickListener(this);

        imgMindology = findViewById(R.id.img_mindology);

        layBannerPhoneNumber = findViewById(R.id.lay_banner_phone_number);
        layBannerActivationCode = findViewById(R.id.lay_banner_activation_code);
        txtBannerActivationDescription = findViewById(R.id.txt_banner_activation_code_description);
        txtBannerActivationWrongNumber = findViewById(R.id.txt_banner_activation_code_wrong_number);
        txtBannerActivationWrongNumber.setOnClickListener(this);


        layBannerProfile = findViewById(R.id.lay_banner_profile);

        layExpandPhoneNumber = findViewById(R.id.expandable_layout_phone_number);
        layExpandActivationCode = findViewById(R.id.expandable_layout_activation_code);
        layExpandProfile = findViewById(R.id.expandable_layout_profile);

        inputPhoneNumber = findViewById(R.id.input_phone_number);
        layInputPhoneNumber = findViewById(R.id.txt_layout_phone_number);
        inputPhoneNumber.setHint(getString(R.string.phone_number_sample));

        inputActivationCode = findViewById(R.id.input_activation_code);
        layInputActivationCode = findViewById(R.id.txt_layout_activation_code);
        txtActivationCodeDescription = findViewById(R.id.txt_activation_code_description);
        txtResendCode = findViewById(R.id.txt_resend_code);
        txtResendCode.setOnClickListener(this);


        inputName = findViewById(R.id.input_name);
        layInputName = findViewById(R.id.txt_layout_name);
        inputName.setHint(getString(R.string.name));

        inputFamily = findViewById(R.id.input_family);
        layInputFamily = findViewById(R.id.txt_layout_family);
        inputFamily.setHint(getString(R.string.family));

        inputAge = findViewById(R.id.input_age);
        layInputAge = findViewById(R.id.txt_layout_age);
        inputAge.setHint(getString(R.string.age));

        inputCity = findViewById(R.id.input_city);
        layInputCity = findViewById(R.id.txt_layout_city);
        inputCity.setHint(getString(R.string.city));
        String[] cities = {"تهران", "اصفهان", "مشهد", "رشت", "تبریز", "اهواز", "کرمان", "شیراز"};
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cities);
        inputCity.setAdapter(citiesAdapter);

        btnNext = findViewById(R.id.btn_next);
        btnActivate = findViewById(R.id.btn_activate);
        btnSaveProfile = findViewById(R.id.btn_save);

        btnNext.setOnClickListener(this);
        btnActivate.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);


        inputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (TextUtils.isEmpty(charSequence.toString())) {
//                    layInputPhoneNumber.setError(getString(R.string.error_username_empty));
//                } else if (!Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches()) {
//                    layInputPhoneNumber.setError(getString(R.string.error_username_valid_email));
//                } else {
//                    layInputPhoneNumber.setError("");
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        subscribeObservers();

        changeStateOfLogin(LoginState.SPLASH);

    }

    private void onBackButtonClicked() {
        switch (currentState) {
            case SPLASH:
            case PHONE_NUMBER:
                return;
            case ACTIVATION_CODE:
                changeStateOfLogin(LoginState.PHONE_NUMBER);
                break;
            case PROFILE:
                changeStateOfLogin(LoginState.ACTIVATION_CODE);
        }
    }

    private void onBtnSaveProfileClicked() {
        if (TextUtils.isEmpty(inputName.getText().toString())) {
            layInputName.setError("نام را وارد نمایید");
        } else {
            layInputName.setError("");
            viewModel.authenticateWithProfile(inputName.getText().toString(), inputFamily.getText().toString(), myPhoneNumber, inputAge.getText().toString(), inputCity.getText().toString());
        }

    }

    private void onBtnActivateClicked() {
        if (TextUtils.isEmpty(inputActivationCode.getText().toString())) {
            layInputActivationCode.setError("کد فعال سازی را وارد نمایید");
        }
//        else if (viewModel.observeAuthState().getValue().status == AuthResource.AuthStatus.AUTHENTICATED) {
//            layInputActivationCode.setError("");
//            changeStateOfLogin(LoginState.PROFILE);
//        }
        else {
            layInputActivationCode.setError("");
            attemptActivationCode();
        }
    }

    private void onBtnNextClicked() {
        if (TextUtils.isEmpty(inputPhoneNumber.getText().toString())) {
            layInputPhoneNumber.setError(getString(R.string.error_phone_number_empty));
        } else if (inputPhoneNumber.getText().toString().trim().length() != 11) {
            layInputPhoneNumber.setError(getString(R.string.error_phone_number_not_valid));
        } else if (viewModel.observeAuthState().getValue().status == AuthResource.AuthStatus.PHONE_VALID_NOT_REGISTERED
                && inputPhoneNumber.getText().toString().equals(viewModel.getLatestMobileAttempted())) {
            layInputPhoneNumber.setError("");
            changeStateOfLogin(LoginState.ACTIVATION_CODE);
        }
        else if (viewModel.observeAuthState().getValue().status == AuthResource.AuthStatus.PHONE_VALID_REGISTERED
                && inputPhoneNumber.getText().toString().equals(viewModel.getLatestMobileAttempted())) {
            layInputPhoneNumber.setError("");
            changeStateOfLogin(LoginState.ENTER_CODE);
        } else {
            layInputPhoneNumber.setError("");
            attemptPhoneNumber();
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Contact us");

        startActivity(Intent.createChooser(intent, "Send Email"));
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
                        case ERROR: {
                            showProgressBar(false);
                            new SweetAlertDialog(AuthActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(userAuthResource.message)
                                    .setConfirmText(getString(R.string.ok))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            if ((viewModel.observeAuthState().getValue().status == AuthResource.AuthStatus.PHONE_VALID_NOT_REGISTERED
                                                    || viewModel.observeAuthState().getValue().status == AuthResource.AuthStatus.PHONE_VALID_REGISTERED)
                                                    && inputPhoneNumber.getText().toString().equals(viewModel.getLatestMobileAttempted())) {
                                                changeStateOfLogin(LoginState.ACTIVATION_CODE);
                                            }
                                        }
                                    })
                                    .show();

                            break;
                        }

                        case PHONE_VALID_REGISTERED: {
                            showProgressBar(false);
                            changeStateOfLogin(LoginState.ENTER_CODE);
                            break;
                        }

                        case PHONE_VALID_NOT_REGISTERED: {
                            showProgressBar(false);
                            changeStateOfLogin(LoginState.ACTIVATION_CODE);
                            break;
                        }

                        case PROFILE_FILLED:
                        case CODE_ENTERED: {
                            showProgressBar(false);
                            onLoginSuccess();
                            break;
                        }

                        case ACTIVATED: {
                            showProgressBar(false);
                            changeStateOfLogin(LoginState.PROFILE);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        softInputAssist.onResume();
        if (viewModel.authenticateWithSavedToken())
            onLoginSuccess();
    }

    @Override
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        softInputAssist.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }

    }

    private void onLoginSuccess() {
//        Log.d(TAG, "onLoginSuccess: login successful!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
//
//    private void setLogo() {
////        requestManager
////                .load(logo)
////                .into((ImageView) findViewById(R.id.login_logo));
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_btn_back:
                onBackButtonClicked();
//                attemptLogin();
                break;
            case R.id.btn_next:
                onBtnNextClicked();
                break;
            case R.id.btn_activate:
                onBtnActivateClicked();
                break;
            case R.id.btn_save:
                onBtnSaveProfileClicked();
                break;
            case R.id.txt_banner_activation_code_wrong_number:
                onBackButtonClicked();
                break;
            case R.id.txt_resend_code:
                onResendCodeClicked();
                break;

        }
    }

    private void onResendCodeClicked() {
        if (resendEnabled) {
            attemptPhoneNumber();
        }
    }


    private void attemptPhoneNumber() {
        if (!TextUtils.isEmpty(inputPhoneNumber.getText().toString()) && TextUtils.isEmpty(layInputPhoneNumber.getError())) {
            viewModel.authenticateWithPhoneNumber(inputPhoneNumber.getText().toString());
        }
    }

    private void attemptActivationCode() {
        if (!TextUtils.isEmpty(inputPhoneNumber.getText().toString()) && TextUtils.isEmpty(layInputPhoneNumber.getError())) {
            viewModel.authenticateWithActivationCode(inputPhoneNumber.getText().toString(), inputActivationCode.getText().toString());
        }
    }

    private void changeStateOfLogin(LoginState state) {
        mainScrollView.scrollTo(0, mainScrollView.getBottom());
        currentState = state;

        switch (state) {
            case SPLASH:
                imgBtnBack.setVisibility(View.GONE);
                imgMindology.setVisibility(View.VISIBLE);
                layBannerPhoneNumber.setVisibility(View.GONE);
                layBannerActivationCode.setVisibility(View.GONE);
                layBannerProfile.setVisibility(View.GONE);
                layExpandPhoneNumber.collapse();
                layExpandActivationCode.collapse();
                layExpandProfile.collapse();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeStateOfLogin(LoginState.PHONE_NUMBER);
                    }
                }, 2500);
                break;
            case PHONE_NUMBER:
                imgBtnBack.setVisibility(View.GONE);
                imgMindology.setVisibility(View.GONE);
                layBannerPhoneNumber.setVisibility(View.VISIBLE);
                layBannerActivationCode.setVisibility(View.GONE);
                layBannerProfile.setVisibility(View.GONE);
                layExpandPhoneNumber.expand(true);
                layExpandActivationCode.collapse();
                layExpandProfile.collapse();
                break;
            case ENTER_CODE:
                if (resendTimer != null) {
                    resendTimer.cancel();
                }
                resendEnabled = false;
                inputActivationCode.setHint(getString(R.string.enter_code));
                btnActivate.setText(getString(R.string.login));
                txtResendCode.setTextColor(getResources().getColor(R.color.gray));
                myPhoneNumber = inputPhoneNumber.getText().toString();
                txtBannerActivationDescription.setText("یک کد به شماره " + myPhoneNumber + " ارسال شده است. لطفا کد را در باکس زیر وارد کنید");
                resendTimer = new CountDownTimer(120000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        String time = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) + ":" +
                                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        txtActivationCodeDescription.setText("پس از " + time + " دقیقه می توانید دوباره درخواست ارسال کد کنید");
                    }

                    public void onFinish() {
                        txtResendCode.setTextColor(getResources().getColor(R.color.green));
                        txtActivationCodeDescription.setText("در صورتی که هنوز کد را دریافت نکرده اید ارسال دوباره را بزنید");
                        resendEnabled = true;
                    }

                }.start();
                imgBtnBack.setVisibility(View.VISIBLE);
                imgMindology.setVisibility(View.GONE);
                layBannerPhoneNumber.setVisibility(View.GONE);
                layBannerActivationCode.setVisibility(View.VISIBLE);
                layBannerProfile.setVisibility(View.GONE);
                layExpandPhoneNumber.collapse();
                layExpandActivationCode.expand(true);
                layExpandProfile.collapse();
                inputActivationCode.setText("");
                txtActivationCodeDescription.setText("");
                break;
            case ACTIVATION_CODE:
                if (resendTimer != null) {
                    resendTimer.cancel();
                }
                resendEnabled = false;
                inputActivationCode.setHint(getString(R.string.activation_code));
                btnActivate.setText(getString(R.string.activate));
                txtResendCode.setTextColor(getResources().getColor(R.color.gray));
                myPhoneNumber = inputPhoneNumber.getText().toString();
                txtBannerActivationDescription.setText("یک کد به شماره " + myPhoneNumber + " ارسال شده است. لطفا کد را در باکس زیر وارد کنید");
                resendTimer = new CountDownTimer(120000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        String time = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) + ":" +
                                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        txtActivationCodeDescription.setText("پس از " + time + " دقیقه می توانید دوباره درخواست ارسال کد کنید");
                    }

                    public void onFinish() {
                        txtResendCode.setTextColor(getResources().getColor(R.color.green));
                        txtActivationCodeDescription.setText("در صورتی که هنوز کد را دریافت نکرده اید ارسال دوباره را بزنید");
                        resendEnabled = true;
                    }

                }.start();
                imgBtnBack.setVisibility(View.VISIBLE);
                imgMindology.setVisibility(View.GONE);
                layBannerPhoneNumber.setVisibility(View.GONE);
                layBannerActivationCode.setVisibility(View.VISIBLE);
                layBannerProfile.setVisibility(View.GONE);
                layExpandPhoneNumber.collapse();
                layExpandActivationCode.expand(true);
                layExpandProfile.collapse();
                inputActivationCode.setText("");
                txtActivationCodeDescription.setText("");
                break;
            case PROFILE:
                imgBtnBack.setVisibility(View.VISIBLE);
                imgMindology.setVisibility(View.GONE);
                layBannerPhoneNumber.setVisibility(View.GONE);
                layBannerActivationCode.setVisibility(View.GONE);
                layBannerProfile.setVisibility(View.VISIBLE);
                layExpandPhoneNumber.collapse();
                layExpandActivationCode.collapse();
                layExpandProfile.expand(true);
                break;
        }
    }


    public enum LoginState {
        SPLASH,
        PHONE_NUMBER,
        ACTIVATION_CODE,
        ENTER_CODE,
        PROFILE,
    }
}
