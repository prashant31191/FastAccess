package com.fasty.ui.modules.floating.apps;

import com.fasty.data.dao.AppsModel;
import com.fasty.ui.modules.floating.BaseFloatingMvp;

/**
 * Created by Kosh on 14 Oct 2016, 8:54 PM
 */

public interface FloatingHVMvp {

    interface Presenter extends BaseFloatingMvp.BasePresenter<AppsModel, BaseFloatingMvp.BaseView<AppsModel>> {}
}
