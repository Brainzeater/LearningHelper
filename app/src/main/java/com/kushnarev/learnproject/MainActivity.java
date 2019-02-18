package com.kushnarev.learnproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kushnarev.learnproject.Adapters.CourseAdapter;
import com.kushnarev.learnproject.database.AppDatabase;
import com.kushnarev.learnproject.database.Courses.CourseEntry;

import java.util.Calendar;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements CourseAdapter.ItemClickListener, CourseAdapter.ItemLongClickListener {
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private CourseAdapter mAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: DELETE IT
//        Context context = this;
//        context.deleteDatabase("learning");

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewCourses);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new CourseAdapter(this, this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

    /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddCourseActivity.
         */
        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddCourseActivity
                Intent addCourseIntent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivity(addCourseIntent);
            }
        });

        mDb = AppDatabase.getInstance(getApplicationContext());
//        setupViewModel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // call the diskIO execute method with a new Runnable and implement its run method
        retrieveCourses();
    }

    public void retrieveCourses() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Extract the list of tasks to a final variable
                final List<CourseEntry> courses = mDb.courseDao().loadAllCourses();
                // We will be able to simplify this once we learn more
                // about Android Architecture Components
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setCourses(courses);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        // Launch AddCourseActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, TopicActivity.class);
        intent.putExtra(TopicActivity.EXTRA_COURSE_ID, itemId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int itemId) {
        System.out.println("Long click detected");
        Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
        intent.putExtra(AddCourseActivity.EXTRA_COURSE_ID, itemId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_textbook) {

            Intent intent = new Intent(MainActivity.this, TextbookActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
