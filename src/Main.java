import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.*;

/**
  * Implementation for basic and compacted tree nodes.
  *
  *  @authors Silvia Usón: 681721 at unizar dot es
  *           Álvaro Monteagudo: 681060 at unizar dot es
  *
  *  @version 1.0

 */
public class Main {

    /**
     * Strategies for tree construction
     */
    public enum AlgorithmFeatures {
        N2, NLGN
    }

    private static boolean getLongest = false;
    private static boolean getMaximals = false;
    private static boolean time = false;
    private static String treeWord = "";
    private static ArrayList<String> words = new ArrayList<>();
    private static AlgorithmFeatures feature = AlgorithmFeatures.NLGN;
    private static int longestLength = 0;

    /**
     * Prints instructions of use
     */
    private static void printUsage() {
        out.println("./Main [-h] [-time] [-cost] [-longest] [-maximals] [-random] [-file] ");
        out.println("This program builds the corresponding suffix tree for a word or a bunch of words from" +
                "which you can check a time comparision between different algorithm strategies: n2 or nlgn," +
                "if a certain pattern exists, you can also get the longest repeated substring and all the maximals of the tree.\n" +
                "Word can be supplied from a file, generated randomly with n characters or from the input " +
                "of this program.\nFile has priority over random string and random over user input.\n" +
                "Visualization of Ukkonen's algorithm step by step, visit the next webpage: " +
                "http://brenden.github.io/ukkonen-animation/\n" +
                "All credits to: Brenden Kokoszka. Git user: https://github.com/brenden");
        out.println("Available options:");
        out.println("\t-time: prints a comparision table of tree's construction time, n squared vs n log n.");
        out.println("\t-cost <STRING>: n2 or nlgn tree construction, nlgn by default.");
        out.println("\t-longest: get the longest repeated substring.");
        out.println("\t-maximals: get all maximal repetitions in the string.");
        out.println("\t-random <INTEGER>: generate a random word with n characters.");
        out.println("\t-file <INTEGER> <STRING>: number of words to read from file, -1 to read full file.");
        out.println("\t-h: this helpful message.");
        out.println();
    }

    /**
     * Main function
     * @param args containing features for concrete algorithm
     */
    public static void main(String[] args) {

        parseArguments(args);
        Scanner keyboard = new Scanner(in);

        if (treeWord.equals("") && words.isEmpty()) {
            out.print("Enter word: ");
            treeWord = keyboard.next();
            treeWord = removeSpecialChars(treeWord);
            words.add(treeWord);
            longestLength = treeWord.length();
            out.println();
        }

        if (time) printComparingTable();

        out.println("Creating tree.");

        CompactSuffixTree compactTree = //(words.isEmpty()) ?
                    //new CompactSuffixTree(treeWord, feature) :
                    new CompactSuffixTree(words.toArray(new String [0]), feature);

        searchPatterns(keyboard, compactTree);

        if (words.size() > 1 && (getLongest && getMaximals)) {
            System.out.println("Longest substring and maximals only available with one word tree.");
        } else {
            if (getLongest) out.println("Longest repeated substring: " + compactTree.getLongestSubstring() + "\n");
            if (getMaximals) printMaximals(compactTree.getMaximals());
        }
    }

