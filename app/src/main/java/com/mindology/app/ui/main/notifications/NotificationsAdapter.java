package com.mindology.app.ui.main.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mindology.app.databinding.ViewHolderNotificationBinding;
import com.mindology.app.databinding.ViewHolderPostsBinding;
import com.mindology.app.models.Notification;
import com.mindology.app.models.Post;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.mindology.app.util.Utils.getBitmapFromBase64String;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private LayoutInflater inflater;
    private List<Notification> data = new ArrayList<>();
    private OnNotificationClickListener listener = null;

    public NotificationsAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewHolderNotificationBinding binding = ViewHolderNotificationBinding.inflate(inflater, parent, false);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = data.get(position);
        holder.bind(notification);
    }

    public void setData(List<Notification> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderNotificationBinding binding;

        private Notification notification;

        public NotificationViewHolder(@NonNull ViewHolderNotificationBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void bind(Notification notification) {
            this.notification = notification;

            binding.txtDate.setText(Utils.dateDifferenceCalculate(notification.getCreatedDate()));
            binding.txtContent.setText(notification.getContent());
            if (notification.isRead())
            {
                binding.viewUnreadIndicator.setVisibility(View.GONE);
            }
            else
            {
                binding.viewUnreadIndicator.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notification.setRead(true);
                    if (listener != null)
                    {
                        listener.onNotificationClicked(notification);
                    }

                }
            });

            binding.executePendingBindings();

        }
    }
}
