package com.sanj.besid.watchDog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.sanj.besid.R;
import com.sanj.besid.exception.BESIDException;

public class BESIDWatchDogDialog extends AlertDialog {
    private final Params params;
    private final AlertDialog dialog;
    private TextView actionButton, title, trace;

    private BESIDWatchDogDialog(@NonNull Params params) {
        super(params.context);
        this.params = params;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_dog_dialog);
        title = findViewById(R.id.title);
        trace = findViewById(R.id.trace);
        actionButton = findViewById(R.id.actionButton);
        setCanceledOnTouchOutside(false);
        dialog.setCancelable(params.cancelable);

        initText();
        initClicks();
    }

    @SuppressLint("SetTextI18n")
    private void initText() {
        String stringTrace = Log.getStackTraceString(params.throwable);
        trace.setText(stringTrace);
        String type = params.throwable.toString();
        String errorType;
        if (type.contains(":")) {
            String[] typeParts = type.split(":");
            errorType = typeParts[0];
        } else {
            errorType = type;
        }

        title.setText("ERROR TYPE: " + errorType);
    }

    private void initClicks() {
        actionButton.setOnClickListener(v -> dialog.dismiss());
    }

    public static class Builder {
        private final Params params;

        public Builder(Context context) {
            params = new Params();
            params.context = context;
        }

        public Builder setThrowable(Throwable throwable) {
            params.throwable = throwable;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }

        public AlertDialog build() throws BESIDException {
            if (params.throwable == null) {
                throw new BESIDException("didn't set throwable to print the stack trace");
            }
            return new BESIDWatchDogDialog(params);
        }

    }

    private static class Params {
        public Context context;
        public Throwable throwable;
        public boolean cancelable = true; //default dialog behavior
    }
}
