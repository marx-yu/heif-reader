package com.marx.jt.iso23008.part12;

import org.mp4parser.support.AbstractFullBox;
import org.mp4parser.tools.IsoTypeReader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyg on 2018/4/20.
 */
public class PixelInformationBox extends AbstractFullBox {
    public static final String TYPE = "pixi";

    public int numChannels;
    public List<Short> bitsPerChannel;

    public PixelInformationBox() {
        super(TYPE);
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        numChannels = IsoTypeReader.readUInt8(content);
        bitsPerChannel = new ArrayList<>(numChannels);
        for (int i = 0; i < numChannels; i++) {
            Short bits = (short) IsoTypeReader.readUInt8(content);
            bitsPerChannel.add(bits);
        }
    }

    @Override
    public long getContentSize() {
        return numChannels + 1 + 4;
    }

    @Override
    public void getContent(ByteBuffer byteBuffer) {
        throw new RuntimeException(TYPE + " not implemented");
    }

    @Override
    public String toString() {
        return "PixelInformationBox[" + numChannels + "]";
    }
}
