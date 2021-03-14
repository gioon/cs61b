public class ArrayDeque<T> {
    private static final int RFACTOR = 2;
    private T[] items;
    private int size;

    private int nextFirst, nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
        size = 0;
    }

//    public ArrayDeque(T item) {
//        items = (T[]) new Object[8];
//        items[3] = item;
//        nextFirst = 2;
//        nextLast = 4;
//        size = 1;
//    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int aLast = capacity / 2;
        int aFirst = aLast - 1;

        for (int i = 0; i < size; i++) {
            a[aLast] = get(i);
            aLast++;
            if (aLast == capacity) {
                aLast = 0;
            }
        }

        items = a;
        nextFirst = aFirst;
        nextLast = aLast;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = item;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int i = nextFirst + 1;

        for (int cnt = 0; cnt < size; cnt++) {
            if (i >= items.length) {
                i -= items.length;
            }
            System.out.print(items[i] + " ");
            i++;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst++;
        }
        T item = items[nextFirst];
        items[nextFirst] = null;

        size--;
        if ((double) size / items.length < 0.25 && items.length > 16) {
            resize(items.length / 2);
        }
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            nextLast--;
        }
        T item = items[nextLast];
        items[nextLast] = null;

        size--;
        if ((double) size / items.length < 0.25 && items.length > 16) {
            resize(items.length / 2);
        }
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
