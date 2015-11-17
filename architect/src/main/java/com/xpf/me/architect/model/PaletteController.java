package com.xpf.me.architect.model;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xgo on 11/15/15.
 */
public class PaletteController implements IModel<Palette> {

    private Bitmap mBitmap;

    public PaletteController(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    @Override
    public Observable<Palette> execute() {
        return Observable.create(new Observable.OnSubscribe<Palette>() {
            @Override
            public void call(Subscriber<? super Palette> subscriber) {
                Palette palette = Palette.from(mBitmap).generate();
                subscriber.onNext(palette);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

