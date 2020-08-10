package com.mindology.app.ui.main.mood;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circleview.CircleView;
import com.mindology.app.databinding.ViewHolderMoodTypeBinding;
import com.mindology.app.models.MoodDTO;
import com.mindology.app.models.MoodType;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MoodTypeAdapter extends RecyclerView.Adapter<MoodTypeAdapter.MoodTypeViewHolder> {

    private LayoutInflater inflater;
    private List<MoodType> data = new ArrayList<>();
    private OnMoodTypeClickListener listener = null;
    private MoodType selected = null;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                {
                    for (MoodType type : data)
                    {
                        type.setSelected(false);
                    }
                    moodType.setSelected(true);
                    selected = moodType;
                    notifyDataSetChanged();
                    listener.onMoodTypeClicked(moodType);
                }
            }
        });
    }

    public void setData(List<MoodType> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MoodType getSelected() {
        return selected;
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
            binding.imgIcon.setImageResource(moodType.getResourceId());
            if (moodType.isSelected()) {
                binding.circleView.setVisibility(View.INVISIBLE);
                binding.circleViewShadow.setVisibility(View.VISIBLE);
                binding.layBorder.setVisibility(View.VISIBLE);
                binding.circleViewShadow.setShadowColor(moodType.getShadowColor());
                binding.circleViewShadow.setShadowGravity(CircleView.ShadowGravity.CENTER);
                binding.circleViewShadow.setCircleColor(moodType.getBackgroundColor());
                GradientDrawable border = (GradientDrawable) binding.layBorder.getBackground();
                border.setStroke(1, moodType.getBorderColor(), 4, 8);
            }
            else
            {
                binding.circleView.setVisibility(View.VISIBLE);
                binding.circleViewShadow.setVisibility(View.INVISIBLE);
                binding.circleView.setCircleColor(moodType.getBackgroundColor());
                binding.layBorder.setVisibility(View.INVISIBLE);
            }

            binding.executePendingBindings();

        }
    }
}
