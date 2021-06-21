package com.example.mapnote.notefragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapnote.MainActivity;
import com.example.mapnote.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES;
import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES_LOGIN;
import static com.example.mapnote.MainActivity.FIREBASE_PREFERENCES_PASSWORD;

public class NotesFragment extends Fragment {

    final static int RECORD_AUDIO_PERMISSION = 0;
    FloatingActionButton addNoteButton;
    RecyclerView notesRecyclerView;
    List<Note> notes;
    StorageReference firebaseStorage;
    DatabaseReference userRef;
    FirebaseDatabase firebaseDatabase;
    String filePath = "";
    private MediaRecorder mediaRecorder;
    Toolbar toolbar;
    ValueEventListener eventListener;

    public NotesFragment() {
    }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.logout){
                    SharedPreferences firebasePrefs = getActivity().getSharedPreferences(FIREBASE_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = firebasePrefs.edit();
                    editor.remove(FIREBASE_PREFERENCES_LOGIN);
                    editor.remove(FIREBASE_PREFERENCES_PASSWORD);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                return false;
            }
        });
        updateToolbarColors();
        if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        filePath = getActivity().getApplicationContext().getCacheDir() + "/";

        notes = new ArrayList<Note>();

        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        NotesAdapter notesAdapter = new NotesAdapter(notes);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        notesAdapter.itemClickListener = new NotesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_note, null);
                AlertDialog dialog = adb.setView(dialogView).show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ((EditText) dialog.findViewById(R.id.noteTitleET)).setText(notes.get(position).getTitle());
                ((EditText) dialog.findViewById(R.id.noteDescriptionET)).setText(notes.get(position).getDescription());

                final boolean[] isRecording = {false};
                final boolean[] isRecorded = {notes.get(position).isHasSoud()};
                final String[] recordedFileName = {""};

                dialogView.findViewById(R.id.editNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredText = ((EditText) dialogView.findViewById(R.id.noteDescriptionET)).getText().toString();
                        String enteredTitle = ((EditText) dialogView.findViewById(R.id.noteTitleET)).getText().toString();
                        if (isRecorded[0]) {
                            uploadRecordedAudio(recordedFileName[0], true);
                            notes.set(position, new Note(enteredTitle, enteredText, recordedFileName[0]));
                        } else {
                            if (notes.get(position).isHasSoud()) {
                                deleteFileOnDevice(notes.get(position).getSoundPath());
                                deleteInFirebase(notes.get(position).getSoundPath());
                            }
                            notes.set(position, new Note(enteredTitle, enteredText));
                        }
                        notesAdapter.notifyItemChanged(position);
                        saveNotes();
                        dialog.cancel();
                    }
                });
                dialogView.findViewById(R.id.deleteNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (notes.get(position).isHasSoud()) {
                            deleteFileOnDevice(notes.get(position).getSoundPath());
                            deleteInFirebase(notes.get(position).getSoundPath());
                        }
                        notes.remove(position);
                        notesAdapter.notifyItemRemoved(position);
                        saveNotes();
                        dialog.cancel();
                    }
                });


                if (!notes.get(position).isHasSoud()) {
                    String fileName = "record";
                    boolean found = false;
                    int i = 0;
                    while (!found) {
                        boolean has = false;
                        for (Note note : notes) {
                            recordedFileName[0] = fileName + i;
                            if (note.isHasSoud()) {
                                if ((recordedFileName[0] + ".3gp").equals(note.getSoundPath())) {
                                    has = true;
                                    break;
                                }
                            }
                        }
                        if (!has) {
                            found = true;
                            recordedFileName[0] += ".3gp";
                        }
                        i++;
                    }
                } else {
                    recordedFileName[0] = notes.get(position).getSoundPath();
                }

                if (isRecorded[0]){
                    dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.trash);
                }else{
                    dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.microphone);
                }

                dialogView.findViewById(R.id.recordBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isRecording[0] && !isRecorded[0]) {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.stop);
                            isRecording[0] = true;
                            startRecording(recordedFileName[0]);
                            dialog.setCancelable(false);

                        } else if (isRecording[0]) {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.trash);
                            isRecorded[0] = true;
                            isRecording[0] = false;
                            stopRecording();
                            dialog.setCancelable(true);

                        } else {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.microphone);
                            isRecorded[0] = false;
                        }
                    }
                });

            }

            @Override
            public void onPlayClick(int position) {
                if (new File(filePath + notes.get(position).getSoundPath()).exists()) {
                    MediaPlayer.create(getActivity().getApplicationContext(), Uri.fromFile(new File(filePath + notes.get(position).getSoundPath()))).start();
                }
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

                final boolean[] isRecording = {false};
                final boolean[] isRecorded = {false};
                final String[] recordedFileName = {""};

                dialogView.findViewById(R.id.addNoteBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredText = ((EditText) dialogView.findViewById(R.id.noteDescriptionET)).getText().toString();
                        String enteredTitle = ((EditText) dialogView.findViewById(R.id.noteTitleET)).getText().toString();
                        if (!isRecorded[0]) {
                            notes.add(new Note(enteredTitle, enteredText));
                        } else {
                            notes.add(new Note(enteredTitle, enteredText, recordedFileName[0]));
                            uploadRecordedAudio(recordedFileName[0], false);
                        }
                        notesAdapter.notifyDataSetChanged();
                        saveNotes();
                        dialog.cancel();
                    }
                });

                String fileName = "record";
                boolean found = false;
                int i = 0;
                while (!found) {
                    boolean has = false;
                    for (Note note : notes) {
                        recordedFileName[0] = fileName + i;
                        if (note.isHasSoud()) {
                            if ((recordedFileName[0] + ".3gp").equals(note.getSoundPath())) {
                                has = true;
                                break;
                            }
                        }
                    }
                    if (!has) {
                        found = true;
                        recordedFileName[0] += ".3gp";
                    }
                    i++;
                }

                dialogView.findViewById(R.id.recordBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isRecording[0] && !isRecorded[0]) {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.stop);
                            isRecording[0] = true;
                            startRecording(recordedFileName[0]);
                            dialog.setCancelable(false);

                        } else if (isRecording[0]) {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.trash);
                            isRecorded[0] = true;
                            isRecording[0] = false;
                            stopRecording();
                            dialog.setCancelable(true);

                        } else {
                            dialogView.findViewById(R.id.recordBTN).setBackgroundResource(R.drawable.microphone);
                            isRecorded[0] = false;
                            deleteFileOnDevice(recordedFileName[0]);
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
                        Note note = new Note("", "");
                        if (ds.child("hasSound").getValue().toString().equals("false")) {
                            note = new Note(ds.child("title").getValue().toString(), ds.child("description").getValue().toString());
                        } else if (ds.child("hasSound").getValue().toString().equals("true")){
                            note = new Note(ds.child("title").getValue().toString(), ds.child("description").getValue().toString(), ds.child("soundPath").getValue().toString());
                            File loadedFile = new File(filePath + ds.child("soundPath").getValue().toString());
                            firebaseStorage.child(userRef.toString()).child("Recordings").child(ds.child("soundPath").getValue().toString()).getFile(loadedFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                }
                            });
                        }
                        notes.add(note);
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

    public void saveNotes() {
        int i = 0;
        userRef.child("notes").removeValue();
        for (Note note : notes) {
            Log.d("SAVINGLOL", Integer.toString(notes.size()));
            userRef.child("notes").child(Integer.toString(i)).child("title").setValue(note.getTitle());
            userRef.child("notes").child(Integer.toString(i)).child("hasSound").setValue(note.isHasSoud());
            if (note.isHasSoud()) {
                userRef.child("notes").child(Integer.toString(i)).child("soundPath").setValue(note.getSoundPath());
            }
            userRef.child("notes").child(Integer.toString(i)).child("description").setValue(note.getDescription());
            i++;
        }
    }

    private void startRecording(String filename) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filePath + filename);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
        }

        mediaRecorder.start();

    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    }

    private void uploadRecordedAudio(String filename, boolean needReload) {
        StorageReference filepath = firebaseStorage.child(userRef.toString()).child("Recordings").child(filename);
        Uri uri = Uri.fromFile(new File(filePath + filename));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        if (needReload){
            File loadedFile = new File(filePath + filename);
            firebaseStorage.child(userRef.toString()).child("Recordings").child(filename).getFile(loadedFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }


    }

    private void deleteInFirebase(String filename) {
        StorageReference deleteFile = FirebaseStorage.getInstance().getReference().child(userRef.toString()).child("Recordings").child(filename);
        deleteFile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    private void deleteFileOnDevice(String filename) {
        File fileToDelete = new File(filePath + filename);
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (!deleted) {
                boolean deleted2 = false;
                try {
                    deleted2 = fileToDelete.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!deleted2) {
                    boolean deleted3 = getActivity().deleteFile(fileToDelete.getName());
                }
            }
        }
    }

    public void updateToolbarColors(){
        for (int i = 0; i < toolbar.getMenu().size(); i ++){
            Drawable icon = toolbar.getMenu().getItem(i).getIcon();
            icon = DrawableCompat.wrap(icon);
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireActivity(), R.color.white));
            toolbar.getMenu().getItem(i).setIcon(icon);
        }
    }

}