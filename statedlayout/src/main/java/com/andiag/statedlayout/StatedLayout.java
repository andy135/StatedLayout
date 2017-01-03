package com.andiag.statedlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by andyq on 28/12/2016.
 */

public class StatedLayout extends RelativeLayout {
    //States
    public static final int STATE_CONTENT = 10, STATE_LOADING = 20, STATE_ERROR = 30, STATE_EMPTY = 40;
    private int actualState = STATE_LOADING;

    //Views
    private ImageView image;
    private TextView text;
    private Button button;
    private View content;

    //Customization
    private int textSize, tintColor, textColor;
    @StringRes
    private int labelEmpty, labelLoading, labelError;
    @DrawableRes
    private int imageEmpty, imageLoading, imageError;
    private boolean alternateIcons;

    //Callback
    private StateCallbackListener stateCallbackListener;

    //region Constuctors
    public StatedLayout(Context context) {
        super(context);
        initViews(context, null, 0);
    }

    public StatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs, 0);
    }

    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
    }
    //endregion

    //region Customization/FirstState
    private void initViews(Context context, AttributeSet attrs, int defStyle) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatedLayout, defStyle, 0);

        try {
            labelEmpty = array.getResourceId(R.styleable.StatedLayout_emptyLabel, R.string.default_empty);
            labelLoading = array.getResourceId(R.styleable.StatedLayout_loadingLabel, R.string.default_loading);
            labelError = array.getResourceId(R.styleable.StatedLayout_errorLabel, R.string.default_error);

            imageEmpty = array.getResourceId(R.styleable.StatedLayout_emptyImage, R.drawable.stated_empty);
            imageLoading = array.getResourceId(R.styleable.StatedLayout_loadingImage, R.drawable.stated_loading);
            imageError = array.getResourceId(R.styleable.StatedLayout_errorImage, R.drawable.stated_error);

            alternateIcons = array.getBoolean(R.styleable.StatedLayout_alternateIcons, false);

            tintColor = array.getColor(R.styleable.StatedLayout_android_tint, getColor(context, R.attr.colorAccent));
            textColor = array.getColor(R.styleable.StatedLayout_android_textColor, getColor(context, android.R.attr.textColorSecondary));

            textSize = array.getDimensionPixelSize(R.styleable.StatedLayout_android_textSize, 18);
        } finally {
            array.recycle();
        }

        setAlternateIcons(alternateIcons);

        View view = LayoutInflater.from(context).inflate(R.layout.statedlayout_base, this);

        image = (ImageView) view.findViewById(R.id.statelayout_image);
        image.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
        text = (TextView) view.findViewById(R.id.statelayout_text);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        text.setTextColor(textColor);
        button = (Button) view.findViewById(R.id.statelayout_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateCallbackListener != null) {
                    setLoading();
                    button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stateCallbackListener.onRetryClick();
                        }
                    }, 100);
                }
            }
        });
    }

    private int getColor(Context context, int colorAttr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{colorAttr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLoading();
    }
    //endregion

    //region Add Child Override
    private void allowOnlyOneChild(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatedLayout cannot have more than 1 child");
        }
        content = child;
    }

    @Override
    public void addView(View child) {
        allowOnlyOneChild(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        allowOnlyOneChild(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        allowOnlyOneChild(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        allowOnlyOneChild(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        allowOnlyOneChild(child);
        super.addView(child, width, height);
    }
    //endregion

    //region State Switching
    public void setEmpty() {
        actualState = STATE_EMPTY;
        text.setText(labelEmpty);
        image.setImageResource(imageEmpty);
        updateVisibleState(true, true, false, false);
        notifyCallbackListener();
    }

    public void setLoading() {
        actualState = STATE_LOADING;
        text.setText(labelLoading);
        image.setImageResource(imageLoading);
        updateVisibleState(true, true, false, false);
        notifyCallbackListener();
    }

    public void setError() {
        actualState = STATE_ERROR;
        text.setText(labelError);
        image.setImageResource(imageError);
        button.setText(R.string.retry);
        updateVisibleState(true, true, true, false);
        notifyCallbackListener();
    }

    public void setContent() {
        actualState = STATE_CONTENT;
        updateVisibleState(false, false, false, true);
        notifyCallbackListener();
    }
    //endregion

    //region Visibility
    private void updateVisibleState(boolean txt, boolean img, boolean btn, boolean cnt) {
        setVisible(text, txt);
        setVisible(image, img);
        setVisible(button, btn);
        setVisible(content, cnt);
    }

    private void setVisible(View view, boolean wanttosee) {
        if (view.isShown() && !wanttosee) {
            view.setVisibility(View.GONE);
        } else if (!view.isShown() && wanttosee) {
            view.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    //region Public Methods
    public void setStateCallbackListener(StateCallbackListener stateCallbackListener) {
        this.stateCallbackListener = stateCallbackListener;
    }

    public int getState() {
        return actualState;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        text.setTextColor(textColor);
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        image.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
    }

    public void setAlternateIcons(boolean alternateIcons) {
        this.alternateIcons = alternateIcons;
        if (alternateIcons) {
            this.imageEmpty = R.drawable.stated_empty_;
            this.imageLoading = R.drawable.stated_loading_;
            this.imageError = R.drawable.stated_error_;
        }
    }
    //endregion

    //region Callbacks
    public void notifyCallbackListener() {
        if (stateCallbackListener != null) {
            switch (actualState) {
                case STATE_EMPTY:
                    stateCallbackListener.onStateEmpty();
                    break;
                case STATE_ERROR:
                    stateCallbackListener.onStateError();
                    break;
                case STATE_LOADING:
                    stateCallbackListener.onStateLoading();
                    break;
                case STATE_CONTENT:
                    stateCallbackListener.onStateContent();
                    break;
            }
        }
    }

    public interface StateCallbackListener {
        void onRetryClick();

        void onStateLoading();

        void onStateError();

        void onStateContent();

        void onStateEmpty();
    }
    //endregion

}
