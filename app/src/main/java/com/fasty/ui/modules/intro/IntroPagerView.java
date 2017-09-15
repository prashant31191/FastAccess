package com.fasty.ui.modules.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fasty.R;
import com.fasty.ui.adapter.IntroAdapter;
import com.fasty.ui.base.BaseActivity;
import com.fasty.ui.base.mvp.presenter.BasePresenter;
import com.fasty.ui.widgets.IntroTransformer;
import com.fasty.ui.widgets.ViewPagerView;
import com.rd.PageIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 06 Nov 2016, 12:29 PM
 */

public class IntroPagerView extends BaseActivity {

    @BindView(R.id.pager) ViewPagerView pager;
    @BindView(R.id.pageIndicatorView) PageIndicatorView pageIndicatorView;

    @Override protected int layout() {
        return R.layout.intro_pager_layout;
    }

    @NonNull @Override protected BasePresenter getPresenter() {
        return null;
    }//op-out

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @OnClick(R.id.introDone) public void onClick() {
        finish();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pager.setPageTransformer(true, new IntroTransformer());
        pager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(pager.getAdapter().getCount());
        pageIndicatorView.setViewPager(pager);
    }

}
