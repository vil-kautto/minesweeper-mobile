package fi.tuni.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TableRow;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

/**
 * Cell object base used in minesweeper's game -activity
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.07
 * @since       2020.03.24
 */
public class Cell extends AppCompatButton{
    private boolean revealed;           // is the cell revealed
    private boolean isMine;             // does the cell contain a mine
    private boolean isFlagged;          // is cell flagged as a potential mine
    private boolean isQuestionMarked;   // is cell question marked
    private boolean isClickable;        // can block accept click events
    private int surroundingMines;       // number of mines in nearby blocks

    public Cell(Context context) {
        super(context);
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
        surroundingMines = 0;

        this.setBackgroundResource(R.drawable.square_gray);

        // Scaling pixels to dps
        int dps = 45;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        this.setLayoutParams(new TableRow.LayoutParams(pixels, pixels));
        setBoldFont();
    }

    /**
     * Sets the background for the cell and assigns the count of mines to a cell
     * @param numberOfMines
     */
    public void setMineCount(int numberOfMines) {
        this.surroundingMines = numberOfMines;
    }

    /**
     * returns the number of mines on  surrounding Cells
     * @return  number of mines on surrounding cells
     */
    public int getMineCount() {
        return this.surroundingMines;
    }

    /**
     * Places a mine icon to cell
     */
    public void setMineIcon() {
        this.setBackgroundResource(R.drawable.square_gray_mine);
    }

    /**
     * Places a flag icon to cell
     */
    public void setFlagIcon() {
        this.setBackgroundResource(R.drawable.square_gray_flagged);
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
     * if status is true, the cell will be disabled
     * @param status sets the state of the cell
     */
    public void setCellDisabled(boolean status) {
        this.isClickable = !status;
        if(status) {
            this.setBackgroundResource(R.drawable.square_gray_dark);
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
        if(!this.revealed && this.isClickable) {
            revealed = true;
            setClickable(false);

            // Check for a mine
            if(hasMine()) {
                setMineIcon();
            } else {
                // add a number if surrounding cells contain a mine
                updateNumber();
            }
        }
    }

    /**
     * Assaings a different colour on numbers based on the count of nearby mines
     */
    public void updateNumber() {
        // Change background color
        this.setBackgroundResource(R.drawable.square_gray_revealed);
        // skip this step if there are no mines nearby
        if(this.surroundingMines > 0) {
            this.setText(Integer.toString(this.surroundingMines));

            // It is possible to get 8 mines on surrounding tiles
            switch(this.surroundingMines) {
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
        } else {
            this.setTextColor(Color.BLACK);
        }
    }

    /**
     * Plants a mine to specified cell
     */
    public void plantMine() {
        isMine = true;
    }

    /**
     * Plants a mine to specified cell
     */
    public void triggerMine() {
        this.setBackgroundResource(R.drawable.square_gray_mine_triggered);
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