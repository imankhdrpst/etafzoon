package com.mindology.app.ui.auth;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindology.app.R;
import com.mindology.app.models.ContactMessageResponse;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.android.support.DaggerAppCompatActivity;

public class ContactUsActivity extends DaggerAppCompatActivity {

    private static final String TAG = "ContactUsActivity";

    private ContactUsViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    private LinearLayout btnSend;
    private RelativeLayout progressBar;
    private TextInputEditText txtName, txtEmail, txtMessage;
    private int currentApiVersion;
    private TextInputLayout txtLayoutName, txtLayoutEmail, txtLayoutMessage;
    private View imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_us);

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

        viewModel = ViewModelProviders.of(this, providerFactory).get(ContactUsViewModel.class);

        progressBar = findViewById(R.id.progress_bar);

        txtName = findViewById(R.id.txt_name);
        txtName.setHint(getString(R.string.name));
        txtLayoutName = findViewById(R.id.txt_layout_contact_us_name);
        txtEmail = findViewById(R.id.txt_email);
        txtEmail.setHint(getString(R.string.email));
        txtLayoutEmail = findViewById(R.id.txt_layout_contact_us_email);
        txtMessage = findViewById(R.id.txt_message);
        txtMessage.setHint(getString(R.string.message));
        txtLayoutMessage = findViewById(R.id.txt_layout_contact_us_message);
        imgBack = findViewById(R.id.img_back);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend = findViewById(R.id.btn_send_message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtName.getText().toString())) {
                    txtLayoutName.setError("Name must not be empty");
                } else {
                    txtLayoutMessage.setError(null);
                }
                if (TextUtils.isEmpty(txtEmail.getText().toString())) {
                    txtLayoutEmail.setError("Email must not be empty");
                } else {
                    txtLayoutEmail.setError(null);
                }
                if (TextUtils.isEmpty(txtMessage.getText().toString())) {
                    txtLayoutMessage.setError("Message must not be empty");
                } else {
                    txtLayoutMessage.setError(null);
                }
                if (TextUtils.isEmpty(txtLayoutName.getError()) && TextUtils.isEmpty(txtLayoutEmail.getError()) && TextUtils.isEmpty(txtLayoutMessage.getError())) {
                    subscribeSendMessageObserver(txtName.getText().toString(), txtEmail.getText().toString(), "spinnerCategories.toString()", txtMessage.getText().toString());
                }
            }
        });

        List<String> types = new ArrayList<>();

        types.add("General");
        types.add("Pricing & Plans");
        types.add("Billing");
        types.add("Partnership");
        types.add("Employment");
        types.add("Tenant Problem");
        types.add("House Owner Problem");

        ArrayAdapter propertyTypeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, types);
        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ((MaterialSpinner) findViewById(R.id.spinnerCategories)).setAdapter(propertyTypeAdapter);

        if (TempDataHolder.getCurrentUser() != null) {
            txtName.setText(TempDataHolder.getCurrentUser().getFirstName());
            txtEmail.setText(TempDataHolder.getCurrentUser().getEmail());
        }

        subscribeObservers();

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

    private void subscribeSendMessageObserver(String name, String email, String category, String message) {
//        viewModel.observeSendMessage(name, email, category, message).removeObservers(this);
        viewModel.observeSendMessage(name, email, category, message).observe(this, new Observer<Resource<ContactMessageResponse>>() {
            @Override
            public void onChanged(Resource<ContactMessageResponse> contactMessageResponseResource) {
                if (contactMessageResponseResource != null) {
                    switch (contactMessageResponseResource.status) {
                        case ERROR:
                            progressBar.setVisibility(View.GONE);

                            SweetAlertDialog dialog = new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.ERROR_TYPE);
                            dialog.setTitleText(getString(R.string.excepttion));
                            dialog.setContentText(contactMessageResponseResource.message);
                            dialog.setConfirmText(getString(R.string.button_ok));
                            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismissWithAnimation();

                                    finish();
                                }
                            });

                            dialog.show();

                            break;
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:

                            progressBar.setVisibility(View.GONE);

                            SweetAlertDialog sDialog = new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            sDialog.setTitleText(getString(R.string.success));
                            sDialog.setContentText("Your message has been sent with id : " + contactMessageResponseResource.data.getId());
                            sDialog.setConfirmText(getString(R.string.ok));
                            sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sDialog.dismissWithAnimation();

                                    finish();
                                }
                            });

                            sDialog.show();
                            break;
                    }
                }
            }
        });
    }


    private void subscribeObservers() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
