package com.fasty.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.fasty.data.dao.AppsModel;
import com.fasty.ui.adapter.viewholder.FloatingFoldersAppsViewHolder;
import com.fasty.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder.OnItemClickListener;

import java.util.List;

/**
 * Created by Kosh on 30 Aug 2016, 11:42 PM
 */

public class FloatingFolderAppsAdapter extends BaseRecyclerAdapter<AppsModel, FloatingFoldersAppsViewHolder,
        OnItemClickListener<AppsModel>> {

    private boolean isHorizontal;

    public FloatingFolderAppsAdapter(@NonNull List<AppsModel> data, @Nullable OnItemClickListener<AppsModel> listener,
                                     boolean isHorizontal) {
        super(data, listener);
        this.isHorizontal = isHorizontal;
    }

    @Override protected FloatingFoldersAppsViewHolder viewHolder(ViewGroup parent, int viewType) {
        return FloatingFoldersAppsViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(FloatingFoldersAppsViewHolder holder, int position) {
        AppsModel model = getItem(position);
        if (model != null) {
            holder.bind(model, isHorizontal);
        }
    }

}
