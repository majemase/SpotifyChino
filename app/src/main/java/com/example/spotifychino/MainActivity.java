package com.example.spotifychino;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    MediaPlayer reproductor;
    ArrayList<Integer> playlist = new ArrayList<>();
    int musica = 0;
    Runnable handlertask;
    TextView reproduciendo, total;
    SeekBar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.menu));

        playlist.add(R.raw.enemy);
        playlist.add(R.raw.comeplay);
        playlist.add(R.raw.toashes);
        reproductor = MediaPlayer.create(this, playlist.get(0));

        reproduciendo = findViewById(R.id.reproduciendo);
        total = findViewById(R.id.total);

        reproduciendo.setText(formato(0));
        total.setText(formato(reproductor.getDuration()));

        barra = findViewById(R.id.barra);
        barra.setMax(reproductor.getDuration());
        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(reproductor.isPlaying() && b){
                    reproductor.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btnplay).setOnClickListener(v -> pausa());
        findViewById(R.id.btnplay).setOnLongClickListener(v -> parar());
        findViewById(R.id.btnprev).setOnClickListener(v -> anterior());
        findViewById(R.id.btnsig).setOnClickListener(v -> siguiente());
    }

    public void pausa(){
        if(reproductor.isPlaying()){
            reproductor.pause();
        }else{
            reproductor.start();
        }
    }

    public boolean parar(){
        if(reproductor.isPlaying()){
            reproductor.stop();
        }
        return true;
    }

    public void anterior(){
        if(reproductor.isPlaying()){
            if(musica == 0){
                musica = playlist.size() + 1;
                reproductor.stop();
                reproductor.release();
                reproductor = MediaPlayer.create(this, playlist.get(musica));
            }else{
                musica--;
                reproductor.stop();
                reproductor.release();
                reproductor = MediaPlayer.create(this, playlist.get(musica));

            }
        }
    }

    public void siguiente(){
        if(reproductor.isPlaying()){
            if(musica == playlist.size()) {
                musica = 0;
                reproductor.stop();
                reproductor.release();
                reproductor = MediaPlayer.create(this, playlist.get(musica));
            }else{
                musica++;
                reproductor.stop();
                reproductor.release();
                reproductor = MediaPlayer.create(this, playlist.get(musica));
            }
        }
    }

    public String formato(int millis){
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public void salir(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setCancelable(false)
                .setTitle("Salir")
                .setMessage("¿Quiere salir de la app?")
                .setNegativeButton("No", null)
                .setPositiveButton("Si", (dialog, which) -> finish())
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnsalir){
            salir();
        }
        return true;
    }
}