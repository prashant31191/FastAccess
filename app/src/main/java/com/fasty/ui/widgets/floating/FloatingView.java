package com.fasty.ui.widgets.floating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.fasty.R;
import com.fasty.helper.InputHelper;
import com.fasty.helper.PrefConstant;
import com.fasty.helper.PrefHelper;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import java.io.File;

import io.codetail.widget.RevealFrameLayout;

/**
 * Created by Kosh on 14 Oct 2016, 7:11 PM
 */

@SuppressLint("ViewConstructor")
public class FloatingView extends RevealFrameLayout implements TouchTypeDetector.TouchTypListener, View.OnTouchListener {

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private ImageView imageView;
    private FloatingTouchCallback callback;

    public FloatingView(@NonNull Context context, @NonNull FloatingTouchCallback callback) {
        super(context);
        this.callback = callback;
        imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        imageView.setContentDescription(getResources().getString(R.string.app_name));
        setupImageView();
        addView(imageView);
        this.setOnTouchListener(this);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Sensey.getInstance().init(getContext().getApplicationContext());
        Sensey.getInstance().startTouchTypeDetection(this);
    }

    @Override protected void onDetachedFromWindow() {
        Sensey.getInstance().stopTouchTypeDetection();
        callback = null;
        removeAllViews();
        super.onDetachedFromWindow();
    }

    @Override public void onTwoFingerSingleTap() {}//op-out

    @Override public void onThreeFingerSingleTap() {}//op-out

    @Override public void onDoubleTap() {
        if (callback != null) callback.onDoubleTapped();
    }

    @Override public void onScroll(int scrollDirection) {}//op-out

    @Override public void onSingleTap() {
        if (callback != null) callback.onSingleTapped();
    }

    @Override public void onSwipe(int swipeDirection) {
        if (callback != null) callback.onSwipe(swipeDirection);
    }

    @Override public void onLongPress() {
        if (callback != null) callback.onLongPressed();
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (callback != null) callback.onBackPressed();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (callback != null) callback.onConfigChanged(newConfig.orientation);
    }

    @Override public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                imageView.setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                setClickable(true);
                if (callback != null) callback.onStoppedMoving();
                onMoving(false);
                imageView.setPressed(false);
                break;
            case MotionEvent.ACTION_MOVE:
                setClickable(false);
                onMoving(true);
                if (callback != null)
                    callback.onViewMoving(initialX + (int) (event.getRawX() - initialTouchX), initialY + (int) (event.getRawY() - initialTouchY));
                break;
            case MotionEvent.ACTION_OUTSIDE:
                if (callback != null) callback.onTouchOutside();
                imageView.setPressed(false);
                break;
        }
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return false;
    }

    public void setupImageView() {
        if (imageView != null) {
            String path = PrefHelper.getString(PrefConstant.CUSTOM_ICON);
            if (!InputHelper.isEmpty(path)) {
                path = Uri.decode(PrefHelper.getString(PrefConstant.CUSTOM_ICON));
                boolean fileExists = new File(path).exists();
                if (fileExists) {
                    imageView.setImageDrawable(null);
                    Bitmap src = BitmapFactory.decodeFile(path);
                    if (src == null) {
                        imageView.setImageResource(R.drawable.ic_app_drawer_icon);
                        onMoving(false);
                        return;
                    }
                    RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), src);
                    dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                    imageView.setImageDrawable(dr);
                    return;
                }
            }
            imageView.setImageResource(R.drawable.ic_app_drawer_icon);
            onMoving(false);
        }
    }

    private void onMoving(boolean isMoving) {
        if (imageView == null) return;
        if (isMoving) {
            imageView.setImageAlpha(255);
        } else {
            boolean isAutoTransparent = PrefHelper.getBoolean(PrefConstant.AUTO_TRANS);
            int alpha = PrefHelper.getInt(PrefConstant.ICON_ALPHA);
            if (isAutoTransparent) {
                imageView.setImageAlpha(alpha == 0 ? 100 : alpha);
            } else {
                imageView.setImageAlpha(255);
            }
        }
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;

    }
}
