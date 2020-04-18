package fi.tuni.minesweeper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int sid;

    @ColumnInfo
    public int score;

    @ColumnInfo
    public String date;

    @ColumnInfo
    public String difficulty;

    public Score(int score, String difficulty) {
        this.score = score;
        this.difficulty = difficulty;

        Date currentDate = new Date();
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd.MM.yyyy hh:mm");
        this.date = dateFormat.format(currentDate);
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
