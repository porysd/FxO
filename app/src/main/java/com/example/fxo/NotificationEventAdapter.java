package com.example.fxo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationEventAdapter extends RecyclerView.Adapter<NotificationEventAdapter.ViewHolder> {

    private Context context;
    private List<String> eventDetails;

    public NotificationEventAdapter(Context context, List<String> eventDetails) {
        this.context = context;
        this.eventDetails = eventDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String eventDetail = eventDetails.get(position);
        holder.eventDetailTextView.setText(eventDetail);
    }

    @Override
    public int getItemCount() {
        return eventDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventDetailTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDetailTextView = itemView.findViewById(R.id.notification_event_detail);
        }
    }
}
