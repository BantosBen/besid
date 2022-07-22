package com.sanj.besid;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.sanj.besid.confirmation.BESIDConfirmationDialog;
import com.sanj.besid.error.BESIDErrorDialog;
import com.sanj.besid.exception.BESIDException;
import com.sanj.besid.info.BESIDInfoDialog;
import com.sanj.besid.success.BESIDSuccessDialog;
import com.sanj.besid.textInput.BESIDTextInputDialog;
import com.sanj.besid.warning.BESIDWarningDialog;
import com.sanj.besid.watchDog.BESIDWatchDogDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        findViewById(R.id.btnInputDialog).setOnClickListener(v -> textInputSample());
        findViewById(R.id.btnErrorDialog).setOnClickListener(v -> errorDialogSample());
        findViewById(R.id.btnWarningDialog).setOnClickListener(v -> warningDialogSample());
        findViewById(R.id.btnConfirmationDialog).setOnClickListener(v -> confirmationSample());
        findViewById(R.id.btnInfoDialog).setOnClickListener(v -> infoDialogSample());
        findViewById(R.id.btnSuccessDialog).setOnClickListener(v -> successDialogSample());
        findViewById(R.id.btnWatchDogDialog).setOnClickListener(v -> watchDogDialogSample());
    }

    private void textInputSample() {
        try {
            new BESIDTextInputDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage("Type target year")
//                    .setTitle("TextInput Dialog")
                    .setNegativeButton("Back", AppCompatDialog::dismiss)
                    .setPositiveButton("Submit", (dialog, userInput) -> {
                        Toast.makeText(MainActivity.this, userInput, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void errorDialogSample() {
        try {
            new BESIDErrorDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Contact the developer on this issue")
//                    .setTitle("Failed to delete the account")
                    .setActionButton("Hmm, okay", AppCompatDialog::dismiss)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void warningDialogSample() {
        try {
            new BESIDWarningDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Kindly ensure you have provided the right inputs. Please rectify and retry")
//                    .setTitle("Out of range!")
                    .setActionButton("Retry", AppCompatDialog::dismiss)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
            try {
                new BESIDWatchDogDialog.Builder(this)
                        .setCancelable(true)
                        .setThrowable(e)
                        .build()
                        .show();
            } catch (BESIDException besidException) {
                besidException.printStackTrace();
            }
        }
    }

    private void confirmationSample() {
        try {
            new BESIDConfirmationDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Are you sure you want to leave?")
                    .setTitle("Attempting to Logout")
                    .setNegativeButton("No, cancel", AppCompatDialog::dismiss)
                    .setPositiveButton("Yes, continue", AppCompatDialog::dismiss)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void infoDialogSample() {
        try {
            new BESIDInfoDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage("BESID(BEautiful and SImple Dialogs) is an upcoming dialog android library " +
                            "supported in Java and Kotlin languages. It provides a variety of dialogs for the " +
                            "developer to use during development. BESID is powered by SANJ Inc. Stay connected with" +
                            " us for the initial launch of the library ready for use. Updates will follow to make the" +
                            " library relevant. #BESID")
                    .setTitle("Random message")
                    .setActionButton("Okay, I understand", AppCompatDialog::dismiss)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void successDialogSample() {
        try {
            new BESIDSuccessDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Action Completed successfully")
                    .setActionButton("Okay", AppCompatDialog::dismiss)
                    .build()
                    .show();
        } catch (BESIDException e) {
            e.printStackTrace();
        }
    }

    private void watchDogDialogSample() {
        try {
            Integer.parseInt("io");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                new BESIDWatchDogDialog.Builder(this)
                        .setCancelable(true)
                        .setThrowable(e)
                        .build()
                        .show();
            } catch (BESIDException besidException) {
                besidException.printStackTrace();
            }
        }
    }
}