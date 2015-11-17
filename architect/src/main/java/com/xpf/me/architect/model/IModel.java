package com.xpf.me.architect.model;

import rx.Observable;

/**
 * Created by xgo on 11/14/15.
 */
public interface IModel<T> {

    Observable<T> execute();

}
