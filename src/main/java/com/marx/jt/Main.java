package com.marx.jt;

import com.marx.jt.entity.ImageSimpleMeta;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception {
        String gifFile = "/Users/yuyg/Downloads/C003.HEIC";
        InputStream in = new FileInputStream(gifFile);
        byte[] imgData = IOUtils.toByteArray(in);
        ImageSimpleMeta imageInfo = HeicMetaReader.decodeByteArray(imgData);
        System.out.println(imageInfo);

    }
}
