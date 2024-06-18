package com.example.fxo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    // Interface for handling item clicks
    private final RecyclerViewInterface recyclerViewInterface;

    // Context and list of folders
    private final Context context;
    private final List<String> folders;

    // Constructor to initialize context, folders list, and the interface
    public FolderAdapter(Context context, List<String> folders, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.folders = folders;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    // Inflate the item layout and create the holder
    @NonNull
    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    // Bind data to the item view
    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.ViewHolder holder, int position) {
        String mFolders = folders.get(position);
        holder.name.setText(mFolders);
    }

    // Return the size of the folders list
    @Override
    public int getItemCount() {
        return folders.size();
    }

    // ViewHolder class to hold item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        // ViewHolder constructor
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // Initialize the TextView
            name = itemView.findViewById(R.id.folder_name);

            // Set click listener for the item view
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
