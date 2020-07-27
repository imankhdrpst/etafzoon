package com.mindology.app.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.models.ContactMessageResponse;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ContactUsViewModel extends ViewModel {

    private static final String TAG = "ContactUsViewModel";

    private final MainApi mainApi; // @ForgetPasswordScope scoped dependency

    private MediatorLiveData<Resource<ContactMessageResponse>> messageLiveData = null;


    @Inject
    public ContactUsViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
    }


    public LiveData<Resource<ContactMessageResponse>> observeSendMessage(final String name, final String email, final String category, final String message) {

        if (messageLiveData == null) {

            messageLiveData = new MediatorLiveData<>();
            messageLiveData.setValue(Resource.loading((ContactMessageResponse) null));

            ContactMessageResponse model = new ContactMessageResponse();
            model.setName(name);
            model.setEmail(email);
            model.setCategory(category);
            model.setMessage(message);

            final LiveData<Resource<ContactMessageResponse>> source = LiveDataReactiveStreams.fromPublisher(
                    mainApi.sendMessage(model)
                            // instead of calling onError, do this
                            .onErrorReturn(new Function<Throwable, ContactMessageResponse>() {
                                @Override
                                public ContactMessageResponse apply(Throwable throwable) throws Exception {
                                    ErrorResponse errorResponse = Utils.fetchError(throwable);

                                    ContactMessageResponse result = new ContactMessageResponse();
//                                    result.setErrorResponse(errorResponse);
                                    return result;
                                }
                            })
                            // wrap User object in AuthResource
                            .map(new Function<ContactMessageResponse, Resource<ContactMessageResponse>>() {
                                @Override
                                public Resource<ContactMessageResponse> apply(ContactMessageResponse result) throws Exception {

//                                    if (result == null) {
//                                        return Resource.error("Something went wrong", null);
//                                    }
//                                    else if (result.getErrorResponse() != null)
//                                    {
//                                        return Resource.error(result.getErrorResponse().getMessage(), null);
//                                    }
                                    return Resource.success(result);
                                }
                            })
                            .subscribeOn(Schedulers.io()));

            messageLiveData.addSource(source, new Observer<Resource<ContactMessageResponse>>() {
                @Override
                public void onChanged(Resource<ContactMessageResponse> contactMessageResponseResource) {
                    messageLiveData.setValue(contactMessageResponseResource);
                    messageLiveData.removeSource(source);
                }
            });

        }

        return messageLiveData;
    }


}









