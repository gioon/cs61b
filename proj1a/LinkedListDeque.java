/** circular sentinel topology */
public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev, next;
        Node(Node p, T i, Node n) {
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

//    public LinkedListDeque(T item) {
//        sentinel = new Node(null, null, null);
//        sentinel.next = new Node(sentinel, item, sentinel);
//        sentinel.prev = sentinel.next;
//        size = 1;
//    }

    public void addFirst(T item) {
        Node old = sentinel.next;
        sentinel.next = new Node(sentinel, item, old);
        old.prev = sentinel.next;
        size++;
    }

    public void addLast(T item) {
        Node old = sentinel.prev;
        old.next = new Node(old, item, sentinel);
        sentinel.prev = old.next;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    public T removeFirst() {
        Node old = sentinel.next;
        if (old == sentinel) {
            return null;
        }
        sentinel.next = old.next;
        old.next.prev = sentinel;
        size--;
        return old.item;
    }

    public T removeLast() {
        Node old = sentinel.prev;
        if (old == sentinel) {
            return null;
        }
        sentinel.prev = old.prev;
        old.prev.next = sentinel;
        size--;
        return old.item;
    }

    public T get(int index) {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            if (index == 0) {
                return p.item;
            }
            index--;
        }
        return null;
    }

    private T getHelper(Node p, int index) {
        if (p == sentinel) {
            return null;
        }
        if (index == 0) {
            return p.item;
        }
        return getHelper(p.next, index - 1);
    }

    public T getRecursive(int index) {
        return getHelper(sentinel.next, index);
    }
}

/** two sentinel topology */
//public class LinkedListDeque<T> {
//
//    private class Node {
//        private T item;
//        private Node prev, next;
//        Node(Node p, T i, Node n) {
//            prev = p;
//            item = i;
//            next = n;
//        }
//    }
//
//    private Node sentFront, sentBack;
//    private int size;
//
//    public LinkedListDeque() {
//        sentFront = new Node(null, null, null);
//        sentBack = new Node(sentFront, null, null);
//        sentFront.next = sentBack;
//        size = 0;
//    }
//
////    public LinkedListDeque(T item) {
////        sentFront = new Node(null, null, null);
////        sentFront.next = new Node(sentFront, item, null);
////        sentBack = new Node(sentFront.next, null, null);
////        sentFront.next.next = sentBack;
////        size = 1;
////    }
//
//    public void addFirst(T item) {
//        Node old = sentFront.next;
//        sentFront.next = new Node(sentFront, item, old);
//        old.prev = sentFront.next;
//        size++;
//    }
//
//    public void addLast(T item) {
//        Node old = sentBack.prev;
//        old.next = new Node(old, item, sentBack);
//        sentBack.prev = old.next;
//        size++;
//    }
//
//    public boolean isEmpty() {
//        return size == 0;
//    }
//
//    public int size() {
//        return size;
//    }
//
//    public void printDeque() {
//        Node p = sentFront;
//        while (p.next != sentBack) {
//            p = p.next;
//            System.out.print(p.item + " ");
//        }
//    }
//
//    public T removeFirst() {
//        Node old = sentFront.next;
//        if (old == sentBack) {
//            return null;
//        }
//        sentFront.next = old.next;
//        old.next.prev = sentFront;
//        size--;
//        return old.item;
//    }
//
//    public T removeLast() {
//        Node old = sentBack.prev;
//        if (old == sentFront) {
//            return null;
//        }
//        sentBack.prev = old.prev;
//        old.prev.next = sentBack;
//        size--;
//        return old.item;
//    }
//
//    public T get(int index) {
//        Node p = sentFront;
//        while (p.next != sentBack) {
//            p = p.next;
//            if (index == 0) {
//                return p.item;
//            }
//            index--;
//        }
//        return null;
//    }
//
//    private T getHelper(Node p, int index) {
//        if (p == sentBack) {
//            return null;
//        }
//        if (index == 0) {
//            return p.item;
//        }
//        return getHelper(p.next, index - 1);
//    }
//
//    public T getRecursive(int index) {
//        return getHelper(sentFront.next, index);
//    }
//}
