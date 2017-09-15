package com.fasty.ui.modules.intro;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;

import com.fasty.R;
import com.fasty.helper.Bundler;
import com.fasty.ui.base.BaseFragment;
import com.fasty.ui.widgets.FontTextView;

import butterknife.BindView;

/**
 * Created by Kosh on 06 Nov 2016, 12:32 PM
 */

public class IntroPageView extends BaseFragment<IntroPageMvp.View, IntroPagePresenter> implements IntroPageMvp.View {

    @BindView(R.id.introTitle) FontTextView introTitle;
    @BindView(R.id.introImage) ImageView introImage;
    @BindView(R.id.introDescription) FontTextView introDescription;
    private IntroPagePresenter presenter;

    @Override protected int fragmentLayout() {
        return R.layout.intro_page_layout;
    }

    @NonNull @Override protected IntroPagePresenter getPresenter() {
        if (presenter == null) presenter = IntroPagePresenter.with(this);
        return presenter;
    }

    @Override protected void onFragmentCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            return;
        }
        int titleRes = getArguments().getInt("titleRes");
        int drawableRes = getArguments().getInt("drawableRes");
        int descriptionRes = getArguments().getInt("descriptionRes");
        introTitle.setText(titleRes);
        introImage.setImageResource(drawableRes);
        introImage.setContentDescription(getString(titleRes));
        introDescription.setText(descriptionRes);
    }

    public static IntroPageView newInstance(@StringRes int titleRes, @DrawableRes int drawableRes, @StringRes int descriptionRes) {
        IntroPageView pageView = new IntroPageView();
        pageView.setArguments(Bundler
                .start()
                .put("titleRes", titleRes)
                .put("drawableRes", drawableRes)
                .put("descriptionRes", descriptionRes)
                .end());
        return pageView;
    }
}
