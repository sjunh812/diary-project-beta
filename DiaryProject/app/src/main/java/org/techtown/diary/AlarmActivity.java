package org.techtown.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import org.techtown.diary.custom.CustomTimePickerDialog;
import org.techtown.diary.helper.AlarmHelper;
import org.techtown.diary.helper.MyTheme;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {
    // 상수
    private static final String LOG = "AlarmActivity";
    public static final String SHARED_PREFERENCES_NAME2 = "pref2";  // 알림기능 관련 SharedPreference 이름
    public static final String IS_ALARM_KEY = "is_alarm_key";       // 알림기능 사용 여부 KEY
    public static final String HOUR_KEY = "hour_key";               // 알림 시간 KEY
    public static final String MINUTE_KEY = "minute_key";           // 알림 분 KEY

    // UI
    private RelativeLayout timeLayout;
    private CustomTimePickerDialog timePickerDialog;
    private Switch alarmSwitch;
    private TextView timeTextView;

    // Helper
    private AlarmManager alarmManager;
    private AlarmHelper alarmHelper;

    // 데이터
    private boolean isAlarm = false;
    private int hour = 22;
    private int minute = 0;
    private Calendar cal = Calendar.getInstance();
    private PendingIntent pendingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyTheme.applyTheme(this);
        setContentView(R.layout.activity_alarm);

        alarmHelper = new AlarmHelper(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("알림");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeTextView = (TextView)findViewById(R.id.timeTextView);

        alarmSwitch = (Switch)findViewById(R.id.alarmSwitch);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME2, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(IS_ALARM_KEY, isChecked);
                if(isChecked) {
                    editor.putInt(HOUR_KEY, hour);
                    editor.putInt(MINUTE_KEY, minute);
                }
                editor.commit();

                isAlarm = isChecked;
                setTimeLayout();
            }
        });

        timeLayout = (RelativeLayout)findViewById(R.id.timeLayout);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePickerDialog();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME2, Activity.MODE_PRIVATE);
        if(pref != null) {
            isAlarm = pref.getBoolean(IS_ALARM_KEY, false);
            hour = pref.getInt(HOUR_KEY, 22);
            minute = pref.getInt(MINUTE_KEY, 0);

            timePickerDialog = new CustomTimePickerDialog(this, hour, minute);

            setTimeLayout();
            setAlarmSwitch();
            setTimeTextView();
        } else {
            timePickerDialog = new CustomTimePickerDialog(this);
            setTimeLayout();
        }
    }

    private void setAlarmSwitch() {
        alarmSwitch.setChecked(isAlarm);
    }

    private void setTimeLayout() {
        if(isAlarm) {
            timeLayout.setVisibility(View.VISIBLE);
            alarmHelper.startAlarm(false);
            Log.d(LOG, "HOUR : " + hour + ",  MINUTE : " + minute);
        } else {
            timeLayout.setVisibility(View.GONE);
            alarmHelper.stopAlarm();
        }
    }

    private void setTimeTextView() {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        Date date = cal.getTime();

        String timeFormat = MainActivity.timeFormat.format(date);
        timeTextView.setText(timeFormat);
    }

    private void setTimePickerDialog() {
        timePickerDialog.show();

        timePickerDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.dismiss();
            }
        });

        timePickerDialog.setOkButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePickerDialog.get_hour();
                minute = timePickerDialog.get_minute();

                SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME2, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(IS_ALARM_KEY, true);
                editor.putInt(HOUR_KEY, hour);
                editor.putInt(MINUTE_KEY, minute);
                editor.commit();

                alarmHelper.startAlarm(false);
                setTimeTextView();
                timePickerDialog.dismiss();
            }
        });
    }

/*
    public void startAlarm() {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent rIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, rIntent, 0);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(cal.getTimeInMillis(), pendingIntent), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
    }

    private void stopAlarm() {
        if(alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);

            pendingIntent = null;
            alarmManager = null;
        }
    }
*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {   // 툴바 왼쪽 돌아가기 버튼 선택 시
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}