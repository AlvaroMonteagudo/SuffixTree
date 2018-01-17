import java.util.ArrayList;

class SuffixTree {

    private SuffixTreeNode root;

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

    CompactSuffixTree(String word) {
        string = "$" + word + "$";
        SuffixTree tree = new SuffixTree(string);
        maximals = new ArrayList<>();
        root = generateCompactSuffixTree(tree.getRoot(), 0);
    }

    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node, int depth) {
        CompactSuffixTreeNode result;
        int begin, end;
        begin = node.getPosition();

        // Compact into one single node all consecutive nodes with one children
        while (node.getChildren().size() == 1) {
            node = node.getChildren().get(0);
        }
        end = node.getPosition();

        result = new CompactSuffixTreeNode(begin, end, node.isLeftDiverse(), node.getIndexStartPath());

        if (depth != 0 && node.getChildren().size() > 0 && node.isLeftDiverse()) {
            maximals.add(result);
        }

        int newDepth = depth + end - begin;

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

    ArrayList<String> getMaximals() {
        ArrayList<String> result = new ArrayList<>();
        for (CompactSuffixTreeNode node: maximals) {
            int start = nodeLongestSubstring.getIndexStartPath();
            int end =  nodeLongestSubstring.getEnd();
            result.add(string.substring(start, end - start + 1));
        }
        return result;
    }
}
