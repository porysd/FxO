package com.example.fxo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class FlashcardFolderAdapter extends RecyclerView.Adapter<FlashcardFolderAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<String> folders;
    List<Integer> flashcardFoldersID;
    String folder;
    String folderName;
    int folderID, userID;
    DatabaseHelper Users_DB;

    public FlashcardFolderAdapter(Context context, List<String> folders, List<Integer> flashcardFoldersID, int folderID, int userID, String folderName, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.folders = folders;
        this.folderID = folderID;
        this.flashcardFoldersID = flashcardFoldersID;
        this.userID = userID;
        this.folderName = folderName;
        this.recyclerViewInterface = recyclerViewInterface;
        Users_DB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public FlashcardFolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flashcardfolder_item,parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardFolderAdapter.ViewHolder holder, int position) {
        folder = folders.get(position);
        holder.name.setText(folder);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageButton moreOptionsButton;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.flashcardfolder_name);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface !=  null){
                        int pos = getAdapterPosition();

                        if (pos !=  RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
            //Triple Dot Button
            moreOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_options, popup.getMenu());
                    int position = getAdapterPosition();
                    Toast.makeText(FlashcardFolderAdapter.this.context, "FLASHCARDFOLDERID:" + (position + flashcardFoldersID.get(0)) , Toast.LENGTH_SHORT).show();
                    if (position != RecyclerView.NO_POSITION) {
                        String foldersName = folders.get(position);
                        int flashcardID = Users_DB.getFolderIDByName(foldersName);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int itemId = menuItem.getItemId();

                                if (itemId == R.id.menu_add) {
                                    Intent i = new Intent(FlashcardFolderAdapter.this.context, AddFlashcardActivity.class);
                                    User.getInstance().setFlashcardFolderID(position + flashcardFoldersID.get(0));
                                    context.startActivity(i);
                                    return true;
                                } else if (itemId == R.id.menu_edit) {
                                    // Handle edit action
                                    Toast.makeText(FlashcardFolderAdapter.this.context, "Edit action clicked", Toast.LENGTH_SHORT).show();
                                    return true;
                                } else if (itemId == R.id.menu_delete) {
                                    confirmDelete(flashcardID, position);
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
    private void confirmDelete(int flashcardID, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean deleted = Users_DB.deleteData(String.valueOf(flashcardID));
                if (deleted) {
                    folders.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                    Users_DB.resetAutoIncrement();
                } else {
                    Toast.makeText(context, "Failed to delete record", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
