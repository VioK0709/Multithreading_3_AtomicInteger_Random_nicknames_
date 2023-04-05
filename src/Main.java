import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger length3 = new AtomicInteger();
    public static AtomicInteger length4 = new AtomicInteger();
    public static AtomicInteger length5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isSameLetter(text))
                    increasingCounter(text.length());
            }
        });
        palindrome.start();

        Thread sameLetter = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetter(text))
                    increasingCounter(text.length());
            }
        });
        sameLetter.start();

        Thread lexicographic = new Thread(() -> {
            for (String text : texts) {
                if (isLexicographic(text))
                    increasingCounter(text.length());
            }
        });

        lexicographic.start();

        sameLetter.join();
        lexicographic.join();
        palindrome.join();

        System.out.println("Красивых слов с длиной 3: " + length3 + " шт.");
        System.out.println("Красивых слов с длиной 4: " + length4 + " шт.");
        System.out.println("Красивых слов с длиной 5: " + length5 + " шт.");
    }


    static boolean isPalindrome(String text) {
        StringBuilder sb = new StringBuilder(text);
        return sb.reverse().toString().equals(text);
    }

    static boolean isSameLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i - 1) == text.charAt(i))
                return true;
        }
        return false;
    }

    static boolean isLexicographic(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i - 1) < text.charAt(i))
                return true;
        }
        return false;
    }

    public static void increasingCounter(int textLength) {
        if (textLength == 3) {
            length3.getAndIncrement();
        } else if (textLength == 4) {
            length4.getAndIncrement();
        } else {
            length5.getAndIncrement();
        }
    }

    private static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}