public class ArrayDeque<T> {
    private static final int RFACTOR = 2;
    private T[] items;
    private int size;

    private int nextFirst, nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }

//    public ArrayDeque(T item) {
//        items = (T[]) new Object[8];
//        items[4] = item;
//        nextFirst = 3;
//        nextLast = 5;
//        size = 1;
//    }

//    private void resize(int capacity) {
//        T[] a = (T[]) new Object[capacity];
//        int aNextFirst = capacity / 2;
//        int aNextLast = aNextFirst + 1;
//
////        for (int i = 0; i < size; i++) {
////            a[aNextLast] = get(i);
////            aNextLast++;
////        }
//
//        int i = 0;
//        while (i < size / 2) {
//            a[aNextFirst] = get(i);
//            aNextFirst--;
//            i++;
//        }
//        while (i < size) {
//            a[aNextLast] = get(i);
//            aNextLast++;
//            i++;
//        }
//
//        items = a;
//    }

    public void addFirst(T item) {
//        if (size == items.length) {
//            resize(size * RFACTOR);
//        }
        items[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
    }

    public void addLast(T item) {
//        if (size == items.length) {
//            resize(size * RFACTOR);
//        }
        items[nextLast] = item;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (nextFirst > nextLast) {
            for (int i = nextFirst + 1; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i < nextLast; i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            for (int i = nextFirst + 1; i < nextLast; i++) {
                System.out.print(items[i] + " ");
            }
        }
    }

    public T removeFirst() {
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst++;
        }
        T item = items[nextFirst];
        items[nextFirst] = null;

//        if (size / items.length < 0.25 && items.length > 16) {
//            resize(items.length / 2);
//        }
        return item;
    }

    public T removeLast() {
        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            nextLast--;
        }
        T item = items[nextLast];
        items[nextLast] = null;

//        if (size / items.length < 0.25 && items.length > 16) {
//            resize(items.length / 2);
//        }
        return item;
    }

    public T get(int index) {
        int realIndex = nextFirst + index + 1;
        if (realIndex >= items.length) {
            realIndex -= items.length;
        }
        return items[realIndex];
    }
}
