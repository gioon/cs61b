import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayDequeTest {

    @Test
    public void testIsEmpty() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertEquals(true, a.isEmpty());
    }

    @Test
    public void testSize() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertEquals(0, a.size());
    }

    @Test
    public void testGet() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertEquals(null, a.get(0));
        assertEquals(null, a.get(1));
    }

    @Test
    public void testAdd() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(2);
        a.addLast(3);
        a.addFirst(1);
        a.addFirst(0);
        assertEquals(4, a.size());
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        a.addLast(7);

        assertEquals(0, (int) a.get(0));
        assertEquals(1, (int) a.get(1));
        assertEquals(2, (int) a.get(2));
        assertEquals(3, (int) a.get(3));
        assertEquals(4, (int) a.get(4));
        assertEquals(5, (int) a.get(5));
        assertEquals(6, (int) a.get(6));
        assertEquals(7, (int) a.get(7));
        assertEquals(8, a.size());
    }

    @Test
    public void testRemove() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(2);
        a.addFirst(1);
        a.addFirst(0);
        a.addLast(3);
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        a.addLast(7);

        assertEquals(0, (int) a.removeFirst());
        assertEquals(1, (int) a.removeFirst());
        assertEquals(6, a.size());
        assertEquals(2, (int) a.get(0));
        assertEquals(false, a.isEmpty());
        assertEquals(7, (int) a.removeLast());
        assertEquals(6, (int) a.removeLast());
        assertEquals(5, (int) a.removeLast());
        assertEquals(4, (int) a.removeLast());
        a.removeFirst();
        a.removeLast();
        assertEquals(null, a.get(1));
        assertEquals(0, a.size());
        assertEquals(true, a.isEmpty());
    }
}
