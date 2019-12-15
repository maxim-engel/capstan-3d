package crossword;

enum CellOrientationState {
    HORIZONTAL {
        @Override
        public CellOrientationState inverse() {
            return VERTICAL;
        }

        @Override
        public Pair<Integer, Integer> getOrientationDeltas() {
            return new Pair<>(0, 1);
        }
    }, 
    VERTICAL {
        @Override
        public CellOrientationState inverse() {
            return HORIZONTAL;
        }

        @Override
        public Pair<Integer, Integer> getOrientationDeltas() {
            return new Pair<>(1, 0);
        }
    };

    public abstract CellOrientationState inverse();

    public abstract Pair<Integer, Integer> getOrientationDeltas();
}
