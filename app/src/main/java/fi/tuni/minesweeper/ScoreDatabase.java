package fi.tuni.minesweeper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Score.class}, version = 2)
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
