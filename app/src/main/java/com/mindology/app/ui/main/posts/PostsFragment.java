package com.mindology.app.ui.main.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.Post;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;

import java.util.List;

public class PostsFragment extends BaseFragment implements OnPostListener {

    private PostsViewModel viewModel;
    private RecyclerView rvPosts;
    private PostsAdapter postsAdapter;
    private View progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(PostsViewModel.class);

        progressBar = view.findViewById(R.id.prg_latest_posts);

        rvPosts = view.findViewById(R.id.rv_latest_posts);
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.HORIZONTAL, true));
        postsAdapter = new PostsAdapter(this);
        rvPosts.setAdapter(postsAdapter);

        subscribeObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void subscribeObservers() {
        viewModel.queryLatestPosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            break;
                        case UPDATED:
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            postsAdapter.setData(listResource.data);
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void onPostClicked(Post post) {
        TempDataHolder.setSelectedPost(post);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.postDetailScreen);

    }
}




















