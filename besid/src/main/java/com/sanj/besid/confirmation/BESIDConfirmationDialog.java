package com.sanj.besid.confirmation;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.sanj.besid.R;
import com.sanj.besid.exception.BESIDException;

public class BESIDConfirmationDialog extends AlertDialog {
    private CharSequence message, title;
    private TextView txtTitle, txtMessage, negativeBtn,positiveBtn;
    private final Params params;
    private final AlertDialog dialog;

    private BESIDConfirmationDialog(@NonNull Params params) {
        super(params.context);
        this.params=params;
        this.message = params.messageId != 0 ? params.context.getString(params.messageId) : params.message;
        this.title = params.titleId != 0 ? params.context.getString(params.titleId) : params.title;
        setCancelable(params.cancelable);
        dialog=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_dialog);
        try {
            txtTitle = findViewById(R.id.title);
            txtMessage = findViewById(R.id.message);
            negativeBtn = findViewById(R.id.negative_button);
            positiveBtn = findViewById(R.id.positive_button);
            setCanceledOnTouchOutside(false);

            initText();
            initClicks();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }
    private void initClicks() {
        negativeBtn.setText(params.negativeButtonText);
        positiveBtn.setText(params.positiveButtonText);
        negativeBtn.setOnClickListener(v -> params.onNegativeButtonClickListener.onClick(dialog));
        positiveBtn.setOnClickListener(v -> params.onPositiveButtonClickListener.onClick(dialog));
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

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
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
        }else{
            throw new BESIDException("dialog message cannot be null");
        }
        if (title != null && title.length() > 0) {
            txtTitle.setText(title);
        }else{
            throw new BESIDException("dialog title cannot be null");
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

        public Builder setTitle(@StringRes int titleId) {
            params.titleId = titleId;
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, BESIDConfirmationInterface.OnClickListener onNegativeButtonClickListener) {
            params.negativeButtonText = negativeButtonText;
            params.onNegativeButtonClickListener = onNegativeButtonClickListener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonTextID, BESIDConfirmationInterface.OnClickListener onNegativeButtonClickListener) {
            params.negativeButtonText = params.context.getString(negativeButtonTextID);
            params.onNegativeButtonClickListener = onNegativeButtonClickListener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, BESIDConfirmationInterface.OnClickListener onPositiveButtonClickListener) {
            params.positiveButtonText = positiveButtonText;
            params.onPositiveButtonClickListener = onPositiveButtonClickListener;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonTextID, BESIDConfirmationInterface.OnClickListener onPositiveButtonClickListener) {
            params.positiveButtonText = params.context.getString(positiveButtonTextID);
            params.onPositiveButtonClickListener = onPositiveButtonClickListener;
            return this;
        }

        public AlertDialog build() throws BESIDException {
            if (params.onNegativeButtonClickListener == null || params.onPositiveButtonClickListener==null) {
                throw new BESIDException("didn't set both the buttons");
            }
            return new BESIDConfirmationDialog(params);
        }

    }

    private static class Params {
        public Context context;
        public String message, title, negativeButtonText = "No, cancel", positiveButtonText = "Yes, continue";
        public int messageId = 0, titleId = 0;
        public boolean cancelable = true; //default dialog behavior
        public BESIDConfirmationInterface.OnClickListener onNegativeButtonClickListener = null;
        public BESIDConfirmationInterface.OnClickListener onPositiveButtonClickListener = null;
    }

    public interface BESIDConfirmationInterface {
        interface OnClickListener {
            void onClick(AlertDialog dialog);
        }
    }
}
