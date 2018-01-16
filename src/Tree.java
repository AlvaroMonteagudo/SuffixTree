import java.util.ArrayList;

class SuffixTree {

    private SuffixTreeNode root;

    SuffixTree(String word) {
        root = new SuffixTreeNode(-1);

        for (int i = 1; i < word.length() - 1; i++) {

            SuffixTreeNode current = root;
            SuffixTreeNode lastValidNode;

            int len = 0;

            // Check how many character are already in the tree
            while (true) {

                lastValidNode = current;

                Pair<Boolean, SuffixTreeNode> pair = current.getChildren(word.charAt(i + len), word);

                if (pair.getFirst()) {
                    lastValidNode.updateLeftDiverse(i, word);
                    current = pair.getSecond();
                    len++;
                } else break;

            }

            // Update if proceeds and not root
            if (!current.isLeftDiverse && current.position != -1) current.updateLeftDiverse(i, word);

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

class CompactSuffixTree {

    private String string;

    private CompactSuffixTreeNode root;

    private ArrayList<CompactSuffixTreeNode> maximals;

    private int indexLongestSubstring = 0;

    private CompactSuffixTreeNode nodeLongestSubstring = null;

    public CompactSuffixTree(String word) {
        string = "$" + word + "$";
        SuffixTree tree = new SuffixTree(string);
        maximals = new ArrayList<>();
        root = generateCompactSuffixTree(tree.getRoot());
    }

    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node) {
        return generateCompactSuffixTree(node, 0);
    }

    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node, int depth) {
        CompactSuffixTreeNode result;
        int begin, end;
        begin = node.position;

        // Compact into one single node all consecutive nodes with one children
        while (node.children.size() == 1) {
            node = node.children.get(0);
        }
        end = node.position;

        result = new CompactSuffixTreeNode(begin, end, node.isLeftDiverse, node.indexStartPath);

        if (depth != 0 && node.children.size() > 0 && node.isLeftDiverse) {
            maximals.add(result);
        }

        int newDepth = depth + end - begin;

        for (SuffixTreeNode children: node.children) {

            result.children.add(generateCompactSuffixTree(children, newDepth + 1));
        }

        if (result.children.size() != 0) {
            if (newDepth > indexLongestSubstring) {
                indexLongestSubstring = newDepth;
                nodeLongestSubstring = result;
            }
        }

        return result;
    }
}
