package com.example.fxo;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder> {

    private Context context;

    List<String> eventName;
    List<Integer> eventID;
    private Cursor cursor;

    public UpcomingEventAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String eventname = eventName.get(position);
        holder.eventDetails.setText(eventname);
    }

    @Override
    public int getItemCount() {
        return eventName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventDetails;

        ImageButton moreOptionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDetails = itemView.findViewById(R.id.flashcardView);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);


        }
    }
}
