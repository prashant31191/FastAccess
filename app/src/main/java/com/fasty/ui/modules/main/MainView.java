package com.fasty.ui.modules.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fasty.R;
import com.fasty.helper.AnimHelper;
import com.fasty.helper.AppHelper;
import com.fasty.helper.Bundler;
import com.fasty.helper.Logger;
import com.fasty.helper.NotificationHelper;
import com.fasty.helper.PermissionsHelper;
import com.fasty.helper.PrefConstant;
import com.fasty.provider.service.FloatingService;
import com.fasty.ui.base.BaseActivity;
import com.fasty.ui.modules.apps.device.DeviceAppsView;
import com.fasty.ui.modules.cloud.auth.LoginView;
import com.fasty.ui.modules.cloud.restore.RestoreView;
import com.fasty.ui.modules.intro.IntroPagerView;
import com.fasty.ui.modules.settings.SettingsView;
import com.fasty.ui.modules.whats_new.WhatsNewView;
import com.fasty.ui.widgets.FontEditText;
import com.fasty.ui.widgets.ForegroundImageView;
import com.fasty.ui.widgets.dialog.MessageDialogView;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import icepick.State;
import it.sephiroth.android.library.bottomnavigation.BadgeProvider;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainView extends BaseActivity<MainMvp.View, MainPresenter> implements MainMvp.View {
    public final static int BACKUP_REQUEST_CODE = 1;
    public final static int RESTORE_REQUEST_CODE = 2;

    @MainMvp.NavigationType @State int navType;

    @BindView(R.id.searchEditText) FontEditText searchEditText;
    @BindView(R.id.clear) ForegroundImageView clear;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.navigation) NavigationView navigation;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.bottomNavigation) BottomNavigation bottomNavigation;
    @BindView(R.id.fab) FloatingActionButton fab;

    private MainPresenter presenter;
    private BadgeProvider badgeProvider;
    private GoogleApiClient mGoogleApiClient;


    @OnClick(R.id.fab) void onClick() {
        if (navType == MainMvp.FOLDERS) {
            getPresenter().onCreateNewFolder(getSupportFragmentManager());
        }
    }

    @OnTouch(R.id.searchEditText) boolean onTouch() {
        appbar.setExpanded(false, true);
        return false;
    }

    @OnTextChanged(value = R.id.searchEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onTextChange(Editable s) {
        String text = s.toString();
        if (text.length() == 0) {
            getPresenter().onFilterResult(getSupportFragmentManager(), text);
            AnimHelper.animateVisibility(clear, false);
        } else {
            AnimHelper.animateVisibility(clear, true);
            getPresenter().onFilterResult(getSupportFragmentManager(), text);
        }
    }

    @OnClick(value = {R.id.searchIcon, R.id.clear}) void onClick(View view) {
        if (view.getId() == R.id.clear) {
            AppHelper.hideKeyboard(searchEditText);
            searchEditText.setText("");
        }
    }

    @Override protected int layout() {
        Log.i("=====cam here=====","=====layout======");
        return R.layout.activity_main;
    }

    @NonNull @Override protected MainPresenter getPresenter() {
        if (presenter == null) {
            presenter = MainPresenter.with(this);
        }
        return presenter;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, DeviceAppsView.newInstance(), DeviceAppsView.TAG)
                    .commit();
            getPresenter().onHandleShortcuts(this, getIntent());
            AppInvite.AppInviteApi.getInvitation(getGoogleApiClient(), this, false).setResultCallback(getPresenter());
        }
        if(drawerLayout == null)
        {
            Log.i("=====drawerLayout=====","=====drawerLayout=null======");
        }
        drawerLayout.setStatusBarBackground(R.color.primary_dark);
        setToolbarIcon(R.drawable.ic_menu);
        getPresenter().onActivityStarted(savedInstanceState, this, bottomNavigation, navigation);
        if (null != savedInstanceState) getBadgeProvider().restore(savedInstanceState);
        if (navType == MainMvp.FOLDERS) {
            fab.show();
        } else {
            fab.hide();
        }
        if (PrefConstant.showIntroScreen()) {
            startActivity(new Intent(this, IntroPagerView.class));
            PrefConstant.setIntroScreen();
        }
        if (PrefConstant.showWhatsNew()) {
            PrefConstant.setWhatsNewVersion();
            startActivity(new Intent(this, WhatsNewView.class));
        }
    }

    @Override public void onNavigationChanged(@MainMvp.NavigationType int navType) {
        //noinspection WrongConstant
        if (bottomNavigation.getSelectedIndex() != navType) bottomNavigation.setSelectedIndex(navType, true);
        this.navType = navType;
        getPresenter().onModuleChanged(getSupportFragmentManager(), navType);
        if (navType == MainMvp.FOLDERS) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override public void onOpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override public void onCloseDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override public void onOpenSettings() {
        startActivity(new Intent(this, SettingsView.class));
    }

    @Override public void onStartService() {
        if (PermissionsHelper.systemAlertPermissionIsGranted(this)) {
            startService(new Intent(this, FloatingService.class));
        } else {
            Toast.makeText(this, R.string.floating_window_warning, Toast.LENGTH_LONG).show();
        }
    }

    @Override public void onStopService() {
        stopService(new Intent(this, FloatingService.class));
        NotificationHelper.cancelAllNotifications(this);
    }

    @Override public void onShowBadge(@IdRes int itemId) {
        if (!getBadgeProvider().hasBadge(itemId)) {
            getBadgeProvider().show(itemId);
            onStartService();
        }
    }

    @Override public void onHideBadge(@IdRes int itemId) {
        if (getBadgeProvider().hasBadge(itemId)) {
            getBadgeProvider().remove(itemId);
        }
    }

    @Override public void onSelectMenuItem(@IdRes int itemId) {
        navigation.getMenu().findItem(itemId).setChecked(true);
    }

    @Override public void onBackup() {
        MessageDialogView.newInstance(R.string.backup, R.string.backup_warning, BACKUP_REQUEST_CODE)
                .show(getSupportFragmentManager(), "MessageDialogView");
    }

    @Override public void onRestore() {
        MessageDialogView.newInstance(R.string.restore, R.string.restore_warning, RESTORE_REQUEST_CODE)
                .show(getSupportFragmentManager(), "MessageDialogView");
    }

    @Override public void onShareBackup() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, R.string.no_user, Toast.LENGTH_LONG).show();
        } else {
            MessageDialogView.newInstance(R.string.share_my_backup, R.string.confirm_message)
                    .show(getSupportFragmentManager(), "MessageDialogView");
        }
    }

    @Override public void onRestoreFromUserId(@NonNull String userId) {
        Bundle bundle = Bundler.start().put(RestoreView.USER_ID_INTENT, userId).end();
        Intent intent = new Intent(this, RestoreView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override public void onOpenRate() {
        AppHelper.openAppInPlayStore(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onOpenDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        if (getPresenter().canBackPress(drawerLayout)) {
            super.onBackPressed();
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e(requestCode, resultCode, requestCode == PermissionsHelper.OVERLAY_PERMISSION_REQ_CODE);
        if (requestCode == PermissionsHelper.OVERLAY_PERMISSION_REQ_CODE) {
            if (PermissionsHelper.isSystemAlertGranted(this)) onStartService();//start service since the user wanted to in the first time but check
            // first if the permission is granted here otherwise we will run into infinite settings screen being opened.
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().onHandleShortcuts(this, intent);
        AppInvite.AppInviteApi.getInvitation(getGoogleApiClient(), this, false).setResultCallback(getPresenter());
    }

    @NonNull private BadgeProvider getBadgeProvider() {
        if (badgeProvider == null) {
            badgeProvider = bottomNavigation.getBadgeProvider();
        }
        return badgeProvider;
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, int requestCode) {
        if (isOk) {
            if (requestCode == BACKUP_REQUEST_CODE || requestCode == RESTORE_REQUEST_CODE) {
                getPresenter().onBackupRestore(requestCode == BACKUP_REQUEST_CODE ? LoginView.BACKUP_TYPE : LoginView.RESTORE_TYPE, this);
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null/* never will occur, but then we would like to make sure*/) getPresenter().onShareUserBackup(this, user);
            }
        }
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.restore_function_not_available, Toast.LENGTH_LONG).show();
        Logger.e(connectionResult);
    }

    private GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(AppInvite.API)
                    .build();
        }
        return mGoogleApiClient;
    }
}
