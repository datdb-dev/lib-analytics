package com.datdb.analytics;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;


public class AnalyticsManager {
    public static final String PACKAGE_NAME_NEW_APP = "package_name_new_app";
    private static AnalyticsManager instance;
    private static FirebaseAnalytics firebaseAnalytics;

    private AnalyticsManager() {
    }

    public static void init(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        instance = new AnalyticsManager();
        SharedUtils.init(context);
    }

    public static AnalyticsManager getInstance() {
        return instance;
    }

    public void logEvent(String s, Bundle bundle) {
        firebaseAnalytics.logEvent(s, bundle);
    }

    public void check(Activity context) {
        String packageName = SharedUtils.getInstance().getString(PACKAGE_NAME_NEW_APP, "");
        if (!packageName.equals("")) {
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
