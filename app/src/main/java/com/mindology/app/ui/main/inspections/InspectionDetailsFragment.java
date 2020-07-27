package com.mindology.app.ui.main.inspections;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.mindology.app.BaseFragment;
import com.mindology.app.R;
import com.mindology.app.databinding.FragmentInspectionDetailsBinding;
import com.mindology.app.models.Alarm;
import com.mindology.app.models.Area;
import com.mindology.app.models.Attachment;
import com.mindology.app.models.HotSpot;
import com.mindology.app.models.Image;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.Meter;
import com.mindology.app.models.Panorama;
import com.mindology.app.models.UploadResponse;
import com.mindology.app.network.ProgressRequestBody;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Enums;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InspectionDetailsFragment extends BaseFragment {
    private Dialog syncDialog;
    private LinearLayout layoutMainSyncDetails;

    //    private AtomicInteger upProcessCount = new AtomicInteger(0);
    private Queue<AttachmentMeta> queueToUpload = new LinkedList<AttachmentMeta>();

    private InspectionDetailsViewModel viewModel;
    private FragmentInspectionDetailsBinding binding;
    private AtomicBoolean newlyStarted = new AtomicBoolean(false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentInspectionDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        TempDataHolder.setCurrentInspection(null);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, providerFactory).get(InspectionDetailsViewModel.class);

//        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (!viewModel.getCurrentInspection().isSynced())
//                {
//                    binding.expandableLayoutDiscard.expand(true);
//                }
//            }
//        });

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(TempDataHolder.getCurrentInspection() == null ? new Inspection() : TempDataHolder.getCurrentInspection());

        binding.layBtnStartInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInspectionStart();
            }
        });

        binding.layBtnCompleteInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInspectionComplete();
            }
        });

        binding.layBtnCancelInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInspectionCancel();
            }
        });

        binding.expandableLayoutDiscard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if (state == ExpandableLayout.State.EXPANDING) // collapsed
                {
                    binding.scrlDetials.scrollTo(0, binding.scrlDetials.getBottom());
                }
            }
        });

//        binding.imgClosePrompt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.expandableLayoutDiscard.collapse(true);
//            }
//        });

        binding.layBtnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.expandableLayoutDiscard.collapse(true);
                onInspectionSync();
            }
        });

        binding.layBtnDiscardSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.expandableLayoutDiscard.collapse();
                viewModel.discardChanged();
            }
        });


        binding.layBtnEditAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getCurrentInspection().getState() == Enums.InspectionStatus.ASSIGNED) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.excepttion))
                            .setContentText("Please press start to enable edit assets")
                            .setConfirmText(getString(R.string.ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    TempDataHolder.setCurrentInspection(viewModel.getCurrentInspection());
                    viewModel.persistCurrentInspection();
//                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.assetsScreen);
                }
            }
        });

        binding.layBtnEditInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempDataHolder.setCurrentInspection(viewModel.getCurrentInspection());
