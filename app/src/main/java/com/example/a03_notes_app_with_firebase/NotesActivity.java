package com.example.a03_notes_app_with_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Documented;


public class NotesActivity extends AppCompatActivity {

    EditText notes_title, notes_content;
    ImageButton Save_notes_btn;
    TextView pate_title;
    String title, content, DocID;
    boolean isEditMode = false;
    TextView delete_note_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notes_title = findViewById(R.id.notes_title);
        notes_content = findViewById(R.id.notes_content);
        Save_notes_btn = findViewById(R.id.Save_notes_btn);
        pate_title = findViewById(R.id.pate_title);
        delete_note_btn = findViewById(R.id.delete_note_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        DocID = getIntent().getStringExtra("DocID");

        if(DocID!= null && !DocID.isEmpty()){
            isEditMode = true;
        }

        notes_title.setText(title);
        notes_content.setText(content);

        if (isEditMode){
            pate_title.setText("Edit Notes");
            delete_note_btn.setVisibility(View.VISIBLE);
        }


        Save_notes_btn.setOnClickListener(v -> Save_notes());
        delete_note_btn.setOnClickListener(v -> deleteNoteFormFirebase());
    }

    void Save_notes(){
        String title = notes_title.getText().toString();
        String content = notes_content.getText().toString();
        if (title.isEmpty()){
            notes_title.setError("Chưa có tiêu đề");
            Toast.makeText(this, "Chưa có tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteToFireBase(note);
    }

    void saveNoteToFireBase(Note note){
        DocumentReference documentReference;

        if (isEditMode){
            documentReference = Note.getCollectionReferenceForNotes().document(DocID);
        }
        else {
            documentReference = Note.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Ghi chú được thêm
                    Toast.makeText(NotesActivity.this, "Note đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(NotesActivity.this, "Có chỗ nào đó sai rồi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void deleteNoteFormFirebase(){
        DocumentReference documentReference;
        documentReference = Note.getCollectionReferenceForNotes().document(DocID);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Ghi chú được thêm
                    Toast.makeText(NotesActivity.this, "Note đã được xóa!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(NotesActivity.this, "Có chỗ nào đó sai rồi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}