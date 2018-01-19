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

import static java.lang.System.*;

public class Main {

    private static boolean getLongest = false, getMaximals = false;
    private static String treeWord, pattern, filename;
    private static ArrayList<String> words = new ArrayList<>();

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {


        treeWord = "banana";
        CompactSuffixTree tree = new CompactSuffixTree(treeWord, 1);
        System.out.println(tree.getLongestSubstring());
        System.out.println(tree.searchPattern("ana"));

        /*parseArguments(args);

        Scanner keyboard = new Scanner(in);
        out.print("Enter patern: ");
        pattern = keyboard.next();

        if (treeWord.equals("") && words.isEmpty()) {
            out.print("Enter word: ");
            treeWord = keyboard.next();
            treeWord = removeSpecialChars(treeWord);
            out.println("Input word: " + treeWord);
        }

		CompactSuffixTree tree;
		if (!words.isEmpty()) {
            out.println("Input words in file: " + filename);
            tree = new CompactSuffixTree(words.toArray(new String[words.size()]));
        } else {
            out.println("Input word: " + treeWord);
    	    tree = new CompactSuffixTree(treeWord, 0);
        }
        

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
        }*/
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
        out.println("./Main [-h] [-longest] [-maximals] [-random] [-file] ");
        out.println("This program builds the corresponding suffix tree for a word or a bunch of words from" +
                "which you can check if a certain pattern exists, you can also get the longest repeated substring and all the maximals of the tree.\n" +
                "Word can be supplied from a file, generated randomly with n characters or from the input " +
                "of this program.\nFile has priority over random string and random over user input.\n" +
                "Visualization of Ukkonen's algorithm step by step, visit the next webpage: " +
                "http://brenden.github.io/ukkonen-animation/\n" +
                "All credits to: Brenden Kokoszka. Git user: https://github.com/brenden");
        out.println("Available options:");
        out.println("\t-longest: get the longest repeated substring.");
        out.println("\t-maximals: get all maximal repetitions in the string.");
        out.println("\t-random <INTEGER>: generate a random patron with n characters.");
        out.println("\t-file <INTEGER> <STRING>: number of testx to read from file.");
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
        return s /*.replace(',',' ')
                .replace('.',' ')
                .replace('¿',' ')
                .replace('?',' ')
                .replace('¡',' ')
                .replace('!',' ')
                .replace('(',' ')
                .replace(')',' ')*/
                .replaceAll("[^a-zA-Z0-9]+","");
	}

}
