package com.mindology.app.ui.main.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.AuthActivity;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import net.cachapa.expandablelayout.ExpandableLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private ProfileViewModel viewModel;

    private View view;
    private RelativeLayout progressBar;
    private CircleImageView imgProfilePicture;
    private TextView txtFirstNameLastName, txtMobileNumber, txtCityCountry;
    private ExpandableLayout expandableOptions;
    private View handleBottomSheet;
    private View laySignOut, layEditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false).getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        viewModel = ViewModelProviders.of(this, providerFactory).get(ProfileViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        imgProfilePicture = view.findViewById(R.id.img_profile_picture);
        txtFirstNameLastName = view.findViewById(R.id.txt_profile_first_name_last_name);
        txtMobileNumber = view.findViewById(R.id.txt_profile_mobile_number);
        txtCityCountry = view.findViewById(R.id.txt_profile_city_country);
        expandableOptions = view.findViewById(R.id.expandable_layout_profile_options);
        handleBottomSheet = view.findViewById(R.id.handle_bottom_sheet);
        handleBottomSheet.setOnClickListener(this);
        laySignOut = view.findViewById(R.id.lay_sign_out);
        laySignOut.setOnClickListener(this);
        layEditProfile = view.findViewById(R.id.lay_virayeshe_profile);
        layEditProfile.setOnClickListener(this);

        expandableOptions.expand(true);


    }


    private void fillViewWithData(ClientUserDTO data) {

        viewModel.requestManager
                .load(Utils.stringBase64ToBitmap(data.getProfilePicture()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(imgProfilePicture);

        String firstNameLastName = TextUtils.isEmpty(data.getFirstName()) ? "" : data.getFirstName();
        firstNameLastName += " " + (TextUtils.isEmpty(data.getLastName()) ? "" : data.getLastName());
        txtFirstNameLastName.setText(firstNameLastName);
        txtMobileNumber.setText(data.getMobileNumber());
        txtCityCountry.setText(data.getLivingCity());

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

    private void subscribeGetProfileObservers() {
        viewModel.observerGetProfile().observe(getViewLifecycleOwner(), new Observer<Resource<ClientUserDTO>>() {
            @Override
            public void onChanged(Resource<ClientUserDTO> clientUserDTOResource) {
                if (clientUserDTOResource != null)
                {
                    switch (clientUserDTOResource.status)
                    {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(clientUserDTOResource.message)
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
                        case SUCCESS:
                        case UPDATED:
                            progressBar.setVisibility(View.GONE);
                            TempDataHolder.setCurrentUser(clientUserDTOResource.data);
                            fillViewWithData(clientUserDTOResource.data);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_sign_out:
                onSignOutClicked();
                break;
            case R.id.handle_bottom_sheet:
                expandableOptions.toggle(true);
                break;
            case R.id.lay_virayeshe_profile:
                onEditProfileClicked();
                break;
        }
    }

    private void onEditProfileClicked() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.editProfileScreen);
    }

    private void onSignOutClicked() {
        viewModel.getSessionManager().logOut();
    }
}




















