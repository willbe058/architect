package com.xpf.me.architect.recyclerview;

import java.util.HashMap;

/**
 * Created by xgo on 11/15/15.
 */
public class ColorCache {

    private int mBackColor;

    private int mTitleColor;

    private HashMap<Integer, int[]> map = new HashMap<>();

    private static ColorCache ourInstance = new ColorCache();

    public static ColorCache getInstance() {
        return ourInstance;
    }

    private ColorCache() {
    }

    public void storeColors(int backColor, int titleColor) {
        this.mBackColor = backColor;
        this.mTitleColor = titleColor;
    }

    public int[] getColors() {
        int[] colors = new int[2];
        colors[0] = mBackColor;
        colors[1] = mTitleColor;
        return colors;
    }
}
