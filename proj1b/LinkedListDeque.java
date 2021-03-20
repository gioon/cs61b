/** circular sentinel topology */
public class LinkedListDeque<T> implements Deque<T> {

    private class Node {
        private T item;
        private Node prev, next;
        private Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node old = sentinel.next;
        sentinel.next = new Node(sentinel, item, old);
        old.prev = sentinel.next;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node old = sentinel.prev;
        old.next = new Node(old, item, sentinel);
        sentinel.prev = old.next;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node old = sentinel.next;
        sentinel.next = old.next;
        old.next.prev = sentinel;
        size--;
        return old.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node old = sentinel.prev;
        sentinel.prev = old.prev;
        old.prev.next = sentinel;
        size--;
        return old.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }

        Node p = sentinel;
        while (index >= 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    private T getRecursiveHelper(Node p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }

    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }
}
