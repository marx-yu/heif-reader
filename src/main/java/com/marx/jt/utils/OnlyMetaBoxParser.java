package com.marx.jt.utils;

import org.mp4parser.BasicContainer;
import org.mp4parser.BoxParser;
import org.mp4parser.ParsableBox;
import org.mp4parser.PropertyBoxParserImpl;
import org.mp4parser.boxes.iso14496.part12.FileTypeBox;
import org.mp4parser.boxes.iso14496.part12.MetaBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by yuyg on 2018/4/22.
 */
public class OnlyMetaBoxParser extends BasicContainer {
    private static final Logger logger = LoggerFactory.getLogger(OnlyMetaBoxParser.class);

    public OnlyMetaBoxParser(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        this.initContainer(Channels.newChannel(bais), -1L, new PropertyBoxParserImpl(new String[0]));
    }

    public OnlyMetaBoxParser(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        this.initContainer(fin.getChannel(), -1L, new PropertyBoxParserImpl(new String[0]));
    }

    public OnlyMetaBoxParser(InputStream in) throws IOException {
        this.initContainer(Channels.newChannel(in), -1L, new PropertyBoxParserImpl(new String[0]));
    }

    @Override
    public void initContainer(ReadableByteChannel readableByteChannel, long containerSize, BoxParser boxParser) throws IOException {
        long contentProcessed = 0L;
        boolean ftypeBoxFound = false;
        boolean metaBoxFound = false;
        while(containerSize < 0L || contentProcessed < containerSize) {
            try {
                ParsableBox b = boxParser.parseBox(readableByteChannel, this instanceof ParsableBox ? ((ParsableBox)this).getType() : null);
                this.getBoxes().add(b);
                contentProcessed += b.getSize();
                if (b instanceof FileTypeBox) {
                    ftypeBoxFound = true;
                } else if (b instanceof MetaBox) {
                    metaBoxFound = true;
                }
                if (ftypeBoxFound && metaBoxFound) {
                    // only need meta
                    return;
                }
            } catch (EOFException e) {
                if (containerSize < 0L) {
                    return;
                }

                throw e;
            }
        }

    }

}
