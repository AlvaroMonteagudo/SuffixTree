
public class Main {

    public static void main(String[] args) {

       // SuffixTree tree = new SuffixTree("prueba");
        CompactSuffixTree compactTree = new CompactSuffixTree("aaaab");
        System.out.println(compactTree.getLongestSubstring());
    }

}
