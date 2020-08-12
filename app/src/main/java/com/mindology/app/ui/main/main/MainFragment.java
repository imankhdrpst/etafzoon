package com.mindology.app.ui.main.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.Post;
import com.mindology.app.models.PostGroup;
import com.mindology.app.models.SliderItem;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.ui.main.posts.BannerAdapter;
import com.mindology.app.ui.main.posts.OnPostListener;
import com.mindology.app.ui.main.posts.PostsAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainFragment extends BaseFragment implements OnPostListener {

    private MainPageViewModel viewModel;
    private RelativeLayout progressBar;
    private BannerAdapter adapter;
    private SliderView sliderView;
    private LineChart chart;
    private Typeface tfLight;
    private RecyclerView rvPosts;
    private PostsAdapter postsAdapter;
    private MaterialProgressBar prgLatestPosts;
    private TextView txtName;
    private TextView txtShowAllPosts;
    private LinearLayout layPeriodSpinner;
    private Spinner txtPeriodSpinner;
    private ArrayAdapter<String> periodAdapter;
//    private LinearLayout layPeriodSpinner;
//    private AutoCompleteTextView txtPeriodSpinner;
//    private ArrayAdapter<String> periodAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MainPageViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        prgLatestPosts = view.findViewById(R.id.prg_latest_posts);

        sliderView = view.findViewById(R.id.imageSlider);

        adapter = new BannerAdapter(getContext());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        layPeriodSpinner = view.findViewById(R.id.lay_period_spinner);
        txtPeriodSpinner = view.findViewById(R.id.input_period_spinner);
        txtPeriodSpinner.setFocusable(false);
        txtPeriodSpinner.setFocusableInTouchMode(false);
        String[] periodArray = new String[]{"هفته", "ماه", "سال"};
        periodAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, periodArray);
        txtPeriodSpinner.setAdapter(periodAdapter);
        layPeriodSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
//        spinner = view.findViewById(R.id.spinner);
//        spinner.setItems("هفته", "ماه", "سال");
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
//            }
//        });

        chart = view.findViewById(R.id.chart1);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.moodListScreen);
            }
        });
        chart.setViewPortOffsets(0, 0, 0, 50);
        chart.setBackgroundColor(getResources().getColor(R.color.white));

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        tfLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/sans.ttf");

        String[] jalaliMonths = new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};

        XAxis x = chart.getXAxis();
        x.setEnabled(true);
        x.setTypeface(tfLight);
        x.setLabelCount(12);
        x.setTextColor(getResources().getColor(R.color.gray));
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(true);
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = ArrayUtils.indexOf(axis.mEntries, value);
                return jalaliMonths[index];
            }
        });
        x.setAxisLineColor(getResources().getColor(R.color.gray));


        YAxis y = chart.getAxisLeft();
        y.setTypeface(tfLight);
        y.setLabelCount(6, true);
        y.setTextColor(getResources().getColor(R.color.gray));
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(getResources().getColor(R.color.gray));
//        y.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return super.getFormattedValue(value, axis);
//            }
//
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                return "kh";
//            }
//        });
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(true);

        chart.getLegend().setEnabled(false);

        chart.animateXY(1000, 1000);

        // don't forget to refresh the drawing
        chart.invalidate();

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

        rvPosts = view.findViewById(R.id.rv_latest_posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        postsAdapter = new PostsAdapter(this);
        rvPosts.setAdapter(postsAdapter);

        txtShowAllPosts = view.findViewById(R.id.txt_latest_posts_show_all);
        txtShowAllPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.postsScreen);
            }
        });

        subscribeObservers();
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(2.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(getResources().getColor(R.color.red));
            set1.setColor(getResources().getColor(R.color.blue));
            set1.setFillColor(getResources().getColor(R.color.blue));
            set1.setFillAlpha(40);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTypeface(tfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
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

        setData(10, 30.0f);
        chart.invalidate();

        viewModel.queryPostGroups().observe(getViewLifecycleOwner(), new Observer<Resource<List<PostGroup>>>() {
            @Override
            public void onChanged(Resource<List<PostGroup>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
//                            prgBanners.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
//                            prgBanners.setVisibility(View.GONE);
                            break;
                        case UPDATED:
                        case SUCCESS:
//                            prgBanners.setVisibility(View.GONE);
                            List<SliderItem> sliderItemList = new ArrayList<>();
                            for (PostGroup postGroup : listResource.data) {
                                SliderItem sliderItem = new SliderItem();
                                sliderItem.setBase64String(postGroup.getImageContent());
                                sliderItemList.add(sliderItem);
                                adapter.renewItems(sliderItemList);
                            }


                            break;

                    }
                }
            }
        });

        viewModel.queryLatestPosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            prgLatestPosts.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                            prgLatestPosts.setVisibility(View.GONE);
                            break;
                        case UPDATED:
                        case SUCCESS:
                            prgLatestPosts.setVisibility(View.GONE);
                            postsAdapter.setData(listResource.data);
                            break;
                    }
                }
            }
        });


