package fi.tuni.minesweeper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * High score database Interface
 */
@Database(entities = {Score.class}, version = 4)
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
