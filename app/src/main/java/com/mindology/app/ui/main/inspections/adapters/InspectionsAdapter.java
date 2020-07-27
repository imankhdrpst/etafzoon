package com.mindology.app.ui.main.inspections.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mindology.app.R;
import com.mindology.app.models.Inspection;
import com.mindology.app.util.Constants;
import com.mindology.app.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InspectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DRAFT = 0, TYPE_NORMAL = 1;

    private List<Inspection> data;
    private OnInspectionListener onInspectionListener;

    public InspectionsAdapter(OnInspectionListener onInspectionListener, List<Inspection> data) {
        this.onInspectionListener = onInspectionListener;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;
        if (viewType == TYPE_DRAFT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_draft_inspection, viewGroup, false);
            return new DraftInspectionViewHolder(view, onInspectionListener);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_inspection, viewGroup, false);
            return new NormalInspectionViewHolder(view, onInspectionListener);
        }

    }

    public void setData(List<Inspection> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == TYPE_DRAFT) {
            ((DraftInspectionViewHolder) viewHolder).bind(data.get(i));
        } else {
            ((NormalInspectionViewHolder) viewHolder).bind(data.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isSynced() ? TYPE_NORMAL : TYPE_DRAFT;
    }


    @Override
    public int getItemCount() {
        if(data != null){
            return data.size();
        }
        return 0;
    }

    public List<Inspection> getData() {
        return data;
    }

    class NormalInspectionViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        TextView txtPropertyAddress, txtInspectionNote, txtInspectionType, txtInspectionDate, txtCaption;
        ImageView imgInspectionType;
        Inspection inspection;
        MaterialCardView layoutAll;


        public NormalInspectionViewHolder(View view, final OnInspectionListener onInspectionListener) {
            super(view);
            this.view = view;
            layoutAll = view.findViewById(R.id.layout_all);
            txtPropertyAddress = view.findViewById(R.id.txt_property_address);
            txtInspectionType = view.findViewById(R.id.txt_inspection_type);
            txtInspectionDate = view.findViewById(R.id.txt_inspection_date);
            txtCaption = view.findViewById(R.id.txt_inspection_caption);
            txtInspectionNote = view.findViewById(R.id.txt_inspection_note);

            imgInspectionType = view.findViewById(R.id.img_inspection_type);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onInspectionListener != null && inspection != null)
                    {
                        onInspectionListener.onInspectionClicked(inspection);
                    }
                }
            });
        }

        void bind(Inspection inspection) {

            this.inspection = inspection;

            Date date = null;
            try {
                date = (new SimpleDateFormat(Constants.dateFormat)).parse(inspection.getDate());
                String diffCaption = Utils.convertDateToDayCaption(date);
                if (diffCaption.toUpperCase().equals("TODAY") || diffCaption.toUpperCase().equals("TOMORROW"))
                {
                    txtCaption.setText(diffCaption);
                }
                else
                {
                    txtCaption.setVisibility(View.GONE);
                }

                txtInspectionDate.setVisibility(View.VISIBLE);
                txtInspectionDate.setText(Utils.getDateFormat(date));
            } catch (Exception e) {
                txtInspectionDate.setVisibility(View.GONE);
            }

            switch (inspection.getInspectionType())
            {
                case CHECK_IN:
                    txtInspectionType.setText(view.getResources().getString(R.string.check_in));
                    imgInspectionType.setImageResource(R.drawable.ic_check_in_24px);
                    imgInspectionType.setColorFilter(view.getContext().getResources().getColor(R.color.green));
                    txtInspectionType.setTextColor(view.getContext().getResources().getColor(R.color.green));
                    txtInspectionDate.setTextColor(view.getContext().getResources().getColor(R.color.green));
                    break;
                case MID_TERM:
                    txtInspectionType.setText(view.getResources().getString(R.string.mid_term));
                    imgInspectionType.setImageResource(R.drawable.ic_mid);
                    imgInspectionType.setColorFilter(view.getContext().getResources().getColor(R.color.orange));
                    txtInspectionType.setTextColor(view.getContext().getResources().getColor(R.color.orange));
                    txtInspectionDate.setTextColor(view.getContext().getResources().getColor(R.color.orange));
                    break;
                case CHECK_OUT:
                    txtInspectionType.setText(view.getResources().getString(R.string.check_out));
                    imgInspectionType.setImageResource(R.drawable.ic_checkout);
                    imgInspectionType.setColorFilter(view.getContext().getResources().getColor(R.color.red));
                    txtInspectionType.setTextColor(view.getContext().getResources().getColor(R.color.red));
                    txtInspectionDate.setTextColor(view.getContext().getResources().getColor(R.color.red));
                    break;
            }

            try {
                txtPropertyAddress.setText(inspection.getProperty().getAddress().toString());
            } catch (Exception e) {
                txtPropertyAddress.setText("");
            }

            txtInspectionNote.setText(inspection.getNote());
        }

    }

    class DraftInspectionViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        TextView txtPropertyAddress, txtInspectionNote, txtInspectionType, txtInspectionDate, txtCaption;
        ImageView imgInspectionType;
        Inspection inspection;
        MaterialCardView layoutAll;


        public DraftInspectionViewHolder(View view, final OnInspectionListener onInspectionListener) {
            super(view);
            this.view = view;
            layoutAll = view.findViewById(R.id.layout_all);
            txtPropertyAddress = view.findViewById(R.id.txt_property_address);
            txtInspectionType = view.findViewById(R.id.txt_inspection_type);
            txtInspectionDate = view.findViewById(R.id.txt_inspection_date);
            txtCaption = view.findViewById(R.id.txt_inspection_caption);
            txtInspectionNote = view.findViewById(R.id.txt_inspection_note);

            imgInspectionType = view.findViewById(R.id.img_inspection_type);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onInspectionListener != null && inspection != null) {
                        onInspectionListener.onInspectionClicked(inspection);
                    }
                }
            });
        }

        void bind(Inspection inspection) {

            this.inspection = inspection;

            switch (inspection.getInspectionType())
            {
                case CHECK_IN:
                    txtInspectionType.setText(view.getResources().getString(R.string.check_in));
                    imgInspectionType.setImageResource(R.drawable.ic_check_in_24px);
                    break;
                case MID_TERM:
                    txtInspectionType.setText(view.getResources().getString(R.string.mid_term));
                    imgInspectionType.setImageResource(R.drawable.ic_check_in_24px);
                    break;
                case CHECK_OUT:
                    txtInspectionType.setText(view.getResources().getString(R.string.check_out));
                    imgInspectionType.setImageResource(R.drawable.ic_checkout);
                    break;
            }

            try {
                txtPropertyAddress.setText(inspection.getProperty().getAddress().toString());
            } catch (Exception e) {
                txtPropertyAddress.setText("");
            }

            txtInspectionNote.setText(inspection.getNote());
        }

    }

}
