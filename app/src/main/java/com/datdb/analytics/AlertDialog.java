package com.datdb.analytics;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AlertDialog extends Dialog {
    private OnAlertDialogListener onAlertDialogListener;
    private TextView tvCancel;
    private TextView tvOk;
    private TextView tvMessage;

    public AlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alert, null, false);
        setContentView(rootView);
        tvCancel = rootView.findViewById(R.id.tvCancel);
        tvOk = rootView.findViewById(R.id.tvOk);
        tvMessage = rootView.findViewById(R.id.tvMessage);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onAlertDialogListener != null) {
                    onAlertDialogListener.onCancelClickListener();
                }
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onAlertDialogListener != null) {
                    onAlertDialogListener.onOkClickListener();
                }
            }
        });
    }

    public void show(String message, OnAlertDialogListener onAlertDialogListener) {
        super.show();
        this.onAlertDialogListener = onAlertDialogListener;
        tvMessage.setText(message);
    }

    public interface OnAlertDialogListener {
        void onCancelClickListener();

        void onOkClickListener();
    }
}
