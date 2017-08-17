package com.truyayong.gatherstory.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public abstract class BasePresenter<V> {

    protected Reference<V> mViewRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public V getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
