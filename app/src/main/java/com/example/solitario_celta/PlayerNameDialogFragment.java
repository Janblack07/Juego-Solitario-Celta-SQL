package com.example.solitario_celta;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
public class PlayerNameDialogFragment extends android.app.DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        LayoutInflater inflater = main.getLayoutInflater();

        View v = inflater.inflate(R.layout.nombre_jugadores, null);

        builder.setView(v);

        Button save_name = v.findViewById(R.id.player_name_button);
        final EditText editText = v.findViewById(R.id.player_name);
        editText.setText(main.preferences.getString(getString(R.string.playerName_key),""));
        save_name.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = main.preferences.edit();
                        editor.putString(getString(R.string.playerName_key),editText.getText().toString());
                        editor.commit();
                        GamePunctuation gamePunctuation = new GamePunctuation(null,null,0,0);
                        gamePunctuation.setPlayerName(main.preferences.getString(getString(R.string.playerName_key), ""));
                        Calendar calendar = Calendar.getInstance();
                        gamePunctuation.setDateTime(calendar.getTime().toString());
                        gamePunctuation.setPieces(main.mJuego.missingPieces());
                        Log.i("BML",gamePunctuation.toString());
                        main.db.insert(gamePunctuation);
                        new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
                        PlayerNameDialogFragment.this.getDialog().cancel();
                    }
                }
        );
        return builder.create();
    }
}
