package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class GraphPoetTest {

    // Testing strategy:
    // 1. Test with an empty corpus.
    // 2. Test with a corpus containing one word.
    // 3. Test with a corpus containing multiple words:
    //    - Single adjacency for all words.
    //    - Repeated adjacencies for some words.
    // 4. Test poem generation:
    //    - Input with no bridgeable words.
    //    - Input with one bridgeable word pair.
    //    - Input with multiple bridgeable word pairs.

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // Make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testEmptyCorpus() throws IOException {
        File corpus = new File("test/poet/empty.txt");
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Hello world";
        assertEquals("Hello world", poet.poem(input));
    }

    @Test
    public void testSingleWordCorpus() throws IOException {
        File corpus = new File("test/poet/single-word.txt");
        GraphPoet poet = new GraphPoet(corpus);
        String input = "Hello world";
        assertEquals("Hello world", poet.poem(input));
    }

    


}
