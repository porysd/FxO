package com.example.fxo;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder> {

    private Context context;
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
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
        String eventDate = cursor.getString(cursor.getColumnIndex("eventDate"));
        String eventReminder = cursor.getString(cursor.getColumnIndex("eventReminder"));


        String eventDetails = "Event: " + eventName + "\nDate: " + eventDate + "\nTime " + eventReminder;
        holder.eventDetails.setText(eventDetails);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDetails = itemView.findViewById(R.id.event_details);
        }
    }
}