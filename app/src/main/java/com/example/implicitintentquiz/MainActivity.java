package com.example.implicitintentquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText event_title,event_desc,event_email,email_subject,email_recipient;
    TextView event_date,event_start_time,event_end_time,add_image;
    CheckBox cal_all_day,add_attach;
    Switch event_private;
    ImageView uploaded_image;
    Calendar calendarStart,calendarEnd;
    Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        event_title = findViewById(R.id.et_event_title);
        event_desc = findViewById(R.id.et_event_description);
        event_email = findViewById(R.id.et_event_email);
        email_subject = findViewById(R.id.et_email_subject);
        email_recipient = findViewById(R.id.et_email_recipient);
        event_date = findViewById(R.id.tv_date);
        event_start_time = findViewById(R.id.tv_start);
        event_end_time = findViewById(R.id.tv_end);
        cal_all_day = findViewById(R.id.checkbox_all_day);
        add_attach = findViewById(R.id.email_attach_ch);
        event_private = findViewById(R.id.private_event);
        uploaded_image = findViewById(R.id.uploaded_image);
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.HOUR,1);
        uploaded_image.setClipToOutline(true);
        add_image = findViewById(R.id.add_image);

        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dateFormat = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                event_date.setText(dateFormat);
                                calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendarStart.set(Calendar.MONTH,monthOfYear);
                                calendarStart.set(Calendar.YEAR,year);
                                calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendarEnd.set(Calendar.MONTH,monthOfYear);
                                calendarEnd.set(Calendar.YEAR,year);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        event_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String timeFormat = hourOfDay + ":" + minute;
                                event_start_time.setText(timeFormat);
                                calendarStart.set(Calendar.HOUR,hourOfDay);
                                calendarStart.set(Calendar.MINUTE,minute);
                                calendarStart.add(Calendar.HOUR,-12);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        event_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String timeFormat = hourOfDay + ":" + minute;
                                event_end_time.setText(timeFormat);
                                calendarEnd.set(Calendar.HOUR,hourOfDay);
                                calendarEnd.set(Calendar.MINUTE,minute);
                                calendarEnd.add(Calendar.HOUR,-12);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }

    public void addEvent(String title, String desciption, long begin, long end, boolean isAllDay, boolean isPrivate,String[] emails) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION,desciption)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,isAllDay)
                .putExtra(Intent.EXTRA_EMAIL,emails)
                .putExtra(CalendarContract.Events.ACCESS_LEVEL,
                        isPrivate?CalendarContract.Events.ACCESS_PRIVATE:CalendarContract.Events.ACCESS_PUBLIC);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void addEvent(View view) {
        String[] emails = event_email.getText().toString().split(",");
        addEvent(event_title.getText().toString(),
                    event_desc.getText().toString(),
                    calendarStart.getTimeInMillis(),
                    calendarEnd.getTimeInMillis(),
                    cal_all_day.isChecked(),
                    event_private.isChecked(),
                    emails);
    }

    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            uploaded_image.setImageBitmap(bitmap);
        }
    }

    public void addImage(View view) {
        capturePhoto();
    }

    public void composeEmail(String[] addresses, String subject, boolean isAttachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,"Sent from Prem's Android Application");
        if(isAttachment) intent.putExtra(Intent.EXTRA_STREAM,getImageUri(MainActivity.this,bitmap));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void sendMain(View view) {
        String[] emails = email_recipient.getText().toString().split(",");
        if(add_attach.isChecked()){
            if(bitmap == null) {
                Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                uploaded_image.startAnimation(shake);
                add_image.startAnimation(shake);
                return;
            }
            composeEmail(emails,email_subject.getText().toString(),true);
        }
        else{
            composeEmail(emails,email_subject.getText().toString(),false);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image", null);
        return Uri.parse(path);
    }
}
