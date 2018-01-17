import java.util.Random;

public class RandomGenerator {

    private static Random rnd;
    private static char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static char[] numbers = "0123456789".toCharArray();

    private static char[] alphanumeric = (String.valueOf(chars) + String.valueOf(numbers)).toCharArray();

    RandomGenerator() {
        rnd = new Random();
    }

    public Random getRnd() {
        return rnd;
    }

    /*
     * Return a random string
	 */
    public String stringRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(chars[rnd.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /*
     * Return a random string that represents a number
     */
    public String numberStringRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(numbers[rnd.nextInt(numbers.length)]);
        }
        return sb.toString();
    }

    /*
     * Return a random string with numbers and letters
     */
    public String alphanumericRandom(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(alphanumeric[rnd.nextInt(alphanumeric.length)]);
        }
        return sb.toString();
    }
}
