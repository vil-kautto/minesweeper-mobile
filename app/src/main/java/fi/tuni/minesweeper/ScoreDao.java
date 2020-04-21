package fi.tuni.minesweeper;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Scoredao Sends queries to database and reruns requested Score Objects
 */
@Dao
public interface ScoreDao {
    /**
     * Adds a score
     * @param score
     */
    @Insert
    void addScore(Score score);

    /**
     * Adds multiple scores
     * @param scores
     */
    @Insert
    void addScores(Score... scores);

    /**
     * Deletes all the objects from the database
     */
    @Query("DELETE FROM scores")
    void deleteAll();

    /**
     * Gets all the entries in the database
     * @return All the Scores
     */
    @Query("SELECT * FROM scores ORDER BY score ASC LIMIT 10")
    List<Score> getAllScores();

    /**
     * Fetches top 10 scores with easy difficulty
     * @return 10 easy Scores
     */
    @Query("SELECT * FROM scores WHERE difficulty = 'easy' ORDER BY score ASC LIMIT 10")
    List<Score> getEasyScores();

    /**
     * Fetches top 10 scores with medium difficulty
     * @return 10 medium Scores
     */
    @Query("SELECT * FROM scores WHERE difficulty = 'medium' ORDER BY score ASC LIMIT 10")
    List<Score> getMediumScores();

    /**
     * Fetches top 10 scores with hard difficulty
     * @return 10 hard Scores
     */
    @Query("SELECT * FROM scores WHERE difficulty = 'hard' ORDER BY score ASC LIMIT 10")
    List<Score> getHardScores();

    /**
     * Fetches top 10 scores with extreme difficulty
     * @return 10 extreme Scores
     */
    @Query("SELECT * FROM scores WHERE difficulty = 'extreme' ORDER BY score ASC LIMIT 10")
    List<Score> getExtremeScores();


}
