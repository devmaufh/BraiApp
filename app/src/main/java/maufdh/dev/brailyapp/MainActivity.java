package maufdh.dev.brailyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import java.io.IOException;
import java.util.UUID;

import maufdh.dev.brailyapp.Watson.WatsonSpeechToText;


public class MainActivity extends AppCompatActivity{
    ImageButton btnRecord;
    Button btnStop,btnEnviar;
    String pathSave="";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE=1000;
    private void bindUI(){
       btnRecord=(ImageButton)findViewById(R.id.btn_record);
       btnStop=(Button)findViewById(R.id.btn_stop);
      btnEnviar=(Button)findViewById(R.id.btnSend);
      btnEnviar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // Toast.makeText(MainActivity.this, WatsonSpeechToText.getText(pathSave), Toast.LENGTH_SHORT).show();
          }
      });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();
        if (checkPermissionFromDevice()) {
            btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathSave=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ UUID.randomUUID()+"audio.mp3";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnStop.setEnabled(true);
                    btnRecord.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaRecorder.stop();
                    btnRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                }
            });

        } else {
            requestPermission();
        }

    }

    private void setupMediaRecorder() {
        Toast.makeText(this, pathSave, Toast.LENGTH_SHORT).show();
        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int record_audio_result= ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result==PackageManager.PERMISSION_GRANTED&&
                record_audio_result==PackageManager.PERMISSION_GRANTED;

    }

}