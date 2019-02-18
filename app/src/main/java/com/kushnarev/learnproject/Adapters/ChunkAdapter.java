package com.kushnarev.learnproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kushnarev.learnproject.R;
import com.kushnarev.learnproject.database.Chunks.ChunkEntry;

import java.util.List;

public class ChunkAdapter extends RecyclerView.Adapter<ChunkAdapter.ChunkViewHolder> {
    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    final private ItemLongClickListener mItemLongClickListener;
    // Class variables for the List that holds course data and the Context
    private List<ChunkEntry> mChunkEntries;
    private Context mContext;

    /**
     * Constructor for the ChunkAdapter that initializes the Context.
     *
     * @param context      the current Context
     * @param listener     the ItemClickListener
     * @param longListener the ItemLingClickListener
     */
    public ChunkAdapter(Context context, ItemClickListener listener, ItemLongClickListener longListener) {
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
    public ChunkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the course_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.chunk_layout, parent, false);

        return new ChunkViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ChunkViewHolder chunkViewHolder, int position) {
        // Determine the values of the wanted data
        ChunkEntry chunkEntry = mChunkEntries.get(position);
        String chunkName = chunkEntry.getChunkName();

        // Set values
        chunkViewHolder.chunkTitleView.setText(chunkName);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mChunkEntries == null) {
            return 0;
        }
        return mChunkEntries.size();
    }

    public List<ChunkEntry> getCourses() {
        return mChunkEntries;
    }

    /**
     * When data changes, this method updates the list of chunkEntries
     * and notifies the adapter to use the new values on it
     */
    public void setChunks(List<ChunkEntry> chunkEntries) {
        mChunkEntries = chunkEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public interface ItemLongClickListener {
        void onItemLongClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class ChunkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // Class variables for the course title TextViews
        TextView chunkTitleView;

        /**
         * Constructor for the ChunkViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public ChunkViewHolder(View itemView) {
            super(itemView);

            chunkTitleView = itemView.findViewById(R.id.chunkTitle);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mChunkEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }

        @Override
        public boolean onLongClick(View v) {
            int elementId = mChunkEntries.get(getAdapterPosition()).getId();
            mItemLongClickListener.onItemLongClickListener(elementId);
            return true;
        }
    }
}
