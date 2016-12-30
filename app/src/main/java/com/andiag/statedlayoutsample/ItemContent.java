package com.andiag.statedlayoutsample;

import android.support.annotation.DrawableRes;

/**
 * Created by andyq on 30/12/2016.
 */

public class ItemContent {

    @DrawableRes
    private int image;

    private String text;

    public ItemContent() {
        super();
    }

    public ItemContent(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
