package com.example.recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AudioRecordTest extends AppCompatActivity {
    private static final String LOG_TAG = "Audio Record Test";
    private static final int PERMISSION_ALL = 200;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    private static String curFileName = null;
    boolean isRecording = false;
    boolean isPlaying = false;
    boolean isPermissionChecking = false;


    private Button recordButton = null;
    private MediaRecorder recorder = null;
    private EditText fileName;
    private Button playButton = null;
    private MediaPlayer player = null;

    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void onRecord() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
        isRecording = !isRecording;
    }

    private void onPlay() {
        if (isPlaying) {
            stopPlaying();
        } else {
            startPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(curFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //TODO: спинер для выбора формата
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //TODO: проверка на пустоту
        String mFileName = AudioRecordTest.this.getCacheDir() + "/";
        mFileName += fileName.getText();
        mFileName += ".3gp";
        curFileName = mFileName;
        recorder.setOutputFile(mFileName);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.audio_recorder_test);
        fileName = findViewById(R.id.name);
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }
        //ActivityCompat.requestPermissions(this, permissions, WRITE_EXTERNAL_STORAGE_PERMISSION);

        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(v -> {
            onRecord();
            if (isRecording) {
                recordButton.setText("Stop recording");
            } else {
                recordButton.setText("Start recording");
            }
        });
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            onPlay();
            if (isPlaying) {
                recordButton.setText("Stop playing");
            } else {
                recordButton.setText("Start playing");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

}