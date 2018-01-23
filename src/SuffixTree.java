/**
 *  Implementation for basic and compacted suffix trees.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */

/**
 * Implementation of a basic suffix tree
 */
class SuffixTree {

    // Root of the tree
    public SuffixTreeNode root;

    /**
     * Suffix tree constructor
     * @param word from which the tree is built.
     */
    SuffixTree(String word) {
        //System.out.println(word);
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; ++i) {
            //System.out.println();
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

            //System.out.println("Añadiendo a: " + current.toString());
            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length() - 1; j++) {
                current = current.addChildren(j, i, word.charAt(j));
                //System.out.println(current.toString());
            }
        }
    }

    public void addWord(String word, int pos) {
        word += "$";

        for (int i = 0; i < word.length() - 1; i++) {
            addSuffixes(root, i, word);
        }
    }

    private void addSuffixes(SuffixTreeNode current, int i, String word) {
        SuffixTreeNode foundChild = null;
        for (SuffixTreeNode child : current.children) {
            if (child.character == word.charAt(i)) {
                //System.out.println(child.toString());
                //foundChild = child;
                i++;
                addSuffixes(child, i, word);
                return;
            }
        }

        //System.out.println(current.toString());
        for (int j = i; j < word.length() - 1; j++) {
            current = current.addChildren(j, i, word.charAt(j));
            //System.out.println("AÑADIDO " + current.toString());
        }

    }

    public boolean search(SuffixTreeNode current, String pattern, int pos) {
        SuffixTreeNode matchedNode = null;

        for (SuffixTreeNode child : current.children) {
            if (pattern.charAt(pos) == child.character) { // Match character

                matchedNode = child;
                pos++;
                if (pos == pattern.length()) return true;
            }
        }
        return matchedNode != null && search(matchedNode, pattern, pos);
    }

    // Root of the tree
    public SuffixTreeNode getRoot() {
        return root;
    }
}

