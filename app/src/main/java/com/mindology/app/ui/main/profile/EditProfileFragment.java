package com.mindology.app.ui.main.profile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Constants;
import com.mindology.app.util.Enums;
import com.mindology.app.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "EditProfileFragment";
    private static final int REQUEST_CODE_CHOOSE = 1001;

    private EditProfileViewModel viewModel;

    private View view;
    private List<Uri> mSelected;
    private CircleImageView imgProfilePicture;
    private TextInputEditText txtName, txtFamily, txtAge, txtEmail;
    private TextInputLayout txtLayoutName, txtLayoutFamily, txtLayoutAge, txtLayoutCity, txtLayoutMarriageStatus, txtLayoutEmail, txtLayoutEducationType;
    private AutoCompleteTextView txtCity, txtMarriageStatus, txtEducationType;
    private TextView lblNameAndFamily, lblMobileNumber;
    private View progressBar;
    private String changedPhotoUri = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ImageView imgEditPicture;
    private MaterialButton btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false).getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: GroupedInspections. " + this);

        this.view = view;

        viewModel = ViewModelProviders.of(this, providerFactory).get(EditProfileViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        imgProfilePicture = view.findViewById(R.id.img_profile_picture);

        imgEditPicture = view.findViewById(R.id.img_edit_picture);
        imgEditPicture.setOnClickListener(this);

        lblNameAndFamily = view.findViewById(R.id.txt_profile_first_name_last_name);
        lblMobileNumber = view.findViewById(R.id.txt_profile_mobile_number);

        txtName = view.findViewById(R.id.input_name);
        txtLayoutName = view.findViewById(R.id.txt_layout_name);
        txtName.setHint(getString(R.string.name));

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lblNameAndFamily.setText(s.toString() + " " + txtFamily.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtFamily = view.findViewById(R.id.input_family);
        txtLayoutFamily = view.findViewById(R.id.txt_layout_family);
        txtFamily.setHint(getString(R.string.family));

        txtFamily.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lblNameAndFamily.setText(txtName.getText().toString() + " " + s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtAge = view.findViewById(R.id.input_age);
        txtLayoutAge = view.findViewById(R.id.txt_layout_age);
        txtAge.setHint(getString(R.string.age));

        txtCity = view.findViewById(R.id.input_city);
        txtLayoutCity = view.findViewById(R.id.txt_layout_city);
        txtCity.setHint(getString(R.string.city));

        txtEmail = view.findViewById(R.id.input_email);
        txtLayoutEmail = view.findViewById(R.id.txt_layout_email);
        txtEmail.setHint(getString(R.string.email));

        txtMarriageStatus = view.findViewById(R.id.input_gender);
        txtLayoutMarriageStatus = view.findViewById(R.id.txt_layout_gender);
        txtMarriageStatus.setHint(getString(R.string.gender));

        txtEducationType = view.findViewById(R.id.input_education_type);
        txtLayoutEducationType = view.findViewById(R.id.txt_layout_education_type);
        txtEducationType.setHint(getString(R.string.education_type));

        btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        subscribeProfile();

    }

    private void onConfirmChanges() {

        if (TextUtils.isEmpty(txtName.getText().toString()))
        {
            txtLayoutName.setError("نام را وارد نمایید");
            return;
        }
        else
        {
            txtLayoutName.setError("");
        }


        ClientUserDTO user = TempDataHolder.getCurrentUser();

        user.setFirstName(txtName.getText().toString());
        user.setLastName(txtFamily.getText().toString());
        user.setAge(txtAge.getText().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setLivingCity(txtCity.getText().toString());
        user.setEducationType(Enums.EducationType.findByName(txtEducationType.getText().toString()));
        user.setMarriageStatus(Enums.MarriageStatus.findByName(txtMarriageStatus.getText().toString()));

        if (changedPhotoUri != null) {

            progressBar.setVisibility(View.VISIBLE);

            Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {

                    try {
                        Bitmap bitmap = new Compressor(getActivity())
                                .setQuality(Constants.COMPRESS_QUALITY)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setMaxHeight(Constants.COMPRESS_MAX_HEIGHT)
                                .setMaxWidth(Constants.COMPRESS_MAX_WIDTH)
//                                .compressToBitmap(new File(Utils.getRealPathFromURI(getContext(), changedPhotoUri)));
                                .compressToBitmap(new File(changedPhotoUri));
                        return Utils.bitmapToStringBase64(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return "";
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String o) throws Exception {

                            user.setProfilePicture(o);
                            viewModel.saveProfile(user);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            viewModel.saveProfile(user);
        }

    }

    private void subscribeProfile() {
//        viewModel.observerGetProfile().removeObservers(getViewLifecycleOwner());
        viewModel.observerProfile().observe(getViewLifecycleOwner(), new Observer<Resource<ClientUserDTO>>() {
            @Override
            public void onChanged(Resource<ClientUserDTO> userResource) {
                if (userResource != null) {
                    switch (userResource.status) {
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(userResource.message)
                                    .setConfirmText(getString(R.string.ok))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                                        }
                                    })
                                    .show();
                            break;

                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;

                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            TempDataHolder.setCurrentUser(userResource.data);
                            fillWithData(userResource.data);
                            break;
                    }
                }
            }
        });
    }

    private void fillWithData(ClientUserDTO currentUser) {

        viewModel.getRequestManager()
                .load(Utils.stringBase64ToBitmap(currentUser.getProfilePicture()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(imgProfilePicture);

        lblNameAndFamily.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        lblMobileNumber.setText(currentUser.getMobileNumber());

        txtName.setText(currentUser.getFirstName());
        txtFamily.setText(currentUser.getLastName());
        txtAge.setText(currentUser.getAge());
        txtCity.setText(currentUser.getLivingCity());
        txtMarriageStatus.setText(currentUser.getMarriageStatus().name());

    }


    public void onChangePhotoClicked() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ImagePicker.create(EditProfileFragment.this)
                                    .showCamera(true)
                                    .single()
                                    .start();
                        } else {
                            // ermission is not granted
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            if (images != null && images.size() > 0) {

                viewModel.getRequestManager().load(images.get(0).getPath()).into(imgProfilePicture);

                changedPhotoUri = images.get(0).getPath();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit_picture:
                onChangePhotoClicked();
                break;
            case R.id.btn_save:
                onConfirmChanges();
                break;
        }
    }
}




















