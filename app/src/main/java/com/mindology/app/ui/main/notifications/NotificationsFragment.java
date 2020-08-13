package com.mindology.app.ui.main.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.Notification;
import com.mindology.app.models.Post;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;

import java.util.List;

public class NotificationsFragment extends BaseFragment implements OnNotificationClickListener {

    private NotificationsViewModel viewModel;
    private RecyclerView rvNotifications;
    private NotificationsAdapter notificationsAdapter;
    private View progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(NotificationsViewModel.class);

        progressBar = view.findViewById(R.id.prg_notifications);

        rvNotifications = view.findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        notificationsAdapter = new NotificationsAdapter(this);
        rvNotifications.setAdapter(notificationsAdapter);

        subscribeObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void subscribeObservers() {
        mainViewModel.observeNotifications().observe(getViewLifecycleOwner(), new Observer<Resource<List<Notification>>>() {
            @Override
            public void onChanged(Resource<List<Notification>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            break;
                        case UPDATED:
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            notificationsAdapter.setData(listResource.data);
                            break;
                    }
                }
            }
        });

    }


    @Override
    public void onNotificationClicked(Notification notification) {

    }
}




















