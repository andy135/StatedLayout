package com.andiag.statedlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Parcelable;
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
    private State mActualState = State.STATE_LOADING;

    //Views
    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;
    private View mContent;

    //Customization
    private int mTintColor, mTextColor, mImageSize;
    private float mTextSize;
    @StringRes
    private int mLabelEmpty, mLabelLoading, mLabelError;
    @DrawableRes
    private int mImageEmpty, mImageLoading, mImageError;
    private boolean mAlternateIcons;

    //Callback
    private OnRetryListener mOnRetryListener;
    private OnStateChangeListener mOnStateChangeListener;

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

    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr, OnStateChangeListener onStateChangeListener) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs, defStyleAttr);
        this.mOnStateChangeListener = onStateChangeListener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, OnStateChangeListener onStateChangeListener) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
        this.mOnStateChangeListener = onStateChangeListener;
    }

    //endregion

    //region Customization/FirstState
    private void initViews(Context context, AttributeSet attrs, int defStyle) {
        setSaveEnabled(true);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatedLayout, defStyle, 0);

        try {
            mLabelEmpty = array.getResourceId(R.styleable.StatedLayout_emptyLabel, R.string.default_empty);
            mLabelLoading = array.getResourceId(R.styleable.StatedLayout_loadingLabel, R.string.default_loading);
            mLabelError = array.getResourceId(R.styleable.StatedLayout_errorLabel, R.string.default_error);

            mImageEmpty = array.getResourceId(R.styleable.StatedLayout_emptyImage, R.drawable.stated_empty);
            mImageLoading = array.getResourceId(R.styleable.StatedLayout_loadingImage, R.drawable.stated_loading);
            mImageError = array.getResourceId(R.styleable.StatedLayout_errorImage, R.drawable.stated_error);

            mAlternateIcons = array.getBoolean(R.styleable.StatedLayout_alternateIcons, false);

            mTintColor = array.getColor(R.styleable.StatedLayout_android_tint, getColor(context, R.attr.colorAccent));
            mTextColor = array.getColor(R.styleable.StatedLayout_android_textColor, getColor(context, android.R.attr.textColorSecondary));

            mTextSize = array.getDimension(R.styleable.StatedLayout_android_textSize, 18);
            mImageSize = array.getDimensionPixelSize(R.styleable.StatedLayout_imageSize, 0);
        } finally {
            array.recycle();
        }

        setAlternateIcons(mAlternateIcons);

        View view = LayoutInflater.from(context).inflate(R.layout.statedlayout_base, this);

        mImageView = (ImageView) view.findViewById(R.id.statelayout_image);
        mImageView.setColorFilter(mTintColor, PorterDuff.Mode.MULTIPLY);
        if (mImageSize != 0) {
            LayoutParams layoutParams = (LayoutParams) mImageView.getLayoutParams();
            layoutParams.width = mImageSize;
            layoutParams.height = mImageSize;
            mImageView.setLayoutParams(layoutParams);
        }
        mTextView = (TextView) view.findViewById(R.id.statelayout_text);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        mTextView.setTextColor(mTextColor);
        mButton = (Button) view.findViewById(R.id.statelayout_button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    setLoading();
                    mButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOnRetryListener.onRetryClick();
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
        recoverState();
    }
    //endregion

    //region Add Child Override
    private void allowOnlyOneChild(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatedLayout cannot have more than 1 child");
        }
        mContent = child;
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
        mActualState = State.STATE_EMPTY;
        mTextView.setText(mLabelEmpty);
        mImageView.setImageResource(mImageEmpty);
        updateVisibleState(true, true, false, false);
        notifyCallbackListener();
    }

    public void setLoading() {
        mActualState = State.STATE_LOADING;
        mTextView.setText(mLabelLoading);
        mImageView.setImageResource(mImageLoading);
        updateVisibleState(true, true, false, false);
        notifyCallbackListener();
    }

    public void setError() {
        mActualState = State.STATE_ERROR;
        mTextView.setText(mLabelError);
        mImageView.setImageResource(mImageError);
        mButton.setText(R.string.retry);
        updateVisibleState(true, true, true, false);
        notifyCallbackListener();
    }

    public void setContent() {
        mActualState = State.STATE_CONTENT;
        updateVisibleState(false, false, false, true);
        notifyCallbackListener();
    }

    private void recoverState() {
        switch (mActualState) {
            case STATE_EMPTY:
                setEmpty();
                break;
            case STATE_ERROR:
                setError();
                break;
            case STATE_LOADING:
                setLoading();
                break;
            case STATE_CONTENT:
                setContent();
                break;
        }
    }
    //endregion

    //region Visibility
    private void updateVisibleState(boolean txt, boolean img, boolean btn, boolean cnt) {
        setVisible(mTextView, txt);
        setVisible(mImageView, img);
        setVisible(mButton, btn);
        setVisible(mContent, cnt);
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
    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.mOnRetryListener = onRetryListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public State getState() {
        return mActualState;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextView.setTextColor(textColor);
    }

    public void setTintColor(int tintColor) {
        this.mTintColor = tintColor;
        mImageView.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
    }

    public void setAlternateIcons(boolean alternateIcons) {
        this.mAlternateIcons = alternateIcons;
        if (alternateIcons) {
            this.mImageEmpty = R.drawable.stated_empty_;
            this.mImageLoading = R.drawable.stated_loading_;
            this.mImageError = R.drawable.stated_error_;
        }
    }
    //endregion

    //region Callbacks
    public void notifyCallbackListener() {
        switch (mActualState) {
            case STATE_EMPTY:
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onEmpty();
                }
                break;
            case STATE_ERROR:
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onError();
                }
                break;
            case STATE_LOADING:
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onLoading();
                }
                break;
            case STATE_CONTENT:
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onContent();
                }
                break;
        }
    }
    //endregion


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = mActualState.toString();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mActualState = State.valueOf(ss.value);
        recoverState();
    }

}
