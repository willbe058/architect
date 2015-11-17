package com.xpf.me.architect.presenter;

import android.support.annotation.Nullable;

import com.xpf.me.architect.view.IView;

import java.lang.ref.WeakReference;

/**
 * Created by xgo on 11/14/15.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {

    private WeakReference<V> viewRef;

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    @Nullable
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}
