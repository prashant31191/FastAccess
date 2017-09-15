package com.fasty.data.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasty.data.dao.events.FloatingEventModel;
import com.fasty.data.dao.events.FolderEventModel;
import com.fasty.data.dao.events.FragmentFolderEventModel;
import com.fasty.data.dao.events.SelectedAppsEventModel;
import com.fasty.data.dao.events.ThemePackEventModel;
import com.fasty.helper.PrefHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kosh on 23 Oct 2016, 8:51 PM
 */

public class BackupRestoreModel {

    private String uid;
    private List<FolderModel> folders;
    private List<AppsModel> appsModels;
    private Map<String, Object> settings;
    private String backupDate;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<FolderModel> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderModel> folders) {
        this.folders = folders;
    }

    public List<AppsModel> getAppsModels() {
        return appsModels;
    }

    public void setAppsModels(List<AppsModel> appsModels) {
        this.appsModels = appsModels;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public String getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }

    @Nullable public static BackupRestoreModel backup() {
        BackupRestoreModel model = new BackupRestoreModel();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return null;
        model.setUid(firebaseUser.getUid());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mma", Locale.getDefault());
        model.setBackupDate(simpleDateFormat.format(new Date().getTime()));
        model.setFolders(FolderModel.getFolders());
        model.setSettings(PrefHelper.getAll());
        model.setAppsModels(AppsModel.getApps());
        return model;
    }

    public static void restore(@NonNull BackupRestoreModel model) {
        if (model.getFolders() != null) {
            FolderModel.saveInTx(model.getFolders());
            EventBus.getDefault().post(new FolderEventModel()); // update Floating Folders if any.
            EventBus.getDefault().post(new FragmentFolderEventModel()); // update MyFolders fragment separately
        }
        if (model.getAppsModels() != null) {
            AppsModel.saveInTx(model.getAppsModels());
            EventBus.getDefault().post(new FloatingEventModel());// update selected apps if any
            EventBus.getDefault().post(new SelectedAppsEventModel());
        }
        if (model.getSettings() != null) {
            for (String key : model.getSettings().keySet()) {
                if (key != null && !key.equalsIgnoreCase("null")) {
                    PrefHelper.set(key, model.getSettings().get(key));
                }
            }
            EventBus.getDefault().post(new ThemePackEventModel());//update icon theme
        }
    }

    @Override public String toString() {
        return "BackupRestoreModel{" +
                "uid='" + uid + '\'' +
                ", settings='" + settings + '\'' +
                '}';
    }
}
