package com.fasty.ui.modules.floating.folders;

import android.support.annotation.NonNull;
import android.view.View;

import com.fasty.data.dao.FolderModel;
import com.fasty.ui.modules.floating.BaseFloatingPresenter;

/**
 * Created by Kosh on 14 Oct 2016, 9:00 PM
 */

public class FloatingFoldersPresenter extends BaseFloatingPresenter<FolderModel, FloatingFoldersMvp.View> implements FloatingFoldersMvp
        .Presenter {

    private FloatingFoldersPresenter(@NonNull FloatingFoldersMvp.View view) {
        super(view);
    }

    public static FloatingFoldersPresenter with(@NonNull FloatingFoldersMvp.View view) {
        return new FloatingFoldersPresenter(view);
    }

    @Override public void onItemClick(int position, View v, FolderModel item) {
        super.onItemClick(position, v, item);
        if (isAttached()) getView().onOpenFolder(v, item);
    }
}
