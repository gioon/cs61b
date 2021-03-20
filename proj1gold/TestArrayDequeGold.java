import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        String message = "";
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        int t = StdRandom.uniform(100, 200);
        for (int i = 0; i < t; i++) {
            Integer a = StdRandom.uniform(0, 10);
            Integer b = StdRandom.uniform(0, 10);
            Integer c = StdRandom.uniform(0, 10);
            sad.addLast(a);
            ads.addLast(a);
            message = message + "addLast(" + a + ")\n" + "removeFirst()\n";
            assertEquals(message, ads.removeFirst(), sad.removeFirst());
            sad.addFirst(b);
            ads.addFirst(b);
            message = message + "addFirst(" + b + ")\n" + "removeLast()\n";
            assertEquals(message, ads.removeLast(), sad.removeLast());
            sad.addLast(c);
            ads.addLast(c);
            message = message + "addLast(" + c + ")\n";
        }
    }
}
