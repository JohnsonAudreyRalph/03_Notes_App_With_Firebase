package com.example.a03_notes_app_with_firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton Add_notes_btn;
    RecyclerView recyclerView;
    ImageButton menu_btn;
    Notes_Update notes_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Add_notes_btn = findViewById(R.id.Add_notes_btn);
        recyclerView = findViewById(R.id.recyclerView);
        menu_btn = findViewById(R.id.menu_btn);

        Add_notes_btn.setOnClickListener(v -> {
            startActivities(new Intent[]{new Intent(MainActivity.this, NotesActivity.class)});
        });
        menu_btn.setOnClickListener(v -> ShowMenu());
        setupRecycleView();
    }

    void ShowMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu_btn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivities(new Intent[]{new Intent(MainActivity.this, LoginActivity.class)});
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecycleView(){
        // Lấy dữ liệu từ trên FireBase và hiển thị thông tin ở đây
        Query query = Note.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notes_update = new Notes_Update(options, this);
        recyclerView.setAdapter(notes_update);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notes_update.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notes_update.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notes_update.notifyDataSetChanged();
    }
}