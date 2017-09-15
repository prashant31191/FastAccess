package com.fasty.ui.modules.floating.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.fasty.data.dao.AppsModel;
import com.fasty.ui.modules.floating.BaseFloatingMvp;
import com.fasty.ui.modules.floating.BaseFloatingPresenter;

/**
 * Created by Kosh on 14 Oct 2016, 9:00 PM
 */

public class FloatingVHPresenter extends BaseFloatingPresenter<AppsModel, BaseFloatingMvp.BaseView<AppsModel>> implements FloatingHVMvp.Presenter {


    public FloatingVHPresenter(@NonNull BaseFloatingMvp.BaseView<AppsModel> view) {
        super(view);
    }

    public static FloatingVHPresenter with(@NonNull BaseFloatingMvp.BaseView<AppsModel> view) {
        return new FloatingVHPresenter(view);
    }

    @Override public void onItemClick(int position, View v, AppsModel item) {
        try {
            Context context = v.getContext();
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(item.getPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(item.getPackageName(), item.getActivityInfoName()));
            context.startActivity(intent);
        } catch (Exception e) {// app uninstalled/not found
            e.printStackTrace();
            item.delete();
        }
        super.onItemClick(position, v, item);
    }
}
