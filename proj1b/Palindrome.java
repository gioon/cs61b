public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> dc = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            dc.addLast(word.charAt(i));
        }
        return dc;
    }

//    public boolean isPalindrome(String word) {
//        Deque<Character> d = wordToDeque(word);
//        while (!d.isEmpty() && d.size() != 1) {
//            if (d.removeFirst() != d.removeLast()) {
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean isPalindrome(String word) {
        return isPalindromeHelper(wordToDeque(word));
    }

    private Boolean isPalindromeHelper(Deque<Character> d) {
        if (d.isEmpty() || d.size() == 1) {
            return true;
        }
        return d.removeFirst() == d.removeLast() && isPalindromeHelper(d);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        while (!d.isEmpty() && d.size() != 1) {
            if (!cc.equalChars(d.removeFirst(), d.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
