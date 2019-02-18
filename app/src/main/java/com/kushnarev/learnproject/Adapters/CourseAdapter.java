package com.kushnarev.learnproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kushnarev.learnproject.R;
import com.kushnarev.learnproject.database.Courses.CourseEntry;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    final private ItemLongClickListener mItemLongClickListener;
    // Class variables for the List that holds course data and the Context
    private List<CourseEntry> mCourseEntries;
    private Context mContext;

    /**
     * Constructor for the CourseAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     * @param longListener the ItemLingClickListener
     */
    public CourseAdapter(Context context, ItemClickListener listener, ItemLongClickListener longListener) {
        mContext = context;
        mItemClickListener = listener;
        mItemLongClickListener = longListener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new CourseViewHolder that holds the view for each course
     */
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the course_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.course_layout, parent, false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder courseViewHolder, int position) {
        // Determine the values of the wanted data
        CourseEntry courseEntry = mCourseEntries.get(position);
        String title = courseEntry.getTitle();

        // Set values
        courseViewHolder.courseTitleView.setText(title);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCourseEntries == null) {
            return 0;
        }
        return mCourseEntries.size();
    }

    public List<CourseEntry> getCourses() {
        return mCourseEntries;
    }

    /**
     * When data changes, this method updates the list of courseEntries
     * and notifies the adapter to use the new values on it
     */
    public void setCourses(List<CourseEntry> courseEntries) {
        mCourseEntries = courseEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public interface ItemLongClickListener {
        void onItemLongClickListener(int itemId);
    }
    // Inner class for creating ViewHolders
    class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // Class variables for the course title TextViews
        TextView courseTitleView;

        /**
         * Constructor for the CourseViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public CourseViewHolder(View itemView) {
            super(itemView);

            courseTitleView = itemView.findViewById(R.id.courseTitle);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mCourseEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }


        @Override
        public boolean onLongClick(View v) {
            int elementId = mCourseEntries.get(getAdapterPosition()).getId();
            mItemLongClickListener.onItemLongClickListener(elementId);
            return true;
        }
    }
}
