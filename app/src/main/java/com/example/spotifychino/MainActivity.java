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

public class MainActivity extends AppCompatActivity {

    MaterialToolbar menu;
    AlertDialog salir;
    ImageButton btnplay, btnprev, btnsig;
    MediaPlayer mp;
    ArrayList<Integer> playlist = new ArrayList<>();
    int cancion = 0;
    SeekBar barra;
    TextView total, reproduciendo;
    Handler handler = new Handler();

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

        init();

        setSupportActionBar(menu);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        salir = builder.setCancelable(false)
                .setTitle("Salir del programa")
                .setMessage("¿Quieres salir del programa?")
                .setNegativeButton("No", null)
                .setPositiveButton("Si", (dialog, which) -> finish())
                .create();

        btnplay.setOnClickListener(v -> play(cancion));

        btnplay.setOnLongClickListener(v -> {
            if(mp != null){
                mp.stop();
                mp.release();
                mp = null;
            }
            return true;
        });

        btnprev.setOnClickListener(v -> siguiente());
        btnsig.setOnClickListener(v -> anterior());

        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mp.isPlaying()){
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void init(){
        menu = findViewById(R.id.menu);
        btnplay = findViewById(R.id.btnplay);
        btnprev = findViewById(R.id.btnprev);
        btnsig = findViewById(R.id.btnsig);
        barra = findViewById(R.id.barra);
        total = findViewById(R.id.total);
        reproduciendo = findViewById(R.id.reproduciendo);
        playlist.add(R.raw.enemy);
        playlist.add(R.raw.comeplay);
        playlist.add(R.raw.toashes);
        mp = MediaPlayer.create(this, playlist.get(0));
    }

    public void play(int indice) {
        if (mp == null) {
            mp = MediaPlayer.create(this, playlist.get(indice));
            if (mp != null) {
                mp.start();
                barra.setMax(mp.getDuration());
                total.setText(formatTime(mp.getDuration()));
                empezartiempo();
                btnplay.setImageResource(R.drawable.pausa);
                mp.setOnCompletionListener(mediaPlayer -> siguiente());
            }
        } else {
            if (mp.isPlaying()) {
                mp.pause();
                btnplay.setImageResource(R.drawable.play);
            } else {
                mp.start();
                btnplay.setImageResource(R.drawable.pausa);
            }
        }
    }

    public void empezartiempo() {
        handler.removeCallbacksAndMessages(null);
        Runnable handlertask = new Runnable() {
            @Override
            public void run() {
                if (mp != null && mp.isPlaying()) {
                    barra.setProgress(mp.getCurrentPosition());
                    reproduciendo.setText(formatTime(mp.getCurrentPosition()));
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(handlertask); // Ejecuta el Runnable
    }

    private String formatTime(int milliseconds) {
        int min = (milliseconds / 1000) / 60;
        int seg = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", min, seg);
    }

    public void siguiente() {
        if (playlist != null && !playlist.isEmpty()) {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
            cancion = (cancion + 1) % playlist.size();
            play(cancion);
        }
    }

    public void anterior() {
        if (playlist != null && !playlist.isEmpty()) {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
            cancion = (cancion - 1 + playlist.size()) % playlist.size();
            play(cancion);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnsalir){
            salir.show();
        }
        return true;
    }
}