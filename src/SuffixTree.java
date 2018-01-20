/**
 *  Implementation for basic and compacted suffix trees.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */


import java.lang.reflect.Array;
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
            System.out.println();
            SuffixTreeNode current = root;
            int len = 0;

            // Check how many characters are already in the tree
            while (true) {

                SuffixTreeNode lastValidNode = current;
                SuffixTreeNode children;

                children = current.getChildren(word.charAt(i + len), word);

                if (children != null) {
                    lastValidNode.updateLeftDiverse(i, word);
                    current = children;
                    len++;
                } else {
                    break;
                }
            }

            // Update if proceeds
            if (!current.isLeftDiverse) {
                current.updateLeftDiverse(i, word);
            }

            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length(); j++) {
                current = current.addChildren(j, i, word.charAt(j));
            }
        }
    }

    public void addWord(String word) {
        SuffixTreeNode current = root;

        System.out.println("ADD WORD");

        for (SuffixTreeNode child : current.children) {
            System.out.println(child.toString());
        }
    }

    // Root of the tree
    public SuffixTreeNode getRoot() {
        return root;
    }
}

