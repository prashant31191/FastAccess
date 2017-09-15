package com.fasty.ui.modules.floating.folders;

import android.support.annotation.NonNull;

import com.fasty.data.dao.FolderModel;
import com.fasty.ui.modules.floating.BaseFloatingMvp;

/**
 * Created by Kosh on 14 Oct 2016, 8:54 PM
 */

public interface FloatingFoldersMvp {

    interface View extends BaseFloatingMvp.BaseView<FolderModel> {
        void onOpenFolder(@NonNull android.view.View v, @NonNull FolderModel item);
    }

    interface Presenter extends BaseFloatingMvp.BasePresenter<FolderModel,View> {

    }
}
