package com.fasty.provider.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fasty.helper.PermissionsHelper;
import com.fasty.helper.PrefConstant;
import com.fasty.provider.service.FloatingService;

public class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (PrefConstant.isAutoStart() && PermissionsHelper.isSystemAlertGranted(context)) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent serviceIntent = new Intent(context, FloatingService.class);
                context.startService(serviceIntent);
            }
        }
    }
}
