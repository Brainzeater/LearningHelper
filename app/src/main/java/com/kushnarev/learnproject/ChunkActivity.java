package com.kushnarev.learnproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kushnarev.learnproject.Adapters.ChunkAdapter;
import com.kushnarev.learnproject.Adapters.TopicAdapter;
import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Chunks.ChunkEntry;
import com.kushnarev.learnproject.database.Topics.TopicEntry;
import com.kushnarev.learnproject.utilities.NotificationUtils;

import java.util.List;
import java.util.Locale;

import static android.widget.LinearLayout.VERTICAL;

public class ChunkActivity extends AppCompatActivity implements ChunkAdapter.ItemClickListener, ChunkAdapter.ItemLongClickListener {


    // Extra for the course ID to be received in the intent
    public static final String EXTRA_TOPIC_ID = "extraCourseId";
    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_TOPIC_ID = -1;


    private int mTopicId = DEFAULT_TOPIC_ID;
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private ChunkAdapter mAdapter;
    // Member variable for the Database
    private AppDatabase mDb;

    private CountDownTimer mCountDownTimer;

    TextView mTimerTextView;
    TextView mTime;
    ImageButton mPlay;
    ImageButton mPause;
    Button mStop;

    TopicEntry mTopic;
    private int currentTopicId = DEFAULT_TOPIC_ID;
//        private static final long START_TIME_IN_MILLIS = 25 * 60* 1000;
    private static final long START_TIME_IN_MILLIS = 10 * 1000;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chunk);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TOPIC_ID)) {
            if (mTopicId == DEFAULT_TOPIC_ID) {
                // populate the UI
                mTopicId = intent.getIntExtra(EXTRA_TOPIC_ID, DEFAULT_TOPIC_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final TopicEntry topic = mDb.topicDao().loadTopicById(mTopicId);
                        // We will be able to simplify this once we learn more
                        // about Android Architecture Components
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(topic);
                            }
                        });
                    }
                });
            }
        }

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewChunks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new ChunkAdapter(this, this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        FloatingActionButton fabButton = findViewById(R.id.fab_chunk);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTopicActivity
                Intent addChunkIntent = new Intent(ChunkActivity.this, AddChunkActivity.class);
                addChunkIntent.putExtra(AddChunkActivity.EXTRA_TOPIC_ID, mTopicId);
                startActivity(addChunkIntent);
            }
        });

        mTimerTextView = findViewById(R.id.tv_timer);
        mPlay = findViewById(R.id.ib_play);
        mPlay.setEnabled(true);
        mPlay.setBackgroundResource(R.color.colorAccent);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                startTimer();

            }
        });
        mPause = findViewById(R.id.ib_pause);
        mPause.setEnabled(false);
        mPause.setBackgroundResource(R.color.colorDividerColor);
        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });
        mStop = findViewById(R.id.b_stop);
        mStop.setEnabled(false);
        mStop.setBackgroundResource(R.color.colorDividerColor);
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
//        updateCountDownText();
        mTime = findViewById(R.id.tv_time);
//        mTime.setText();
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(ChunkActivity.this, StudyChunkActivity.class);
        intent.putExtra(StudyChunkActivity.EXTRA_CHUNK_ID, itemId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int itemId) {

        Intent intent = new Intent(ChunkActivity.this, AddChunkActivity.class);
        intent.putExtra(AddChunkActivity.EXTRA_CHUNK_ID, itemId);
        intent.putExtra(AddTopicActivity.EXTRA_TOPIC_ID, mTopicId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call the diskIO execute method with a new Runnable and implement its run method
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Extract the list of tasks to a final variable
                final List<ChunkEntry> chunks = mDb.chunkDao().findChunksForTopic(mTopicId);
                // We will be able to simplify this once we learn more
                // about Android Architecture Components
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setChunks(chunks);
                    }
                });
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param topic the CourseEntry to populate the UI
     */
    private void populateUI(TopicEntry topic) {
        if (topic == null) {
            return;
        }

//        String text = String.format(getResources().getString(R.string.topic_activity_title), topic.getTopicName());
        String text = topic.getTopicName() + " chunks";
        long time = topic.getTime();
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTime.setText(timeLeftFormatted);
        setTitle(text);
        mTopic = topic;
//        mEditText.setText(task.getDescription());
//        setPriorityInViews(task.getPriority());
    }

    @SuppressLint("ResourceAsColor")
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
//        long nextTime = START_TIME_IN_MILLIS;  //Repeat alarm time in seconds
        AlarmManager processTimer = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(ChunkActivity.this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChunkActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Repeat alarm every second
        processTimer.set(AlarmManager.RTC_WAKEUP, mEndTime, pendingIntent);
//                processTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),repeatTime*1000, pendingIntent);
        System.out.println("Alarm is called");
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
//                mCountDownTimer.cancel();
                final TopicEntry topic = mTopic;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        long current = topic.getTime();
                        topic.setTime(current + START_TIME_IN_MILLIS);
                        mDb.topicDao().updateTopic(topic);
                        populateUI(topic);
                    }
                });
                updateCountDownText();
                updateButtons();

//                NotificationUtils.remindUser(ChunkActivity.this);
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        Intent intent = new Intent(ChunkActivity.this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChunkActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTimerTextView.setText(timeLeftFormatted);
    }

    @SuppressLint("ResourceAsColor")
    private void updateButtons() {
        if (mTimerRunning) {
            mPlay.setEnabled(false);
            mPlay.setBackgroundResource(R.color.colorDividerColor);
            mPause.setEnabled(true);
            mPause.setBackgroundResource(R.color.colorAccent);
            mStop.setEnabled(true);
            mStop.setBackgroundResource(R.color.colorAccent);
        } else {
            if (mTimeLeftInMillis < 1000) {
//                mButtonStartPause.setVisibility(View.INVISIBLE);

                mPause.setEnabled(false);
                mPause.setBackgroundResource(R.color.colorDividerColor);
            } else {
//                mButtonStartPause.setVisibility(View.VISIBLE);

                mPause.setEnabled(true);
                mPause.setBackgroundResource(R.color.colorAccent);
            }

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
//                mButtonReset.setVisibility(View.VISIBLE);

                mStop.setEnabled(true);
                mStop.setBackgroundResource(R.color.colorAccent);
            } else {
//                mButtonReset.setVisibility(View.INVISIBLE);

                mStop.setEnabled(false);
                mStop.setBackgroundResource(R.color.colorDividerColor);
            }

            mPlay.setEnabled(true);
            mPlay.setBackgroundResource(R.color.colorAccent);
            mPause.setEnabled(false);
            mPause.setBackgroundResource(R.color.colorDividerColor);
        }
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mTimerRunning = false;
        mCountDownTimer.cancel();
        Intent intent = new Intent(ChunkActivity.this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChunkActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        updateCountDownText();
        updateButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        // TODO: Test
//        currentTopicId = prefs.getInt("topicId", DEFAULT_TOPIC_ID);
//        if (currentTopicId != DEFAULT_TOPIC_ID) {
//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    final TopicEntry topic = mDb.topicDao().loadTopicById(currentTopicId);
//                    // We will be able to simplify this once we learn more
//                    // about Android Architecture Components
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            populateUI(topic);
//                        }
//                    });
//                }
//            });
//        }
        System.out.println(currentTopicId + " WO-HO!");
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        // TODO: Test
//        editor.putInt("topicId", mTopicId);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


}


