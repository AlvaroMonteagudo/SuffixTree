import org.javatuples.Pair;

public class SuffixTree {

    private SuffixTreeNode root;

    SuffixTree(String word) {
        word = "$" + word + "$";
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; i++) {

            SuffixTreeNode current = root;
            SuffixTreeNode lastValidNode;

            int len = 0;

            // Check how many character are already in the tree
            while (true) {

                lastValidNode = current;

                Pair<Boolean, SuffixTreeNode> pair = current.getChildren(word.charAt(i + len), word);

                if (pair.getValue0()) {
                    lastValidNode.updateLeftDiverse(i, word);
                    current = pair.getValue1();
                    len++;
                } else break;

            }

            // Update if proceeds and not root
            if (!current.isLeftDiverse() && current.getPosition() != -1) current.updateLeftDiverse(i, word);

            // Add new characters that are not in the tree yet.
            for (int j = i + len; j < word.length(); j++) {
                current = current.addChildren(j, i);
            }

        }
    }

    public SuffixTreeNode getRoot() {
        return root;
    }
}
