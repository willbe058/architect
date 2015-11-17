package com.xpf.me.architect.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public class FragmentDelegate<V extends IView, P extends IPresenter<V>> implements IFragmentDelegate<V, P> {

    protected BaseDelegateCallback<V, P> delegateCallback;

    protected InternalDelegate<V, P> internalDelegate;

    public FragmentDelegate(BaseDelegateCallback<V, P> callback) {
        if (callback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.delegateCallback = callback;
    }

    protected InternalDelegate<V, P> getInternalDelegate() {
        if (internalDelegate == null) {
            internalDelegate = new InternalDelegate<>(delegateCallback);
        }

        return internalDelegate;
    }

    @Override
    public void onCreate(Bundle saved) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getInternalDelegate().createPresenter();
        getInternalDelegate().attachView();
    }

    @Override
    public void onDestroyView() {
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
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
