package com.example.fxo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class FlashcardHomeAdapter extends RecyclerView.Adapter<FlashcardHomeAdapter.ViewHolder> {

    private final FlashcardHomeInterface flashcardHomeInterface;
    static Context context;
    List<String> flashcardFolderNames;


    public FlashcardHomeAdapter(Context context, List<String> flashcardFolderNames, FlashcardHomeInterface flashcardHomeInterface) {
        this.context = context;
        this.flashcardFolderNames = flashcardFolderNames;
        this.flashcardHomeInterface = flashcardHomeInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card, parent, false);
        return new ViewHolder(view, flashcardHomeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String folderName = flashcardFolderNames.get(position);
        holder.textView.setText(folderName);
    }

    @Override
    public int getItemCount() {
        return flashcardFolderNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView, FlashcardHomeInterface flashcardHomeInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.flashcardView);

            itemView.setOnClickListener(view -> {
                if (flashcardHomeInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        flashcardHomeInterface.onFlashcardFolderItemClick(pos);
                    }
                }
            });
        }
    }
}


// get date created
// get recent opened flashcards
// apply the recent opened flashcard in home page