package com.example.adventuality;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DevotionalAdapter extends RecyclerView.Adapter<DevotionalAdapter.ViewHolder> {
    private ArrayList<Devotional> devotionals;
    private OnClickListener mOnClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView layout;
        private TextView devotionalTitle;
        private ImageView devotionalImage;
        private CheckBox checkBox;
        OnClickListener onClickListener;

        public ViewHolder(MaterialCardView view, OnClickListener onClickListener) {
            super(view);
            layout = view;
            devotionalTitle = view.findViewById(R.id.devotionalTitle);
            devotionalImage = view.findViewById(R.id.devotionalImage);
            checkBox = view.findViewById(R.id.devotionalCheck);
            this.onClickListener = onClickListener;
            checkBox.setOnClickListener(this);
            // Define click listener for the ViewHolder's View;
            view.setOnClickListener(this);
        }

        public MaterialCardView getCardView() {
            return layout;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.devotionalCheck){
                onClickListener.onCheckClick(getAdapterPosition());
            } else {
                onClickListener.onDevotionalClick(getAdapterPosition());
            }
        }
    }

    public interface OnClickListener{
        void onDevotionalClick(int position);
        void onCheckClick(int position);
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param devotionals String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public DevotionalAdapter(ArrayList<Devotional> devotionals, OnClickListener onClickListener) {
        this.devotionals = devotionals;
        this.mOnClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.devotional_list_item, viewGroup, false);

        return new ViewHolder(view, mOnClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.devotionalTitle.setText(devotionals.get(position).getName());
        if(devotionals.get(position).isFollowed()){
            viewHolder.layout.setStrokeColor(viewHolder.getCardView().getContext().getColor(R.color.colorAccent));
            viewHolder.layout.setStrokeWidth(12);
            if(!viewHolder.checkBox.isChecked()){
                viewHolder.checkBox.setChecked(true);
            }
        } else {
            viewHolder.layout.setStrokeWidth(0);
            if(viewHolder.checkBox.isChecked()){
                viewHolder.checkBox.setChecked(false);
            }
        }
        Picasso.get().load(devotionals.get(position).getImageURL()).placeholder(R.drawable.example_image).into(viewHolder.devotionalImage, new Callback() {
            @Override
            public void onSuccess() {
//                viewHolder.devotionalImage.setScaleType(ImageView.ScaleType.FIT_CENTER);//Or ScaleType.FIT_CENTER
            }
            @Override
            public void onError(Exception e) {
                //nothing for now
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devotionals.size();
    }

    public Devotional getItem(int position) {
        return devotionals.get(position);
    }


}

