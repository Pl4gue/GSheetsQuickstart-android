package com.github.pl4gue.mvp.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.pl4gue.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class BaseActivity extends AppCompatActivity {

    public void showError(String msg,Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public void showMessage(String msg,View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    static class DialogManagers {
        static class ProgressDialogManager {
            private static ProgressDialog mProgressDialog;
            private static int progressBarStatus = 0;
            private static Handler progressBarHandler = new Handler();
            private static int counter = 0;

            static void setUpProgressDialog(Context context) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage(context.getString(R.string.loading));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setProgress(0);
                mProgressDialog.setMax(100);
            }

            static void startProgressDialog(String title) {
                if (mProgressDialog != null) {
                    mProgressDialog.setMessage(title);
                    mProgressDialog.show();
                    progressBarStatus = 0;
                    counter = 0;
                    new Thread(() -> {
                        while (progressBarStatus < 100) {
                            progressBarStatus = counter;
                            counter += 1;
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBarHandler.post(() -> mProgressDialog.setProgress(progressBarStatus)
                            );
                        }
                    }
                    ).start();
                }
            }

            static void stopProgressDialog() {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
            }
        }

        static class DatePickerManager {
            static void datePickerDialog(Calendar calendar, Context context, EditText dueDateEditText) {
                DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(dueDateEditText, context, calendar);
                };
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }

            private static void updateLabel(EditText dueDateEditText, Context context, Calendar calendar) {
                if (calendar.getTime().before(Calendar.getInstance().getTime())) {
                    Snackbar.make(null,"Selected date is already over",Snackbar.LENGTH_LONG);
                    return;
                }

                String myFormat = "EEE, dd.MM.yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.GERMANY);

                dueDateEditText.setText(sdf.format(calendar.getTime()));
            }
        }
    }

}
