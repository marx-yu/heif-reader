package com.marx.jt;

import com.marx.jt.entity.ImageSimpleMeta;
import com.marx.jt.iso14496.part12.ItemPropertiesBox;
import com.marx.jt.iso14496.part12.ItemPropertyAssociation;
import com.marx.jt.iso14496.part12.ItemPropertyContainerBox;
import com.marx.jt.iso23008.part12.ImageRotationBox;
import com.marx.jt.iso23008.part12.ImageSpatialExtentsBox;
import com.marx.jt.iso23008.part12.PixelInformationBox;
import com.marx.jt.utils.OnlyMetaBoxParser;
import com.marx.jt.iso14496.part12.PrimaryItemBox;
import org.mp4parser.BasicContainer;
import org.mp4parser.Box;
import org.mp4parser.boxes.iso14496.part12.FileTypeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyg on 2018/4/22.
 */
public class HeicMetaReader {
    private static final Logger logger = LoggerFactory.getLogger(HeicMetaReader.class);

    public static ImageSimpleMeta decodeByteArray(byte[] data) {
        try {
            OnlyMetaBoxParser parser = new OnlyMetaBoxParser(data);
            return parseImageMeta(parser);
        } catch (IOException e) {
            logger.info("decode byte array exception: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static ImageSimpleMeta decodeFile(File file) {
        try {
            OnlyMetaBoxParser parser = new OnlyMetaBoxParser(file);
            return parseImageMeta(parser);
        } catch (IOException e) {
            logger.info("decode file exception: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static ImageSimpleMeta decodeInputStream(InputStream in) {
        try {
            OnlyMetaBoxParser parser = new OnlyMetaBoxParser(in);
            return parseImageMeta(parser);
        } catch (IOException e) {
            logger.info("decode input stream exception: " + e.getLocalizedMessage());
        }
        return null;
    }


    private static ImageSimpleMeta parseImageMeta(BasicContainer basicContainer) throws IOException {
        // validate brand compatibility ('ftyp' box)
        List<FileTypeBox> ftypBoxes = basicContainer.getBoxes(FileTypeBox.class);
        if (ftypBoxes.size() != 1) {
            throw new IOException("FileTypeBox('ftyp') shall be unique");
        }
        FileTypeBox ftypBox = ftypBoxes.get(0);
        if (!"mif1".equals(ftypBox.getMajorBrand()) && !"heic".equals(ftypBox.getMajorBrand())) {
            throw new IOException("unsupported FileTypeBox('ftyp') brands: " + ftypBox.getMajorBrand());
        }

        // get primary item_ID
        List<PrimaryItemBox> pitmBoxes = basicContainer.getBoxes(PrimaryItemBox.class, true);
        if (pitmBoxes.isEmpty()) {
            throw new IOException("PrimaryItemBox('pitm') not found");
        }
        PrimaryItemBox pitmBox = pitmBoxes.get(0);
        pitmBox.parseDetails();
        logger.debug("HEIC primary item_ID=" + pitmBox.getItemId());

        // get associative item properties
        ItemPropertiesBox iprpBox = basicContainer.getBoxes(ItemPropertiesBox.class, true).get(0);
        ItemPropertyAssociation ipmaBox = iprpBox.getBoxes(ItemPropertyAssociation.class).get(0);
        ItemPropertyContainerBox ipcoBox = iprpBox.getBoxes(ItemPropertyContainerBox.class).get(0);
        List<Box> primaryPropBoxes = new ArrayList<>();
        for (ItemPropertyAssociation.Item item : ipmaBox.getItems()) {
            if (item.item_ID == pitmBox.getItemId()) {
                for (ItemPropertyAssociation.Assoc assoc : item.associations) {
                    primaryPropBoxes.add(ipcoBox.getBoxes().get(assoc.property_index - 1));
                }
            }
        }

        // get image size
        ImageSimpleMeta simpleMeta = new ImageSimpleMeta();
        ImageSpatialExtentsBox ispeBox = findBox(primaryPropBoxes, ImageSpatialExtentsBox.class);
        if (ispeBox == null) {
            throw new IOException("ImageSpatialExtentsBox('ispe') not found");
        }
        simpleMeta.setWidth((int) ispeBox.display_width);
        simpleMeta.setHeight((int) ispeBox.display_height);

        ImageRotationBox irBox = findBox(primaryPropBoxes, ImageRotationBox.class);
        if (irBox != null) {
            logger.debug("image orientation: " + irBox.getOrientation());
            simpleMeta.setOrientation(irBox.getOrientation());
        }

        PixelInformationBox piBox = findBox(primaryPropBoxes, PixelInformationBox.class);
        if (piBox != null) {
            logger.debug("image pixel bits num: " + piBox.numChannels);
            simpleMeta.setNumChannels(piBox.numChannels);
            simpleMeta.setBitsPerChannel(piBox.bitsPerChannel);
        }

        return simpleMeta;
    }


    private static <T extends Box> T findBox(List<Box> container, Class<T> clazz) {
        for (Box box : container) {
            if (clazz.isInstance(box)) {
                return (T) box;
            }
        }
        return null;
    }

}
