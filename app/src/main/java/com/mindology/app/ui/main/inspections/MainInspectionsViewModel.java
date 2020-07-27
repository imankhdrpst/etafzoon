package com.mindology.app.ui.main.inspections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.User;
import com.mindology.app.network.ListResponse;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.network.models.Filter;
import com.mindology.app.network.models.Filters;
import com.mindology.app.network.models.Sort;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Enums;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainInspectionsViewModel extends ViewModel {

    private MediatorLiveData<Resource<ListResponse<Inspection>>> inspections;

    // inject
    private final MainApi mainApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPrefs;

    @Inject
    public MainInspectionsViewModel(MainApi mainApi, SessionManager sessionManager, SharedPrefrencesHelper helper) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.sharedPrefs = helper;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<ListResponse<Inspection>>> queryInspections() {
        if (inspections == null)
            inspections = new MediatorLiveData<>();
        inspections.setValue(Resource.loading((ListResponse<Inspection>) new ListResponse<Inspection>()));

        Filters filters = new Filters();
        Filter assignedFilter = new Filter();
        assignedFilter.setField("state");
        assignedFilter.setValue(Enums.InspectionStatus.ASSIGNED.name());
        assignedFilter.setOperator(Enums.FilterOperators.like);
        filters.add(assignedFilter);

        Filter startedFilter = new Filter();
        startedFilter.setField("state");
        startedFilter.setValue(Enums.InspectionStatus.STARTED.name());
        startedFilter.setOperator(Enums.FilterOperators.like);
        filters.add(startedFilter);

        Sort sort = new Sort();
        sort.setOrder(Enums.SortOrder.DESC);
        sort.setField("date");

        AtomicInteger reqCounter = new AtomicInteger(0);

        final LiveData<Resource<ListResponse<Inspection>>> assignedSourc = LiveDataReactiveStreams.fromPublisher(

                mainApi.getSummaryInspections(Enums.InspectionStatus.ASSIGNED.name().toLowerCase(), 1, 1000)

                        .onErrorReturn(new Function<Throwable, ListResponse<Inspection>>() {
                            @Override
                            public ListResponse<Inspection> apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);

                                ListResponse<Inspection> result = new ListResponse<>();
//                                result.setErrorResponse(errorResponse);
                                return result;
                            }
                        })

                        .map(new Function<ListResponse<Inspection>, Resource<ListResponse<Inspection>>>() {
                            @Override
                            public Resource<ListResponse<Inspection>> apply(ListResponse<Inspection> response) throws Exception {

//                                if (response == null) {
//                                    reqCounter.decrementAndGet();
//                                    return Resource.error("Something went wrong", new ListResponse<Inspection>());
//                                } else if (response.getErrorResponse() != null) {
//                                    reqCounter.decrementAndGet();
//                                    return Resource.error(response.getErrorResponse().getMessage(), new ListResponse<Inspection>());
//                                }
                                List<Inspection> listOfStored = sharedPrefs.getPersistedInspectionList();
                                if (listOfStored.size() > 0)
                                    for (Inspection inspection : response.getData()) {
                                        if (isThisInspectionStored(inspection.getId(), listOfStored) != null && !isThisInspectionStored(inspection.getId(), listOfStored).isSynced()) {
                                            inspection.setSynced(false);
                                        }
                                    }
                                reqCounter.decrementAndGet();
                                return Resource.updated(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        reqCounter.incrementAndGet();
                    }
                })
        );
        final LiveData<Resource<ListResponse<Inspection>>> startedSource = LiveDataReactiveStreams.fromPublisher(

                mainApi.getSummaryInspections(Enums.InspectionStatus.STARTED.name().toLowerCase(), 1, 1000)

                        .onErrorReturn(new Function<Throwable, ListResponse<Inspection>>() {
                            @Override
                            public ListResponse<Inspection> apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);

                                ListResponse<Inspection> result = new ListResponse<>();
//                                result.setErrorResponse(errorResponse);
                                return result;
                            }
                        })

                        .map(new Function<ListResponse<Inspection>, Resource<ListResponse<Inspection>>>() {
                            @Override
                            public Resource<ListResponse<Inspection>> apply(ListResponse<Inspection> response) throws Exception {

//                                if (response == null) {
//                                    reqCounter.decrementAndGet();
//                                    return Resource.error("Something went wrong",  new ListResponse<Inspection>());
//                                } else if (response.getErrorResponse() != null) {
//                                    reqCounter.decrementAndGet();
//                                    return Resource.error(response.getErrorResponse().getMessage(),  new ListResponse<Inspection>());
//                                }
                                List<Inspection> listOfStored = sharedPrefs.getPersistedInspectionList();
                                if (listOfStored.size() > 0)
                                    for (Inspection inspection : response.getData()) {
                                        if (isThisInspectionStored(inspection.getId(), listOfStored) != null && !isThisInspectionStored(inspection.getId(), listOfStored).isSynced()) {
                                            inspection.setSynced(false);
                                        }
                                    }
                                reqCounter.decrementAndGet();
                                return Resource.updated(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        reqCounter.incrementAndGet();
                    }
                })
        );
        inspections.addSource(assignedSourc, new Observer<Resource<ListResponse<Inspection>>>() {
            @Override
            public void onChanged(Resource<ListResponse<Inspection>> listResource) {
                if (listResource.status == Resource.Status.ERROR) {
                    if (reqCounter.compareAndSet(0, 0)) {
                        inspections.setValue(listResource);
                    }
                }
                else {
                    List<Inspection> res = inspections.getValue().data.getData();
                    res.addAll(listResource.data.getData());
                    if (reqCounter.compareAndSet(0, 0)) {
                        Inspection persistedDraft = sharedPrefs.getPersistedInspectionById("");
                        if (persistedDraft != null) {
                            res.add(0, persistedDraft);
                        }
                        inspections.setValue(Resource.success(new ListResponse<>(res)));
                    }
                }
                inspections.removeSource(assignedSourc);
            }
        });
        inspections.addSource(startedSource, new Observer<Resource<ListResponse<Inspection>>>() {
            @Override
            public void onChanged(Resource<ListResponse<Inspection>> listResponseResource) {
                if (listResponseResource.status == Resource.Status.ERROR) {
                    if (reqCounter.compareAndSet(0, 0)) {
                        inspections.setValue(listResponseResource);
                    }
                }
                else {
                    List<Inspection> res = inspections.getValue().data.getData();
                    res.addAll(listResponseResource.data.getData());
                    if (reqCounter.compareAndSet(0, 0)) {
                        Inspection persistedDraft = sharedPrefs.getPersistedInspectionById("");
                        if (persistedDraft != null) {
                            res.add(0, persistedDraft);
                        }
                        inspections.setValue(Resource.success(new ListResponse<>(res)));
                    }
                }
                inspections.removeSource(startedSource);
            }
        });
        return inspections;
    }

    public List<Inspection> fetchInProgressInspections() {
        List<Inspection> tempData = new ArrayList<>();
        try {
            for (Inspection inspection : inspections.getValue().data.getData()) {
                if (inspection.getState() == Enums.InspectionStatus.STARTED) {
                    tempData.add(inspection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempData;
    }

    public List<Inspection> fetchPendingInspections() {
        List<Inspection> tempData = new ArrayList<>();
        try {
            for (Inspection inspection : inspections.getValue().data.getData()) {
                if (inspection.getState() == Enums.InspectionStatus.ASSIGNED) {
                    tempData.add(inspection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempData;

    }

    private synchronized Inspection isThisInspectionStored(String id, List<Inspection> listOfStored) {
        if (listOfStored != null) {
            for (Inspection item : listOfStored) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public void refresh() {
        queryInspections();
    }

    public boolean isThereAnyDraftedNewInspection() {
        return sharedPrefs.getPersistedInspectionById(null) != null;
    }

    public Inspection getNewInspectionDrafted() {
        return sharedPrefs.getPersistedInspectionById(null);
    }
}



















