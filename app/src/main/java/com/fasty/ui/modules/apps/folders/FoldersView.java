package com.fasty.ui.modules.apps.folders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.fasty.R;
import com.fasty.data.dao.FolderModel;
import com.fasty.data.dao.events.FolderEventModel;
import com.fasty.data.dao.events.FragmentFolderEventModel;
import com.fasty.helper.InputHelper;
import com.fasty.ui.adapter.FoldersAdapter;
import com.fasty.ui.base.BaseFragment;
import com.fasty.ui.modules.apps.folders.create.CreateFolderView;
import com.fasty.ui.modules.apps.folders.select.SelectFolderAppsView;
import com.fasty.ui.widgets.FontTextView;
import com.fasty.ui.widgets.dialog.MessageDialogView;
import com.fasty.ui.widgets.recyclerview.DynamicRecyclerView;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Kosh on 11 Oct 2016, 7:42 PM
 */

public class FoldersView extends BaseFragment<FoldersMvp.View, FoldersPresenter> implements FoldersMvp.View {
    public final static String TAG = "FoldersView";

    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.empty_text) FontTextView emptyText;
    @BindView(R.id.empty) NestedScrollView empty;
    @BindView(R.id.progressBar) CircularFillableLoaders progressBar;
    private FoldersPresenter presenter;
    private FoldersAdapter adapter;
    private Loader loader;

    public static FoldersView newInstance() {
        return new FoldersView();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override protected int fragmentLayout() {
        return R.layout.small_grid_list;
    }

    @NonNull @Override protected FoldersPresenter getPresenter() {
        if (presenter == null) {
            presenter = FoldersPresenter.with(this);
        }
        return presenter;
    }

    @Override protected void onFragmentCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new FoldersAdapter(new ArrayList<FolderModel>(), getPresenter());
        recycler.setEmptyView(empty);
        emptyText.setText(R.string.no_folders);
        recycler.setAdapter(adapter);
        loader = getLoaderManager().initLoader(1, null, getPresenter());
    }

    @Override public void onStartLoading() {
        recycler.showProgress(progressBar);
    }

    @Override public void onFoldersLoaded(@Nullable List<FolderModel> models) {
        recycler.hideProgress(progressBar);
        if (models == null) adapter.clear();
        else adapter.insertItems(models);
    }

    @Override public void onLoaderReset() {
        recycler.hideProgress(progressBar);
        adapter.clear();
    }

    @Override public void onEditFolder(@NonNull FolderModel folder) {
        CreateFolderView.newInstance(folder.getId()).show(getChildFragmentManager(), "CreateFolderView");
    }

    @Override public void onAddAppsToFolder(@NonNull FolderModel folder) {
        SelectFolderAppsView.newInstance(folder.getId()).show(getChildFragmentManager(), "SelectFolderAppsView");
    }

    @Override public void onCreateNewFolder() {
        CreateFolderView.newInstance(-1).show(getChildFragmentManager(), "CreateFolderView");
    }

    @Override public void onDeleteFolder(@NonNull FolderModel folder, int position) {
        MessageDialogView.newInstance(R.string.delete, R.string.delete_folder_confirm_msg, position)
                .show(getChildFragmentManager(), "MessageDialogView");
    }

    @Override public void onFilter(@Nullable String text) {
        if (progressBar.isShown()) {
            return;
        }
        if (InputHelper.isEmpty(text)) {
            loader.onContentChanged();
        } else {
            adapter.getFilter().filter(text);
        }
    }

    @Override public void onNotifyChanges() {
        if (loader != null) loader.onContentChanged();
        EventBus.getDefault().post(new FolderEventModel());
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, int requestCode) {
        if (isOk) {
            FolderModel folderModel = adapter.getItem(requestCode);
            if (folderModel != null) {
                FolderModel.deleteFolder(folderModel);
                adapter.removeItem(folderModel);
                onNotifyChanges();
            }
        }
    }

    @Override public void onDialogDismissed() {}//opt-out

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEvent(@NonNull FragmentFolderEventModel model) {
        if (loader != null) loader.forceLoad();
    }
}
