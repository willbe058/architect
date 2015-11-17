package com.xpf.me.architect.delegate;

import android.support.annotation.NonNull;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public interface BaseDelegateCallback<V extends IView, P extends IPresenter<V>> {
    /**
     * Creates the presenter instance
     *
     * @return the created presenter instance
     */
    @NonNull
    P createPresenter();

    /**
     * Get the presenter. If null is returned, then a internally a new presenter instance gets
     * created
     * by calling {@link #createPresenter()}
     *
     * @return the presenter instance. can be null.
     */
    P getPresenter();

    /**
     * Sets the presenter instance
     *
     * @param presenter The presenter instance
     */
    void setPresenter(P presenter);

    /**
     * Get the MvpView for the presenter
     *
     * @return The view associated with the presenter
     */
    V getMvpView();

    /**
     * Indicate whether the retain instance feature is enabled by this view or not
     *
     * @return true if the view has  enabled retaining, otherwise false.
     * @see #setRetainInstance(boolean)
     */
    boolean isRetainInstance();

    /**
     * Mark this instance as retaining. This means that the feature of a retaining instance is
     * enabled.
     *
     * @param retainingInstance true if retaining instance feature is enabled, otherwise false
     * @see #isRetainInstance()
     */
    void setRetainInstance(boolean retainingInstance);

    /**
     * Indicates whether or not the the view will be retained during next screen orientation change.
     * This boolean flag is used for {@link MvpPresenter#detachView(boolean)}
     * as parameter. Usually you should take {@link Activity#isChangingConfigurations()} into
     * account. The difference between {@link #shouldInstanceBeRetained()} and {@link
     * #isRetainInstance()} is that {@link #isRetainInstance()} indicates that retain instance
     * feature is enabled or disabled while {@link #shouldInstanceBeRetained()} indicates if the
     * view is going to be destroyed permanently and hence should no more be retained (i.e. Activity
     * is finishing and not just screen orientation changing)
     *
     * @return true if the instance should be retained, otherwise false
     */
    boolean shouldInstanceBeRetained();
}
