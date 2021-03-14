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
    public void testAddFirst() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(0);
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        a.addFirst(4);
        a.addFirst(5);
        a.addFirst(6);
        a.addFirst(7);

        assertEquals(7, (int) a.get(0));
        assertEquals(6, (int) a.get(1));
        assertEquals(5, (int) a.get(2));
        assertEquals(4, (int) a.get(3));
        assertEquals(3, (int) a.get(4));
        assertEquals(2, (int) a.get(5));
        assertEquals(1, (int) a.get(6));
        assertEquals(0, (int) a.get(7));
    }

    @Test
    public void testAddLast() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addLast(0);
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
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
    }

    @Test
    public void testAdd() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(2);
        a.addLast(3);
        a.addFirst(1);
        a.addFirst(0);
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
    }

    @Test
    public void testRemoveFirst() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(2);
        a.addFirst(1);
        a.addFirst(0);
        assertEquals(0, (int) a.removeFirst());
        assertEquals(1, (int) a.get(0));
        assertEquals(2, (int) a.get(1));
        assertEquals(1, (int) a.removeFirst());
        assertEquals(2, (int) a.get(0));
        assertEquals(null, a.get(1));
    }
}
