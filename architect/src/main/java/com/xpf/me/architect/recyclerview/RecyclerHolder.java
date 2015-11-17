package com.xpf.me.architect.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by xgo on 11/15/15.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;

    public ImageLoader.ImageContainer mImageRequest;

    public boolean loadFinish;

    public boolean animated = false;

    public RecyclerHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>(8);
    }

    public SparseArray<View> getAllViews() {
        return mViews;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public RecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
}
