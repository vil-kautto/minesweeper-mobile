package fi.tuni.minesweeper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Score.class}, version = 3)
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
