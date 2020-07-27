package com.mindology.app.util;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class QueryTextWatcher implements TextWatcher {

    private PublishSubject<String> subject = PublishSubject.create();

    public void init()
    {
        subject.onNext("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == null || editable.toString().isEmpty())
        {
            subject.onNext("");
        }
        else
        {
            subject.onNext(editable.toString());
        }
    }

    public Observable<String> queryChangeObserver()
    {
        return subject.debounce(200, TimeUnit.MICROSECONDS).distinctUntilChanged();
    }

}
