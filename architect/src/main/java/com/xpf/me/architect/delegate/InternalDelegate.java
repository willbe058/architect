package com.xpf.me.architect.delegate;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
class InternalDelegate<V extends IView, P extends IPresenter<V>> {
    protected BaseDelegateCallback<V, P> delegateCallback;

    InternalDelegate(BaseDelegateCallback<V, P> delegateCallback) {

        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }

        this.delegateCallback = delegateCallback;
    }

    /**
     * Called  to create the presenter (if no other one already exisits)
     */
    void createPresenter() {

        P presenter = delegateCallback.getPresenter();

        if (presenter == null) {
            presenter = delegateCallback.createPresenter();
        }

        if (presenter == null) {
            throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
        }

        delegateCallback.setPresenter(presenter);
    }

    /**
     * Attaches the view to the presenter
     */
    void attachView() {
        getPresenter().attachView(delegateCallback.getMvpView());
    }

    /**
     * Called to detach the view from presenter
     */
    void detachView() {
        getPresenter().detachView(delegateCallback.shouldInstanceBeRetained());
    }

    private P getPresenter() {
        P presenter = delegateCallback.getPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter returned from getPresenter() is null");
        }
        return presenter;
    }
}
