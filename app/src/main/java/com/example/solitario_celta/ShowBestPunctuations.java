package com.example.solitario_celta;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
public class ShowBestPunctuations  extends AppCompatActivity {
    ListView lvPunctuation;
    private GamePunctuationRepository db;
    ArrayList<GamePunctuation> punctuations;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuacion);
        lvPunctuation = findViewById(R.id.lvListadoPuntuaciones);
        db = new GamePunctuationRepository(this);
        punctuations = db.getAll();

        lvPunctuation.setAdapter(new GamePunctuationAdapter(
                this,
                punctuations,

                R.layout.layout_puntuacion
        ));
    }
    public void deleteBD (View view){
        new DeleteInformationDialogFragment().show(getFragmentManager(),"DIALOG");
    }
}
