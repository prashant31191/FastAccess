package com.fasty.ui.widgets.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Kosh on 17 May 2016, 7:13 PM
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public interface OnItemClickListener<T> {
        void onItemClick(int position, View v, T item);

        void onItemLongClick(int position, View v, T item);
    }

    protected BaseRecyclerAdapter adapter;

    public static View getView(ViewGroup parent, @LayoutRes int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public BaseViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.adapter = adapter;
    }

    @SuppressWarnings("unchecked") @Override public void onClick(View v) {
        int position = getAdapterPosition();
        if (adapter != null && adapter.listener != null) {
            if (position != -1 && position < adapter.getItemCount())
                adapter.listener.onItemClick(position, v, adapter.getItem(position));
        }
    }

    @SuppressWarnings("unchecked") @Override public boolean onLongClick(View v) {
        int position = getAdapterPosition();
        if (adapter != null && adapter.listener != null) {
            if (position != -1 && position < adapter.getItemCount())
                adapter.listener.onItemLongClick(position, v, adapter.getItem(position));
        }
        return true;
    }

    public abstract void bind(@NonNull T t);
}
