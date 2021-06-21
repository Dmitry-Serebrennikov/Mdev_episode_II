  package com.example.mapnote.notefragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapnote.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class    NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ItemViewHolder> {

    public List<Note> notes;

    public interface ItemClickListener{
        void onItemClick(int position);
        void onPlayClick(int position);
    }

    public ItemClickListener itemClickListener = null;

    public NotesAdapter(List<Note> notes){
        this.notes = notes;
    }

    @NonNull
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, int position) {
        holder.titleTV.setText(notes.get(position).getTitle());
        holder.noteDescriptionTV.setText(notes.get(position).getDescription());

        if (notes.get(position).isHasSoud()){
            holder.playBTN.setVisibility(View.VISIBLE);
            holder.playBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onPlayClick(position);
                }
            });
        }else{
            holder.playBTN.setVisibility(View.GONE);
        }

        if (itemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV,noteDescriptionTV;
        Button playBTN;

        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            noteDescriptionTV = itemView.findViewById(R.id.noteDescritpionTV);
            playBTN = itemView.findViewById(R.id.playRecordingBTN);
        }
    }

}
