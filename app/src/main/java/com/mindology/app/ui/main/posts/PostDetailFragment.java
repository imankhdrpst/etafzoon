package com.mindology.app.ui.main.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.models.HelpfulPostDTO;
import com.mindology.app.models.Post;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.AuthActivity;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.mindology.app.util.Utils.getBitmapFromBase64String;

public class PostDetailFragment extends BaseFragment {

    private PostDetailViewModel viewModel;
    private View progressBar;
    private TextView txtPostTitle, txtPostDateTime, txtPostContent, txtUsefulYes, txtUsefulNo, txtAuthorName, txtAuthorProfession, txtAuthorStarCount;
    private ImageView imgPostPicture, imgBookmark, imgShare, imgAuthorPicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, providerFactory).get(PostDetailViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);
        txtPostTitle = view.findViewById(R.id.txt_post_title);
        txtPostDateTime = view.findViewById(R.id.txt_post_date_time);
        txtPostContent = view.findViewById(R.id.txt_post_content);
        imgPostPicture = view.findViewById(R.id.img_post_image);
        txtUsefulYes = view.findViewById(R.id.txt_post_useful_yes);
        txtUsefulYes.setTag(true);
        txtUsefulNo = view.findViewById(R.id.txt_post_useful_no);
        txtUsefulNo.setTag(true);
        txtAuthorName = view.findViewById(R.id.txt_post_author_name);
        txtAuthorProfession = view.findViewById(R.id.txt_post_author_profession);
        txtAuthorStarCount = view.findViewById(R.id.txt_post_author_stars_count);
        imgPostPicture = view.findViewById(R.id.img_post_image);
        imgAuthorPicture = view.findViewById(R.id.img_author_picture);
        imgBookmark = view.findViewById(R.id.img_bookmark);
        imgBookmark.setTag(true);
        imgShare = view.findViewById(R.id.img_share);

        imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookmarkClicked();
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClicked();
            }
        });
        txtUsefulNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsefulNo.getTag() != null && (boolean) txtUsefulNo.getTag()) {
                    onNotUsefulClicked();
                }
            }
        });
        txtUsefulYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsefulYes.getTag() != null && (boolean) txtUsefulYes.getTag()) {
                    onUsefulYesClicked();
                }
            }
        });

        subscribeObservers();
    }

    private void onUsefulYesClicked() {
        viewModel.setHelpful(mainViewModel.getMyProfile().getMobileNumber(), true);
    }

    private void onNotUsefulClicked() {
        viewModel.setHelpful(mainViewModel.getMyProfile().getMobileNumber(), false);
    }

    private void onShareClicked() {
        viewModel.sharePost();
    }

    private void onBookmarkClicked() {
        viewModel.setBookmark(mainViewModel.getMyProfile().getMobileNumber(), (boolean) imgBookmark.getTag());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void subscribeObservers() {
        viewModel.queryPostDetails().observe(getViewLifecycleOwner(), new Observer<Resource<Post>>() {
            @Override
            public void onChanged(Resource<Post> listResource) {
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
                            fillFormWithData(listResource.data);
                            progressBar.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });

        viewModel.observerHelpful().observe(getViewLifecycleOwner(), new Observer<Resource<HelpfulPostDTO>>() {
            @Override
            public void onChanged(Resource<HelpfulPostDTO> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.GONE);
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(listResource.message)
                                    .setConfirmText(getString(R.string.ok))
                                    .setCancelText(null)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .show();
                            break;
                        case UPDATED:
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });

        viewModel.observerBookmark().observe(getViewLifecycleOwner(), new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case ERROR:
                        case UPDATED:
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        TempDataHolder.setSelectedPost(null);
        super.onDestroyView();
    }

    private void fillFormWithData(Post data) {
        txtPostTitle.setText(data.getTitle());
        txtAuthorName.setText(data.getAuthor());
        txtAuthorStarCount.setText("5");
        txtPostContent.setText(data.getDescription());
        txtPostDateTime.setText(Utils.dateDifferenceCalculate(data.getCreateDate()));
        Glide.with(getContext())
                .load(getBitmapFromBase64String(data.getAuthorProfilePicture()))
                .into(imgAuthorPicture);
        Glide.with(getContext())
                .load(getBitmapFromBase64String(data.getImageContent()))
                .into(imgPostPicture);

        if (data.getHelpful().equals("0")) // post has been set as not useful
        {
            txtUsefulYes.setTextColor(getResources().getColor(R.color.text_dark_gray));
            txtUsefulYes.setTag(true);
            txtUsefulNo.setTextColor(getResources().getColor(R.color.green));
            txtUsefulNo.setTag(false);
        } else if (data.getHelpful().equals("1")) // post has been set as useful
        {
            txtUsefulYes.setTextColor(getResources().getColor(R.color.green));
            txtUsefulYes.setTag(false);
            txtUsefulNo.setTextColor(getResources().getColor(R.color.text_dark_gray));
            txtUsefulNo.setTag(true);
        } else  // the useful has not been set yet
        {
            txtUsefulYes.setTextColor(getResources().getColor(R.color.text_dark_gray));
            txtUsefulYes.setTag(true);
            txtUsefulNo.setTextColor(getResources().getColor(R.color.text_dark_gray));
            txtUsefulNo.setTag(true);
        }

        if (data.isBookmarked()) {
            imgBookmark.setColorFilter(getResources().getColor(R.color.red));
            imgBookmark.setTag(false);
        } else {
            imgBookmark.setColorFilter(getResources().getColor(R.color.blue));
            imgBookmark.setTag(true);
        }
    }

}




















