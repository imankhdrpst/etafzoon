package com.mindology.app.ui.main.mood;

import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circleview.CircleView;
import com.mindology.app.R;
import com.mindology.app.databinding.ViewHolderMoodTypeBinding;
import com.mindology.app.databinding.ViewHolderPostsBinding;
import com.mindology.app.models.MoodType;
import com.mindology.app.models.Post;

import java.util.ArrayList;
import java.util.List;

import static com.mindology.app.util.Utils.getBitmapFromBase64String;

public class MoodTypeAdapter extends RecyclerView.Adapter<MoodTypeAdapter.MoodTypeViewHolder> {

    private LayoutInflater inflater;
    private List<MoodType> data = new ArrayList<>();
    private OnMoodTypeClickListener listener = null;

    public MoodTypeAdapter(OnMoodTypeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewHolderMoodTypeBinding binding = ViewHolderMoodTypeBinding.inflate(inflater, parent, false);

        return new MoodTypeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodTypeViewHolder holder, int position) {
        MoodType moodType = data.get(position);
        holder.bind(moodType);
    }

    public void setData(List<MoodType> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MoodTypeViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderMoodTypeBinding binding;

        private MoodType moodType;

        public MoodTypeViewHolder(@NonNull ViewHolderMoodTypeBinding b) {
            super(b.getRoot());
            binding = b;

        }

        public void bind(MoodType moodType) {
            this.moodType = moodType;

            binding.txtTitle.setText(moodType.getTitle());
            binding.imgIcon.setVisibility(moodType.getResourceId());
            binding.circleView.setCircleColor(moodType.getBackgroundColor());
            binding.circleView.setShadowEnable(true);
            binding.circleView.setShadowColor(moodType.getBorderColor());
            binding.circleView.setShadowGravity(CircleView.ShadowGravity.CENTER);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.circleView.getLayoutParams();
            params.height = binding.circleView.getRootView().getHeight();
            params.width = binding.circleView.getRootView().getWidth();
            binding.layBorder.setLayoutParams(params);
            GradientDrawable border = (GradientDrawable)binding.layBorder.getBackground();
            border.setStroke(1, moodType.getBorderColor(), 10, 10);

            binding.executePendingBindings();

        }
    }
}
