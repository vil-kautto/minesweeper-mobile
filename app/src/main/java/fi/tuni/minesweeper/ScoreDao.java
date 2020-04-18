package fi.tuni.minesweeper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void addScore(Score score);

    @Insert
    void addScores(Score... scores);

    @Query("DELETE FROM scores")
    void deleteAll();

    @Query("SELECT * FROM scores ORDER BY score ASC LIMIT 10")
    List<Score> getAllScores();

    @Query("SELECT * FROM scores WHERE difficulty = 'easy' ORDER BY score ASC LIMIT 10")
    List<Score> getEasyScores();

    @Query("SELECT * FROM scores WHERE difficulty = 'medium' ORDER BY score ASC LIMIT 10")
    List<Score> getMediumScores();

    @Query("SELECT * FROM scores WHERE difficulty = 'hard' ORDER BY score ASC LIMIT 10")
    List<Score> getHardScores();

    @Query("SELECT * FROM scores WHERE difficulty = 'extreme' ORDER BY score ASC LIMIT 10")
    List<Score> getExtremeScores();


}
