package com.example.fxo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder> {

    private Context context;
    private DatabaseHelper Users_DB;

    private List<String> eventTitle;
    private List<Integer> eventID;

    public UpcomingEventAdapter(Context context, List<String> eventTitle, List<Integer> eventID) {
        this.context = context;
        this.eventTitle = eventTitle;
        this.eventID = eventID;
        this.Users_DB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String eventName = eventTitle.get(position);
        holder.textView.setText(eventName);
    }

    @Override
    public int getItemCount() {
        return eventTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton moreOptionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.event_details);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);

            moreOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.event_item_menu, popup.getMenu());
                    final int position = getAdapterPosition(); // Important to use final here

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();

                            if (itemId == R.id.event_edit) {
                                editEvent(eventID.get(position));
                                return true;
                            } else if (itemId == R.id.event_delete) {
                                confirmDelete(eventID.get(position), position);
                                return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    private void editEvent(int eventId) {
        Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra("eventId", eventId);
        context.startActivity(intent);
    }

    private void confirmDelete(int eventId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this event?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            boolean deleted = Users_DB.deleteEvent(eventId);
            if (deleted) {
                eventTitle.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
