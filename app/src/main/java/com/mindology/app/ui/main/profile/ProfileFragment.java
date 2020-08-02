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
import androidx.lifecycle.ViewModelProviders;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {

    private ProfileViewModel viewModel;

    private View view;
    private RelativeLayout progressBar;
    private CircleImageView imgProfilePicture;
    private TextView txtFirstNameLastName, txtMobileNumber, txtCityCountry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
    }


    private void fillViewWithData(ClientUserDTO data) {

        viewModel.requestManager
                .load(Utils.stringBase64ToBitmap(data.getProfilePicture()))
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
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
//            subscribeGetProfileObservers();
        }
    }

}




















