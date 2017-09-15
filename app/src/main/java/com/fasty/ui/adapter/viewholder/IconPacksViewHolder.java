package com.fasty.ui.adapter.viewholder;

import android.view.View;

import com.fasty.R;
import com.fasty.ui.widgets.FontRadioButton;
import com.fasty.ui.widgets.FontTextView;
import com.fasty.ui.widgets.ForegroundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IconPacksViewHolder {
    @BindView(R.id.icon) public ForegroundImageView icon;
    @BindView(R.id.title) public FontTextView title;
    @BindView(R.id.radio) public FontRadioButton radio;

    public IconPacksViewHolder(View view) {ButterKnife.bind(this, view);}
}
