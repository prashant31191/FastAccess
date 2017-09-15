package com.fasty.ui.modules.floating.apps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.fasty.R;
import com.fasty.data.dao.AppsModel;
import com.fasty.provider.loader.SelectedAppsLoader;
import com.fasty.ui.adapter.FloatingAppsAdapter;
import com.fasty.ui.adapter.viewholder.FloatingAppsViewHolder;
import com.fasty.ui.modules.floating.BaseFloatingView;
import com.fasty.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 14 Oct 2016, 9:12 PM
 */


public class FloatingVHView extends BaseFloatingView<AppsModel> {
    private FloatingAppsAdapter adapter;
    private SelectedAppsLoader appsLoader;
    private FloatingVHPresenter presenter;

    protected FloatingVHView(@NonNull Context context, boolean isHorizontal) {
        super(context, isHorizontal);
    }

    public static FloatingVHView with(@NonNull Context context, boolean isHorizontal) {
        return new FloatingVHView(context, isHorizontal);
    }

    @Override public Loader getLoader() {
        if (appsLoader == null) {
            appsLoader = new SelectedAppsLoader(context);
            appsLoader.registerListener(10, getPresenter());
            appsLoader.startLoading();
        }
        return appsLoader;
    }

    @Override public void onLoaderLoaded(@Nullable List<AppsModel> data) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(context, R.string.no_apps_selected, Toast.LENGTH_LONG).show();
        }
        super.onLoaderLoaded(data);
    }

    @Override public BaseRecyclerAdapter<AppsModel, FloatingAppsViewHolder,
            BaseViewHolder.OnItemClickListener<AppsModel>> getAdapter() {
        if (adapter == null) {
            adapter = new FloatingAppsAdapter(new ArrayList<AppsModel>(), getPresenter(), isHorizontal);
        }
        return adapter;
    }

    @Override public FloatingVHPresenter getPresenter() {
        if (presenter == null) presenter = new FloatingVHPresenter(this);
        return presenter;
    }
}
