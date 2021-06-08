package com.example.mapnote.notefragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapnote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment {

    public NotesFragment() { }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FloatingActionButton addNoteButton;
    RecyclerView notesRecyclerView;
    List<Note> notes;

    DatabaseReference userRef;
    FirebaseDatabase firebaseDatabase;

    boolean isLoad = true;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notes = new ArrayList<Note>();

        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        NotesAdapter notesAdapter = new NotesAdapter(notes);
        firebaseDatabase = FirebaseDatabase.getInstance();
        notesAdapter.itemClickListener = new NotesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_note, null);
                AlertDialog dialog = adb.setView(dialogView).show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ((EditText)dialog.findViewById(R.id.noteTitleET)).setText(notes.get(position).getTitle());
                ((EditText)dialog.findViewById(R.id.noteDescriptionET)).setText(notes.get(position).getDescription());
                dialogView.findViewById(R.id.editNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredText = ((EditText)dialogView.findViewById(R.id.noteDescriptionET)).getText().toString();
                        if (!enteredText.trim().isEmpty()) {
                            String enteredTitle = ((EditText)dialogView.findViewById(R.id.noteTitleET)).getText().toString();
                            notes.set(position, new Note(enteredTitle, enteredText));
                            notesAdapter.notifyDataSetChanged();
                            saveNotes();
                            dialog.cancel();
                        }
                    }
                });
                dialogView.findViewById(R.id.deleteNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notes.remove(position);
                        notesAdapter.notifyItemRemoved(position);
                        saveNotes();
                        dialog.cancel();
                    }
                });
            }
        };

        addNoteButton = view.findViewById(R.id.addNoteFAB);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
                AlertDialog dialog = adb.setView(dialogView).show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.addNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredText = ((EditText)dialogView.findViewById(R.id.noteDescriptionET)).getText().toString();
                        if (!enteredText.trim().isEmpty()) {
                            String enteredTitle = ((EditText)dialogView.findViewById(R.id.noteTitleET)).getText().toString();
                            notes.add(new Note(enteredTitle, enteredText));
                            notesAdapter.notifyDataSetChanged();
                            saveNotes();
                            dialog.cancel();
                        }
                    }
                });
            }
        });

        notesRecyclerView.setAdapter(notesAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notesRecyclerView.setLayoutManager(layoutManager);

        userRef = firebaseDatabase.getReference().child(FirebaseAuth.getInstance().getUid());
        userRef.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                notes.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("description").getValue() != null) {
                        notes.add(new Note(ds.child("title").getValue().toString(), ds.child("description").getValue().toString()));
                    }
                }
                notesAdapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    public void saveNotes(){
        int i = 0;
        userRef.child("notes").removeValue();
        for (Note note : notes) {
            Log.d("SAVINGLOL", Integer.toString(notes.size()));
            userRef.child("notes").child(Integer.toString(i)).child("title").setValue(note.getTitle());
            userRef.child("notes").child(Integer.toString(i)).child("hasSound").setValue(note.isHasSoud());
            if(note.isHasSoud()){
                userRef.child("notes").child(Integer.toString(i)).child("soundPath").setValue(note.getSoundPath());
            }
            userRef.child("notes").child(Integer.toString(i)).child("description").setValue(note.getDescription());
            i++;
        }
    }

}