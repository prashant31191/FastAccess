package com.fasty.ui.modules.apps.folders.select;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.view.View;

import com.fasty.App;
import com.fasty.data.dao.AppsModel;
import com.fasty.provider.loader.DeviceAppsLoader;
import com.fasty.ui.base.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * Created by Kosh on 11 Oct 2016, 8:26 PM
 */

public class SelectFolderAppsPresenter extends BasePresenter<SelectFolderAppsMvp.View> implements SelectFolderAppsMvp.Presenter {

    protected SelectFolderAppsPresenter(@NonNull SelectFolderAppsMvp.View view) {
        super(view);
    }

    public static SelectFolderAppsPresenter with(@NonNull SelectFolderAppsMvp.View view) {
        return new SelectFolderAppsPresenter(view);
    }

    @Override public void onItemClick(int position, View v, AppsModel item) {
        getView().onRowClicked(item, position);
    }

    @Override public void onItemLongClick(int position, View v, AppsModel item) {
        onItemClick(position, v, item);
    }

    @Override public Loader<List<AppsModel>> onCreateLoader(int id, Bundle args) {
        getView().onStartLoading();
        return new DeviceAppsLoader(App.getInstance().getApplicationContext());
    }

    @Override public void onLoadFinished(Loader<List<AppsModel>> loader, List<AppsModel> data) {
        if (isAttached()) getView().onAppsLoaded(data);
    }

    @Override public void onLoaderReset(Loader<List<AppsModel>> loader) {
        if (isAttached()) getView().onLoaderReset();
    }
}
