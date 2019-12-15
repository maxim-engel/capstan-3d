package crossword;

class Cell {
    private char symbol;
    private boolean visible;
    private boolean dead;

    public Cell(char symbol, boolean visible) {
        this(symbol, visible, false);
    }

    private Cell(char symbol, boolean visible, boolean dead) {
        this.symbol = symbol;
        this.visible = visible;
        this.dead = dead;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = dead;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }

    public boolean canBeCombinedWith(Cell other) {
        return symbol == other.symbol && !dead && !other.dead;
    }

    public static Cell createDeadCell() {
        return new Cell('#', true, true);
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if (other != null && other instanceof Cell) {
            Cell cell = (Cell) other;
            equal = getSymbol() == cell.getSymbol();
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return symbol;
    }
}
