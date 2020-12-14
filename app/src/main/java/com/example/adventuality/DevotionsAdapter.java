package com.example.adventuality;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DevotionsAdapter extends RecyclerView.Adapter<DevotionsAdapter.ViewHolder> {

    private ArrayList<Devotional> devotions;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView layout;
        private TextView devotionTitle;
        private TextView devotionDescription;
        private ImageView devotionImage;

        public ViewHolder(CardView view) {
            super(view);
            layout = view;
            devotionTitle = view.findViewById(R.id.dailyDevTitle);
            devotionDescription = view.findViewById(R.id.dailyDevText);
            devotionImage = view.findViewById(R.id.dailyDevImage);
            // Define click listener for the ViewHolder's View;
        }

        public CardView getCardView() {
            return layout;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public DevotionsAdapter(ArrayList<Devotional> dataSet) {
        devotions = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        CardView view = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.devotionTitle.setText(devotions.get(position).getName());
        viewHolder.devotionDescription.setText(devotions.get(position).getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devotions.size();
    }
}

