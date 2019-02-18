package com.kushnarev.learnproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kushnarev.learnproject.Adapters.TopicAdapter;
import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Courses.CourseEntry;
import com.kushnarev.learnproject.database.Topics.TopicEntry;

import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class TopicActivity extends AppCompatActivity implements TopicAdapter.ItemClickListener, TopicAdapter.ItemLongClickListener {

    // Extra for the course ID to be received in the intent
    public static final String EXTRA_COURSE_ID = "extraCourseId";
    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_COURSE_ID = -1;


    private int mCourseId = DEFAULT_COURSE_ID;
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TopicAdapter mAdapter;
    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_COURSE_ID)) {
            if (mCourseId == DEFAULT_COURSE_ID) {
                // populate the UI
                mCourseId = intent.getIntExtra(EXTRA_COURSE_ID, DEFAULT_COURSE_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final CourseEntry course = mDb.courseDao().loadCourseById(mCourseId);
                        // We will be able to simplify this once we learn more
                        // about Android Architecture Components
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(course);
                            }
                        });
                    }
                });
            }
        }

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTopics);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TopicAdapter(this, this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        FloatingActionButton fabButton = findViewById(R.id.fab_topic);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTopicActivity
                Intent addTopicIntent = new Intent(TopicActivity.this, AddTopicActivity.class);
                addTopicIntent.putExtra(AddTopicActivity.EXTRA_COURSE_ID, mCourseId);
                startActivity(addTopicIntent);
            }
        });


//        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_COURSE_ID)) {
//            mCourseId = savedInstanceState.getInt(INSTANCE_COURSE_ID, DEFAULT_COURSE_ID);
//        }

    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
//    private void initViews() {
//
//        String courseTitle = "Booo";
//        String text = String.format(getResources().getString(R.string.topic_activity_title), courseTitle);
//
//        mEditText = findViewById(R.id.new_course_title);
//        mButton = findViewById(R.id.saveButton);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSaveButtonClicked();
//            }
//        });
//    }
    @Override
    public void onItemClickListener(int itemId) {
        // Launch AddCourseActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(TopicActivity.this, ChunkActivity.class);
        intent.putExtra(ChunkActivity.EXTRA_TOPIC_ID, itemId);
        startActivity(intent);

    }

    @Override
    public void onItemLongClickListener(int itemId) {
        Intent intent = new Intent(TopicActivity.this, AddTopicActivity.class);
        intent.putExtra(AddTopicActivity.EXTRA_TOPIC_ID, itemId);
        intent.putExtra(AddTopicActivity.EXTRA_COURSE_ID, mCourseId);
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
                final List<TopicEntry> topics = mDb.topicDao().findTopicsForCourse(mCourseId);
                // We will be able to simplify this once we learn more
                // about Android Architecture Components
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTopics(topics);
                    }
                });
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param course the CourseEntry to populate the UI
     */
    private void populateUI(CourseEntry course) {
        if (course == null) {
            return;
        }

//        String text = String.format(getResources().getString(R.string.topic_activity_title), course.getTitle());
        String text = course.getTitle() + " topics";
        setTitle(text);
//        mEditText.setText(task.getDescription());
//        setPriorityInViews(task.getPriority());
    }


}
