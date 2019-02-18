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

import java.util.Date;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    // Extra for the course ID to be received in the intent
    public static final String EXTRA_COURSE_ID = "extraCourseId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_COURSE_ID = "instanceCourseId";

    // Constant for default course id to be used when not in update mode
    private static final int DEFAULT_COURSE_ID = -1;
    // Constant for logging
    private static final String TAG = AddCourseActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    Button mButton;
    Button mButtonDelete;

    private int mCourseId = DEFAULT_COURSE_ID;

    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_COURSE_ID)) {
            mCourseId = savedInstanceState.getInt(INSTANCE_COURSE_ID, DEFAULT_COURSE_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_COURSE_ID)) {
            mButton.setText(R.string.update_button);
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

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_COURSE_ID, mCourseId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.new_course_title);
        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        mButtonDelete = findViewById(R.id.deleteButton);
        mButtonDelete.setVisibility(View.GONE);
    }


    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new course data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String title = mEditText.getText().toString();

        final String toastMessage;
        final CourseEntry course = new CourseEntry(title);// call the diskIO execute method with a new Runnable and implement its run method
        final boolean create = mCourseId == DEFAULT_COURSE_ID;
        toastMessage = course.getTitle() + " was " + (create ? "created!" : "updated!");
        Toast toast = Toast.makeText(getApplicationContext(),
                toastMessage,
                Toast.LENGTH_SHORT);
        toast.show();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (create) {
                    mDb.courseDao().insertCourse(course);
                } else {
                    course.setId(mCourseId);
                    mDb.courseDao().updateCourse(course);
                }
                finish();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param course the CourseEntry to populate the UI
     */
    private void populateUI(final CourseEntry course) {
        if (course == null) {
            return;
        }
        setTitle("Edit course");
        mEditText.setText(course.getTitle());
        mButtonDelete.setVisibility(View.VISIBLE);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.courseDao().deleteCourse(course);
                        finish();
//                        retrieveTasks();
                    }
                });
            }
        });

    }
}
