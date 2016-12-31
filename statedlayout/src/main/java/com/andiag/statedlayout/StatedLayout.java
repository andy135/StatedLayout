package com.andiag.statedlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
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

    public static final int STATE_CONTENT  = 10;
    public static final int STATE_LOADING  = 20;
    public static final int STATE_ERROR    = 30;
    public static final int STATE_EMPTY    = 40;

    private int actualState = STATE_LOADING;

    private ImageView image;
    private TextView text;
    private Button button;

    private int textSize;

    @StringRes
    private int labelEmpty, labelLoading, labelError;

    @DrawableRes
    private int imageEmpty, imageLoading, imageError;

    private boolean alternateIcons;

    private ViewGroup content;

    public StatedLayout(Context context) {
        super(context);
        initViews(context,null,0);
    }

    public StatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs,0);
    }

    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs,defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs,defStyleAttr);
    }

    public int getActualState() {
        return actualState;
    }

    public void setActualState(int actualState) {
        this.actualState = actualState;
    }

    private void initViews(Context context, AttributeSet attrs, int defStyle){
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.StatedLayout,defStyle,0);

        try {
            labelEmpty = array.getResourceId(R.styleable.StatedLayout_emptyLabel,R.string.default_empty);
            labelLoading = array.getResourceId(R.styleable.StatedLayout_loadingLabel,R.string.default_loading);
            labelError = array.getResourceId(R.styleable.StatedLayout_errorLabel,R.string.default_error);

            imageEmpty = array.getResourceId(R.styleable.StatedLayout_emptyImage, R.drawable.stated_empty);
            imageLoading = array.getResourceId(R.styleable.StatedLayout_loadingImage, R.drawable.stated_loading);
            imageError = array.getResourceId(R.styleable.StatedLayout_errorImage, R.drawable.stated_error);

            alternateIcons = array.getBoolean(R.styleable.StatedLayout_alternateIcons, false);

            textSize = array.getDimensionPixelSize(R.styleable.StatedLayout_android_textSize,18);
        } finally {
            array.recycle();
        }

        if (alternateIcons) {
            imageEmpty = R.drawable.stated_empty_;
            imageLoading = R.drawable.stated_loading_;
            imageError = R.drawable.stated_error_;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.statedlayout_base, this);

        image = (ImageView) view.findViewById(R.id.statelayout_image);
        text = (TextView) view.findViewById(R.id.statelayout_text);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        button = (Button) view.findViewById(R.id.statelayout_button);
        content = (ViewGroup) view.findViewById(R.id.statedlayout_content);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setState(actualState);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (content == null){
            super.addView(child,index,params);
        } else {
             content.addView(child, index, params);
        }
    }

    public void setState(int state){
        switch (state) {
            case STATE_EMPTY :
                setEmpty();
                break;
            case STATE_ERROR :
                setError();
                break;
            case STATE_LOADING :
                setLoading();
                break;
            case STATE_CONTENT :
                setContent();
                break;
            default:
                break;
        }
        invalidate();
    }

    private void setEmpty(){
        actualState = STATE_EMPTY;
        text.setText(labelEmpty);
        image.setImageResource(imageEmpty);
        setVisible(text,true);
        setVisible(image,true);
        setVisible(button,false);
        setVisible(content,false);
    }

    private void setLoading(){
        actualState = STATE_LOADING;
        text.setText(labelLoading);
        image.setImageResource(imageLoading);
        setVisible(text,true);
        setVisible(image,true);
        setVisible(button,false);
        setVisible(content,false);
    }

    private void setError(){
        actualState = STATE_ERROR;
        text.setText(labelError);
        image.setImageResource(imageError);
        button.setText(R.string.retry);
        setVisible(text,true);
        setVisible(image,true);
        setVisible(button,true);
        setVisible(content,false);
    }

    private void setContent(){
        actualState = STATE_CONTENT;
        setVisible(text,false);
        setVisible(image,false);
        setVisible(button,false);
        setVisible(content,true);
    }

    private void setVisible(View view, boolean wanttosee){
        if (view.isShown() && !wanttosee){
            view.setVisibility(View.GONE);
        } else if (!view.isShown() && wanttosee) {
            view.setVisibility(View.VISIBLE);
        }
    }

}