//                CreateInspectionFragmentArgs args =
//                        new CreateInspectionFragmentArgs
//                                .Builder()
//                                .setInspectionId(viewModel.getCurrentInspection().getId())
//                                .build();
//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.createInspectionScreen, args.toBundle());
            }
        });

        subscribeCurrentInspection();
    }

    private void subscribeCurrentInspection() {
        String id = InspectionDetailsFragmentArgs.fromBundle(getArguments()).getInspectionId();
        viewModel.observeCurrentInspection(id).observe(getViewLifecycleOwner(), new Observer<Resource<Inspection>>() {
            @Override
            public void onChanged(Resource<Inspection> resource) {
                Inspection inspection = resource.data;
                switch (resource.status) {
                    case ERROR: {
                        binding.progressBar.setVisibility(View.GONE);
                        if (resource.message.equals("CLOSE_PAGE")) {
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                        } else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.excepttion))
                                    .setContentText(resource.message)
                                    .setConfirmText(getString(R.string.ok))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            if (viewModel.getCurrentInspection() == null)
                                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                                        }
                                    })
                                    .show();
                        }
                        break;
                    }
                    case LOADING: {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    }
                    case SUCCESS:
                    case UPDATED: {
                        if (inspection != null) {
                            if (inspection.getState() != null) {
                                if (inspection.getState().equals(Enums.InspectionStatus.COMPLETED) || inspection.getState().equals(Enums.InspectionStatus.CANCELLED)) {
                                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                                    return;
                                }
                            }
                            binding.setViewModel(inspection);
                        }
                        if (TextUtils.isEmpty(inspection.getId())) {
                            binding.txtExpandableDiscardDescription.setText("You have created this inspection locally , you could save it using Save button or you could remove this inspection by tapping on Discard button");
//                            binding.btnSync.setText(getString(R.string.save));
                        } else {
                            binding.txtExpandableDiscardDescription.setText("You have edited this inspection locally , you could sync them using Sync button or you could restore latest state by tapping on Discard button");
//                            binding.btnSync.setText(getString(R.string.sync));
                        }
                        binding.progressBar.setVisibility(View.GONE);

                        if (newlyStarted.compareAndSet(true, false)) {
                            TempDataHolder.setCurrentInspection(viewModel.getCurrentInspection());
                            viewModel.persistCurrentInspection();
//                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.assetsScreen);
                        }

                        break;
                    }
                }

                configButtonBarForInspection(inspection);
                binding.setViewModel(inspection);
            }
        });
    }

    private void configButtonBarForInspection(Inspection inspection) {
        if (inspection == null) {
            binding.layBtnStartInspection.setVisibility(View.GONE);
            binding.layOtherButtonsControl.setVisibility(View.GONE);
            binding.layBtnEditAssets.setVisibility(View.GONE);
            binding.layBtnEditInspection.setVisibility(View.GONE);
        } else {
            binding.layBtnEditAssets.setVisibility(View.VISIBLE);
            binding.layBtnEditInspection.setVisibility(View.VISIBLE);
            binding.expandableLayoutDiscard.collapse(true);
            if (inspection.isSynced()) {
                if (inspection.getState() == Enums.InspectionStatus.ASSIGNED) {
                    binding.layBtnStartInspection.setVisibility(View.VISIBLE);
                    binding.layOtherButtonsControl.setVisibility(View.GONE);
                } else {
                    binding.layBtnStartInspection.setVisibility(View.GONE);
                    binding.layOtherButtonsControl.setVisibility(View.VISIBLE);
                }
            } else {
                binding.expandableLayoutDiscard.expand(true);
                if (inspection.getState() == Enums.InspectionStatus.ASSIGNED) {
                    binding.layBtnStartInspection.setVisibility(View.VISIBLE);
                    binding.layOtherButtonsControl.setVisibility(View.GONE);
                } else {
                    binding.layBtnStartInspection.setVisibility(View.GONE);
                    binding.layOtherButtonsControl.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void processUpload() {
        if (queueToUpload.size() > 0) {

            if (syncDialog == null || !syncDialog.isShowing()) {
                syncDialog = new Dialog(getContext());
                syncDialog.setContentView(R.layout.dialog_sync_process);

                Window window = syncDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                layoutMainSyncDetails = syncDialog.findViewById(R.id.layout_main);

                syncDialog.setCancelable(false);
                syncDialog.setCanceledOnTouchOutside(false);
                syncDialog.show();
            }

            AttachmentMeta attachmentMeta = queueToUpload.poll();

            TextView txtUploadLabel = new TextView(getContext());
            txtUploadLabel.setTextSize(12);
            txtUploadLabel.setTextColor(getResources().getColor(R.color.orange));
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtUploadLabel.setLayoutParams(labelParams);
            layoutMainSyncDetails.addView(txtUploadLabel);

            attachmentMeta.txtLabel = txtUploadLabel;

            ProgressBar prgUpload = new ProgressBar(getContext(),
                    null,
                    android.R.attr.progressBarStyleHorizontal);
            prgUpload.setMax(100);
            LinearLayout.LayoutParams prgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            prgParams.setMargins(8, 4, 8, 4);
            prgUpload.setLayoutParams(prgParams);
            layoutMainSyncDetails.addView(prgUpload);

            attachmentMeta.progressBar = prgUpload;


            File file;
            if (attachmentMeta.attachment.getClass().equals(Panorama.class)) {
                file = new File(((Panorama) attachmentMeta.attachment).getImage().getUrl());
            } else {
                file = new File(((Attachment) attachmentMeta.attachment).getLocalPath());
                //new File(Utils.getRealPathFromURI(getContext(), attachment.getLocalPath()));
            }

            ProgressRequestBody fileRequestBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
                @Override
                public void onProgressUpdate(int percentage) {
                    attachmentMeta.progressBar.setProgress(percentage);
                    if (attachmentMeta.attachment.getClass().equals(Panorama.class)) {
                        attachmentMeta.txtLabel.setText("Uploading Panorama : " + percentage + "%");
                    } else {
                        attachmentMeta.txtLabel.setText("Uploading Attachment : " + percentage + "%");
                    }
                    attachmentMeta.progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
                    attachmentMeta.txtLabel.setTextColor(getResources().getColor(R.color.orange));
                }

                @Override
                public void onError() {

                    attachmentMeta.progressBar.setProgress(attachmentMeta.progressBar.getMax());
                    attachmentMeta.txtLabel.setText("Upload failed");
                    attachmentMeta.txtLabel.setTextColor(getResources().getColor(R.color.red));
                    attachmentMeta.progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);

                }

                @Override
                public void onFinish() {
                    attachmentMeta.progressBar.setProgress(attachmentMeta.progressBar.getMax());
                    attachmentMeta.txtLabel.setText("Upload success");
                    attachmentMeta.txtLabel.setTextColor(getResources().getColor(R.color.green));
                    attachmentMeta.progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
                }
            });

            viewModel
                    .uploadFile(file, fileRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UploadResponse>() {
                        @Override
                        public void accept(UploadResponse response) throws Exception {

                            if (attachmentMeta.meta.getClass().equals(Panorama.class)) {
                                Image imageUploaded = new Image(response.getImageUrl());
                                imageUploaded.setId(response.getId());

                                ((Panorama) attachmentMeta.attachment).setImage(imageUploaded);

                                processUpload();
                            } else {
                                Attachment attachment = (Attachment) attachmentMeta.attachment;
                                attachment.setFileName(response.getName());
                                attachment.setFileId(response.getId());
                                attachment.setFileUrl(response.getImageUrl());
                                attachment.setCreatedAt(response.getCreatedAt());

                                processUpload();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            processUpload();
                        }
                    });
        } else {

            if (syncDialog != null && syncDialog.isShowing()) {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        syncDialog.dismiss();
                        viewModel.saveInspection();
                    }
                }, 1250);
            } else {
                viewModel.saveInspection();
            }
        }

    }

    private void onInspectionSync() {


        List<String> errors = new ArrayList<>();
//        try {
//            InspectionAssets assets = viewModel.getCurrentInspection().getAssets();
//            if (assets != null) {
//                for (Area area : assets.getAreas().getData()) {
//                    if (!TextUtils.isEmpty(area.isValid())) // it has some errors
//                    {
//                        errors.add(area.isValid());
//                    }
//                }
//                for (Alarm alarm : assets.getAlarms().getData()) {
//                    if (!TextUtils.isEmpty(alarm.isValid())) // it has some errors
//                    {
//                        errors.add(alarm.isValid());
//                    }
//                }
//                for (Meter meter : assets.getMeters().getData()) {
//                    if (!TextUtils.isEmpty(meter.isValid())) // it has some errors
//                    {
//                        errors.add(meter.isValid());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (viewModel.getCurrentInspection().getProperty() == null) {
            errors.add("Property is empty");
        }
        if (errors.size() > 0) {
            String errorsString = "";
            for (String err : errors) {
                errorsString += err + "\n";
            }
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.excepttion))
                    .setContentText(errorsString)
                    .setConfirmText(getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
//                            binding.layDiscardChanges.setVisibility(View.VISIBLE);
                            binding.expandableLayoutDiscard.expand(true);
//                            binding.btnSync.setVisibility(View.VISIBLE);
                        }
                    })
                    .show();
        } else {

            queueToUpload.clear();

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtUploadingIndicator.setVisibility(View.VISIBLE);
            for (Area area : viewModel.getCurrentInspection().getAssets().getAreas().getData()) {
                for (Panorama panorama : area.getPanoramas().getData()) {
                    if (TextUtils.isEmpty(panorama.getId())) {
                        queueToUpload.add(new AttachmentMeta(panorama, panorama));
                    }
                }
            }

            for (Area area : viewModel.getCurrentInspection().getAssets().getAreas().getData()) {
                for (Panorama panorama : area.getPanoramas().getData()) {
                    for (HotSpot hotSpot : panorama.getHotspots().getData()) {
                        for (Attachment attachment : hotSpot.getReferencedAttachments().getData()) {
                            if (attachment.isLocal()) {
                                queueToUpload.add(new AttachmentMeta(hotSpot, attachment));
                            }
                        }
                    }
                }
            }

            for (Meter meter : viewModel.getCurrentInspection().getAssets().getMeters().getData()) {
                try {
                    for (Attachment attachment : meter.getReferencedAttachments().getData()) {
                        if (attachment.isLocal()) {
                            queueToUpload.add(new AttachmentMeta(meter, attachment));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (Alarm alarm : viewModel.getCurrentInspection().getAssets().getAlarms().getData()) {
                try {
                    for (Attachment attachment : alarm.getReferencedAttachments().getData()) {
                        if (attachment.isLocal()) {
                            queueToUpload.add(new AttachmentMeta(alarm, attachment));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            processUpload();

        }
    }

    private class AttachmentMeta {
        public Object meta;
        public Object attachment;
        public ProgressBar progressBar;
        public TextView txtLabel;

        public AttachmentMeta(Object meta, Object attachment) {
            this.meta = meta;
            this.attachment = attachment;
        }

    }


    private void onInspectionCancel() {
        viewModel.changeState(Enums.InspectionStatus.CANCELLED);
    }

    private void onInspectionComplete() {
        viewModel.changeState(Enums.InspectionStatus.COMPLETED);

    }

    private void onInspectionStart() {
        newlyStarted.set(true);
        viewModel.changeState(Enums.InspectionStatus.STARTED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        } else {
//            if (TempDataHolder.getSuggestedInspection() != null) {
//                Inspection currentInspection = TempDataHolder.getCurrentInspection();
//                Inspection linkedInspection = TempDataHolder.getSuggestedInspection();
//                currentInspection.setLinkedInspection(linkedInspection);
//                if (currentInspection.getProperty() == null) {
//                    currentInspection.setProperty(linkedInspection.getProperty());
//                }
//                TempDataHolder.notifyCurrentInspectionChanged();
//                TempDataHolder.setSuggestedInspection(null);
//            }
//            if (TempDataHolder.getSelectedProperty() != null) {
//                if (!TempDataHolder.getCurrentInspection().getState().equals(Enums.InspectionStatus.ASSIGNED)) {
//                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(getString(R.string.excepttion))
//                            .setContentText(getString(R.string.exception_could_not_change_property_in_this_state))
//                            .setConfirmText(getString(R.string.ok))
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
//                            .show();
//                } else {
//                    TempDataHolder.getCurrentInspection().setLinkedInspection(null);
//                    TempDataHolder.getCurrentInspection().setProperty(TempDataHolder.getSelectedProperty().copyOf());
//                    TempDataHolder.notifyCurrentInspectionChanged();
//                }
//                TempDataHolder.setSelectedProperty(null);
//            }
//            if (TempDataHolder.getSelectedClient() != null) {
//                Inspection currentInspection = TempDataHolder.getCurrentInspection();
//                currentInspection.setClient(TempDataHolder.getSelectedClient().copyOf());
//                viewModel.setCurrentInspection(currentInspection);
//                TempDataHolder.setSelectedClient(null);
//            }
//        }
    }

}




















