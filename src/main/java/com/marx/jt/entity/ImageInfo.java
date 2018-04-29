package com.marx.jt.entity;

import java.nio.ByteBuffer;

/**
 * Created by yuyg on 2018/4/22.
 */
public class ImageInfo {
    Size size;
    ByteBuffer paramset;
    int offset;
    int length;

    public Size getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public ByteBuffer getParamset() {
        return paramset;
    }

    public void setParamset(ByteBuffer paramset) {
        this.paramset = paramset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
