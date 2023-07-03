package com.example.solitario_celta;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SCeltaPreferenceFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuracion);
    }
}