package com.mindology.app.ui.main.mood;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mindology.app.databinding.ViewHolderMoodBinding;
import com.mindology.app.models.MoodDTO;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MoodsAdapter extends RecyclerView.Adapter<MoodsAdapter.MoodViewHolder> {

    private LayoutInflater inflater;
    private List<MoodDTO> data = new ArrayList<>();

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewHolderMoodBinding binding = ViewHolderMoodBinding.inflate(inflater, parent, false);

        return new MoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodDTO moodDTO = data.get(position);
        holder.bind(moodDTO);

    }

    public void setData(List<MoodDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MoodViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderMoodBinding binding;

        private MoodDTO moodDTO;

        public MoodViewHolder(@NonNull ViewHolderMoodBinding b) {
            super(b.getRoot());
            binding = b;

        }

        public void bind(MoodDTO moodDTO) {
            this.moodDTO = moodDTO;

            binding.imgIcon.setImageResource(moodDTO.getMoodType().getResourceId());
            binding.circleView.setCircleColor(moodDTO.getMoodType().getBackgroundColor());
            binding.txtMoodTime.setText(Utils.dateDifferenceCalculate(moodDTO.getMoodDate()));
            binding.txtMoodDescription.setText(moodDTO.getDescription());

            binding.executePendingBindings();

        }
    }
}
