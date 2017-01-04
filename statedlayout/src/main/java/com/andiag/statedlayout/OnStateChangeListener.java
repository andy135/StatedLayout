package com.andiag.statedlayout;

/**
 * Created by Canalejas on 03/01/2017.
 */
public interface OnStateChangeListener {
    void onLoading();

    void onError();

    void onContent();

    void onEmpty();
}
