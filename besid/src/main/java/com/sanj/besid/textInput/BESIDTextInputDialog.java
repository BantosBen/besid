package com.sanj.besid.textInput;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import com.sanj.besid.R;
import com.sanj.besid.exception.BESIDException;

public class BESIDTextInputDialog extends AlertDialog {
    private final Params params;
    private final AlertDialog dialog;
    private CharSequence message, title;
    private TextView txtTitle, txtMessage, negativeBtn, positiveBtn;
    private EditText userInput;

    private BESIDTextInputDialog(@NonNull Params params) {
        super(params.context);
        this.params = params;
        this.message = params.messageId != 0 ? params.context.getString(params.messageId) : params.message;
        this.title = params.titleId != 0 ? params.context.getString(params.titleId) : params.title;
        setCancelable(params.cancelable);
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.text_input_dialog);
            txtTitle = findViewById(R.id.text_input_title);
            txtMessage = findViewById(R.id.text_input_prompt);
            negativeBtn = findViewById(R.id.text_input_negative_button);
            positiveBtn = findViewById(R.id.text_input_positive_button);
            userInput = findViewById(R.id.text_input_edittext);
            setCanceledOnTouchOutside(false);
            if (params.inputType != 0) {
                userInput.setInputType(params.inputType);
            }

            initText();
            initClicks();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        userInput.requestFocus();
    }

    private void initClicks() {

        negativeBtn.setText(params.negativeButtonText);
        positiveBtn.setText(params.positiveButtonText);
        negativeBtn.setOnClickListener(v -> params.onNegativeButtonClickListener.onClick(dialog));
        positiveBtn.setOnClickListener(v -> params.onPositiveButtonClickListener.onClick(dialog, userInput.getText().toString()));
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
        } else {
            throw new BESIDException("dialog message cannot be null");
        }
        if (title != null) {
            txtTitle.setText(title);
        } else {
            throw new BESIDException("dialog title cannot be null");
        }
    }

    public interface BESIDTextInputInterface {
        interface OnNegativeButtonClickListener {
            void onClick(AlertDialog dialog);
        }

        interface OnPositiveButtonClickListener {
            void onClick(AlertDialog dialog, String userInput);
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

        public Builder setInputType(int inputType) {
            params.inputType = inputType;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, BESIDTextInputInterface.OnNegativeButtonClickListener onNegativeButtonClickListener) {
            params.negativeButtonText = negativeButtonText;
            params.onNegativeButtonClickListener = onNegativeButtonClickListener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonTextID, BESIDTextInputInterface.OnNegativeButtonClickListener onNegativeButtonClickListener) {
            params.negativeButtonText = params.context.getString(negativeButtonTextID);
            params.onNegativeButtonClickListener = onNegativeButtonClickListener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, BESIDTextInputInterface.OnPositiveButtonClickListener onPositiveButtonClickListener) {
            params.positiveButtonText = positiveButtonText;
            params.onPositiveButtonClickListener = onPositiveButtonClickListener;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonTextID, BESIDTextInputInterface.OnPositiveButtonClickListener onPositiveButtonClickListener) {
            params.positiveButtonText = params.context.getString(positiveButtonTextID);
            params.onPositiveButtonClickListener = onPositiveButtonClickListener;
            return this;
        }

        public AlertDialog build() throws BESIDException {
            if (params.onNegativeButtonClickListener == null || params.onPositiveButtonClickListener == null) {
                throw new BESIDException("didn't set both the buttons");
            }
            return new BESIDTextInputDialog(params);
        }

    }

    private static class Params {
        public int inputType = 0;
        public Context context;
        public String message = "", title = "Input Data", negativeButtonText = "Back", positiveButtonText = "Submit";
        public int messageId = 0, titleId = 0;
        public boolean cancelable = true; //default dialog behavior
        public BESIDTextInputDialog.BESIDTextInputInterface.OnNegativeButtonClickListener onNegativeButtonClickListener = AppCompatDialog::dismiss;
        public BESIDTextInputDialog.BESIDTextInputInterface.OnPositiveButtonClickListener onPositiveButtonClickListener = (dialog, userInput) -> dialog.dismiss();
    }
}
