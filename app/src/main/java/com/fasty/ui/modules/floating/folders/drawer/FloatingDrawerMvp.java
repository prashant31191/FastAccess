package com.fasty.ui.modules.floating.folders.drawer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.WindowManager;

import com.fasty.data.dao.AppsModel;
import com.fasty.data.dao.FolderModel;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 22 Oct 2016, 3:11 PM
 */

public interface FloatingDrawerMvp {

    interface View {

        void onShow(@NonNull WindowManager windowManager, @NonNull android.view.View view, @NonNull FolderModel folder);

        void onAppsLoaded(@Nullable List<AppsModel> models);

        void onConfigChanged(int orientation);

        void onTouchedOutside();

        void onBackPressed();

        void onDestroy();
    }

    interface Presenter extends BaseViewHolder.OnItemClickListener<AppsModel>,
            Loader.OnLoadCompleteListener<List<AppsModel>> {}
}
