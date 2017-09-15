package com.fasty.ui.adapter.viewholder;

import android.view.MotionEvent;
import android.view.View;

import com.fasty.R;
import com.fasty.ui.modules.floating.folders.drawer.FloatingDrawerMvp;
import com.fasty.ui.widgets.FontTextView;
import com.fasty.ui.widgets.floating.FloatingLayout;
import com.fasty.ui.widgets.recyclerview.DynamicRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import butterknife.Unbinder;

public class AppDrawerHolder {
    @BindView(R.id.appDrawer) public FloatingLayout appDrawer;
    @BindView(R.id.recycler) public DynamicRecyclerView recycler;
    @BindView(R.id.empty_text) public FontTextView emptyText;
    @BindView(R.id.folderName) public FontTextView folderName;
    private FloatingDrawerMvp.View viewCallback;
    private Unbinder unbinder;

    @OnTouch(R.id.appDrawer) boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (viewCallback != null) viewCallback.onTouchedOutside();
        }
        return false;
    }

    public AppDrawerHolder(View view, FloatingDrawerMvp.View viewCallback) {
        this.viewCallback = viewCallback;
        unbinder = ButterKnife.bind(this, view);
        appDrawer.setViewCallback(viewCallback);
    }

    public void onDestroy() {
        appDrawer.setViewCallback(null);
        viewCallback = null;
        if (unbinder != null) unbinder.unbind();
    }

}