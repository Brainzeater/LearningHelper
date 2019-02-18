package com.kushnarev.learnproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kushnarev.learnproject.R;
import com.kushnarev.learnproject.database.Topics.TopicEntry;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    final private ItemLongClickListener mItemLongClickListener;
    // Class variables for the List that holds course data and the Context
    private List<TopicEntry> mTopicEntries;
    private Context mContext;

    /**
     * Constructor for the CourseAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     * @param longListener the ItemLingClickListener
     */
    public TopicAdapter(Context context, ItemClickListener listener, ItemLongClickListener longListener) {
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
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the course_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_layout, parent, false);

        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder topicViewHolder, int position) {
        // Determine the values of the wanted data
        TopicEntry topicEntry = mTopicEntries.get(position);
        String topicName = topicEntry.getTopicName();

        // Set values
        topicViewHolder.topicTitleView.setText(topicName);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTopicEntries == null) {
            return 0;
        }
        return mTopicEntries.size();
    }

    public List<TopicEntry> getCourses() {
        return mTopicEntries;
    }

    /**
     * When data changes, this method updates the list of courseEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTopics(List<TopicEntry> topicEntries) {
        mTopicEntries = topicEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public interface ItemLongClickListener {
        void onItemLongClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // Class variables for the course title TextViews
        TextView topicTitleView;

        /**
         * Constructor for the TopicViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TopicViewHolder(View itemView) {
            super(itemView);

            topicTitleView = itemView.findViewById(R.id.topicTitle);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mTopicEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }

        @Override
        public boolean onLongClick(View v) {
            int elementId = mTopicEntries.get(getAdapterPosition()).getId();
            mItemLongClickListener.onItemLongClickListener(elementId);
            return true;
        }
    }
}
