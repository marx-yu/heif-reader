package com.marx.jt.iso23008.part12;

import org.mp4parser.support.AbstractBox;
import org.mp4parser.tools.IsoTypeReader;

import java.nio.ByteBuffer;

/**
 * Created by yuyg on 2018/4/20.
 */
public class ImageRotationBox  extends AbstractBox {
    public static final String TYPE = "irot";

    /** Rotation angle in anti-clockwise direction (0, 90, 180 and 270 degrees). */
    private int rotation;
    /** orientation of exif, (1, 8, 3, 6) */
    private int orientation;

    public ImageRotationBox() {
        super(TYPE);
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        int r = IsoTypeReader.readUInt8(content);
        r &= 0x3;    //  read 2 bits
        rotation = r * 90;
        switch (r) {
            case 0:
                orientation = 1;
                break;
            case 1:
                orientation = 8;
                break;
            case 2:
                orientation = 3;
                break;
            case 3:
                orientation = 6;
                break;
            default:
                  orientation = 1;
        }

    }

    @Override
    public long getContentSize() {
        return 1;
    }

    @Override
    public void getContent(ByteBuffer byteBuffer) {
        throw new RuntimeException(TYPE + " not implemented");
    }

    @Override
    public String toString() {
        return "ImageRotationBox[" + rotation + "]";
    }

    public int getRotation() {
        return rotation;
    }

    public int getOrientation() {
        return orientation;
    }
}
