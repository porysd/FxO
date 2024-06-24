package com.example.fxo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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

public class SeeAllFlashcardsAdapter extends RecyclerView.Adapter<SeeAllFlashcardsAdapter.ViewHolder> {

    private Context context;
    private List<String> flashcardTitles;
    private List<Integer> flashcardIDs;
    private DatabaseHelper Users_DB;

    public SeeAllFlashcardsAdapter(Context context, List<String> flashcardTitles, List<Integer> flashcardIDs) {
        this.context = context;
        this.flashcardTitles = flashcardTitles;
        this.flashcardIDs = flashcardIDs;
        Users_DB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcardfolder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = flashcardTitles.get(position);
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        return flashcardTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton moreOptionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.flashcardfolder_name);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);

            itemView.setOnClickListener(view -> {

            });

            moreOptionsButton.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.menu_options, popup.getMenu());
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        int flashcardID = flashcardIDs.get(position);
                        String flashcardTitle = flashcardTitles.get(position);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int itemId = menuItem.getItemId();

                                if(itemId == R.id.menu_add){
                                    Intent addIntent = new Intent(context, AddFlashcardActivity.class);
                                    User.getInstance().setFlashcardFolderID(flashcardID);
                                    User.getInstance().setFlashcardFolderTitle(flashcardTitle);
                                    context.startActivity(addIntent);
                                    return true;
                                } else if (itemId == R.id.menu_edit){
                                    Intent editIntent = new Intent(context, EditFlashcardActivity.class);
                                    User.getInstance().setFlashcardFolderID(flashcardID);
                                    User.getInstance().setFlashcardFolderTitle(flashcardTitle);
                                    context.startActivity(editIntent);
                                    return true;
                                } else if (itemId == R.id. menu_delete){
                                    confirmDelete(flashcardID, position);
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
        private void confirmDelete(int flashcardID, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this flashcard?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Perform delete operation
                boolean deleted = Users_DB.deleteFlashcardData(String.valueOf(flashcardID));
                if (deleted) {
                    flashcardTitles.remove(position);
                    flashcardIDs.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Flashcard deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete flashcard", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

