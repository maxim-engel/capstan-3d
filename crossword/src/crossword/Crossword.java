package crossword;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import java.util.ArrayList;

public class Crossword {
    private final int SCALE = 3;
    private final int ARRAY_WIDTH;
    private final int ARRAY_HEIGHT;

    private CellOrientationState currentOrientation = CellOrientationState.VERTICAL;
    
    private int actualWidth;
    private int actualHeight;

    private int minRow;
    private int minColumn;

    private int currentIndex;
    private int maxWidth;
    private int maxHeight;
    private int wordCount;
    private List<Word> wordPool;
    private List<Word> placedWords;
    private List<Word> garbageHeap;
    
    private Cell[][] cells;

    public Crossword(int maxWidth, int maxHeight, int wordCount, String wordResource) throws IOException {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.wordCount = wordCount;

        ARRAY_HEIGHT = maxHeight * SCALE;
        ARRAY_WIDTH = maxWidth * SCALE;

        cells = new Cell[ARRAY_HEIGHT][ARRAY_WIDTH];
        placedWords = new ArrayList<>();
        garbageHeap = new ArrayList<>();
        wordPool = new ArrayList<>();
        
        // The first word will always be placed horizontally.
        minRow = maxHeight * SCALE / 2;
        minColumn = maxWidth;
        actualHeight = 1;
        
        loadWords(wordResource);
    }

    public int getWidth() {
        return actualWidth;
    }

    public int getHeight() {
        return actualHeight;
    }
    
    public boolean isFieldVisible(int row, int column) {
        boolean visible = false;
        row += minRow;
        row += minColumn;
        if (isIndexValid(row, column) && cells[row][column] != null) {
            visible = cells[row - minRow][column - minColumn].isVisible();
        }
        return visible;
    }

    
    public char getSymbolAt(int row, int column) throws ArrayIndexOutOfBoundsException {
        row += minRow;
        column += minColumn;
        if (!isIndexValid(row, column)) {
            throw new ArrayIndexOutOfBoundsException("This is actually bad... :-(");
        }
        
        Cell cell = cells[row][column];
        return cell == null ? '\0' : cell.getSymbol();
    }
    
    public void generate() {
        // First, lets put a random word somewhere.
        Word word = next();
        if (word != null) {
            placeFirstWord(word);
            word = next();
            while (word != null) {
                placeWordOnBoard(word);
    
                // Get next word
                word = next();
            }
        }
    }

    private void loadWords(String resource) throws IOException {
        int maxWordLength = Math.min(maxWidth, maxHeight);

        File file = new File(resource);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String s;
        while ((s = reader.readLine()) != null) {
            s = s.toUpperCase().trim();
            int length = s.length();
            if (0 < length && length <= maxWordLength && s.chars().allMatch(Character::isLetter)) {
                wordPool.add(new Word(s, CellOrientationState.HORIZONTAL));
            }
        }

        reader.close();
        Collections.shuffle(wordPool);
    }

    private void placeFirstWord(Word word) {
        if (placedWords.size() == 0) {
            word.setOrientation(CellOrientationState.HORIZONTAL);
            word.setPosition(minRow, minColumn);
            actualWidth = word.getLength();
            copyWordTo(word);
            placedWords.add(word);
        }
    }

    private boolean isIndexValid(int row, int column) {
        return minRow <= row && row < minRow + actualHeight 
            && minColumn <= column && column < minColumn + actualWidth;
    }

    private void setCellAt(Cell cell, int row, int column) {
        if (isIndexValid(row, column)) {
            cells[row][column] = cell;
        }
    }

    private void copyWordTo(Word word) {
        int row = word.getRow();
        int column = word.getColumn();
        List<Cell> wordCells = word.getCells();
        CellOrientationState currentOrientation = word.getOrientation();

        Pair<Integer, Integer> orientationDeltas = currentOrientation.getOrientationDeltas();
        int dr = orientationDeltas.getLeft();
        int dc = orientationDeltas.getRight();
        for (int i = 0; i < wordCells.size(); i++) {
            setCellAt(wordCells.get(i), row + i * dr, column + i * dc);
        }
    }

    private boolean checkPossibleNewBorders(int row, int column, int length) {
        boolean valid = false;
        int startRow = Math.min(row, minRow);
        int finalRow = Math.max(row + length, minRow + actualHeight);
        int startColumn = Math.min(column, minColumn);
        int finalColumn = Math.max(column + length, minColumn + actualWidth);

        if (currentOrientation == CellOrientationState.VERTICAL) {

            valid = finalRow < ARRAY_HEIGHT 
                && finalRow - startRow <= maxHeight
                && finalColumn - column <= maxWidth;
        } else {
            valid = finalColumn < ARRAY_WIDTH
                && finalColumn - startColumn <= maxHeight 
                && finalRow - row <= maxHeight;
        }

        return valid;
    }

    private boolean canPlaceWordAt(Word word, int row, int column) {
        int length = word.getLength();
        boolean valid = checkPossibleNewBorders(row, column, length);

        // If it is not the first word...
        if (getHeight() != 0 && valid) {
            List<Cell> cells = word.getCells();
            Pair<Integer, Integer> deltas = currentOrientation.getOrientationDeltas();
            int dr = deltas.getLeft();
            int dc = deltas.getRight();

            for (int i = 0; i < cells.size() && valid; i++) {
                Cell cell = this.cells[row + i * dr][column + i * dc];
                char original = cell == null ? '\0' : cell.getSymbol();
                valid = original == '\0' || original == cells.get(i).getSymbol();
            }
        }

        return valid;
    }

