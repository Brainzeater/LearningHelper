package com.kushnarev.learnproject;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Chunks.ChunkEntry;
public class StudyChunkActivity extends AppCompatActivity {


    public static final String EXTRA_CHUNK_ID = "extraChunkId";

    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_CHUNK_ID = -1;

    private int mChunkId = DEFAULT_CHUNK_ID;

    TextView mChunkTitle;
    TextView mChunkContent;
    Button mButton;
    Button testButton;
    // Member variable for the Database
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chunk);
        mChunkContent = findViewById(R.id.tv_chunk_content);
        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_CHUNK_ID)) {
            if (mChunkId == DEFAULT_CHUNK_ID) {
                mChunkId = intent.getIntExtra(EXTRA_CHUNK_ID, DEFAULT_CHUNK_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final ChunkEntry chunkEntry = mDb.chunkDao().loadChunkById(mChunkId);
                        // We will be able to simplify this once we learn more
                        // about Android Architecture Components
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(chunkEntry);
                            }
                        });
                    }
                });
            }
        }


    }

//    public void testNotification(View view) {
//        NotificationUtils.remindUser(this, mChunkId);
//
//    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param chunkEntry the CourseEntry to populate the UI
     */
    private void populateUI(ChunkEntry chunkEntry) {
        if (chunkEntry == null) {
            return;
        }
        mChunkContent.setText(chunkEntry.getDescription());
//        TODO: get chunk name
        setTitle(chunkEntry.getChunkName());
    }

    public void stupidCalendar() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "This is a message displayed in a Toast",
                Toast.LENGTH_SHORT);
        toast.show();
        /*ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com");
        contentValues.put(CalendarContract.Calendars.NAME, "zoftino calendar");
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Zoftino.com Calendar");
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, "232323");
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "cal@zoftino.com");
        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }*/
//        ContentResolver resolver = getContentResolver();
//
//        ContentValues contentValues = new ContentValues();
////        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "brainzeater@gmail.com");
//        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
////        Cursor cursor = resolver.insert(CalendarContract.Calendars.CONTENT_URI, values, null, null);
//        Cursor cursor = resolver.insert(CalendarContract.Calendars.CONTENT_URI, contentValues);
    }


}
