package com.example.solitario_celta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    JuegoCelta mJuego;
    private String fichero;
    SharedPreferences preferences;
    private final String CLAVE_TABLERO = "TABLERO_SOLITARIO_CELTA";
    private CountDownTimer countDownTimer;

    private long remainingTimeInMillis = 5 * 60 * 1000; // 5 minutos en milisegundos
    GamePunctuationRepository db = new GamePunctuationRepository(this);
    private final int[][] ids = {
            {0, 0, R.id.p02, R.id.p03, R.id.p04, 0, 0},
            {0, 0, R.id.p12, R.id.p13, R.id.p14, 0, 0},
            {R.id.p20, R.id.p21, R.id.p22, R.id.p23, R.id.p24, R.id.p25, R.id.p26},
            {R.id.p30, R.id.p31, R.id.p32, R.id.p33, R.id.p34, R.id.p35, R.id.p36},
            {R.id.p40, R.id.p41, R.id.p42, R.id.p43, R.id.p44, R.id.p45, R.id.p46},
            {0, 0, R.id.p52, R.id.p53, R.id.p54, 0, 0},
            {0, 0, R.id.p62, R.id.p63, R.id.p64, 0, 0}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTimer(remainingTimeInMillis);
        fichero = getString(R.string.save_file);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mJuego = new JuegoCelta();
        mostrarTablero();


    }

    public void startTimer(long timeInMillis) {
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                updateTimerDisplay();

            }

            @Override
            public void onFinish() {
                remainingTimeInMillis = 0;
                updateTimerDisplay();
                juegoTerminadoGuardado();
            }
        }.start();
    }

    private void updateTimerDisplay() {
        // Convertir remainingTimeInMillis a minutos y segundos
        int minutos = (int) (remainingTimeInMillis / 1000) / 60;
        int segundos = (int) (remainingTimeInMillis / 1000) % 60;


        // Actualizar la visualización del temporizador en tu diseño (asumiendo que tienes un TextView con id timerDisplay)
        TextView timerDisplay = findViewById(R.id.timerDisplay);
        timerDisplay.setText(String.format("%02d:%02d", minutos, segundos));
    }
    public void posicionPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';
        int j = resourceName.charAt(2) - '0';

        mJuego.jugar(i, j);

        mostrarTablero();
        if (mJuego.juegoTerminado() || remainingTimeInMillis <= 0) {
            this.juegoTerminadoGuardado();
        }
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        TextView remaining = findViewById(R.id.remaining);
        for (int i = 0; i < JuegoCelta.TAMANIO; i++) {
            for (int j = 0; j < JuegoCelta.TAMANIO; j++)
                if (ids[i][j] != 0) {
                    button = findViewById(ids[i][j]);
                    button.setChecked(mJuego.obtenerFicha(i, j) == 1);
                }
        }
        remaining.setText(String.valueOf(mJuego.missingPieces()));
    }
    private void resetGame() {
        mJuego.reiniciar();
        mostrarTablero();

        // Reiniciar el contador
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis=5*60*1000;
        startTimer(remainingTimeInMillis);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CLAVE_TABLERO, mJuego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(CLAVE_TABLERO);
        mJuego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void saveGame(int position) {
        this.createFileIfNotExist();
        try {
            String [] saves = this.savedGames();
            FileOutputStream fos = openFileOutput(fichero, Context.MODE_PRIVATE);
            saves [position] = mJuego.serializaTablero();
            for (int i = 0;i<3;i++){
                fos.write(saves[i].getBytes());
                fos.write('\n');
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.i("INFO", "File not found");
        } catch (IOException e) {
            Log.i("INFO", "Escribir el fichero no ha sido posible");
        }
    }

    private String [] savedGames (){
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(fichero)));
            String[] saves = new String[3];
            for (int i = 0; i < 3; i++) {
                saves[i] = fin.readLine();
            }
            fin.close();
            return saves;
        }catch (FileNotFoundException e) {
            Log.i("INFO", "File not found");
        } catch (IOException e) {
            Log.i("INFO", "Escribir el fichero no ha sido posible");
        }
        return null;
    }

    private void createFileIfNotExist(){
        File f = new File("data/data/es.upm.miw.SolitarioCelta/files/"+fichero);
        if(!f.exists()){
            try {
                FileOutputStream fos = openFileOutput(fichero, Context.MODE_PRIVATE);
                for (int i = 0; i<3;i++){
                    fos.write(mJuego.tableroInicialSerializado.getBytes());
                    fos.write('\n');
                }
                fos.close();
            } catch (FileNotFoundException e) {
                Log.i("INFO", "File not found");
            } catch (IOException e) {
                Log.i("INFO", "Escribir el fichero no ha sido posible");
            }
        }
    }

    public String resumeGame(int position) {
        return this.savedGames()[position];
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menuAbout){
            startActivity(new Intent(this, About.class));
            return true;
        } else if (item.getItemId()==R.id.preferences) {
            startActivity(new Intent(this, SCeltaPreferences.class));
            return true;
        } else if (item.getItemId()==R.id.restart) {
          resetGame();

            return true;
        } else if (item.getItemId()==R.id.save_game_1) {
            saveGame(0);
            return true;
        } else if (item.getItemId()==R.id.save_game_2) {
            saveGame(1);
            return true;
        } else if (item.getItemId()==R.id.save_game_3) {
            saveGame(2);
            return true;
        } else if (item.getItemId()==R.id.resume_game_1) {
                if (isGameIniciated(0)) {
                    new ResumeDialogFragment(0).show(getFragmentManager(), "ALERT DIALOG");
                } else {
                    mJuego.deserializaTablero(resumeGame(0));
                    mostrarTablero();
                }
                return true;
            } else if (item.getItemId()==R.id.resume_game_2) {
                if (isGameIniciated(1)) {
                    new ResumeDialogFragment(1).show(getFragmentManager(), "ALERT DIALOG");
                } else {
                    mJuego.deserializaTablero(resumeGame(1));
                    mostrarTablero();
                }
                return true;
            } else if (item.getItemId()==R.id.resume_game_3) {
                if (isGameIniciated(2)) {
                    new ResumeDialogFragment(2).show(getFragmentManager(), "ALERT DIALOG");
                } else {
                    mJuego.deserializaTablero(resumeGame(2));
                    mostrarTablero();
                }
                return true;
        } else if (item.getItemId()==R.id.bestPunctuations) {
            startActivity(new Intent(this, ShowBestPunctuations.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void juegoTerminadoGuardado() {
        new PlayerNameDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
    }

    public boolean isGameIniciated(int position) {
        if (mJuego.serializaTablero().equals(mJuego.tableroInicialSerializado) ||
                mJuego.serializaTablero().equals(this.resumeGame(position))) {
            return false;
        } else {
            return true;
        }
    }
}
