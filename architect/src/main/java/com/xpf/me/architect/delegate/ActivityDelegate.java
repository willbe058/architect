package com.xpf.me.architect.delegate;

import android.os.Bundle;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public class ActivityDelegate<V extends IView, P extends IPresenter<V>> implements IActivityDelegate {

    protected InternalDelegate<V, P> internalDelegate;

    protected ActivityDelegateCallback<V, P> delegateCallback;

    public ActivityDelegate(ActivityDelegateCallback<V, P> callback) {
        if (callback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.delegateCallback = callback;
    }

    /**
     * Get the internal delegate.
     */
    protected InternalDelegate<V, P> getInternalDelegate() {
        if (internalDelegate == null) {
            internalDelegate = new InternalDelegate<>(delegateCallback);
        }

        return internalDelegate;
    }

    @Override
    public void onCreate(Bundle bundle) {
        getInternalDelegate().createPresenter();
        getInternalDelegate().attachView();
    }

    @Override
    public void onDestroy() {
        getInternalDelegate().detachView();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    @Override
    public Object getNonMosbyLastCustomNonConfigurationInstance() {
        return null;
    }
}
