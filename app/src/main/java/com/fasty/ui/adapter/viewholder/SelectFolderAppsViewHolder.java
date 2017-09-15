package com.fasty.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fasty.R;
import com.fasty.data.dao.AppsModel;
import com.fasty.ui.widgets.FontCheckbox;
import com.fasty.ui.widgets.FontTextView;
import com.fasty.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fasty.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 30 Aug 2016, 11:42 PM
 */

public class SelectFolderAppsViewHolder extends BaseViewHolder<AppsModel> {
    @BindView(R.id.appIcon) ImageView appIcon;
    @BindView(R.id.appName) FontTextView appName;
    @BindView(R.id.checkbox) FontCheckbox checkbox;
    private boolean selected;

    public static SelectFolderAppsViewHolder newInstance(@NonNull ViewGroup parent, @NonNull BaseRecyclerAdapter adapter) {
        return new SelectFolderAppsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_apps_folder_row, parent, false), adapter);
    }

    public SelectFolderAppsViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
    }

    public void bind(@NonNull AppsModel model, boolean selected) {
        this.selected = selected;
        bind(model);
    }

    @Override public void bind(@NonNull AppsModel model) {
        appName.setText(model.getAppName());
        appIcon.setImageBitmap(model.getBitmap());
        appIcon.setContentDescription(model.getAppName());
        checkbox.setChecked(selected);
    }
}
