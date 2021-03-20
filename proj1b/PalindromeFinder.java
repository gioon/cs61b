/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {

    public static void main(String[] args) {
        int minLength = 4;
//        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();

//        while (!in.isEmpty()) {
//            String word = in.readString();
//            if (word.length() >= minLength && palindrome.isPalindrome(word)) {
//                System.out.println(word);
//            }
//        }

//        CharacterComparator cc = new OffByOne();
//        while (!in.isEmpty()) {
//            String word = in.readString();
//            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
//                System.out.println(word);
//            }
//        }

//        CharacterComparator cc = new OffByN(2);
//        while (!in.isEmpty()) {
//            String word = in.readString();
//            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
//                System.out.println(word);
//            }
//        }

        int mostCount = 0;
        int mostN = 0;
        String longest = "";
        for (int i = 2; i <= 56; i++) {
            int count = 0;

            System.out.println("OffBy: " + i);
            CharacterComparator cc = new OffByN(i);

            In in = new In("../library-sp18/data/words.txt");
            while (!in.isEmpty()) {
                String word = in.readString();
                if (word.length() >= 2 && palindrome.isPalindrome(word, cc)) {
                    count++;
                    if (word.length() > longest.length()) {
                        longest = word;
                    }
                }
            }
            if (count > mostCount) {
                mostCount = count;
                mostN = i;
            }
            System.out.println("offBy" + i + " palindromes: " + count);
            System.out.println("longest offByN palindrome now: " + longest);
            System.out.println("most palindromes: " + mostN + " count: " + mostCount);
        }
    }
}
