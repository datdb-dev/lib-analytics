package com.js.analytics;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.js.analytics.callback.OnRemoteConfigListener;
import com.js.analytics.dialog.AlertDialog;

public class RemoteConfigManager {
    private static final String PACKAGE_NAME = "package_name";

    private static RemoteConfigManager instance;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    public static void init() {
        instance = new RemoteConfigManager();
    }

    public static RemoteConfigManager getInstance() {
        return instance;
    }

    private RemoteConfigManager() {
    }

    public void check(Activity activity, OnRemoteConfigListener onRemoteConfigListener) {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    String packageName = firebaseRemoteConfig.getString(PACKAGE_NAME);
                    boolean dead = !packageName.equals("");
                    onRemoteConfigListener.onSuccess(dead);
                    if (dead) {
                        showAlert(activity, packageName);
                    }
                }
            }
        });
    }

    private void showAlert(Activity context, String packageName) {
        AlertDialog alertDialog = new AlertDialog(context);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show("Install new application to use, latest updates", new AlertDialog.OnAlertDialogListener() {
            @Override
            public void onCancelClickListener() {
                context.finish();
            }

            @Override
            public void onOkClickListener() {
                openMarket(context, packageName);
                context.finish();
            }
        });
    }

    private void openMarket(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        } catch (ActivityNotFoundException ignored) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
