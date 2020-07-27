package com.mindology.app.ui.main.inspections;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.Alarm;
import com.mindology.app.models.Area;
import com.mindology.app.models.Client;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.HotSpot;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.InspectionAssets;
import com.mindology.app.models.Meter;
import com.mindology.app.models.Panorama;
import com.mindology.app.models.UploadResponse;
import com.mindology.app.network.ListResponse;
import com.mindology.app.network.ProgressRequestBody;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.network.storage.StorageApi;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Constants;
import com.mindology.app.util.Enums;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class InspectionDetailsViewModel extends ViewModel {

    private MediatorLiveData<Resource<Inspection>> currentInspectionLiveData;
    private Inspection currentInspection = null;

    // inject
    private final MainApi mainApi;
    private final StorageApi storageApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPreferences;

    @Inject
    public InspectionDetailsViewModel(MainApi mainApi, SessionManager sessionManager, StorageApi storageApi, SharedPrefrencesHelper sp) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.storageApi = storageApi;
        this.sharedPreferences = sp;
    }

    public LiveData<Resource<Inspection>> observeCurrentInspection(String id) {

        if (currentInspectionLiveData == null) {
            currentInspectionLiveData = new MediatorLiveData<>();
            currentInspectionLiveData.setValue(Resource.loading(currentInspection));
            if (getCurrentInspectionPersisted(id) != null && getCurrentInspectionPersisted(id).getId().equals(id) && !getCurrentInspectionPersisted(id).isSynced()) {
                currentInspection = getCurrentInspectionPersisted(id);
                currentInspectionLiveData.setValue(Resource.success(getCurrentInspectionPersisted(id)));
            } else {
                final LiveData<Resource<Inspection>> source = LiveDataReactiveStreams.fromPublisher(
                        mainApi.getInspection(id)
                                .onErrorReturn(new Function<Throwable, Inspection>() {
                                    @Override
                                    public Inspection apply(Throwable throwable) throws Exception {
                                        ErrorResponse errorResponse = Utils.fetchError(throwable);
                                        Inspection result = new Inspection();
//                                        result.setErrorResponse(errorResponse);
                                        return result;
                                    }
                                })
                                .map(new Function<Inspection, Resource<Inspection>>() {
                                    @Override
                                    public Resource<Inspection> apply(Inspection result) throws Exception {
//                                        if (result == null) {
//                                            return Resource.error("Error", currentInspection);
//                                        } else if (result.getErrorResponse() != null) {
//                                            return Resource.error(result.getErrorResponse().getMessage(), currentInspection);
//                                        }
                                        try {
                                            for (Area area : result.getAssets().getAreas().getData()) {
                                                for (Panorama p : area.getPanoramas().getData()) {
                                                    for (HotSpot h : p.getHotspots().getData()) {
                                                        h.getCoordination().convertToDegrees();
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        currentInspection = result;
                                        persistCurrentInspection();
                                        return Resource.success(result);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                );
                currentInspectionLiveData.addSource(source, new Observer<Resource<Inspection>>() {
                    @Override
                    public void onChanged(Resource<Inspection> listResource) {
                        currentInspectionLiveData.setValue(listResource);
                        currentInspectionLiveData.removeSource(source);
                    }
                });
            }
        } else {
            if (getCurrentInspectionPersisted(id) != null && getCurrentInspectionPersisted(id).getId().equals(id)) {
                currentInspection = getCurrentInspectionPersisted(id);
                currentInspectionLiveData.setValue(Resource.success(getCurrentInspectionPersisted(id)));
            } else {
            }
        }
        return currentInspectionLiveData;
    }

    public Flowable<UploadResponse> uploadFile(File file, ProgressRequestBody fileRequestBody) {
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        return storageApi
                .upload(body)
                .onErrorReturn(new Function<Throwable, UploadResponse>() {
                    @Override
                    public UploadResponse apply(Throwable throwable) throws Exception {
                        return null;
                    }
                });
    }

    public Flowable<UploadResponse> uploadFile(String uri, ProgressRequestBody fileRequestBody) {
        return uploadFile(new File(uri), fileRequestBody);
    }

//    public Panorama postPanorama(InspectionDetailsFragment.PanoramaToUpload panoramaToUpload) {
//
//        Panorama panorama = mainApi.postPanorama(panoramaToUpload)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
//                .onErrorReturn(new Function<Throwable, Panorama>() {
//                    @Override
//                    public Panorama apply(Throwable throwable) throws Exception {
//                        return null;
//                    }
//                })
//                .blockingGet();
//        return panorama;
//    }

//    public HotSpot postHotspot(HotSpot hotSpot, String panoramaId) {
//        return mainApi.postHotspot(hotSpot, panoramaId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
//                .onErrorReturn(new Function<Throwable, HotSpot>() {
//                    @Override
//                    public HotSpot apply(Throwable throwable) throws Exception {
//                        HotSpot hotSpot2 = new HotSpot();
//                        ErrorResponse errorResponse = new ErrorResponse();
//                        errorResponse.setMessage(Utils.fetchError(throwable).getMessage());
//                        hotSpot2.setErrorResponse(errorResponse);
//                        return hotSpot2;
//                    }
//                })
//                .blockingSingle();
//    }

//    public ResponseBody bindPanoramaToId(String id, InspectionDetailsFragment.PanoramaToArea panoramaToArea) {
//        return mainApi.bindPanoramaToId(id, panoramaToArea)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
//                .blockingSingle();
//    }

    public void changeInspectionType(Enums.InspectionTypes type) {
        currentInspection.setInspectionType(type);
        currentInspection.setSynced(getCurrentInspectionPersisted(currentInspection.getId()) != null && getCurrentInspectionPersisted(currentInspection.getId()).isEqualTo(currentInspection));
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void changeLinkedInspection(Inspection inspection) {
        currentInspection.setLinkedInspection(inspection);
        currentInspection.setSynced(getCurrentInspectionPersisted(currentInspection.getId()) != null && getCurrentInspectionPersisted(currentInspection.getId()).isEqualTo(currentInspection));
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void changeClient(Client client) {
        try {
            if ((client == null && currentInspection.getClient() == null) || client.isEqualTo(currentInspection.getClient()))
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentInspection.setClient(client);
        currentInspection.setSynced(detectSync());
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void changeDate(Date date) {
        DateFormat format = new SimpleDateFormat(Constants.dateFormat);
        String strDate = format.format(new Date(date.getTime()));
        try {
            if ((TextUtils.isEmpty(strDate) && TextUtils.isEmpty(currentInspection.getDate())) || strDate.equals(currentInspection.getDate()))
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentInspection.setDate(strDate);
        currentInspection.setSynced(detectSync());

        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void changeState(Enums.InspectionStatus status) {
        currentInspectionLiveData.setValue(Resource.loading(currentInspection));
        final LiveData<Resource<Inspection>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.changeInspectionState(currentInspection.getId(), new Inspection.InspectionStateJsonable(status.name()))

                        .onErrorReturn(new Function<Throwable, Inspection>() {
                            @Override
                            public Inspection apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);
//                                currentInspection.setErrorResponse(errorResponse);
                                return currentInspection;
                            }
                        })

                        .map(new Function<Inspection, Resource<Inspection>>() {
                            @Override
                            public Resource<Inspection> apply(Inspection result) throws Exception {

//                                if (result == null) {
//                                    return Resource.error("Error", currentInspection);
//                                } else if (result.getErrorResponse() != null) {
//                                    return Resource.error(result.getErrorResponse().getMessage(), currentInspection);
//                                }

                                currentInspection = result;

                                if (currentInspection.getState().equals(Enums.InspectionStatus.STARTED) && status.equals(Enums.InspectionStatus.STARTED)) {
                                    checkForTemplates();
                                }
                                return Resource.success(currentInspection);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );
        currentInspectionLiveData.addSource(source, new Observer<Resource<Inspection>>() {
            @Override
            public void onChanged(Resource<Inspection> listResource) {
                currentInspectionLiveData.setValue(listResource);
                currentInspectionLiveData.removeSource(source);
            }
        });
    }

    private void checkForTemplates() {
        // check that the inspection assets are empty, if yes it must be filled with prepared template
        if (currentInspection.getAssets() == null
                ||
                (currentInspection.getAssets().getAlarms().getData().size() == 0 &&
                        currentInspection.getAssets().getAreas().getData().size() == 0 &&
                        currentInspection.getAssets().getAlarms().getData().size() == 0)) {


            InspectionAssets assets = new InspectionAssets();

            ListResponse<Alarm> alarms = new ListResponse<>();
            List<Alarm> alarmsList = new ArrayList<>();
            Alarm coAlarm = new Alarm();
            coAlarm.setTitle("Carbon Monoxide Alarm");
            coAlarm.setType(Enums.AssetType.ALARM);
            Alarm smokeAlarm = new Alarm();
            smokeAlarm.setTitle("Smoke Alarm");
            smokeAlarm.setType(Enums.AssetType.ALARM);
            alarmsList.add(smokeAlarm);
            alarmsList.add(coAlarm);
            alarms.setData(alarmsList);

            ListResponse<Meter> meters = new ListResponse<>();
            List<Meter> metersList = new ArrayList<>();
            Meter electricMeter = new Meter();
            electricMeter.setTitle("Electric Meter");
            electricMeter.setType(Enums.AssetType.METER);
            Meter waterMeter = new Meter();
            waterMeter.setTitle("Water Meter");
            waterMeter.setType(Enums.AssetType.METER);
            Meter gasMeter = new Meter();
            gasMeter.setTitle("Gas Meter");
            gasMeter.setType(Enums.AssetType.METER);
            metersList.add(electricMeter);
            metersList.add(waterMeter);
            metersList.add(gasMeter);
            meters.setData(metersList);

            ListResponse<Area> areas = new ListResponse<>();
            List<Area> areasList = new ArrayList<>();
            Area hallwayArea = new Area();
            hallwayArea.setTitle("Hallway");
            hallwayArea.setType(Enums.AssetType.AREA);
            hallwayArea.setIndex(1);
            Area kitchenArea = new Area();
            kitchenArea.setTitle("Kitchen");
            kitchenArea.setType(Enums.AssetType.AREA);
            kitchenArea.setIndex(2);
            Area receptionRoomArea = new Area();
            receptionRoomArea.setTitle("Reception Room");
            receptionRoomArea.setType(Enums.AssetType.AREA);
            receptionRoomArea.setIndex(3);
            areasList.add(hallwayArea);
            areasList.add(kitchenArea);
            areasList.add(receptionRoomArea);
            if (currentInspection.getProperty().getBedrooms() > 0) {
                for (int i = 1; i <= currentInspection.getProperty().getBedrooms(); i++) {
                    Area bedroomArea = new Area();
                    bedroomArea.setTitle("Bedroom " + i);
                    bedroomArea.setType(Enums.AssetType.AREA);
                    bedroomArea.setIndex(areasList.size() + 1);
                    areasList.add(bedroomArea);
                }
            }
            if (currentInspection.getProperty().getBathrooms() > 0) {
                for (int i = 1; i <= currentInspection.getProperty().getBathrooms(); i++) {
                    Area bathroomArea = new Area();
                    bathroomArea.setTitle("Bathroom " + i);
                    bathroomArea.setType(Enums.AssetType.AREA);
                    bathroomArea.setIndex(areasList.size() + 1);
                    areasList.add(bathroomArea);
                }
            }

            if (currentInspection.getProperty().isGarageContained()) {
                Area garageArea = new Area();
                garageArea.setTitle("Garage");
                garageArea.setType(Enums.AssetType.AREA);
                garageArea.setIndex(areasList.size() + 1);
                areasList.add(garageArea);
            }
            if (currentInspection.getProperty().isGardenContained()) {
                Area gardenArea = new Area();
                gardenArea.setTitle("Garden");
                gardenArea.setType(Enums.AssetType.AREA);
                gardenArea.setIndex(areasList.size() + 1);
                areasList.add(gardenArea);
            }
            if (currentInspection.getProperty().isParkingContained()) {
                Area parkingArea = new Area();
                parkingArea.setTitle("Parking");
                parkingArea.setType(Enums.AssetType.AREA);
                parkingArea.setIndex(areasList.size() + 1);
                areasList.add(parkingArea);
            }
            areas.setData(areasList);

            assets.setAlarms(alarms);
            assets.setMeters(meters);
            assets.setAreas(areas);

            setAssets(assets);
        }
    }


    public void clearProperty() {
        if (currentInspection.getProperty() == null) return;
        currentInspection.setProperty(null);
        currentInspection.setSynced(detectSync());
        persistCurrentInspection();
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void clearClient() {
        if (currentInspection.getClient() == null) return;
        currentInspection.setClient(null);
        currentInspection.setSynced(detectSync());
        persistCurrentInspection();
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void clearLinkedInspection() {
        if (currentInspection.getLinkedInspection() == null) return;
        currentInspection.setLinkedInspection(null);
        currentInspection.setSynced(detectSync());
        persistCurrentInspection();
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void changeNote(String toString) {
        try {
            if ((TextUtils.isEmpty(toString) && TextUtils.isEmpty(currentInspection.getNote())) || (toString.equals(currentInspection.getNote())))
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentInspection.setNote(toString);
        currentInspection.setSynced(detectSync());
        currentInspectionLiveData.setValue(Resource.success(currentInspection));
    }

    public void setAssets(InspectionAssets assets) {
        currentInspection.setAssets(assets);
        currentInspection.setSynced(false);
        SharedPrefrencesHelper.addInspectionToDrafts(currentInspection);
    }

    private boolean detectSync() {
        Inspection pInspection = getCurrentInspectionPersisted(currentInspection.getId());
        if (pInspection == null) {
            return false;
        } else {
            return pInspection.isSynced() && pInspection.isEqualTo(currentInspection);
        }
    }

    public Inspection getCurrentInspection() {

        return currentInspection;
    }

    public void saveInspection() {
//        currentInspection.setErrorResponse(null);
        currentInspectionLiveData.setValue(Resource.loading(currentInspection));
        try {
            for (Area area : currentInspection.getAssets().getAreas().getData()) {
                for (Panorama p : area.getPanoramas().getData()) {
                    int index = 1;
                    for (HotSpot h : p.getHotspots().getData()) {
                        h.getCoordination().convertToRadians();
                        h.setIndex(index);
                        index++;
                    }
                }
            }
            int index = 1;
            for (Meter meter : currentInspection.getAssets().getMeters().getData()) {
                meter.setIndex(index);
                index++;
            }
            index = 1;
            for (Alarm alarm : currentInspection.getAssets().getAlarms().getData()) {
                alarm.setIndex(index);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(currentInspection.getId())) {
            final LiveData<Resource<Inspection>> source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.createInspection(currentInspection.getCreateObject())

                            .onErrorReturn(new Function<Throwable, Inspection>() {
                                @Override
                                public Inspection apply(Throwable throwable) throws Exception {
                                    ErrorResponse errorResponse = Utils.fetchError(throwable);
//                                    currentInspection.setErrorResponse(errorResponse);
                                    return currentInspection;
                                }
                            })
                            .map(new Function<Inspection, Resource<Inspection>>() {
                                @Override
                                public Resource<Inspection> apply(Inspection result) throws Exception {
//                                    if (result == null) {
//                                        return Resource.error("Something went wrong", currentInspection);
//                                    } else if (result.getErrorResponse() != null) {
//                                        return Resource.error(result.getErrorResponse().getMessage(), currentInspection);
//                                    }
                                    // clear draft one
                                    SharedPrefrencesHelper.removeInspectionFromPersistedList(null);
                                    currentInspection = result;
                                    return Resource.updated(result);
                                }
                            })
                            .subscribeOn(Schedulers.io())
            );

            currentInspectionLiveData.addSource(source, new Observer<Resource<Inspection>>() {
                @Override
                public void onChanged(Resource<Inspection> listResource) {
                    currentInspectionLiveData.setValue(listResource);
                    currentInspectionLiveData.removeSource(source);
                }
            });
        } else {
            final LiveData<Resource<Inspection>> source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.editInspection(currentInspection.getId(), currentInspection)

                            .onErrorReturn(new Function<Throwable, Inspection>() {
                                @Override
                                public Inspection apply(Throwable throwable) throws Exception {
                                    ErrorResponse errorResponse = Utils.fetchError(throwable);

//                                    currentInspection.setErrorResponse(errorResponse);
                                    return currentInspection;
                                }
                            })

                            .map(new Function<Inspection, Resource<Inspection>>() {
                                @Override
                                public Resource<Inspection> apply(Inspection result) throws Exception {

//                                    if (result == null) {
//                                        return Resource.error("Error", currentInspection);
//                                    } else if (result.getErrorResponse() != null) {
//                                        return Resource.error(result.getErrorResponse().getMessage(), result);
//                                    }
                                    try {
                                        for (Area area : result.getAssets().getAreas().getData()) {
                                            for (Panorama p : area.getPanoramas().getData()) {
                                                for (HotSpot h : p.getHotspots().getData()) {
                                                    h.getCoordination().convertToDegrees();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    currentInspection = result;

                                    persistCurrentInspection();
                                    return Resource.updated(result);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );

            currentInspectionLiveData.addSource(source, new Observer<Resource<Inspection>>() {
                @Override
                public void onChanged(Resource<Inspection> listResource) {
                    currentInspectionLiveData.setValue(listResource);
                    currentInspectionLiveData.removeSource(source);
                }
            });
        }
    }

    public void discardChanged() {
        if (currentInspection == null || TextUtils.isEmpty(currentInspection.getId())) {
            SharedPrefrencesHelper.removeInspectionFromPersistedList(null);
            currentInspectionLiveData.setValue(Resource.error("CLOSE_PAGE", null));
            return;
        }
        currentInspectionLiveData.setValue(Resource.loading(currentInspection));

        final LiveData<Resource<Inspection>> source = LiveDataReactiveStreams.fromPublisher(
                mainApi.getInspection(currentInspection.getId())
                        .onErrorReturn(new Function<Throwable, Inspection>() {
                            @Override
                            public Inspection apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);
//                                currentInspection.setErrorResponse(errorResponse);
                                return currentInspection;
                            }
                        })
                        .map(new Function<Inspection, Resource<Inspection>>() {
                            @Override
                            public Resource<Inspection> apply(Inspection result) throws Exception {
//                                if (result == null) {
//                                    return Resource.error("Error", currentInspection);
//                                } else if (result.getErrorResponse() != null) {
//                                    return Resource.error(result.getErrorResponse().getMessage(), currentInspection);
//                                }
                                try {
                                    for (Area area : result.getAssets().getAreas().getData()) {
                                        for (Panorama p : area.getPanoramas().getData()) {
                                            for (HotSpot h : p.getHotspots().getData()) {
                                                h.getCoordination().convertToDegrees();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                currentInspection = result;
                                persistCurrentInspection();
                                return Resource.success(result);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
        currentInspectionLiveData.addSource(source, new Observer<Resource<Inspection>>() {
            @Override
            public void onChanged(Resource<Inspection> listResource) {
                currentInspectionLiveData.setValue(listResource);
                currentInspectionLiveData.removeSource(source);
            }
        });
    }

    public void persistCurrentInspection() {
        SharedPrefrencesHelper.addInspectionToDrafts(currentInspection);
    }

    public Inspection getCurrentInspectionPersisted(String id) {
        return SharedPrefrencesHelper.getPersistedInspectionById(id);
    }

}



















