package com.xpf.me.architect.delegate;

import android.os.Bundle;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

/**
 * Created by xgo on 11/14/15.
 */
public interface IActivityDelegate<V extends IView, P extends IPresenter<V>> {
    /**
     * This method must be called from {@link Activit#onCreate(Bundle)}.
     * This method internally creates the presenter and attaches the view to it.
     */
    void onCreate(Bundle bundle);

    /**
     * This method must be called from {@link Activity#onDestroy()}}.
     * This method internally detaches the view from presenter
     */
    void onDestroy();

    /**
     * This method must be called from {@link Activity#onPause()}
     */
    void onPause();

    /**
     * This method must be called from {@link Activity#onResume()}
     */
    void onResume();

    /**
     * This method must be called from {@link Activity#onStart()}
     */
    void onStart();

    /**
     * This method must be called from {@link Activity#onStop()}
     */
    void onStop();

    /**
     * This method must be called from {@link Activity#onRestart()}
     */
    void onRestart();

    /**
     * This method must be called from {@link Activity#onContentChanged()}
     */
    void onContentChanged();

    /**
     * This method must be called from {@link Activity#onSaveInstanceState(Bundle)}
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * This method must be called from {@link Activity#onPostCreate(Bundle)}
     */
    void onPostCreate(Bundle savedInstanceState);

    /**
     * This method must be called from {@link FragmentActivity#onRetainCustomNonConfigurationInstance()}
     *
     * @return Don't forget to return the value returned by this delegate method
     */
    Object onRetainCustomNonConfigurationInstance();

    /**
     * @return the value returned from {@link ActivityDelegateCallback#onRetainNonMosbyCustomNonConfigurationInstance()}
     */

    Object getNonMosbyLastCustomNonConfigurationInstance();
}
