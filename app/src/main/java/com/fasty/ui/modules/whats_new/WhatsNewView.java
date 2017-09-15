package com.fasty.ui.modules.whats_new;

import android.support.annotation.NonNull;

import com.fasty.R;
import com.fasty.ui.base.BaseActivity;
import com.fasty.ui.widgets.FontButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 05 Nov 2016, 3:31 AM
 */

public class WhatsNewView extends BaseActivity<WhatsNewMvp.View, WhatsNewPresenter> implements WhatsNewMvp.View {

    @BindView(R.id.rate) FontButton rate;

    private WhatsNewPresenter presenter;

    @Override protected int layout() {
        return R.layout.whats_new_layout;
    }

    @NonNull @Override protected WhatsNewPresenter getPresenter() {
        if (presenter == null) presenter = WhatsNewPresenter.with(this);
        return presenter;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @OnClick(R.id.rate) public void onClick() {
        getPresenter().openRateApp(this);
    }
}
