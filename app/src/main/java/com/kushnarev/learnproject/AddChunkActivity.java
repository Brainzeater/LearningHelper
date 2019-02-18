package com.kushnarev.learnproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Chunks.ChunkEntry;

public class AddChunkActivity extends AppCompatActivity {
    // Extra for the topic ID to be received in the intent
    public static final String EXTRA_TOPIC_ID = "extraTopicId";
    public static final String EXTRA_CHUNK_ID = "extraChunkId";
    // Extra for the topic ID to be received after rotation
    public static final String INSTANCE_CHUNK_ID = "instanceChunkId";

    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_CHUNK_ID = -1;
    private static final int DEFAULT_TOPIC_ID = -1;
    // Constant for logging
    private static final String TAG = AddCourseActivity.class.getSimpleName();
    private static final int MY_CAL_WRITE_REQ = 1;
    // Fields for views
    TextView mChunkName;
    EditText mEditText;
    TextView mChunkContent;
    EditText mContent;

    Button mButton;
    Button mButtonDelete;

    private int mChunkId = DEFAULT_CHUNK_ID;

    private int topicId;
    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chunk);
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_CHUNK_ID)) {
            mChunkId = savedInstanceState.getInt(INSTANCE_CHUNK_ID, DEFAULT_CHUNK_ID);
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_TOPIC_ID)) {
                topicId = intent.getIntExtra(EXTRA_TOPIC_ID, DEFAULT_TOPIC_ID);
            }
            if (intent.hasExtra(EXTRA_CHUNK_ID)) {
                mButton.setText(R.string.update_button);
                if (mChunkId == DEFAULT_CHUNK_ID) {
                    // populate the UI
                    mChunkId = intent.getIntExtra(EXTRA_CHUNK_ID, DEFAULT_CHUNK_ID);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            final ChunkEntry chunk = mDb.chunkDao().loadChunkById(mChunkId);
                            // We will be able to simplify this once we learn more
                            // about Android Architecture Components
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    populateUI(chunk);
                                }
                            });
                        }
                    });
                }

            }
//            if (mCourseId == DEFAULT_COURSE_ID) {
//                // populate the UI
//                mCourseId = intent.getIntExtra(EXTRA_COURSE_ID, DEFAULT_COURSE_ID);
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        final CourseEntry course = mDb.courseDao().loadCourseById(mCourseId);
//                        // We will be able to simplify this once we learn more
//                        // about Android Architecture Components
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                populateUI(course);
//                            }
//                        });
//                    }
//                });
//            }
        }

//        addCalendar();


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_CHUNK_ID, mChunkId);
        super.onSaveInstanceState(outState);
    }
    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mChunkName = findViewById(R.id.tv_chunk_name);
        mEditText = findViewById(R.id.new_chunk_title);
        mChunkContent = findViewById(R.id.tv_chunk_content);
        mContent = findViewById(R.id.et_chunk_content);
        mButton = findViewById(R.id.saveChunkButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        mButtonDelete = findViewById(R.id.deleteButtonChunk);
        mButtonDelete.setVisibility(View.GONE);

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new course data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String title = mEditText.getText().toString();
        String descriprion = mContent.getText().toString();
        String toastMessage;

        final ChunkEntry chunk = new ChunkEntry(title, topicId, descriprion);// call the diskIO execute method with a new Runnable and implement its run method
        final boolean create = mChunkId == DEFAULT_CHUNK_ID;
        toastMessage = chunk.getChunkName() + " was " + (create ? "created!" : "updated!");
        Toast toast = Toast.makeText(getApplicationContext(),
                toastMessage,
                Toast.LENGTH_SHORT);
        toast.show();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (create) {
                    mDb.chunkDao().insertChunk(chunk);
                } else {
                    chunk.setId(mChunkId);
                    mDb.chunkDao().updateChunk(chunk);
                }
                finish();
            }
        });
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                if (mTaskId == DEFAULT_TASK_ID) {
//                    // insert new task
//                    mDb.taskDao().insertTask(task);
//                } else {
//                    //update task
//                    task.setId(mTaskId);
//                    mDb.taskDao().updateTask(task);
//                }
//                finish();
//            }
//        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param chunk the CourseEntry to populate the UI
     */
    private void populateUI(final ChunkEntry chunk) {
        if (chunk == null) {
            return;
        }
        setTitle("Edit chunk");
        mEditText.setText(chunk.getChunkName());
        mContent.setText(chunk.getDescription());
        mButtonDelete.setVisibility(View.VISIBLE);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.chunkDao().deleteChunk(chunk);
                        finish();
//                        retrieveTasks();
                    }
                });
            }
        });

    }
//    public void

    public void addCalendar() {

        ContentValues contentValues = new ContentValues();
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
        }

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com").build();
        getContentResolver().insert(uri, contentValues);
    }
}
