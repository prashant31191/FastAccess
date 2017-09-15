package com.fasty.ui.widgets.recyclerview.touch;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onItemStoppedMoving();
}
