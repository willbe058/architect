package com.xpf.me.architect.recyclerview;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.xpf.me.architect.R;
import com.xpf.me.architect.app.AppData;
import com.xpf.me.architect.utils.CacheUtils;
import com.xpf.me.architect.utils.ImageUtils;

import java.io.File;

/**
 * Created by xgo on 11/15/15.
 */
public class RequestManager {

    private static RequestQueue requestQueue = newRequestQueue();

    // 取运行内存阈值的1/3作为图片缓存
    private static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) AppData.getContext()
            .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 3;

    private static ImageLoader mImageLoader = new ImageLoader(requestQueue, new BitmapLruCache(
            MEM_CACHE_SIZE));

    private static DiskBasedCache mDiskCache = (DiskBasedCache) requestQueue.getCache();

    public static RequestQueue newRequestQueue() {
        RequestQueue requestQueue = new RequestQueue(openCache(), new BasicNetwork(new HurlStack()));
        requestQueue.start();
        return requestQueue;
    }

    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        requestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        requestQueue.cancelAll(tag);
    }

    private static Cache openCache() {
        return new DiskBasedCache(CacheUtils.getExternalCacheDir(AppData.getContext()),
                10 * 1024 * 1024);
    }

    public static File getCachedImageFile(String url) {
        return mDiskCache.getFileForKey(url);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
                                                       ImageLoader.ImageListener imageListener) {
        return loadImage(requestUrl, imageListener, 0, 0);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
                                                       ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight);
    }

    public static ImageLoader.ImageListener getImageListener(final ImageView view, final TextView title,
                                                             final Drawable defaultImageDrawable, final Drawable errorImageDrawable,
                                                             final boolean isBusy) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    view.setImageDrawable(errorImageDrawable);
                    title.setBackgroundColor(AppData.getColor(R.color.default_color));
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                final int[] backColor = new int[1];
                final int[] titleColor = new int[1];
                boolean isFirst = true;

                if (response.getBitmap() != null) {

                    if (isImmediate) {
                        view.setImageBitmap(response.getBitmap());
//                        title.setBackgroundColor(backColor[0]);
//                        title.setTextColor(titleColor[0]);
                    } else {
                        if (!isBusy && defaultImageDrawable != null) {
                            TransitionDrawable transitionDrawable = new TransitionDrawable(
                                    new Drawable[]{
                                            defaultImageDrawable,
                                            new BitmapDrawable(AppData.getResources(), response
                                                    .getBitmap())
                                    });
                            transitionDrawable.setCrossFadeEnabled(true);
                            view.setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(200);

//                            new PaletteController(response.getBitmap()).execute()
//                                    .subscribe(new Subscriber<Palette>() {
//                                        @Override
//                                        public void onCompleted() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//
//                                        }
//
//                                        @Override
//                                        public void onNext(Palette palette) {
//                                            Palette.Swatch vibrant = palette.getVibrantSwatch();
//                                            if (vibrant != null) {
//                                                backColor[0] = vibrant.getRgb();
//                                                titleColor[0] = vibrant.getTitleTextColor();
//                                                title.setBackgroundColor(vibrant.getRgb());
//                                                title.setTextColor(vibrant.getTitleTextColor());
//                                            } else {
//                                                backColor[0] = AppData.getColor(R.color.default_color);
//                                                titleColor[0] = AppData.getColor(android.R.color.white);
//                                                title.setBackgroundColor(AppData.getColor(R.color.default_color));
//                                                title.setTextColor(AppData.getColor(android.R.color.white));
//                                            }
//                                        }
//                                    });
                        } else {
                            view.setImageDrawable(defaultImageDrawable);
                        }
                    }
                } else if (defaultImageDrawable != null) {
                    view.setImageDrawable(defaultImageDrawable);
                }
            }
        };
    }

    public static ImageLoader.ImageListener getRoundImageListener(final ImageView view,
                                                                  final Drawable defaultImageDrawable, final Drawable errorImageDrawable,
                                                                  final boolean isBusy) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    view.setImageDrawable(errorImageDrawable);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Bitmap bitmap = ImageUtils.roundBitmap(response.getBitmap());
                    if (isImmediate) {
                        view.setImageBitmap(bitmap);
                    } else {
                        if (!isBusy && defaultImageDrawable != null) {
                            TransitionDrawable transitionDrawable = new TransitionDrawable(
                                    new Drawable[]{
                                            defaultImageDrawable,
                                            new BitmapDrawable(AppData.getResources(), bitmap)
                                    });
                            transitionDrawable.setCrossFadeEnabled(true);
                            view.setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(100);
                        } else {
                            view.setImageDrawable(defaultImageDrawable);
                        }
                    }
                } else if (defaultImageDrawable != null) {
                    view.setImageDrawable(defaultImageDrawable);
                }
            }
        };
    }


}
