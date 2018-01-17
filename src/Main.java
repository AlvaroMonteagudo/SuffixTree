/**
 *  Implementation for basic and compacted tree nodes.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.*;

public class Main {

    private static boolean getLongest = false, getMaximals = false;
    private static int randomChars;
    private static String file;
    private static String treeWord = "";

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        parseArguments(args);

        if (treeWord.equals("")) {
            Scanner keyboard = new Scanner(in);
            out.print("Enter word: ");
            treeWord = keyboard.next();
        } else out.println("Input word: " + treeWord);

        CompactSuffixTree tree = new CompactSuffixTree(treeWord);

        if (getLongest) out.println("Longest repeated substring: " + tree.getLongestSubstring());
        if (getMaximals) {
            ArrayList<String> maximals = tree.getMaximals();
            if (!maximals.isEmpty()) {
                StringBuilder sb = new StringBuilder("Maximals are:\n\t");
                for (int i = 1; i <= maximals.size(); i++) {
                    sb.append(maximals.get(i - 1));
                    sb = (i == (maximals.size())) ? sb.append("") : sb.append(", ");
                    if (i % 5 == 0) {
                        sb.append('\n').append('\t');
                    }
                }
                out.println(sb.toString());
            } else out.println("No maximals for the input word: " + treeWord);
        }
    }

    /**
     * Set algorithm parameters
     * @param args array where options for algorithm are stored
     */
    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length ; i++) {
            switch (args[i]) {
                case "-longest":
                    getLongest = true;
                    break;
                case "-maximals":
                    getMaximals = true;
                    break;
                case "-random":
                    ++i;
                    try {
                        randomChars = Integer.parseInt(args[i]);
                        treeWord = (treeWord.equals("")) ? new RandomGenerator().stringRandom(randomChars) : treeWord;
                    } catch (NumberFormatException | NullPointerException e) {
                        err.println(e.getMessage());
                    }
                    break;
                case "-file":
                    ++i;
                    file = args[i];
                    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                        treeWord = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
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
     * Prints instructions of use
     */
    private static void printUsage() {
        out.println("./Main [-h] [-longest] [-maximals] [-random] [-file] ");
        out.println("This program builds the corresponding suffix tree for a word from" +
                "which you can get the longest repeated substring and all the maximals of the tree.\n" +
                "Word can be supplied from a file, generated randomly with n characters or from the input " +
                "of this program.\nFile has priority over random string and random over user input.\n" +
                "Visualization of Ukkonen's algorithm step by step, visit the next webpage: " +
                "http://brenden.github.io/ukkonen-animation/\n" +
                "All credits to: Brenden Kokoszka. Git user: https://github.com/brenden");
        out.println("Available options:");
        out.println("\t-longest: get the longest repeated substring.");
        out.println("\t-maximals: get all maximal repetitions in the string.");
        out.println("\t-random <INTEGER>: generate a random word with n characters.");
        out.println("\t-file <STRING>: file name where string is located.");
        out.println("\t-h: this helpful message.");
        out.println();
    }

}
