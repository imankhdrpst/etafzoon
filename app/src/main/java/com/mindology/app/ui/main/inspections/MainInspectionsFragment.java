package com.mindology.app.ui.main.inspections;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.SliderItem;
import com.mindology.app.network.ListResponse;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.ui.main.inspections.adapters.InspectionsAdapter;
import com.mindology.app.ui.main.inspections.adapters.OnInspectionListener;
import com.mindology.app.ui.main.inspections.adapters.SliderAdapterExample;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainInspectionsFragment extends BaseFragment implements OnInspectionListener {

    private MainInspectionsViewModel viewModel;
    private TabLayout tablayout;
    private RelativeLayout progressBar;
    private SliderAdapterExample adapter;
    private SliderView sliderView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_inspections, container, false);
    }

    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://t00img.yangkeduo.com/goods/images/2018-09-11/1652dc1065082415d12da181d7fec7b9.jpeg");
            } else {
                sliderItem.setImageUrl("https://t00img.yangkeduo.com/goods/images/2018-09-11/1652dc1065082415d12da181d7fec7b9.jpeg");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, providerFactory).get(MainInspectionsViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        sliderView = view.findViewById(R.id.imageSlider);

        adapter = new SliderAdapterExample(getContext());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        renewItems();

//        rvInspections.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            int yScroll = 0;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (yScroll > 50) {
//                    yScroll = 0;
//                    fabAddInspection.hide();
//                }
//                if (yScroll < -50) {
//                    yScroll = 0;
//                    fabAddInspection.show();
//                }
//                if (Math.signum(dy) != Math.signum(yScroll))
//                    yScroll = 0;
//                yScroll += dy;
//            }
//        });

        tablayout = view.findViewById(R.id.tabs);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    rvAdapter.setData(viewModel.fetchPendingInspections());
//                } else {
//                    rvAdapter.setData(viewModel.fetchInProgressInspections());
//                }
//                if (rvAdapter.getData().size() > 0) {
//                    view.findViewById(R.id.lay_all_pending_done).setVisibility(View.GONE);
//                } else {
//                    getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.VISIBLE);
//
//                    ((TextView) getView().findViewById(R.id.txt_all_done_message)).setText(R.string.no_inspection);
//                }
//                if (fabAddInspection.isHidden())
//                    fabAddInspection.show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        swipeRefreshLayout = view.findViewById(R.id.swiep_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.YIBlue);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshInspections();
//            }
//        });
//        fabAddInspection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (viewModel.isThereAnyDraftedNewInspection()) {
//                    onInspectionClicked(viewModel.getNewInspectionDrafted());
//                } else {
////                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.createInspectionScreen);
//                }
//            }
//        });

        subscribeObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    private void enableDisableSwipeRefresh(boolean b) {
//        swipeRefreshLayout.setEnabled(b);
//    }

//    private void refreshInspections() {
//        swipeRefreshLayout.setRefreshing(false);
//        viewModel.refresh();
//    }

    private void subscribeObservers() {
//        viewModel.queryInspections().removeObservers(getViewLifecycleOwner());
        viewModel.queryInspections().observe(getViewLifecycleOwner(), new Observer<Resource<ListResponse<Inspection>>>() {
            @Override
            public void onChanged(Resource<ListResponse<Inspection>> responseResource) {
                if (responseResource != null) {
                    switch (responseResource.status) {

                        case LOADING: {
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        }
                        case SUCCESS: {
                            progressBar.setVisibility(View.GONE);

                            if (tablayout.getSelectedTabPosition() == 1) {
//                                rvAdapter.setData(viewModel.fetchInProgressInspections());
                            } else {
//                                rvAdapter.setData(viewModel.fetchPendingInspections());
                            }

                            int pendingSize = viewModel.fetchPendingInspections().size();

                            if (pendingSize != 0) {
                                BadgeDrawable pendingBadge = tablayout.getTabAt(0).getOrCreateBadge(); // pending inspections count badge
                                pendingBadge.setVisible(true);
                                pendingBadge.setNumber(pendingSize);
                            }

                            int progressSize = viewModel.fetchInProgressInspections().size();

                            if (progressSize != 0) {
                                BadgeDrawable progressBadge = tablayout.getTabAt(1).getOrCreateBadge(); // pending inspections count badge
                                progressBadge.setVisible(true);
                                progressBadge.setNumber(progressSize);
                            }

//                            if (rvAdapter.getData().size() > 0) {
//                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.GONE);
//                            } else {
//                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.VISIBLE);
//
//                                ((TextView) getView().findViewById(R.id.txt_all_done_message)).setText(R.string.no_inspection);
//                            }

                            break;
                        }
                        case ERROR: {
                            progressBar.setVisibility(View.GONE);

                            if (tablayout.getSelectedTabPosition() == 1) {
//                                rvAdapter.setData(viewModel.fetchInProgressInspections());
                            } else {
//                                rvAdapter.setData(viewModel.fetchPendingInspections());
                            }
                            int pendingSize = viewModel.fetchPendingInspections().size();

                            if (pendingSize != 0) {
                                BadgeDrawable pendingBadge = tablayout.getTabAt(0).getOrCreateBadge(); // pending inspections count badge
                                pendingBadge.setVisible(true);
                                pendingBadge.setNumber(pendingSize);
                            }

                            int progressSize = viewModel.fetchInProgressInspections().size();

                            if (progressSize != 0) {
                                BadgeDrawable progressBadge = tablayout.getTabAt(1).getOrCreateBadge(); // pending inspections count badge
                                progressBadge.setVisible(true);
                                progressBadge.setNumber(progressSize);
                            }
//
//                            if (rvAdapter.getData().size() > 0) {
//                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.GONE);
//                            } else {
//                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.VISIBLE);
//
//                                ((TextView) getView().findViewById(R.id.txt_all_done_message)).setText(R.string.no_inspection);
//                            }

                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(responseResource.message)
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
            }
        });
    }

    @Override
    public void onInspectionClicked(Inspection inspection) {
        InspectionDetailsFragmentArgs args = new InspectionDetailsFragmentArgs.Builder().setInspectionId(inspection.getId()).build();
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.inspectionDetailsScreen, args.toBundle());

    }

    public static class InspectionsItemDecorator extends RecyclerView.ItemDecoration {
        private int boxSpace, betweenSpace;

        public InspectionsItemDecorator(int boxSpace, int betweenSpace) {
            this.boxSpace = boxSpace;
            this.betweenSpace = betweenSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = boxSpace;
            } else {
                outRect.top = 0;
            }

            outRect.bottom = betweenSpace;

            outRect.left = boxSpace;
            outRect.right = boxSpace;
        }
    }


}




















