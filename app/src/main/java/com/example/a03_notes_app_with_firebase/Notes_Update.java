package com.example.a03_notes_app_with_firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Notes_Update extends FirestoreRecyclerAdapter<Note, Notes_Update.NoteViewHolder> {

    Context context;
    public Notes_Update(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.notes_title_textview.setText(note.title);
        holder.notes_content_textview.setText(note.content);
        holder.notes_time_textview.setText(Note.TimestampToString(note.timestamp));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotesActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            String DocID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("DocID", DocID);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_notes_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView notes_title_textview, notes_content_textview, notes_time_textview;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notes_title_textview = itemView.findViewById(R.id.notes_title_textview);
            notes_content_textview = itemView.findViewById(R.id.notes_content_textview);
            notes_time_textview = itemView.findViewById(R.id.notes_time_textview);
        }
    }
}
