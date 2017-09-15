package com.fasty.ui.modules.apps.folders.select;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import com.fasty.data.dao.AppsModel;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Oct 2016, 8:26 PM
 */

public interface SelectFolderAppsMvp {

    interface View {
        void onStartLoading();

        void onAppsLoaded(@Nullable List<AppsModel> models);

        void onLoaderReset();

        void onRowClicked(@NonNull AppsModel model, int position);
    }

    interface Presenter extends BaseViewHolder.OnItemClickListener<AppsModel>, LoaderManager.LoaderCallbacks<List<AppsModel>> {}
}
