package com.mindology.app.ui.main;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.RequestManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.mindology.app.BaseActivity;
import com.mindology.app.R;
import com.mindology.app.models.Devices;
import com.mindology.app.models.User;
import com.mindology.app.util.Utils;
import com.mindology.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private NavController navController = null;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    RequestManager requestManager;
    private int currentApiVersion;

    //    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;
    private TextView txtToolbarTitle;
    private RelativeLayout layToolbar;
    private MainViewModel viewModel;
    private CircleImageView imgProfilePicture;
    private ImageView imgDeleteAssets, imgSearchProperties;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);

        txtToolbarTitle = findViewById(R.id.txt_toolbar_title);
        layToolbar = findViewById(R.id.lay_toolbar);
        imgProfilePicture = findViewById(R.id.img_toolbar_profile);
        imgDeleteAssets = findViewById(R.id.img_delete_assets);
        imgSearchProperties = findViewById(R.id.img_search_property);

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

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isValidDestination(R.id.profileScreen)) {
//                    if (navController.getCurrentDestination().getId() == R.id.editProfileScreen) {
//                        navController.popBackStack();
//                    } else {
//                        navController.navigate(R.id.profileScreen);
//                    }
//                }
            }
        });

        imgDeleteAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        imgSearchProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (navController.getCurrentDestination().getId() == R.id.createInspectionScreen) {
//                    ((CreateInspectionFragment)
//                            getSupportFragmentManager()
//                                    .findFragmentById(R.id.nav_host_fragment)
//                                    .getChildFragmentManager()
//                                    .getFragments()
//                                    .get(0))
//                            .onSearchButtonClicked();
//                }
            }
        });


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) layToolbar.getLayoutParams();
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);

        init();

//
//        requestManager
////                            .load(Utils.stringBase64ToBitmap(userResource.data.getPictureBase64()))
//                .load("https://t00img.yangkeduo.com/goods/images/2018-09-11/1652dc1065082415d12da181d7fec7b9.jpeg")
//                .into(imgProfilePicture);

        subscribeObservers();

    }

    private void subscribeObservers() {
        viewModel.observerGetProfile().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                if (userResource != null && userResource.data != null && userResource.status == Resource.Status.SUCCESS) {
                    requestManager
                            .load(Utils.stringBase64ToBitmap(userResource.data.getPictureBase64()))
//                            .load("http://lorempixel.com/400/200/")
                            .into(imgProfilePicture);
                }
            }
        });

        Devices devices = new Devices();
        devices.setOs("ANDROID");
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            devices.setAppVersion(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        devices.setInfo("Yas inventories android application");
        devices.setOsVersion(String.valueOf(Build.VERSION.SDK_INT));
        devices.setToken(viewModel.getToken());
        viewModel.observerDevices(devices).observe(this, new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> objectResource) {

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

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void init() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, (DrawerLayout) null);
//        NavigationUI.setupWithNavController(toolbar, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                toolbar.setTitle(destination.getLabel());
                toolbar.setTitle("");
                txtToolbarTitle.setText("mindology");
                switch (destination.getId()) {
                    case R.id.profileScreen:
                        imgProfilePicture.setVisibility(View.GONE);
                        imgDeleteAssets.setVisibility(View.GONE);
                        imgSearchProperties.setVisibility(View.GONE);
                        break;
//                    case R.id.createInspectionScreen:
//                        imgProfilePicture.setVisibility(View.GONE);
//                        imgDeleteAssets.setVisibility(View.GONE);
//                        imgSearchProperties.setVisibility(View.VISIBLE);
//                        break;
                    default:
                        imgProfilePicture.setVisibility(View.VISIBLE);
                        imgDeleteAssets.setVisibility(View.GONE);
                        imgSearchProperties.setVisibility(View.GONE);
                        break;
                }


            }
        });
//        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_profile: {
                if (Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.editProfileScreen) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
                } else {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.main, true)
                            .build();

                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                            R.id.mainPageScreen,
                            null,
                            navOptions
                    );
                }

                break;
            }

            case R.id.nav_posts: {

                break;
            }
        }

        menuItem.setChecked(true);
//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isValidDestination(int destination) {
        try {
            return destination != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), (DrawerLayout) null);
    }
}

























