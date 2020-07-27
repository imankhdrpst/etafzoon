package com.mindology.app.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.User;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.ContactUsActivity;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {

    private static final String TAG = "ProfileFragment";

    private ProfileViewModel viewModel;

    private View view;
    private LinearLayout layLogOut;
    //    private LinearLayout layChangePassword;
    private LinearLayout layEditProfile;
    private LinearLayout layContactUs;
    private RelativeLayout progressBar;
    private CircleImageView imgProfilePicture;
    private TextView txtEmail, txtFirstNameLastName, txtPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: GroupedInspections. " + this);

        this.view = view;

        viewModel = ViewModelProviders.of(this, providerFactory).get(ProfileViewModel.class);

        layContactUs = view.findViewById(R.id.lay_contact_us);
        layContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        layLogOut = view.findViewById(R.id.lay_log_out);
        layLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getSessionManager().logOut();
            }
        });

//        layChangePassword = view.findViewById(R.id.lay_change_password);
//        layChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.changePasswordScreen);
//            }
//        });

        layEditProfile = view.findViewById(R.id.lay_edit_profile);
        layEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.editProfileScreen);
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);

        imgProfilePicture = view.findViewById(R.id.img_profile_picture);

        txtEmail = view.findViewById(R.id.txt_profile_email);
//        txtUserName = view.findViewById(R.id.txt_profile_user_name);
        txtFirstNameLastName = view.findViewById(R.id.txt_profile_first_name_last_name);
//        txtNickName = view.findViewById(R.id.txt_profile_nick_name);
//        txtAboutMe = view.findViewById(R.id.txt_profile_about_me);
        txtPhoneNumber = view.findViewById(R.id.txt_profile_phone_number);


    }

    private void subscribeGetProfileObservers() {
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

                            progressBar.setVisibility(View.GONE);

                            TempDataHolder.setCurrentUser(userResource.data);

                            fillViewWithData(userResource.data);

                            break;

                    }
                }
            }
        });
    }

    private void fillViewWithData(User data) {

        viewModel.requestManager
                .load(Utils.stringBase64ToBitmap(data.getPictureBase64()))
                .placeholder(R.drawable.ic_profile)
                .into(imgProfilePicture);

        txtEmail.setText(data.getEmail());
        String firstNameLastName = TextUtils.isEmpty(data.getFirstName()) ? "" : data.getFirstName();
        firstNameLastName += " " + (TextUtils.isEmpty(data.getLastName()) ? "" : data.getLastName());
        txtFirstNameLastName.setText(firstNameLastName);
//        txtUserName.setText(data.getUsername());
//        txtNickName.setText(data.getNickname());
//        txtAboutMe.setText(data.getAboutMe());
        txtPhoneNumber.setText(data.getMobile());

    }


    @Override
    public void onResume() {
        super.onResume();
        if (TempDataHolder.getCurrentUser() != null) {
            fillViewWithData(TempDataHolder.getCurrentUser());
        } else {
            subscribeGetProfileObservers();
        }
    }

}




















