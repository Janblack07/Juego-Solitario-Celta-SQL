package com.example.solitario_celta;
import android.provider.BaseColumns;
public class GamePunctuationContract {
    private GamePunctuationContract (){
}
    public static class GamePunctuationEntry implements BaseColumns {
        public static final String TABLE_NAME = "gamePunctuation";

        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_PLAYERNAME = "playerName";
        public static final String COLUMN_NAME_DATETIME = "dateTime";
        public static final String COLUMN_NAME_MISSINGPIECES = "missingPieces";

    }
}