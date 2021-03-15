//public class ArrayDeque<T> {
//    private static final int RFACTOR = 2;
//    private T[] items;
//    private int size;
//    private int nextFirst, nextLast;
//
//    public ArrayDeque() {
//        items = (T[]) new Object[8];
//        nextFirst = 3;
//        nextLast = 4;
//        size = 0;
//    }
//
////    public ArrayDeque(T item) {
////        items = (T[]) new Object[8];
////        items[3] = item;
////        nextFirst = 2;
////        nextLast = 4;
////        size = 1;
////    }
//
//    private void resize(int capacity) {
//        T[] a = (T[]) new Object[capacity];
//        int aLast = capacity / 2;
//        int aFirst = aLast - 1;
//
//        for (int i = 0; i < size; i++) {
//            a[aLast] = get(i);
//
//            aLast++;
//            if (aLast == capacity) {
//                aLast = 0;
//            }
////            aLast = (aLast + 1) % capacity;
//        }
//
//        items = a;
//        nextFirst = aFirst;
//        nextLast = aLast;
//    }
//
//    public void addFirst(T item) {
//        if (size == items.length) {
//            resize(size * RFACTOR);
//        }
//
//        items[nextFirst] = item;
//
//        if (nextFirst == 0) {
//            nextFirst = items.length - 1;
//        } else {
//            nextFirst--;
//        }
////        nextFirst = (nextFirst - 1 + items.length) % items.length;
//
//        size++;
//    }
//
//    public void addLast(T item) {
//        if (size == items.length) {
//            resize(size * RFACTOR);
//        }
//
//        items[nextLast] = item;
//
//        if (nextLast == items.length - 1) {
//            nextLast = 0;
//        } else {
//            nextLast++;
//        }
////        nextLast = (nextLast + 1 % items.length);
//
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
//        int i = nextFirst + 1;
//
//        for (int cnt = 0; cnt < size; cnt++) {
//            if (i >= items.length) {
//                i -= items.length;
//            }
////            i = i % items.length;
//
//            System.out.print(items[i] + " ");
//            i++;
//        }
//    }
//
//    public T removeFirst() {
//        if (size == 0) {
//            return null;
//        }
//
//        if (nextFirst == items.length - 1) {
//            nextFirst = 0;
//        } else {
//            nextFirst++;
//        }
////        nextFirst = (nextFirst + 1) % items.length;
//
//        T item = items[nextFirst];
//        items[nextFirst] = null;
//
//        size--;
//
////        if ((double) size / items.length < 0.25 && items.length > 16) {
//        if (size * 4 < items.length && items.length > 16) {
//            resize(items.length / 2);
//        }
//        return item;
//    }
//
//    public T removeLast() {
//        if (size == 0) {
//            return null;
//        }
//
//        if (nextLast == 0) {
//            nextLast = items.length - 1;
//        } else {
//            nextLast--;
//        }
////        nextLast = (nextLast - 1 + items.length) % items.length;
//
//        T item = items[nextLast];
//        items[nextLast] = null;
//
//        size--;
//
////        if ((double) size / items.length < 0.25 && items.length > 16) {
//        if (size * 4 < items.length && items.length > 16) {
//            resize(items.length / 2);
//        }
//        return item;
//    }
//
//    public T get(int index) {
//        int realIndex = nextFirst + index + 1;
//        if (realIndex >= items.length) {
//            realIndex -= items.length;
//        }
//        return items[realIndex];
//    }
//
////    public T get(int index) {
////        if (index < 0 || index > size - 1) {
////            return null;
////        }
////        int realIndex = nextFirst + index + 1;
////        return items[realIndex % items.length];
////    }
//}

public class ArrayDeque<T> {
    private static final int RFACTOR = 2;
    private T[] items;
    private int size;
    private int nextFirst, nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    private int forward(int index) {
        return (index + 1) % items.length;
    }

    private int backward(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int i;

//        for (i = 0; i < size; i = (i + 1) % capacity) {
//            a[i] = get(i);
//        }

        int first = nextFirst + 1;
        for (i = 0; i < size; i++) {
            a[i] = items[(first + i) % items.length];
        }

        items = a;
        nextFirst = capacity - 1;
        nextLast = i;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        items[nextFirst] = item;
        nextFirst = backward(nextFirst);
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        items[nextLast] = item;
        nextLast = forward(nextLast);
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = nextFirst, cnt = 0; cnt < size; cnt++) {
            i = (i + 1) % items.length;
            System.out.print(items[i] + " ");
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        nextFirst = forward(nextFirst);
        T item = items[nextFirst];
        items[nextFirst] = null;
        size--;

        if (size * 4 < items.length && items.length > 16) {
            resize(items.length / 2);
        }
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        nextLast = backward(nextLast);
        T item = items[nextLast];
        items[nextLast] = null;
        size--;

        if (size * 4 < items.length && items.length > 16) {
            resize(items.length / 2);
        }
        return item;
    }

    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }
}
