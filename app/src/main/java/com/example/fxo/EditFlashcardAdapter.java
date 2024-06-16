package com.example.fxo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditFlashcardAdapter extends RecyclerView.Adapter<EditFlashcardAdapter.ViewHolder> {

    private final Context context;
    private final List<String> questions;
    private final List<String> answers;
    private final RecyclerViewInterface recyclerViewInterface;

    public EditFlashcardAdapter(Context context, List<String> questions, List<String> answers, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.questions = questions;
        this.answers = answers;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public EditFlashcardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcardedit_item,parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String question = questions.get(position);
        String answer = answers.get(position);
        holder.q.setText(question);
        holder.a.setText(answer);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView q, a;

        // ViewHolder constructor
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // Initialize the TextView
            q = itemView.findViewById(R.id.questionitem);
            a = itemView.findViewById(R.id.answeritem);

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
