
public class Main {

    private boolean getLongest = false, getMaximals = false;
    private int randomChars;
    private String stringFile;

    public static void main(String[] args) {

       // SuffixTree tree = new SuffixTree("prueba");
        CompactSuffixTree compactTree = new CompactSuffixTree("xacxacxxxbcx");
        System.out.println(compactTree.getLongestSubstring());
    }

    private static void printUsage() {
        System.out.println("./Main [-d] [-w] [-num] [-tests] [-f] [-h] ");
        System.out.println("Available options:");
        System.out.println("    -d: debug messages will be printed while executing");
        System.out.println("    -w: graph will be weighted");
        System.out.println("    -num <INTEGER>: number of vertices for the graph");
        System.out.println("    -tests <INTEGER>: number of tests");
        System.out.println("    -f <STRING>: file name to sabe graph");
        System.out.println("    -h: this helpful message");
    }

}
