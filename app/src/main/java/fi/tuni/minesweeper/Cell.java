package fi.tuni.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class Cell extends AppCompatButton {
    private boolean revealed;           // is the cell revealed
    private boolean isMine;             // does the cell contain a mine
    private boolean isFlagged;          // is cell flagged as a potential mine
    private boolean isQuestionMarked;   // is cell question marked
    private boolean isClickable;        // can block accept click events
    private int surroundingMineCount;   // number of mines in nearby blocks

    public Cell(Context context) {
        super(context);
        setDefaults();

    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaults();
    }

    public Cell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaults();
    }

    /**
     * Assigns default values to Cell
     */
    public void setDefaults() {
        revealed = false;
        isMine = false;
        isFlagged = false;
        isQuestionMarked = false;
        isClickable = true;
        surroundingMineCount = 0;

        this.setBackgroundResource(R.drawable.square_gray);
        setBoldFont();
    }

    /**
     * Sets the background for the cell and assigns the count of mines to a cell
     * @param numberOfMines
     */
    public void setMineCount(int numberOfMines) {
        this.setBackgroundResource(R.drawable.square_gray);
        updateNumber(numberOfMines);
    }

    /**
     * Places a mine icon to cell
     */
    public void setMineIcon() {
        this.setText("M");
        this.setBackgroundResource(R.drawable.square_gray_dark);
        this.setTextColor(Color.RED);
    }

    /**
     * Places a flag icon to cell
     */
    public void setFlagIcon() {
        this.setText("F");
        this.setBackgroundResource(R.drawable.square_gray);
        this.setTextColor(Color.RED);
    }

    /**
     * Sets a question mark icon to selected cell
     */
    public void setQuestionMarkIcon() {
        this.setText("?");
        this.setBackgroundResource(R.drawable.square_gray);
        this.setTextColor(Color.BLUE);
    }

    /**
     * Changes the state of the cell from enabled to disabled
     * if enabled is false, the cell will be disabled
     * @param status sets the state of the cell
     */
    public void setCellDisabled(boolean status) {
        isClickable = status;
        if(status) {
            this.setBackgroundResource(R.drawable.square_gray_dark);
        } else {
            this.setBackgroundResource(R.drawable.square_gray);
        }
    }

    // clear all icons/text
    public void clearAllIcons() {
        this.setText("");
    }

    // set font as bold
    private void setBoldFont() {
        this.setTypeface(null, Typeface.BOLD);
    }

    /**
     * reveals a clicked cell
     */
    public void setRevealed() {
        // cannot uncover a mine which is not covered
        if(!revealed) {
            revealed = true;
            setCellDisabled(false);

            // Check for a mine
            if(hasMine()) {
                setMineIcon();
            } else {
                // add a number if surrounding cells contain a mine
                setMineCount(surroundingMineCount);
            }
            this.setBackgroundResource(R.drawable.square_gray_dark);
        }


    }

    /**
     * Assaings a different colour on numbers based on the count of nearby mines
     * @param text the number of mines nearby
     */
    public void updateNumber(int text) {
        // skip this step if there are no mines nearby
        if(text != 0) {
            this.setText(Integer.toString(text));

            // It is possible to get 8 mines on surrounding tiles
            switch(text) {
                case 1:
                    this.setTextColor(Color.BLUE);
                    break;
                case 2:
                    this.setTextColor(Color.rgb(0, 100, 0));
                    break;
                case 3:
                    this.setTextColor(Color.RED);
                    break;
                case 4:
                    this.setTextColor(Color.rgb(85, 26, 139));
                    break;
                case 5:
                    this.setTextColor(Color.rgb(139, 28, 98));
                    break;
                case 6:
                    this.setTextColor(Color.rgb(238, 173, 14));
                    break;
                case 7:
                    this.setTextColor(Color.rgb(47, 79, 79));
                    break;
                case 8:
                    this.setTextColor(Color.rgb(71, 71, 71));
                    break;
            }
        }
    }

    /**
     * Plants a mine to specified cell
     */
    public void plantMine() {
        isMine = true;
    }

    /**
     * Triggers game over if the player clicks the mine
     */
    public void triggerMine() {
        setMineIcon();
        this.setTextColor(Color.RED);
    }

    /**
     * Checks if the cell is relvealed to the player
     * @return true if the cell is revealed
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Checks if the cell has a mine
     * @return returns true if the cell contains a mine
     */
    public boolean hasMine() {
        return isMine;
    }

    /**
     * Sets the count of mines in surrounding cells
     * @param numberOfMines the number of mines nearby
     */
    public void setSurroundingMineCount(int numberOfMines) {
        surroundingMineCount = numberOfMines;
    }

    /**
     * Returns the count of mines in surrounding cells
     * @return number of mines in surrounding cells
     */
    public int getSurroundingMineCount() {
        return surroundingMineCount;
    }

    /**
     * Returns cells current flagged status
     * @return cell's flagged status
     */
    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Sets cell's flagged status
     * @param flagged wanted change in status
     */
    public void setFlagged(boolean flagged) {
        if(!isRevealed()) {
            if(isQuestionMarked()) {
                setQuestionMarked(false);
            }
            isFlagged = flagged;
            setFlagIcon();

        }
    }

    /**
     *
     * @return true if the selected cell is questionMarked
     */
    public boolean isQuestionMarked() {
        return isQuestionMarked;
    }

    /**
     * Marks a cell as questionMarked
     * @param questionMarked
     */
    public void setQuestionMarked(boolean questionMarked) {
        if(!isRevealed()) {
            if(isFlagged()) {
                setFlagged(false);
            }
            isQuestionMarked = questionMarked;
            setQuestionMarkIcon();
        }

    }

    /**
     * gets the clickability state of the cell
     * @return the clickability state of the cell
     */
    public boolean isClickable() {
        return isClickable;
    }

    /**
     * Disables the click interaction of the cell if set to false
     * @param clickable the clickability of the cell
     */
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }
}