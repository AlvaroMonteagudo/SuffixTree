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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.System.*;

public class Main {

    public enum AlgorithmFeatures {
        N2, NLGN
    }

    private static boolean getLongest = false, getMaximals = false, timeComparision = false;
    private static String treeWord = "", pattern = "", filename = "";
    private static ArrayList<String> words = new ArrayList<>();
    private static AlgorithmFeatures feature = AlgorithmFeatures.NLGN;

    /**
     * Main methodnana
     * @param args
     */
    public static void main(String[] args) {

        parseArguments(args);

        Scanner keyboard = new Scanner(in);

        /*if (treeWord.equals("") && words.isEmpty()) {
            out.print("Enter word: ");
            treeWord = keyboard.next();
            treeWord = removeSpecialChars(treeWord);
        }*/

        SuffixTree tree = new SuffixTree("$banana$");
        tree.addWord("lo");

        /*CompactSuffixTree tree = new CompactSuffixTree("banana", feature);
        tree.addWord("xabana");
        System.out.println(tree.search(tree.root, "ana", 0));
        System.out.println(tree.search(tree.root, "nan", 0));
        System.out.println(tree.search(tree.root, "ba", 0));
        System.out.println(tree.search(tree.root, "anana", 0));
        System.out.println(tree.search(tree.root, "nana", 0));
        System.out.println(tree.search(tree.root, "anax", 0));*/



        /*if (timeComparision) {

            Timer timer = Timer.start();

            CompactSuffixTree tree = new CompactSuffixTree(treeWord, AlgorithmFeatures.N2);

            long elapsed_ns = timer.time();
            long elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
            long elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

            String aux = "+-----------+-------+--------+-------------+";

            out.println(aux);
            out.format("%1s%10s%2s%5s%3s%6s%3s%8s%6s\n","|","ALGORITHM", "|", "ms", "|", "us", "|", "ns", "|");
            out.println(aux);
            out.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s\n", "|", "N2", "|", elapsed_ms, "|", elapsed_us, "|", elapsed_ns, "|");

            timer.reset();

            tree = new CompactSuffixTree(treeWord, AlgorithmFeatures.NLGN);

            elapsed_ns = timer.time();
            elapsed_ms = timer.convertTo(TimeUnit.MILLISECONDS, elapsed_ns);
            elapsed_us = timer.convertTo(TimeUnit.MICROSECONDS, elapsed_ns);

            out.format("%1s%10s%2s%5d%3s%7d%2s%10d%4s\n", "|", "NLGN", "|", elapsed_ms, "|", elapsed_us, "|", elapsed_ns, "|");
            out.println(aux);

        } else {

            out.print("Enter pattern: ");
            pattern = keyboard.next();
            pattern = removeSpecialChars(pattern);

            CompactSuffixTree tree;
            if (!words.isEmpty()) {
                out.println("Input words in file: " + filename);
                tree = new CompactSuffixTree(words.toArray(new String[words.size()]));
            } else {
                tree = new CompactSuffixTree(treeWord, feature);
            }

            if (tree.search(tree.root, pattern, 0)) System.out.println("Pattern found in tree");
            else out.println("Pattern not found");


            out.println();

            if (getLongest) out.println("Longest repeated substring: " + tree.getLongestSubstring() + "\n");
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
                    out.println(sb.toString() + "\n");
                } else out.println("No maximals for the input word: " + treeWord + "\n");
            }
        }*/
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
                        case "N2":
                            feature = AlgorithmFeatures.N2;
                            break;
                        case "NLGN":
                            feature = AlgorithmFeatures.NLGN;
                            break;
                        default:
                            System.out.println("Feature not available. Try: N2, NLGN, MS, US ó NS, -h for detailed info");
                            break;
                    }
                    break;
                case "-time" :
                    timeComparision = true;
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
                        filename = args[i];
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
        out.println("\t-file <INTEGER> <STRING>: number of texts to read from file.");
        out.println("\t-h: this helpful message.");
        out.println();
    }

	/*
	 * Lee el fichero de texto y lo asigna a treeWord
	 */
	private static ArrayList<String> readFile(String filename, int n){
        ArrayList<String> words = new ArrayList<>();
    	File f = new File(filename);
        if (f.exists()) {
            Scanner s;
            try {
                s = new Scanner(f);
                s.useDelimiter(" +");
                while (n > 0) {
                    String word = removeSpecialChars(s.next());
                    if (!word.trim().equals("")) {
                        words.add(word);
                        --n;
                    }
                }
                s.close();
            }
            catch (Exception ex) {
                err.println("Trace: " + Arrays.toString(ex.getStackTrace()) + ". Message: " + ex.getMessage());
            }
        }
        return words;
    }

    private static String removeSpecialChars(String s) {
        return s.replaceAll("[^a-zA-Z0-9]+","");
	}

}
