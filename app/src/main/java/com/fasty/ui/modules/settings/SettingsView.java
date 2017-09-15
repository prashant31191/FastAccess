package com.fasty.ui.modules.settings;

import android.support.annotation.NonNull;

import com.fasty.R;
import com.fasty.ui.base.BaseActivity;
import com.fasty.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 15 Oct 2016, 10:52 PM
 */

public class SettingsView extends BaseActivity {

    @Override protected int layout() {
        return R.layout.settings_layout;
    }

    @SuppressWarnings("ConstantConditions") @NonNull @Override protected BasePresenter getPresenter() {
        return null;
    }//op-out

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return true;
    }
}
