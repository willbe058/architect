package com.xpf.me.architect.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public interface IFragmentDelegate<V extends IView, P extends IPresenter<V>> {
    /**
     * Must be called from {@link Fragment#onCreate(Bundle)}
     *
     * @param saved The bundle
     */
    public void onCreate(Bundle saved);

    /**
     * Must be called from {@link Fragment#onDestroy()}
     */
    public void onDestroy();

    /**
     * Must be called from {@link Fragment#onViewCreated(View, Bundle)}
     *
     * @param view               The inflated view
     * @param savedInstanceState the bundle with the viewstate
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    /**
     * Must be called from {@link Fragment#onDestroyView()}
     */
    public void onDestroyView();

    /**
     * Must be called from {@link Fragment#onPause()}
     */
    public void onPause();

    /**
     * Must be called from {@link Fragment#onResume()}
     */
    public void onResume();

    /**
     * Must be called from {@link Fragment#onStart()}
     */
    public void onStart();

    /**
     * Must be called from {@link Fragment#onStop()}
     */
    public void onStop();

    /**
     * Must be called from {@link Fragment#onActivityCreated(Bundle)}
     *
     * @param savedInstanceState The saved bundle
     */
    public void onActivityCreated(Bundle savedInstanceState);

    /**
     * Must be called from {@link Fragment#onAttach(Activity)}
     *
     * @param activity The activity the fragment is attached to
     */
    public void onAttach(Activity activity);

    /**
     * Must be called from {@link Fragment#onDetach()}
     */
    public void onDetach();

    /**
     * Must be called from {@link Fragment#onSaveInstanceState(Bundle)}
     */
    public void onSaveInstanceState(Bundle outState);
}
