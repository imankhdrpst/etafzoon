package com.mindology.app.ui.main.mood;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.MoodDTO;
import com.mindology.app.models.MoodStatisticsDTO;
import com.mindology.app.models.MoodType;
import com.mindology.app.ui.main.Resource;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.ArrayUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;

public class MoodListFragment extends BaseFragment implements OnMoodTypeClickListener {

    private MoodListViewModel viewModel;
    private RelativeLayout progressBar;
    private LineChart chart;
    private Typeface tfLight;
    private RecyclerView rvMoodList;
    private ExtendedFloatingActionButton fabAddMood;
    private TextInputEditText txtDescription;
    private MoodTypeAdapter moodTypesAdtapter;
    private RecyclerView rvMoodTypes;
    private ExpandableLayout expandableAddMood;
    private View handleBottomSheet;
    private MaterialButton btnAddMood;
    private TextInputLayout txtLayoutDescription;
    private MoodsAdapter moodsAdapter;
    private List<MoodType> moodTypes = new ArrayList<>();


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
                expandableAddMood.expand(true);
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
        rvMoodList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        rvMoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int yScroll = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (yScroll > 20) {
                    yScroll = 0;
                    fabAddMood.hide();
                }
                if (yScroll < -20) {
                    yScroll = 0;
                    fabAddMood.show();
                }
                if (Math.signum(dy) != Math.signum(yScroll))
                    yScroll = 0;
                yScroll += dy;
            }
        });
        moodsAdapter = new MoodsAdapter();
        rvMoodList.setAdapter(moodsAdapter);

        rvMoodTypes = view.findViewById(R.id.rv_moods);
        rvMoodTypes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        moodTypesAdtapter = new MoodTypeAdapter(this);
        rvMoodTypes.setAdapter(moodTypesAdtapter);
        moodTypes = new ArrayList<>();

        MoodType moodAnxiety = new MoodType();
        moodAnxiety.setTitle("مضطرب");
        moodAnxiety.setIndex(1);
        moodAnxiety.setResourceId(R.drawable.ic_mood_anxious);
        moodAnxiety.setBackgroundColor(Color.parseColor("#fff9f3"));
        moodAnxiety.setBorderColor(Color.parseColor("#ff8000"));
        moodAnxiety.setShadowColor(Color.parseColor("#50ff8000"));

        MoodType moodCrying = new MoodType();
        moodCrying.setTitle("گریان");
        moodCrying.setIndex(2);
        moodCrying.setResourceId(R.drawable.ic_mood_crying);
        moodCrying.setBackgroundColor(Color.parseColor("#fff8f8"));
        moodCrying.setBorderColor(Color.parseColor("#e8252b"));
        moodCrying.setShadowColor(Color.parseColor("#50e8252b"));

        MoodType moodUpset = new MoodType();
        moodUpset.setTitle("عصبانی");
        moodUpset.setIndex(3);
        moodUpset.setResourceId(R.drawable.ic_mood_upset);
        moodUpset.setBackgroundColor(Color.parseColor("#fbfdff"));
        moodUpset.setBorderColor(Color.parseColor("#a0a5aa"));
        moodUpset.setShadowColor(Color.parseColor("#50a0a5aa"));

        MoodType moodSad = new MoodType();
        moodSad.setTitle("ناراحت");
        moodSad.setIndex(4);
        moodSad.setResourceId(R.drawable.ic_mood_sad);
        moodSad.setBackgroundColor(Color.parseColor("#fef9f6"));
        moodSad.setBorderColor(Color.parseColor("#db7e6c"));
        moodSad.setShadowColor(Color.parseColor("#50db7e6c"));

        moodTypes.add(moodAnxiety);
        moodTypes.add(moodCrying);
        moodTypes.add(moodUpset);
        moodTypes.add(moodSad);
        moodTypes.add(moodSad);
        moodTypes.add(moodSad);

        moodTypesAdtapter.setData(moodTypes);

        txtDescription = view.findViewById(R.id.input_description);
        txtDescription.setHint(getString(R.string.describe_your_mood));
        txtLayoutDescription = view.findViewById(R.id.txt_layout_description);

        expandableAddMood = view.findViewById(R.id.expandable_layout_add_mood);
        expandableAddMood.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                switch (state) {
                    case ExpandableLayout.State.EXPANDING:
                        fabAddMood.hide();
                        break;
                    case ExpandableLayout.State.COLLAPSED:
                        fabAddMood.show();
                        break;
                }
            }
        });

        handleBottomSheet = view.findViewById(R.id.handle_bottom_sheet);
        handleBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableAddMood.collapse(true);
            }
        });

        btnAddMood = view.findViewById(R.id.btn_save);
        btnAddMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddMoodClicked();
            }
        });

        subscribeObservers();
    }

    private void onAddMoodClicked() {
        if (TextUtils.isEmpty(txtDescription.getText().toString()))
        {
            txtLayoutDescription.setError("لطفا حال خودتو توصیف کن");
        }
        else {
            txtLayoutDescription.setError("");
            if (moodTypesAdtapter.getSelected() != null) {
                MoodDTO moodDTO = new MoodDTO();
                moodDTO.setMoodDate(Calendar.getInstance().getTime());
                switch (moodTypesAdtapter.getSelected().getIndex()) {
                    case 1:
                        moodDTO.setReason0(1);
                        break;
                    case 2:
                        moodDTO.setReason1(1);
                        break;
                    case 3:
                        moodDTO.setReason2(1);
                        break;
                    case 4:
                        moodDTO.setReason3(1);
                        break;
                }
                moodDTO.setDescription(txtDescription.getText().toString());
                moodDTO.setUserMobile(mainViewModel.getMyProfile().getMobileNumber());

                viewModel.addMood(moodDTO);
            }
        }
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

        viewModel.observeMoodList(mainViewModel.getMyProfile().getMobileNumber()).observe(getViewLifecycleOwner(), new Observer<Resource<MoodStatisticsDTO>>() {
            @Override
            public void onChanged(Resource<MoodStatisticsDTO> moodStatisticsDTOResource) {
                if (moodStatisticsDTOResource != null)
                {
                    switch (moodStatisticsDTOResource.status)
                    {
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            fabAddMood.show();
                            break;
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            fabAddMood.hide();
                            break;
                        case UPDATED:
                        case SUCCESS:
                            fabAddMood.show();
                            progressBar.setVisibility(View.GONE);
                            List<MoodDTO> result = moodStatisticsDTOResource.data.getMoods();
                            for(MoodDTO moodDTO : result)
                            {
                                if (moodDTO.getReason0() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(0));
                                }
                                if (moodDTO.getReason1() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(1));
                                }
                                if (moodDTO.getReason2() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(2));
                                }
                                if (moodDTO.getReason3() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(3));
                                }
                                if (moodDTO.getReason4() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(4));
                                }
                                if (moodDTO.getReason5() == 1)
                                {
                                    moodDTO.setMoodType(moodTypes.get(5));
                                }
                            }
                            moodsAdapter.setData(result);
                            break;

                    }
                }
            }
        });
        viewModel.observeAddMood().observe(getViewLifecycleOwner(), new Observer<Resource<ResponseBody>>() {
            @Override
            public void onChanged(Resource<ResponseBody> objectResource) {
                if (objectResource != null)
                {
                    switch (objectResource.status)
                    {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            if (expandableAddMood.isExpanded())
                                expandableAddMood.collapse(true);
                            break;

                    }
                }
            }
        });
    }


    @Override
    public void onMoodTypeClicked(MoodType moodType) {

    }
}




















