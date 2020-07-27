package com.mindology.app.ui.main.profile;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.User;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class EditProfileFragment extends BaseFragment {

    private static final String TAG = "EditProfileFragment";
    private static final int REQUEST_CODE_CHOOSE = 1001;

    private EditProfileViewModel viewModel;

    private View view;
    private List<Uri> mSelected;
    private CircleImageView imgProfilePicture;
    private TextInputEditText txtPhoneNumber;
    private View progressBar;
    private LinearLayout btnConfirmChanges;
    private TextView txtProfileFirstNameAndLastName, txtEmail;
    private Uri changedPhotoUri = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: GroupedInspections. " + this);

        this.view = view;

        viewModel = ViewModelProviders.of(this, providerFactory).get(EditProfileViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        txtProfileFirstNameAndLastName = view.findViewById(R.id.txt_profile_first_name_last_name);
        txtEmail = view.findViewById(R.id.txt_profile_email);


        imgProfilePicture = view.findViewById(R.id.img_profile_picture);
        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangePhotoClicked();
            }
        });

        txtPhoneNumber = view.findViewById(R.id.txt_profile_phone_number);

        btnConfirmChanges = view.findViewById(R.id.lay_confirm);
        btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmChanges();
            }
        });

        if (TempDataHolder.getCurrentUser() != null) {
            fillWithData(TempDataHolder.getCurrentUser());
        } else {
            subscribeGetCurrentUserProfile();
        }
    }

    private void onConfirmChanges() {
        User user = TempDataHolder.getCurrentUser();

//        user.setAboutMe(txtAboutMe.getText().toString());
//        user.setNickname(txtNickName.getText().toString());
        user.setMobile(txtPhoneNumber.getText().toString());

        if (changedPhotoUri != null) {

            progressBar.setVisibility(View.VISIBLE);

//            Observable.fromCallable(new Callable<String>() {
//                @Override
//                public String call() throws Exception {
//
//                    try {
//                        Bitmap bitmap = new Compressor(getActivity())
//                                .setQuality(Constants.COMPRESS_QUALITY)
//                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                                .setMaxHeight(Constants.COMPRESS_MAX_HEIGHT)
//                                .setMaxWidth(Constants.COMPRESS_MAX_WIDTH)
//                                .compressToBitmap(new File(Utils.getRealPathFromURI(getContext(), changedPhotoUri)));
//                        return Utils.bitmapToStringBase64(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    return "";
//                }
//            })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String o) throws Exception {
//
//                            user.setPictureBase64(o);
//                            subscribeEditProfileObserver(user);
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });
        } else {
            subscribeEditProfileObserver(user);
        }

    }

    private void subscribeEditProfileObserver(User user) {
//        viewModel.observerEditProfile(user).removeObservers(getViewLifecycleOwner());
        viewModel.observerEditProfile(user).observe(getViewLifecycleOwner(), new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
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

                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(getString(R.string.success_edit_profile))
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
                    }
                }
            }
        });
    }

    private void subscribeGetCurrentUserProfile() {
//        viewModel.observerGetProfile().removeObservers(getViewLifecycleOwner());
        viewModel.observerGetProfile().observe(getViewLifecycleOwner(), new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
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

                            TempDataHolder.setCurrentUser(userResource.data);

                            fillWithData(userResource.data);
                            break;
                    }
                }
            }
        });
    }

    private void fillWithData(User currentUser) {

        viewModel.getRequestManager()
                .load(Utils.stringBase64ToBitmap(currentUser.getPictureBase64()))
                .placeholder(R.drawable.ic_profile)
                .into(imgProfilePicture);

//        txtNickName.setText(currentUser.getNickname());
//        txtAboutMe.setText(currentUser.getAboutMe());
        txtPhoneNumber.setText(currentUser.getMobile());
        txtProfileFirstNameAndLastName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        txtEmail.setText(currentUser.getEmail());
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
                                    .multi()
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

                changedPhotoUri = mSelected.get(0);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}




















