package com.example.fxo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class FlashcardHomeAdapter extends RecyclerView.Adapter<FlashcardHomeAdapter.ViewHolder> {

    private final FlashcardHomeInterface flashcardHomeInterface;

    Context context;
    List<String> flashcardFoldersTitle;
    List<Integer> flashcardFoldersID;
    DatabaseHelper Users_DB;


    public FlashcardHomeAdapter(Context context, List<String> flashcardFoldersTitle, List<Integer> flashcardFoldersID, FlashcardHomeInterface flashcardHomeInterface) {
        this.context = context;
        this.flashcardFoldersTitle = flashcardFoldersTitle;
        this.flashcardFoldersID = flashcardFoldersID;
        this.flashcardHomeInterface = flashcardHomeInterface;
        Users_DB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card, parent, false);
        return new ViewHolder(view, flashcardHomeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String folderName = flashcardFoldersTitle.get(position);
        holder.textView.setText(folderName);
    }

    @Override
    public int getItemCount() {
        return flashcardFoldersTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton moreOptionsButton;
        public ViewHolder(@NonNull View itemView, FlashcardHomeInterface flashcardHomeInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.flashcardView);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);

            itemView.setOnClickListener(view -> {
                if (flashcardHomeInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        flashcardHomeInterface.onFlashcardFolderItemClick(pos);
                    }
                }
            });
            moreOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_options, popup.getMenu());
                    int position = getAdapterPosition();
                    Toast.makeText(context, "FLASHCARDFOLDERID:" + (flashcardFoldersID.get(position)) + "position: " + position, Toast.LENGTH_SHORT).show();
                    if (position != RecyclerView.NO_POSITION) {
                        String foldersName = flashcardFoldersTitle.get(position);
                        int fcfolderID = Users_DB.getFolderIDByName(foldersName);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int itemId = menuItem.getItemId();

                                if (itemId == R.id.menu_add) {
                                    Intent i = new Intent(context, AddFlashcardActivity.class);
                                    User.getInstance().setFlashcardFolderTitle(flashcardFoldersTitle.get(position));
                                    User.getInstance().setFlashcardFolderID(flashcardFoldersID.get(position));
                                    context.startActivity(i);
                                    return true;
                                } else if (itemId == R.id.menu_edit) {
                                    Intent i = new Intent(context, EditFlashcardActivity.class);
                                    User.getInstance().setFlashcardFolderTitle(flashcardFoldersTitle.get(position));
                                    User.getInstance().setFlashcardFolderID(flashcardFoldersID.get(position));
                                    context.startActivity(i);
                                    return true;
                                } else if (itemId == R.id.menu_delete) {
                                    confirmDelete(flashcardFoldersID.get(position), position);
                                    return true;
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }
                }
            });
        }
    }
    private void confirmDelete(int fcfolderID, int position) {
        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        Button buttonYes = dialogView.findViewById(R.id.dialog_button_yes);
        Button buttonNo = dialogView.findViewById(R.id.dialog_button_no);

        AlertDialog dialog = builder.create();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean deleted = Users_DB.deleteData(String.valueOf(fcfolderID));
                if (deleted) {
                    flashcardFoldersTitle.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                    Users_DB.resetAutoIncrement();
                } else {
                    Toast.makeText(context, "Failed to delete record", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss(); // Dismiss dialog after action
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss dialog if "No" button is clicked
            }
        });

        dialog.show();
    }
}


