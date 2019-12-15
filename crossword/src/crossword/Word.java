package crossword;

import java.util.ArrayList;
import java.util.List;

class Word {
    private String word;
    private int row;
    private int column;
    private CellOrientationState orientation;
    List<Cell> cells;

    public Word(String word, CellOrientationState orientation) {
        this.orientation = orientation;
        this.word = word;
        row = -1;
        column = -1;
    }

    public int getLength() {
        return word.length() + 1;
    }

    public CellOrientationState getOrientation() {
        return orientation;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // Yes, ths choice was made deliberately.
    public List<Cell> getCells() {
        if (cells == null) {
            cells = new ArrayList<>(word.length() + 1);
            cells.add(Cell.createDeadCell());
            
            for (char c : word.toCharArray()) {
                cells.add(new Cell(c, true));
            }
        }

        return cells;
    }
    
    public void setOrientation(CellOrientationState orientation) {
        if (this.orientation != orientation 
                && (orientation == CellOrientationState.VERTICAL 
                    || orientation == CellOrientationState.HORIZONTAL)) {
        
            this.orientation = orientation;
        }
    }

    @Override
    public String toString() {
        return word;
    }

    Pair<Integer, Integer> getAddressOf(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("The index position " + index + "is invalid.");
        }

        Pair<Integer, Integer> deltas = orientation.getOrientationDeltas();
        return new Pair<>(row + index * deltas.getLeft(), column + index * deltas.getRight());
    }
}

