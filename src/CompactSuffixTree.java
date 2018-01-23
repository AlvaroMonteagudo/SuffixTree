import java.util.ArrayList;

/**
 * Implementation of a basic compacted suffix tree
 */
class CompactSuffixTree {

    // Word that represents the treee
    private String string;

    // Root node of the tree
    public CompactSuffixTreeNode root;

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
    CompactSuffixTree(String word, Main.AlgorithmFeatures feature) {
        string = "$" + word + "$";
        maximals = new ArrayList<>();

        // N CUADRADO
        if (feature == Main.AlgorithmFeatures.N2) {
            SuffixTree tree = new SuffixTree(string);
            root = generateCompactSuffixTree(tree.getRoot(), 0);
        } else {
            // N LOG N
            root = new CompactSuffixTreeNode(-1, -1, false, 0);
            for (int i = 1; i < string.length() - 1; i++) {
                insertNewNode(root, i, i);
            }
        }
    }

    /**
     * Constructor for compacted suffix tree
     * @param words from which tree is built
     */
    CompactSuffixTree(String [] words, Main.AlgorithmFeatures feature) {
        string = "$" + words[0] + "$";
        maximals = new ArrayList<>();

        // N CUADRADO
        if (feature == Main.AlgorithmFeatures.N2) {
            SuffixTree tree = new SuffixTree(string);
            for (int i = 1; i < words.length; i++) {
                tree.addWord(words[i],0);
            }
            root = generateCompactSuffixTree(tree.getRoot(), 0);
        } else {
            // N LOG N
            root = new CompactSuffixTreeNode(-1, -1, false, 0);
            for (int i = 1; i < string.length() - 1; i++) {
                insertNewNode(root, i, i);
            }
        }
    }


    public void addWord(String word) {

    }

    public boolean search(CompactSuffixTreeNode current, String pattern, int pos) {

        CompactSuffixTreeNode matchedNode = null;

        int aux = pos;
        System.out.println(current.substring);
        System.out.println(pattern);

        for (CompactSuffixTreeNode child : current.children) {
            int i = 0;
            pos = aux;
            System.out.println(i + " " + pos);
            while (pos < pattern.length() && pos < child.substring.length()
                    && pattern.charAt(pos) == child.substring.charAt(i)) { // Match character
                i++;
                pos++;
            }

            if (pos == pattern.length() - 1) return true;
            else if (pos == child.substring.length() -1) {
                matchedNode = child;
                break;
            }
        }
        return matchedNode != null && search(matchedNode, pattern, pos);
    }

    private void insertNewNode(CompactSuffixTreeNode root, int from, int startIndexInWord) {
        CompactSuffixTreeNode node = root;

        int charactersInTree = 0; // Characters already in the tree
        int idx = -1;

        for (int i = 0; i < root.children.size(); i++) {
            CompactSuffixTreeNode child = root.children.get(i);

            if (string.charAt(from) == string.charAt(child.begin)) {
                node = child;
                idx = i;
                while (((child.begin + charactersInTree) <= child.end) &&
                        (string.charAt(child.begin + charactersInTree) ==
                                string.charAt(from + charactersInTree))) {
                    charactersInTree++;
                }
                break;
            }
        }

        // None child matched the character, insert new node
        if (idx == -1) {
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(from, string.length() - 1, false, startIndexInWord);
            root.children.add(newNode);
        } else if (node.begin + charactersInTree <= node.end) {
            int end = from + charactersInTree - 1;
            char leftChar = (node.indexStartPath < 1) ? ' ' : string.charAt(node.indexStartPath - 1);
            char rightChar = (startIndexInWord < 1) ? ' ' : string.charAt(startIndexInWord - 1);
            boolean flag = node.isLeftDiverse || leftChar != rightChar;
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(from, end, flag, startIndexInWord);

            if (newNode.isLeftDiverse) {
                maximals.add(newNode);
            }

            int newDepth = from + charactersInTree - startIndexInWord;

            if (newDepth > indexLongestSubstring) {
                indexLongestSubstring = newDepth;
                nodeLongestSubstring = newNode;
            }

            node.begin += charactersInTree;
            root.children.set(idx, newNode);
            newNode.children.add(node);

            insertNewNode(newNode, from + charactersInTree, startIndexInWord);

        } else {
            char leftChar = (node.indexStartPath < 1) ? ' ' : string.charAt(node.indexStartPath - 1);
            char rightChar = (startIndexInWord < 1) ? ' ' : string.charAt(startIndexInWord - 1);
            if (!node.isLeftDiverse && leftChar != rightChar) {
                node.isLeftDiverse = true;
                maximals.add(node);
            }
            insertNewNode(node, from + charactersInTree, startIndexInWord);
        }
    }

    /**
     * Creation of compacted tree
     * @param node root of suffix tree
     * @param depth we are in the tree
     * @return root of the compacted suffix tree
     */
    private CompactSuffixTreeNode generateCompactSuffixTree(SuffixTreeNode node, int depth) {
        CompactSuffixTreeNode result;

        int start = node.position;
        StringBuilder sb = new StringBuilder("" + node.character);

        // Compact into one single node all consecutive nodes with one children
        System.out.println(node);
        //System.out.println();
        while (node.children.size() == 1) {
            node = node.children.get(0);
            sb.append(node.character);
            System.out.println(node.toString());
        }
        System.out.println();

        int end = node.position;

        result = new CompactSuffixTreeNode(start, end, node.isLeftDiverse, node.indexStartPath, sb.toString(), 0);

        if (depth != 0 && node.children.size() > 0 && node.isLeftDiverse) {
            maximals.add(result);
        }

        int newDepth = depth + end - start;

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

    /**
     * @return longest repeated substring stored in the tree
     */
    String getLongestSubstring() {
        if (nodeLongestSubstring != null) {
            int start = nodeLongestSubstring.indexStartPath;
            int end =  nodeLongestSubstring.end;
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
            int start = node.indexStartPath;
            int end =  node.end;
            int len = end - start + 1;
            result.add(string.substring(start, start + len));
        }
        return result;
    }
}