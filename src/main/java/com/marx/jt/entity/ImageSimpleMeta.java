package com.marx.jt.entity;

import java.util.List;

/**
 * Created by yuyg on 2018/4/22.
 */
public class ImageSimpleMeta {
    private int width;
    private int height;
    /** orientation of exif, (1, 8, 3, 6) */
    private int orientation = 1;

    /** The number and bit depth of colour components in the image item. */
    public int numChannels;
    public List<Short> bitsPerChannel;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getNumChannels() {
        return numChannels;
    }

    public void setNumChannels(int numChannels) {
        this.numChannels = numChannels;
    }

    public List<Short> getBitsPerChannel() {
        return bitsPerChannel;
    }

    public void setBitsPerChannel(List<Short> bitsPerChannel) {
        this.bitsPerChannel = bitsPerChannel;
    }

    @Override
    public String toString() {
        return "SimpleMeta(width=" + width + ", height=" + height + ", orie=" + orientation + ")";
    }
}
