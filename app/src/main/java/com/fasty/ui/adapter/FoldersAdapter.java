package com.fasty.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.fasty.data.dao.FolderModel;
import com.fasty.helper.InputHelper;
import com.fasty.ui.adapter.viewholder.FoldersViewHolder;
import com.fasty.ui.widgets.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 11 Oct 2016, 7:58 PM
 */

public class FoldersAdapter extends BaseRecyclerAdapter<FolderModel, FoldersViewHolder,
        FoldersViewHolder.OnItemClickListener<FolderModel>> implements Filterable {
    public FoldersAdapter(@NonNull List<FolderModel> data, @Nullable FoldersViewHolder.OnItemClickListener<FolderModel> listener) {
        super(data, listener);
    }

    @Override protected FoldersViewHolder viewHolder(ViewGroup parent, int viewType) {
        return FoldersViewHolder.newInstance(parent, this);
    }

    @Override protected void onBindView(FoldersViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<FolderModel> results = new ArrayList<>();
                if (!InputHelper.isEmpty(charSequence)) {
                    if (!getData().isEmpty()) {
                        for (FolderModel folder : getData()) {
                            if (folder.getFolderName().toLowerCase().contains(charSequence.toString())) {
                                results.add(folder);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked") @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                insertItems((List<FolderModel>) results.values);
            }
        };
    }
}
