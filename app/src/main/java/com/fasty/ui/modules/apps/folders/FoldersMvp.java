package com.fasty.ui.modules.apps.folders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import com.fasty.data.dao.FolderModel;
import com.fasty.ui.modules.apps.folders.create.CreateFolderMvp;
import com.fasty.ui.widgets.dialog.MessageDialogView;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by Kosh on 11 Oct 2016, 7:32 PM
 */

public interface FoldersMvp {

    interface View extends CreateFolderMvp.OnNotifyFoldersAdapter, MessageDialogView.MessageDialogViewActionCallback{
        void onStartLoading();

        void onFoldersLoaded(@Nullable List<FolderModel> models);

        void onLoaderReset();

        void onEditFolder(@NonNull FolderModel folder);

        void onAddAppsToFolder(@NonNull FolderModel folder);

        void onCreateNewFolder();

        void onDeleteFolder(@NonNull FolderModel folder, int position);

        void onFilter(@Nullable String text);
    }

    interface Presenter extends LoaderManager.LoaderCallbacks<List<FolderModel>>,
            BaseViewHolder.OnItemClickListener<FolderModel> {

    }
}