    private Pair<Integer, Integer> calculateStartPosition(
        Word word, 
        Pair<Integer, Integer> intersectionAddress, 
        int i) {

        Pair<Integer, Integer> deltas = currentOrientation.getOrientationDeltas();
        
        int row = intersectionAddress.getLeft() - i * deltas.getLeft();
        int column = intersectionAddress.getRight() - i * deltas.getRight();

        return new Pair<Integer, Integer>(row, column);
    }

    private void addMatch(
        Map<Pair<Integer, Integer>, Rating> ratings, 
        Pair<Integer, Integer> startPosition, 
        Word word) {
        
        if (canPlaceWordAt(word, startPosition.getLeft(), startPosition.getRight())) {
            if (ratings.keySet().contains(startPosition)) {
                ratings.get(startPosition).alterRatingBy(1);
            } else {
                Rating rating = new Rating(
                    word, 
                    startPosition.getLeft(), 
                    startPosition.getRight(),
                    currentOrientation,
                    1);
                
                ratings.put(startPosition, rating);
            }
        }
    }

    private void findPossibleIntersectionPoints(
        Word placedWord, 
        Word word, 
        Map<Pair<Integer, Integer>, Rating> ratings) {
        
        List<Cell> placedWordCells = placedWord.getCells();
        List<Cell> wordCells = word.getCells();
        for (int i = 1; i < wordCells.size(); i++) {
            char symbol = wordCells.get(i).getSymbol();
            for (int j = 1; j < placedWordCells.size(); j++) {
                if (symbol == placedWordCells.get(j).getSymbol()) {
                    Pair<Integer, Integer> start = calculateStartPosition(
                        word, 
                        placedWord.getAddressOf(j), 
                        i);
                    
                    addMatch(ratings, start, word);
                }
            }
        }
    }

    private Rating findBestWordFit(Word word) {
        Rating bestFit = null;

        List<Word> wordsToCheck = placedWords
            .stream()
            .filter(w -> w.getOrientation() != currentOrientation)
            .collect(Collectors.toList());

        Map<Pair<Integer, Integer>, Rating> ratings = new HashMap<>();

        wordsToCheck
            .stream()
            .forEach(w -> findPossibleIntersectionPoints(w, word, ratings));

        if (ratings.size() > 0) {
            // There has to be a value returned because the map is not empty.
            bestFit = ratings.values().stream().max(Rating::compareTo).get();
        } else {
            placeWordAtRandom(word, currentOrientation, ratings);
        }

        return bestFit;
    }

    private void placeWordAtRandom(
        Word word, 
        CellOrientationState currentOrientation,
        Map<Pair<Integer, Integer>, Rating> ratings) {
        
        Random random = new Random();
        int row = 0;
        int column = 0;
        
        if (currentOrientation == CellOrientationState.VERTICAL) {
            column = random.nextInt(100) < 50 ? minColumn : minColumn + actualWidth;
            row = random.nextInt(actualHeight) + minRow;
        } else {
            row = random.nextInt(100) < 50 ? minRow : minRow + actualHeight;
            column = random.nextInt(actualWidth) + minColumn;
        }

        addMatch(ratings, new Pair<>(row, column), word);
    }

    private void dumpWord(Word word) {
        if (!garbageHeap.contains(word)) {
            garbageHeap.add(word);
        }
    }

    private void updateBorders(Rating bestFit) {
        Word placedWord = bestFit.getWord();
        Pair<Integer, Integer> deltas = bestFit.getOrientation().getOrientationDeltas();

        int maxRow = Math.max(minRow + actualHeight, 
            bestFit.getRow() + deltas.getLeft() * placedWord.getLength());
        minRow = Math.min(minRow, bestFit.getRow());
        actualHeight = maxRow - minRow;
        
        int maxColumn = Math.max(minColumn + actualWidth, 
            bestFit.getColumn() + deltas.getRight() * placedWord.getLength());
        minColumn = Math.min(minColumn, bestFit.getColumn());
        actualWidth = maxColumn - minColumn;
    }

    private void placeWordOnBoard(Word word) {
        inverseCurrentOrientation();
        Rating bestFit = findBestWordFit(word);

        if (bestFit != null) {
            word.setOrientation(bestFit.getOrientation());
            updateBorders(bestFit);
            word.setPosition(bestFit.getRow(), bestFit.getColumn());
            copyWordTo(word);
            placedWords.add(word);
        } else {
            // Note: we do not change the orientation here because there might be no space left.
            dumpWord(word);
        }
    }

    private void inverseCurrentOrientation() {
        currentOrientation = currentOrientation.inverse();
    }

    private Word next() {
        Word word = null;
        int poolSize = wordPool.size();
        List<Word> pool = wordPool;
        int actualIndex = currentIndex;

        if (currentIndex >= poolSize) {
            actualIndex -= poolSize;
            pool = garbageHeap;
            poolSize = garbageHeap.size();
        }

        if (placedWords.size() < wordCount && actualIndex < poolSize) {
            word = pool.get(actualIndex);
            currentIndex++;
        }

        return word;
    }
}