//        viewModel.queryInspections().removeObservers(getViewLifecycleOwner());
//        viewModel.queryInspections().observe(getViewLifecycleOwner(), new Observer<Resource<ListResponse<Inspection>>>() {
//            @Override
//            public void onChanged(Resource<ListResponse<Inspection>> responseResource) {
//                if (responseResource != null) {
//                    switch (responseResource.status) {
//
//                        case LOADING: {
//                            progressBar.setVisibility(View.VISIBLE);
//                            break;
//                        }
//                        case SUCCESS: {
//                            progressBar.setVisibility(View.GONE);
//
//                            if (tablayout.getSelectedTabPosition() == 1) {
////                                rvAdapter.setData(viewModel.fetchInProgressInspections());
//                            } else {
////                                rvAdapter.setData(viewModel.fetchPendingInspections());
//                            }
//
//                            int pendingSize = viewModel.fetchPendingInspections().size();
//
//                            if (pendingSize != 0) {
//                                BadgeDrawable pendingBadge = tablayout.getTabAt(0).getOrCreateBadge(); // pending inspections count badge
//                                pendingBadge.setVisible(true);
//                                pendingBadge.setNumber(pendingSize);
//                            }
//
//                            int progressSize = viewModel.fetchInProgressInspections().size();
//
//                            if (progressSize != 0) {
//                                BadgeDrawable progressBadge = tablayout.getTabAt(1).getOrCreateBadge(); // pending inspections count badge
//                                progressBadge.setVisible(true);
//                                progressBadge.setNumber(progressSize);
//                            }
//
////                            if (rvAdapter.getData().size() > 0) {
////                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.GONE);
////                            } else {
////                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.VISIBLE);
////
////                                ((TextView) getView().findViewById(R.id.txt_all_done_message)).setText(R.string.no_inspection);
////                            }
//
//                            break;
//                        }
//                        case ERROR: {
//                            progressBar.setVisibility(View.GONE);
//
//                            if (tablayout.getSelectedTabPosition() == 1) {
////                                rvAdapter.setData(viewModel.fetchInProgressInspections());
//                            } else {
////                                rvAdapter.setData(viewModel.fetchPendingInspections());
//                            }
//                            int pendingSize = viewModel.fetchPendingInspections().size();
//
//                            if (pendingSize != 0) {
//                                BadgeDrawable pendingBadge = tablayout.getTabAt(0).getOrCreateBadge(); // pending inspections count badge
//                                pendingBadge.setVisible(true);
//                                pendingBadge.setNumber(pendingSize);
//                            }
//
//                            int progressSize = viewModel.fetchInProgressInspections().size();
//
//                            if (progressSize != 0) {
//                                BadgeDrawable progressBadge = tablayout.getTabAt(1).getOrCreateBadge(); // pending inspections count badge
//                                progressBadge.setVisible(true);
//                                progressBadge.setNumber(progressSize);
//                            }
////
////                            if (rvAdapter.getData().size() > 0) {
////                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.GONE);
////                            } else {
////                                getView().findViewById(R.id.lay_all_pending_done).setVisibility(View.VISIBLE);
////
////                                ((TextView) getView().findViewById(R.id.txt_all_done_message)).setText(R.string.no_inspection);
////                            }
//
//                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText(getString(R.string.excepttion))
//                                    .setContentText(responseResource.message)
//                                    .setConfirmText(getString(R.string.ok))
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            sweetAlertDialog.dismissWithAnimation();
//                                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
//                                        }
//                                    })
//                                    .show();
//
//                            break;
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onPostClicked(Post post) {
        TempDataHolder.setSelectedPost(post);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.postDetailScreen);
    }
}




















