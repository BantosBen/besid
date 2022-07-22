package com.sanj.besid.success;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import com.sanj.besid.R;
import com.sanj.besid.exception.BESIDException;

public class BESIDSuccessDialog extends AlertDialog {
    private final Params params;
    private final AlertDialog dialog;
    private CharSequence message;
    private TextView txtMessage, actionButton;

    private BESIDSuccessDialog(@NonNull Params params) {
        super(params.context);
        this.params = params;
        this.message = params.messageId != 0 ? params.context.getString(params.messageId) : params.message;
        setCancelable(params.cancelable);
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.success_dialog);
            txtMessage = findViewById(R.id.message);
            actionButton = findViewById(R.id.actionButton);
            setCanceledOnTouchOutside(false);

            initText();
            initClicks();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void initClicks() {

        actionButton.setText(params.buttonText);
        actionButton.setOnClickListener(v -> params.onClickListener.onClick(dialog));
    }

    @Override
    public void setMessage(CharSequence message) {
        this.message = message;
        try {
            if (isShowing()) initText();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    //~

    private void initText() throws BESIDException {
        if (message != null && message.length() > 0) {
            txtMessage.setText(message);
        } else {
            throw new BESIDException("dialog message cannot be null");
        }
    }

    public interface BESIDSuccessDialogInterface {
        interface OnClickListener {
            void onClick(AlertDialog dialog);
        }
    }

    public static class Builder {
        private final Params params;

        public Builder(Context context) {
            params = new Params();
            params.context = context;
        }

        public Builder setMessage(String message) {
            params.message = message;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            params.messageId = messageId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }

        public Builder setActionButton(String buttonText, BESIDSuccessDialogInterface.OnClickListener onClickListener) {
            params.buttonText = buttonText;
            params.onClickListener = onClickListener;
            return this;
        }

        public Builder setActionButton(int buttonTextId, BESIDSuccessDialogInterface.OnClickListener onClickListener) {
            params.buttonText = params.context.getString(buttonTextId);
            params.onClickListener = onClickListener;
            return this;
        }


        public AlertDialog build() throws BESIDException {
            if (params.onClickListener == null) {
                throw new BESIDException("didn't set the action button");
            }
            return new BESIDSuccessDialog(params);
        }

    }

    private static class Params {
        public Context context;
        public String message = "", buttonText = "Okay";
        public int messageId = 0;
        public boolean cancelable = true; //default dialog behavior
        public BESIDSuccessDialogInterface.OnClickListener onClickListener = AppCompatDialog::dismiss;
    }
}
