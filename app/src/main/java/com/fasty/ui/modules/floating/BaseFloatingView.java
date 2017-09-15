package com.fasty.ui.modules.floating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.fasty.R;
import com.fasty.data.dao.FolderModel;
import com.fasty.data.dao.events.FloatingEventModel;
import com.fasty.data.dao.events.ThemePackEventModel;
import com.fasty.helper.NotificationHelper;
import com.fasty.helper.PermissionsHelper;
import com.fasty.helper.PrefConstant;
import com.fasty.helper.PrefHelper;
import com.fasty.provider.service.FloatingService;
import com.fasty.ui.adapter.viewholder.FloatingWindowsViewHolder;
import com.fasty.ui.modules.floating.folders.FloatingFoldersMvp;
import com.fasty.ui.widgets.floating.FloatingView;
import com.fasty.ui.widgets.recyclerview.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
import static android.view.WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

/**
 * Created by Kosh on 23 Oct 2016, 12:36 AM
 */

public abstract class BaseFloatingView<M> implements BaseFloatingMvp.BaseView<M> {
    private WindowManager.LayoutParams originalParams;
    protected WindowManager windowManager;
    protected Context context;
    protected FloatingView floatingView;
    private FloatingWindowsViewHolder layoutHolder;
    private Point szWindow = new Point();
    protected boolean isHorizontal;
    private FloatingFoldersMvp.View folderCallback;

    @SuppressWarnings("unused") private BaseFloatingView() {
        throw new RuntimeException("can't call me!");
    }

    @SuppressLint("InflateParams") protected BaseFloatingView(@NonNull Context context, boolean isHorizontal) {
        EventBus.getDefault().register(this);
        this.isHorizontal = isHorizontal;
        this.context = context;
        if (!PermissionsHelper.isSystemAlertGranted(context)) {
            context.stopService(new Intent(context, FloatingService.class));
            return;
        }
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(szWindow);
        originalParams = new WindowManager.LayoutParams(TYPE_PRIORITY_PHONE, FLAG_NOT_TOUCH_MODAL |
                FLAG_WATCH_OUTSIDE_TOUCH | FLAG_NOT_FOCUSABLE, TRANSLUCENT);
        setupParamsSize();
        boolean isAutoSavePosition = PrefHelper.getBoolean(PrefConstant.FA_AUTO_SAVE_POSITION);
        originalParams.x = isAutoSavePosition ? PrefHelper.getInt(PrefConstant.POSITION_X) : 0;
        originalParams.y = isAutoSavePosition ? PrefHelper.getInt(PrefConstant.POSITION_Y) : 100;
        originalParams.gravity = GravityCompat.START | Gravity.TOP;
        floatingView = new FloatingView(context, getPresenter());
        onUpdateXY();
        windowManager.addView(floatingView, originalParams);
        layoutHolder = new FloatingWindowsViewHolder(LayoutInflater.from(context).inflate(isHorizontal ? R.layout.horizontal_layout : R.layout
                .vertical_layout, null, false), this);
        layoutHolder.recycler.setAdapter(getAdapter());
        windowManager.addView(layoutHolder.tabBar, originalParams);
        getLoader();
        moveToEdge();
    }

    @SuppressWarnings("unchecked") @Override public void onLoaderLoaded(@Nullable List<M> data) {
        if (data == null || data.isEmpty()) {
            getAdapter().clear();
            context.stopService(new Intent(context, FloatingService.class));
        } else {
            getAdapter().insertItems(data);
        }
    }

    @Override public void onViewMoving(int x, int y) {
        getPresenter().onUpdateWindowParams(windowManager, originalParams, floatingView, x, y);
    }

    @Override public void onStoppedMoving() {
        moveToEdge();
        onUpdateXY();
        PrefConstant.savePosition(originalParams.x, originalParams.y);
    }

    @Override public void onLongPressed() {
        if (PrefConstant.isLongPressEnabled()) NotificationHelper.collapseFAService(context, getAdapter().getItemCount());
    }

    @Override public void onDoubleTapped() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
    }

    @Override public void onSingleTapped() {
        onToggleVisibility(false);
    }

    @Override public void onTouchedOutside() {
        if (layoutHolder.tabBar.isShown() && !PrefHelper.getBoolean(PrefConstant.FA_ALWAYS_SHOWING)) {
            onToggleVisibility(true);
        }
    }

    @Override public void onBackPressed() {
        onTouchedOutside();
    }

    @SuppressWarnings("unchecked") @Override public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (windowManager != null) {
            windowManager.removeView(floatingView);
            windowManager.removeView(layoutHolder.tabBar);
        }
        if (getLoader() != null) getLoader().unregisterListener(getPresenter());
        if (getAdapter() != null) getAdapter().clear();
        if (layoutHolder != null) layoutHolder.onDestroy();
        if (getPresenter() != null) getPresenter().onDestroy();
        folderCallback = null;
    }

    @Override public void onToggleVisibility(boolean showFloating) {
        if (!showFloating && folderCallback != null) {
            if (layoutHolder.recycler != null && layoutHolder.recycler.getAdapter() != null) {
                RecyclerView.Adapter adapter = layoutHolder.recycler.getAdapter();
                if (adapter.getItemCount() == 1) {
                    FolderModel folderModel = (FolderModel) ((BaseRecyclerAdapter) adapter).getItem(0);
                    if (folderModel != null) {
                        folderCallback.onOpenFolder(floatingView, folderModel);
                        return;
                    }
                }
            }
        }
        getPresenter().onToggleVisibility(showFloating, windowManager, originalParams, layoutHolder.tabBar, floatingView, isHorizontal);
    }

    @Override public void onConfigChanged(int orientation) {
        windowManager.getDefaultDisplay().getSize(szWindow);
        moveToEdge();
    }

    @Override public void onUpdateXY() {
        floatingView.setInitialX(originalParams.x);
        floatingView.setInitialY(originalParams.y);
    }

    @SuppressWarnings("unused") @Subscribe public void onEvent(FloatingEventModel model) {
        if (!model.isSettingsChanged()) {
            if (getLoader() != null) getLoader().onContentChanged();
        } else {
            setupParamsSize();
            floatingView.setupImageView();
            layoutHolder.onSetupBackground();
            moveToEdge();
        }
    }

    @SuppressWarnings("unused") @Subscribe public void onEvent(ThemePackEventModel model) {
        if (getLoader() != null) getLoader().forceLoad();
    }

    @Override public void setupParamsSize() {
        originalParams.width = PrefConstant.getFinalSize(context);
        originalParams.height = isHorizontal ? PrefConstant.getFinalSize(context) : WRAP_CONTENT;
        if (floatingView != null) windowManager.updateViewLayout(floatingView, originalParams);
    }

    private void moveToEdge() {
        getPresenter().onMoveToEdge(windowManager, originalParams, floatingView, szWindow);
    }

    public void setFolderCallback(FloatingFoldersMvp.View folderCallback) {
        this.folderCallback = folderCallback;
    }
}
