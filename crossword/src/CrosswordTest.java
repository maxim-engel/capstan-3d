import crossword.Crossword;
import java.io.IOException;

public class CrosswordTest {
    public static void main(String[] args) {
        try {
            Crossword crossword = new Crossword(20, 20, 10, "resources/words.txt");
            crossword.generate();

            for (int i = 0; i < crossword.getHeight(); i++) {
                for (int j = 0; j < crossword.getWidth(); j++) {
                    char symbol = crossword.getSymbolAt(i, j);
                    System.out.print((symbol == '\0' ? ' ' : symbol) + " ");
                }

                System.out.println();
            }
        } catch(IOException ex) {
            System.out.println("An error occured: " + ex.getMessage());
        }
    }
}
