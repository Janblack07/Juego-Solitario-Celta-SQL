package com.example.solitario_celta;
import android.os.Parcel;
import android.os.Parcelable;
public class GamePunctuation {
    private int id;
    private String playerName;
    private String dateTime;
    private int pieces;

    public GamePunctuation(String playerName, String dateTime, int pieces,int id) {
        this.playerName = playerName;
        this.dateTime = dateTime;
        this.pieces = pieces;
        this.id = id;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }


    @Override
    public String toString() {
        return "GamePunctuation{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", dateTime=" + dateTime +
                ", pieces=" + pieces +
                '}';
    }

    protected GamePunctuation(Parcel in) {
        id = in.readInt();
        playerName = in.readString();
        dateTime = in.readString();
        pieces = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(playerName);
        dest.writeString(dateTime);
        dest.writeInt(pieces);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GamePunctuation> CREATOR = new Parcelable.Creator<GamePunctuation>() {
        @Override
        public GamePunctuation createFromParcel(Parcel in) {
            return new GamePunctuation(in);
        }

        @Override
        public GamePunctuation[] newArray(int size) {
            return new GamePunctuation[size];
        }
    };
}
