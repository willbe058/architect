
package com.xpf.me.architect.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.xpf.me.architect.app.AppData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImageUtils {
    private static final int MAX_TEXTURE_SIZE = getOpengl2MaxTextureSize();

    public static int getOpengl2MaxTextureSize() {
        int[] maxTextureSize = new int[1];
        maxTextureSize[0] = 2048;
        android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_TEXTURE_SIZE,
                maxTextureSize, 0);
        return maxTextureSize[0];
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height(inMutable)
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight) {
        return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight, false);
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param isMutable 可编辑
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight, boolean isMutable) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if (isMutable && Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }
        Bitmap result = BitmapFactory.decodeResource(res, resId, options);
        if (isMutable) {
            result = createMutableBitmap(result);
        }
        return result;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int sampledSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = sampledSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     *
     * @param filePath  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height(inmutable)
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight, false, true);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     *
     * @param filePath  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param isMutable 可编辑
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight,
                                                     boolean isMutable, boolean regionDecode) {
        if (filePath == null) {
            return null;
        }
        if (reqHeight == 0) {
            reqHeight = MAX_TEXTURE_SIZE;
        }
        if (reqWidth == 0) {
            reqWidth = MAX_TEXTURE_SIZE;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if (isMutable && Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }

        Bitmap result = null;

        if ((options.outWidth > MAX_TEXTURE_SIZE || options.outHeight > MAX_TEXTURE_SIZE || (options.outHeight >= options.outWidth * 3))
                && regionDecode) {
            // 长图
            try {
                result = regionDecode(filePath, reqWidth, reqHeight, options.outWidth,
                        options.outHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result = BitmapFactory.decodeFile(filePath, options);
        }

        if (isMutable) {
            result = createMutableBitmap(result);
        }

        return result;
    }

    private static Bitmap regionDecode(String path, int reqWidth, int reqHeight, int outWidth,
                                       int outHeight) throws IOException {
        BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(path, true);
        if (reqWidth > outWidth) {
            reqWidth = outWidth;
        }
        if (reqHeight > outHeight) {
            reqHeight = outHeight;
        }

        return regionDecoder.decodeRegion(new Rect(0, 0, reqWidth, reqHeight), null);
    }

    private static void addInBitmapOptions(BitmapFactory.Options options,
                                           Set<SoftReference<Bitmap>> reuseableBitmaps) {
        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;

        // Try to find a bitmap to use for inBitmap.
        Bitmap inBitmap = getBitmapFromReusableSet(reuseableBitmaps, options);

        if (inBitmap != null) {
            // If a suitable bitmap has been found, set it as the value of
            // inBitmap.
            options.inBitmap = inBitmap;
        }
    }

    private static boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size
            // of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            int width = targetOptions.outWidth / targetOptions.inSampleSize;
            int height = targetOptions.outHeight / targetOptions.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
            return byteCount <= candidate.getAllocationByteCount();
        }

        // On earlier versions, the dimensions must match exactly and the
        // inSampleSize must be 1
        return candidate.getWidth() == targetOptions.outWidth
                && candidate.getHeight() == targetOptions.outHeight
                && targetOptions.inSampleSize == 1;
    }

    // This method iterates through the reusable bitmaps, looking for one
    // to use for inBitmap:
    private static Bitmap getBitmapFromReusableSet(Set<SoftReference<Bitmap>> reusableBitmaps,
                                                   BitmapFactory.Options options) {
        Bitmap bitmap = null;

        if (reusableBitmaps != null && !reusableBitmaps.isEmpty()) {
            synchronized (reusableBitmaps) {
                final Iterator<SoftReference<Bitmap>> iterator = reusableBitmaps.iterator();
                Bitmap item;

                while (iterator.hasNext()) {
                    item = iterator.next().get();

                    if (null != item && item.isMutable()) {
                        // Check to see it the item can be used for inBitmap.
                        if (canUseForInBitmap(item, options)) {
                            bitmap = item;

                            // Remove from reusable set so it can't be used
                            // again.
                            iterator.remove();
                            break;
                        }
                    } else {
                        // Remove from the set if the reference has been
                        // cleared.
                        iterator.remove();
                    }
                }
            }
        }
        return bitmap;
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on
     * its configuration.
     */
    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromByteArray(data, reqWidth, reqHeight, null);
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight,
                                                          HashSet<SoftReference<Bitmap>> reusableBitmapSet) {
        if (data == null) {
            return null;
        }

        if (reqHeight == 0) {
            reqHeight = MAX_TEXTURE_SIZE;
        }
        if (reqWidth == 0) {
            reqWidth = MAX_TEXTURE_SIZE;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if (Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;

            if (reusableBitmapSet != null) {
                addInBitmapOptions(options, reusableBitmapSet);
            }
        }

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeScaledBitmapFromResource(Resources resources, int resId,
                                                        int reqWidth, int reqHeight, boolean mutable) {
        Bitmap raw = decodeSampledBitmapFromResource(resources, resId, reqWidth, reqHeight, mutable);
        if (null == raw) {
            return null;
        }
        if (reqWidth == raw.getWidth() && reqHeight == raw.getHeight()) {
            return raw;
        }

        Bitmap scaled = Bitmap.createScaledBitmap(raw, reqWidth, reqHeight, false);
        if (scaled != raw) {
            raw.recycle();
        }
        return scaled;
    }

    private static Bitmap regionDecode(byte[] data, int reqWidth, int reqHeight, int outWidth,
                                       int outHeight) throws IOException {
        BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(data, 0, data.length,
                true);
        if (reqWidth > outWidth) {
            reqWidth = outWidth;
        }
        if (reqHeight > outHeight) {
            reqHeight = outHeight;
        }

        return regionDecoder.decodeRegion(new Rect(0, 0, reqWidth, reqHeight), null);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int widthSampleSize = 0;
            int heightSampleSize = 0;
            if (reqWidth < width) {
                widthSampleSize = Math.round((float) width / (float) reqWidth);
            }
            if (reqHeight < height) {
                heightSampleSize = Math.round((float) height / (float) reqHeight);
            }
            inSampleSize = Math.max(widthSampleSize, heightSampleSize);
        }
        return inSampleSize;
    }

    /**
     * 通过srcbitmap 创建一个可编辑的bitmap
     *
     * @param src
     * @return
     */
    public static Bitmap createMutableBitmap(Bitmap src) {
        Bitmap result = null;
        if (src == null) {
            return null;
        }
        result = src.copy(Bitmap.Config.ARGB_8888, true);

        return result;
    }

    /**
     * 将subBmp图像合并到oriBmp中
     *
     * @param oriBmp
     * @param subBmp
     * @param oriRect subBmp中取出的bitmap需要填充到oriRect中的区域
     * @param subRect 从subBmp中取出的区域
     * @param paint
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp, final Rect oriRect,
                                     final Rect subRect) {
        if (subBmp == null) {
            return oriBmp;
        }

        if (oriBmp == null) {
            return null;
        }

        if (!oriBmp.isMutable()) {
            oriBmp = createMutableBitmap(oriBmp);
        }

        Canvas canvas = new Canvas(oriBmp);
        canvas.drawBitmap(subBmp, subRect, oriRect, null);
        return oriBmp;
    }

    /**
     * 将subBmp图像合并到oriBmp中
     *
     * @param oriBmp
     * @param subBmp
     * @return oriBmp
     */
    public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp) {
        if (subBmp == null) {
            return oriBmp;
        }

        if (oriBmp == null) {
            return null;
        }

        return mergeBitmap(oriBmp, subBmp, new Rect(0, 0, oriBmp.getWidth(), oriBmp.getHeight()),
                new Rect(0, 0, subBmp.getWidth(), subBmp.getHeight()));
    }

    private static final PorterDuffXfermode SRC_IN_MODE = new PorterDuffXfermode(
            PorterDuff.Mode.SRC_IN);

    private final static Paint SRC_IN_PAINT = new Paint();

    static {
        SRC_IN_PAINT.setXfermode(SRC_IN_MODE);
    }

    public static Bitmap convertToAlphaMask(Bitmap b) {
        Bitmap a = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(a);
        c.drawBitmap(b, 0.0f, 0.0f, null);
        return a;
    }

    public static Bitmap decodeBitmapFromDrawableRes(int resId, final int width, final int height) {
        Drawable drawable = AppData.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap decodeBitmapFromDrawable(Drawable drawable, final int width,
                                                  final int height) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromView(View v, boolean antiPadding) {
        Bitmap raw = getBitmapFromView(v);
        if (!antiPadding) {
            return raw;
        }

        final int w = v.getWidth() - (v.getPaddingLeft() + v.getPaddingRight());
        final int h = v.getHeight() - (v.getPaddingTop() + v.getPaddingBottom());
        if (w <= 0 || h <= 0) {
            return raw;
        }

        Bitmap cropped = Bitmap.createBitmap(raw, v.getPaddingLeft(), v.getPaddingTop(), w, h);
        raw.recycle();
        return cropped;
    }

    public static Bitmap getBitmapFromView(View v, boolean antiPadding, int[] innerPaddings) {
        Bitmap raw = getBitmapFromView(v, antiPadding);
        if (innerPaddings != null && innerPaddings.length == 4) {
            final int paddingLeft = innerPaddings[0];
            final int paddingTop = innerPaddings[1];
            final int paddingRight = innerPaddings[2];
            final int paddingBottom = innerPaddings[3];
            final int w = raw.getWidth() - (paddingLeft + paddingRight);
            final int h = raw.getHeight() - (paddingTop + paddingBottom);

            if (w <= 0 || h <= 0) {
                return raw;
            }

            Bitmap cropped = Bitmap.createBitmap(raw, paddingLeft, paddingTop, w, h);
            raw.recycle();
            return cropped;
        }
        return raw;
    }

    public static boolean writeBitmapToFile(Bitmap src, String path) {
        File file = new File(path);
        FileOutputStream fOut = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fOut = new FileOutputStream(file);
            src.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            return true;
        } catch (IOException e) {
            file.delete();
            return false;
        } finally {
            IOUtils.closeSilently(fOut);
        }
    }

    /**
     * 遮罩图片
     *
     * @param dstBmp
     * @param mask
     * @param paint
     * @return 遮罩后的图片
     */
    public static Bitmap maskBitmap(final Bitmap dstBmp, final Bitmap mask) {
        if (dstBmp == null || mask == null) {
            return dstBmp;
        }
        Bitmap result = Bitmap.createBitmap(dstBmp.getWidth(), dstBmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(mask, new Rect(0, 0, mask.getWidth(), mask.getHeight()), new Rect(0, 0,
                dstBmp.getWidth(), dstBmp.getHeight()), null);
        canvas.drawBitmap(dstBmp, 0, 0, SRC_IN_PAINT);
        return result;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotate);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                false);
    }

    public static Bitmap roundBitmap(Bitmap src) {
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        paint.setColor(Color.BLUE);
        canvas.drawCircle(src.getWidth() / 2f, src.getHeight() / 2f, src.getWidth() / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        return result;
    }
}
