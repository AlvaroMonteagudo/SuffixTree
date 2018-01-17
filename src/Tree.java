/**
 *  Implementation for basic and compacted suffix trees.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */

import java.util.ArrayList;

/**
 * Implementation of a basic suffix tree
 */
class SuffixTree {

    // Root of the tree
    private SuffixTreeNode root;

    /**
     * Suffix tree constructor
     * @param word from which the tree is built.
     */
    SuffixTree(String word) {
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; ++i) {
            SuffixTreeNode current = root;
            int len = 0;

            // Check how many characters are already in the tree
            while (true) {
                SuffixTreeNode lastValidNode = current;

                SuffixTreeNode children = current.getChildren(word.charAt(i + len), word);

                if (children != null) {
                    lastValidNode.updateLeftDiverse(i, word);
                    current = children;
                    len++;
                } else {
                    break;
                }

            }

            // Update if proceeds
            if (!current.isLeftDiverse()) {
                current.updateLeftDiverse(i, word);
            }

            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length(); j++) {
                current = current.addChildren(j, i);
            }

        }
    }

    // Root of the tree
    public SuffixTreeNode getRoot() {
        return root;
    }
}

/**
 * Implementation of a basic compacted suffix tree
 */
class CompactSuffixTree {

    // Word that represents the treee
    private String string;

    // Root node of the tree
    private CompactSuffixTreeNode root;

    // List with nodes marked as maximals
    private ArrayList<CompactSuffixTreeNode> maximals;

    // Index of the word where longest repeated substring starts
    private int indexLongestSubstring = 0;

    // Node where longest repeated substring starts
    private CompactSuffixTreeNode nodeLongestSubstring = null;

    /**
     * Constructor for compacted suffix tree
     * @param word from which tree is built
     */
    CompactSuffixTree(String word) {
        string = "$" + word + "$";
        SuffixTree tree = new SuffixTree(string);
        maximals = new ArrayList<>();
        root = generateCompactSuffixTree(tree.getRoot(), 0);
    }

    /**
     * Creation of compacted tree
     * @param node root of suffix tree
     * @param depth we are in the tree
     * @return root of the compacted suffix tree
     */
    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node, int depth) {
        CompactSuffixTreeNode result;

        int start = node.getPosition();

        // Compact into one single node all consecutive nodes with one children
        while (node.getChildren().size() == 1) {
            node = node.getChildren().get(0);
        }

        int end = node.getPosition();

        result = new CompactSuffixTreeNode(start, end, node.isLeftDiverse(), node.getIndexStartPath());

        if (depth != 0 && node.getChildren().size() > 0 && node.isLeftDiverse()) {
            maximals.add(result);
        }

        int newDepth = depth + end - start;

        for (SuffixTreeNode children: node.getChildren()) {
            result.getChildren().add(generateCompactSuffixTree(children, newDepth + 1));
        }

        if (result.getChildren().size() != 0) {
            if (newDepth > indexLongestSubstring) {
                indexLongestSubstring = newDepth;
                nodeLongestSubstring = result;
            }
        }

        return result;
    }

    /**
     * @return longest repeated substring stored in the tree
     */
    String getLongestSubstring() {
        if (nodeLongestSubstring != null) {
            int start = nodeLongestSubstring.getIndexStartPath();
            int end =  nodeLongestSubstring.getEnd();
            int len = end - start + 1;
            return string.substring(start, start + len);
        } else {
            return "";
        }
    }

    /**
     * @return list with all the maximals of the tree
     */
    ArrayList<String> getMaximals() {
        ArrayList<String> result = new ArrayList<>();
        for (CompactSuffixTreeNode node: maximals) {
            int start = node.getIndexStartPath();
            int end =  node.getEnd();
            int len = end - start + 1;
            result.add(string.substring(start, start + len));
        }
        return result;
    }
}