    /**
     * Set algorithm parameters
     * @param args array where options for algorithm are stored
     */
    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length ; i++) {
            switch (args[i]) {
                case "-cost":
                    ++i;
                    switch (args[i]) {
                        case "n2" :
                            feature = AlgorithmFeatures.N2;
                            break;
                        case "nlgn":
                            feature = AlgorithmFeatures.NLGN;
                            break;
                        default:
                            System.out.println("Feature not available. Try: n2 or nlgn.");
                            break;
                    }
                    break;
                case "-time" :
                    time = true;
                    break;
                case "-longest":
                    getLongest = true;
                    break;
                case "-maximals":
                    getMaximals = true;
                    break;
                case "-random":
                    ++i;
                    try {
                        int randomChars = Integer.parseInt(args[i]);
                        treeWord = (treeWord.equals("")) ? new RandomGenerator().stringRandom(randomChars) : treeWord;
                    } catch (NumberFormatException | NullPointerException e) {
                        err.println(e.getMessage());
                    }
                    break;
                case "-file":
                    ++i;
                    try {
                        int number = Integer.parseInt(args[i]);
                        ++i;
                        String filename = args[i];
                        words = readFile(filename, number);
                        break;
                    } catch (NumberFormatException | NullPointerException ex) {
                        err.println("Trace: " + Arrays.toString(ex.getStackTrace()) + ". Message: " + ex.getMessage());
                        exit(-1);
                    }
                case "-h":
                    printUsage();
                    exit(0);
                default:
                    printUsage();
                    exit(0);
            }
        }
    }

    /**
     * Read alphanumeric words from file [filename]
     * @param filename file where words are
     * @param n number of words to read from file
     * @return list with all words read
     */
    private static ArrayList<String> readFile(String filename, int n){
        ArrayList<String> words = new ArrayList<>();
        File f = new File(filename);
        if (f.exists()) {
            Scanner s;
            try {
                s = new Scanner(f);

                while ((n < 0) ? s.hasNext() : n > 0) {
                    String word = removeSpecialChars(s.next());
                    if (word.length() > longestLength) longestLength = word.length();
                    if (!word.trim().equals("")) {
                        words.add(word);
                        if (n > 0) --n;
                    }
                }
                s.close();
            }
            catch (Exception ex) {
                err.println("Trace: " + Arrays.toString(ex.getStackTrace()) + ". Message: " + ex.getMessage());
            }
        } else System.out.println("File does not exist");
        return words;
    }

    /**
     * Remove special characters from words
     * @param s word
     * @return word without special characters
     */
    private static String removeSpecialChars(String s) {
        return s.replaceAll("[^a-zA-Z0-9]+","");
    }

    /**
     * Search patterns until end condition satisfies
     * @param keyboard input from which read the patterns
     * @param tree compacted tree where patterns ae looked in
     */
    private static void searchPatterns(Scanner keyboard, CompactSuffixTree tree) {
        out.print("Enter pattern (0 to exit): ");
        String pattern = keyboard.next();
        pattern = removeSpecialChars(pattern);

        while(!pattern.equals("0")){
            Set<Integer> listOfWords = tree.search(tree.root, pattern, 0);
            if (listOfWords.isEmpty()) System.out.println("Pattern not found in tree\n");
            else {
                StringBuilder sb = new StringBuilder("Pattern found in this/these words\n");
                int i = 0;
                for (int index : listOfWords) {
                    ++i;
                    sb.append(String.format("%" + longestLength + "s", words.get(index))).append("\t");
                    if (i % 7 == 0) sb.append('\n');
                }
                out.println(sb.toString());
            }
            out.print("Enter pattern (0 to exit): ");
            pattern = keyboard.next();
            pattern = removeSpecialChars(pattern);
        }
    }

    /**
     * Prints all maximals found in the tree
     * @param maximals list where maximals are stored
     */
    private static void printMaximals(ArrayList<String> maximals) {
        if (!maximals.isEmpty()) {
            StringBuilder sb = new StringBuilder("Maximals are:\n\t");
            for (int i = 1; i <= maximals.size(); i++) {
                sb.append(maximals.get(i - 1));
                sb = (i == (maximals.size())) ? sb.append("") : sb.append(", ");
                if (i % 5 == 0) {
                    sb.append('\n').append('\t');
                }
            }
            out.println(sb.toString() + "\n");
        } else out.println("No maximals\n");
    }

    /**
     * Print a comparing table between different algorithm startegies on tree construction.
     */
    private static void printComparingTable() {
        Timer timer = Timer.start();

        AtomicReference<CompactSuffixTree> tree = new AtomicReference<>(new CompactSuffixTree(words.toArray(new String [0]), AlgorithmFeatures.N2));

        long elapsed_ns = timer.time();
        long elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
        long elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

        String aux = "+-----------+-------+--------+-------------+";

        out.println(aux);
        out.format("%1s%10s%2s%5s%3s%6s%3s%8s%6s\n","|","ALGORITHM", "|", "ms", "|", "us", "|", "ns", "|");
        out.println(aux);
        out.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s\n", "|", "N2", "|", elapsed_ms, "|", elapsed_us, "|", elapsed_ns, "|");

        timer.reset();

        tree.set(new CompactSuffixTree(words.toArray(new String [0]), AlgorithmFeatures.NLGN));

        elapsed_ns = timer.time();
        elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
        elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

        out.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s\n", "|", "NLGN", "|", elapsed_ms, "|", elapsed_us, "|", elapsed_ns, "|");
        out.println(aux + '\n');
    }
}
