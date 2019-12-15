package crossword;

class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = false;

        if (other != null && other instanceof Pair<?, ?>) {
            Pair<?, ?> pair = (Pair<?, ?>) other;
            equal = pair.getLeft().equals(getLeft()) && pair.getRight().equals(getRight());
        }

        return equal;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ", " + right.toString() + ")";
    }
}

