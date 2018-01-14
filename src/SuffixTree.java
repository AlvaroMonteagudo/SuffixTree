public class SuffixTree {

    private SuffixTreeNode root;

    SuffixTree(String word) {
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; i++) {

            SuffixTreeNode current = root;
            SuffixTreeNode lastValidNode = current;

            int len = 0;

            // Check how many character are already in the tree
            while ((current = current.getChildren(word.charAt(i + len), word)) != null) {
                lastValidNode.updateLeftDiverse(i, word);
                lastValidNode = current;
                len++;
            }

            if (!lastValidNode.isLeftDiverse()) lastValidNode.updateLeftDiverse(i, word);

            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length(); j++) {
                lastValidNode = lastValidNode.addChildren(j, i);
            }

        }
    }

    public SuffixTreeNode getRoot() {
        return root;
    }
}
