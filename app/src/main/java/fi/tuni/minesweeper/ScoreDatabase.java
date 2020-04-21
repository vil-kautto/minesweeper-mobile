package fi.tuni.minesweeper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * High score database Interface
 * @author Ville Kautto <ville.kautto@hotmail.fi>
 * @version 2020.04.22
 * @since 2020.04.22
 */
@Database(entities = {Score.class}, version = 4)
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();
}
