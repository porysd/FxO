package com.example.fxo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardHomeAdapter extends RecyclerView.Adapter<FlashcardHomeAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    static Context context;
    List<String> folders;
    String folder;

    public FlashcardHomeAdapter(Context context, List<String> folders, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.folders = folders;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public FlashcardHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flash_card,parent,false);
        return new FlashcardHomeAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardHomeAdapter.ViewHolder holder, int position) {
        folder = folders.get(position);
        holder.name.setText(folder);
    }

    @Override
    public int getItemCount()
    {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.flashcardView);
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
        }
    }
}


// get date created
// get recent opened flashcards
// apply the recent opened flashcard in home page

