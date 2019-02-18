package com.kushnarev.learnproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Courses.CourseEntry;
import com.kushnarev.learnproject.database.Topics.TopicEntry;

public class AddTopicActivity extends AppCompatActivity {
    // Extra for the topic ID to be received in the intent
    public static final String EXTRA_COURSE_ID = "extraCourseId";
    public static final String EXTRA_TOPIC_ID = "extraTopicId";
    // Extra for the topic ID to be received after rotation
    public static final String INSTANCE_TOPIC_ID = "instanceTopicId";

    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_TOPIC_ID = -1;
    private static final int DEFAULT_COURSE_ID = -1;
    // Constant for logging
    private static final String TAG = AddCourseActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    Button mButton;
    Button mButtonDelete;

    private int mTopicId = DEFAULT_TOPIC_ID;

    private int courseId;
    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TOPIC_ID)) {
            mTopicId = savedInstanceState.getInt(INSTANCE_TOPIC_ID, DEFAULT_TOPIC_ID);
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_COURSE_ID)) {
                courseId = intent.getIntExtra(EXTRA_COURSE_ID, DEFAULT_COURSE_ID);
            }
            if (intent.hasExtra(EXTRA_TOPIC_ID)) {
                mButton.setText(R.string.update_button);
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

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TOPIC_ID, mTopicId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.new_topic_title);
        mButton = findViewById(R.id.saveTopicButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        mButtonDelete = findViewById(R.id.deleteButtonTopic);
        mButtonDelete.setVisibility(View.GONE);
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new course data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String title = mEditText.getText().toString();
        String toastMessage;

        final TopicEntry topic = new TopicEntry(title, courseId);// call the diskIO execute method with a new Runnable and implement its run method
        final boolean create = mTopicId == DEFAULT_TOPIC_ID;
        toastMessage = topic.getTopicName() + " was " + (create ? "created!" : "updated!");
        Toast toast = Toast.makeText(getApplicationContext(),
                toastMessage,
                Toast.LENGTH_SHORT);
        toast.show();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (create) {
                    mDb.topicDao().insertTopic(topic);
                } else {
                    topic.setId(mTopicId);
                    mDb.topicDao().updateTopic(topic);
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
     * @param topic the CourseEntry to populate the UI
     */
    private void populateUI(final TopicEntry topic) {
        if (topic == null) {
            return;
        }
        setTitle("Edit topic");
        mEditText.setText(topic.getTopicName());
        mButtonDelete.setVisibility(View.VISIBLE);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.topicDao().deleteTopic(topic);
                        finish();
//                        retrieveTasks();
                    }
                });
            }
        });

    }
}
