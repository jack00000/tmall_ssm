package com.edu.tmall.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by taffy on 17/11/27.
 */
public class UploadImageFile {
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "UploadImageFile{" +
                "image=" + image +
                '}';
    }
}
