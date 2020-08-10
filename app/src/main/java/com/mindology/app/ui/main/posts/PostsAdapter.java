package com.mindology.app.ui.main.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mindology.app.databinding.ViewHolderPostsBinding;
import com.mindology.app.models.Post;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mindology.app.util.Utils.getBitmapFromBase64String;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private LayoutInflater inflater;
    private List<Post> data = new ArrayList<>();
    private OnPostListener listener = null;

    public PostsAdapter(OnPostListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewHolderPostsBinding binding = ViewHolderPostsBinding.inflate(inflater, parent, false);

        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post);
    }

    public void setData(List<Post> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderPostsBinding binding;

        private Post post;

        public PostViewHolder(@NonNull ViewHolderPostsBinding b) {
            super(b.getRoot());
            binding = b;

        }

        public void bind(Post post) {
            this.post = post;

            binding.txtPostTitle.setText(post.getTitle());
            binding.txtPostAuthorName.setText(post.getAuthor());
            binding.txtPostTime.setText(Utils.dateDifferenceCalculate(post.getCreateDate()));

            Glide.with(binding.txtPostAuthorName.getContext())
                    .load(getBitmapFromBase64String(post.getImageContent()))
                    .into(binding.imageView1);


            Glide.with(binding.txtPostAuthorName.getContext())
                    .load(getBitmapFromBase64String(post.getAuthorProfilePicture()))
                    .into(binding.imgAuthorPicture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        listener.onPostClicked(post);
                    }

                }
            });

            binding.executePendingBindings();

        }
    }
}
