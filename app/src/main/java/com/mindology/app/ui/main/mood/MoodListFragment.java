package com.mindology.app.ui.main.mood;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.MoodType;
import com.mindology.app.models.Post;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.posts.OnPostListener;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class MoodListFragment extends BaseFragment implements OnPostListener, OnMoodTypeClickListener {

    private MoodListViewModel viewModel;
    private RelativeLayout progressBar;
    private LineChart chart;
    private Typeface tfLight;
    private RecyclerView rvMoodList;
    private ExtendedFloatingActionButton fabAddMood;
    private TextInputEditText txtDescription;
    private MoodTypeAdapter moodTypesAdtapter;
    private RecyclerView rvMoods;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_vaziate_roohi, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MoodListViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);

        fabAddMood = view.findViewById(R.id.fab_add_mood);
        fabAddMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        chart = view.findViewById(R.id.chart1);
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

        rvMoodList = view.findViewById(R.id.rv_mood_list);
        rvMoodList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        rvMoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int yScroll = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (yScroll > 50) {
                    yScroll = 0;
                    fabAddMood.hide();
                }
                if (yScroll < -50) {
                    yScroll = 0;
                    fabAddMood.show();
                }
                if (Math.signum(dy) != Math.signum(yScroll))
                    yScroll = 0;
                yScroll += dy;
            }
        });

        rvMoods = view.findViewById(R.id.rv_moods);
        rvMoods.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        moodTypesAdtapter = new MoodTypeAdapter(this);
        rvMoods.setAdapter(moodTypesAdtapter);
        List<MoodType> moodTypes = new ArrayList<>();
        MoodType type = new MoodType();
        type.setTitle("kناراحت");
        type.setResourceId(R.drawable.ic_edit_24px);
        type.setBackgroundColor(Color.parseColor("#fff9f3"));
        type.setBorderColor(Color.parseColor("#ff8000"));
        moodTypes.add(type);
        moodTypesAdtapter.setData(moodTypes);

        txtDescription = view.findViewById(R.id.input_description);
        txtDescription.setHint(getString(R.string.describe_your_mood));

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

    private void subscribeObservers() {

        setData(10, 30.0f);
        chart.invalidate();

    }

    @Override
    public void onPostClicked(Post post) {
        TempDataHolder.setSelectedPost(post);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.postDetailScreen);
    }
}




















