package com.andiag.statedlayout;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by andyq on 06/01/2017.
 */

class SavedState extends View.BaseSavedState {

    public static final Parcelable.Creator<SavedState> CREATOR
            = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
    String value; //this will store the current value from ValueBar

    SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(value);
    }
}
