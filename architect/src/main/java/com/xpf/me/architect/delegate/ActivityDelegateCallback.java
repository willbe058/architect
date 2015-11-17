package com.xpf.me.architect.delegate;

import com.xpf.me.architect.presenter.IPresenter;
import com.xpf.me.architect.view.IView;

/**
 * Created by xgo on 11/14/15.
 */
public interface ActivityDelegateCallback<V extends IView, P extends IPresenter<V>>
        extends BaseDelegateCallback<V, P> {

    /**
     * Return any Object holding the desired state to propagate to the next activity instance. Please
     * note that mosby internals like the presenter are already saved internally and you don't have
     * to take them into account. You can retrieve this value later with {@link
     * IActivityDelegate#getNonMosbyLastCustomNonConfigurationInstance()}.
     * <p>
     * <p>
     * This mechanism works pretty the same way as {@link FragmentActivity#onRetainCustomNonConfigurationInstance()}
     * and {@link #getNonMosbyLastCustomNonConfigurationInstance()}
     * </p>
     *
     * @return Object holding state.
     */
    Object onRetainNonMosbyCustomNonConfigurationInstance();

    /**
     * @return Return the value previously returned from {@link FragmentActivity#onRetainCustomNonConfigurationInstance()}.
     */
    Object getLastCustomNonConfigurationInstance();

    /**
     * This method should invoke {@link
     * IActivityDelegate#getNonMosbyLastCustomNonConfigurationInstance()}.
     * <p>
     * <p>
     * This method is not really a "callback" method (will not invoked from delegate somehow).
     * However, it's part of this interface to ensure that no custom implementation will miss this
     * method since this method is the counterpart to {@link #onRetainNonMosbyCustomNonConfigurationInstance()}
     * </p>
     */
    Object getNonMosbyLastCustomNonConfigurationInstance();
}
