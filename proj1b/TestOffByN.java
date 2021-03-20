import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    @Test
    public void testEqualChars() {
        CharacterComparator offByN = new OffByN(2);

        assertTrue(offByN.equalChars('a', 'c'));
        assertTrue(offByN.equalChars('c', 'a'));
        assertTrue(offByN.equalChars('r', 'p'));

        assertFalse(offByN.equalChars('a', 'e'));
        assertFalse(offByN.equalChars('z', 'a'));
        assertFalse(offByN.equalChars('a', 'a'));
    }

}
