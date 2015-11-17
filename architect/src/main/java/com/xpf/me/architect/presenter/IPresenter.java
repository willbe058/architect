package com.xpf.me.architect.presenter;

import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public interface IPresenter<V extends IView> {

    void onResume();

    void onPause();

    void onStop();

    void attachView(V view);

    void detachView(boolean retainInstance);
}
