package com.example.adventuality;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
        import java.util.HashMap;

        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

public class DevotionalEntryAdapter extends RecyclerView.Adapter<DevotionalEntryAdapter.ViewHolder> {

    private ArrayList<DevotionalEntry> entries;
    private OnClickListener mOnClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView layout;
        private TextView devotionTitle;
        private TextView devotionDescription;
        private ImageView devotionImage;
        private OnClickListener onClickListener;

        public ViewHolder(CardView view, OnClickListener onClickListener) {
            super(view);
            layout = view;
            devotionTitle = view.findViewById(R.id.dailyDevTitle);
//            devotionDescription = view.findViewById(R.id.dailyDevText);
            devotionImage = view.findViewById(R.id.dailyDevImageView);
            // Define click listener for the ViewHolder's View;
            this.onClickListener = onClickListener;
            view.setOnClickListener(this);
        }

        public CardView getCardView() {
            return layout;
        }


        @Override
        public void onClick(View v) {
            onClickListener.onEntryClick(getAdapterPosition());
        }
    }

    public interface OnClickListener{
        void onEntryClick(int position);
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public DevotionalEntryAdapter(ArrayList<DevotionalEntry> dataSet, OnClickListener onClickListener) {
        entries = dataSet;
        this.mOnClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        CardView view = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_list_item, viewGroup, false);

        return new ViewHolder(view, mOnClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.devotionTitle.setText(entries.get(position).getTitle());
//        viewHolder.devotionDescription.setText(entries.get(position).getDescription());
        Picasso.get().load(entries.get(position).getImageURL()).into(viewHolder.devotionImage, new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.devotionImage.setScaleType(ImageView.ScaleType.CENTER_CROP);//Or ScaleType.FIT_CENTER
            }
            @Override
            public void onError(Exception e) {
                //nothing for now
            }
        });
        Log.d("Image url: ", entries.get(position).getImageURL());
        Log.d("Image width: ", String.valueOf(viewHolder.devotionImage.getWidth()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entries.size();
    }

    public DevotionalEntry getItem(int position) {
        return entries.get(position);
    }
}

