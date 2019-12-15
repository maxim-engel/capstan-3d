package crossword;

class Rating implements Comparable<Rating> {
    private final Word word;
    private final int row;
    private final int column;
    private final CellOrientationState orientation;
    private int rating;

    public Rating(Word word, int row, int column, CellOrientationState orientation, int rating) {
        this.word = word;
        this.rating = rating;
        this.row = row;
        this.column = column;
        this.orientation = orientation;
    }

    public Word getWord() {
        return word;
    }

    public int getRating() {
        return rating;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CellOrientationState getOrientation() {
        return orientation;
    }

    public void alterRatingBy(int value) {
        rating += value;
    }

    @Override
    public int compareTo(Rating other) {
        int compResult = 1;

        if (other != null) {
            compResult = rating - other.rating;
        }

        return compResult;
    }
}