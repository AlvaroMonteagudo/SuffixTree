import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    private int currentWord;


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
        currentWord = 0;

        // N CUADRADO
        if (feature == Main.AlgorithmFeatures.N2) {
            SuffixTree tree = new SuffixTree(string);
            for (int i = 1; i < words.length; i++) {
                tree.addWord(words[i],0, i);
            }
            root = generateCompactSuffixTree(tree.getRoot(), 0);
        } else {
            // N LOG N
            root = new CompactSuffixTreeNode(-1, -1, false, 0);
            for (String word : words) {
                string = "$" + word + "$";
                System.out.println(word);
                for (int i = 1; i < string.length() - 1; i++) {
                    insertSuffix(root, i, i);
                    print(root);
                    System.out.println();
                }

                //System.out.println("ashdhj");
                //print(root);
                currentWord++;
            }
            //print(root);
        }
    }

    public Set<Integer> search(CompactSuffixTreeNode current, String pattern, int pos) {

        CompactSuffixTreeNode matchedNode = null;

        int aux = pos;

        for (CompactSuffixTreeNode child : current.children) {
            int i = 0;
            pos = aux;
            while (pos < pattern.length() && i < child.substring.length()
                    && pattern.charAt(pos) == child.substring.charAt(i)) { // Match character
                //System.out.println(pattern.charAt(pos) + " " + child.substring.charAt(i));
                i++;
                pos++;
            }

            if (pos == pattern.length()) return child.listOfWords;
            else if (i == child.substring.length()) {
                matchedNode = child;
                break;
            }
        }
        return (matchedNode != null) ? search(matchedNode, pattern, pos) : new HashSet<>();
    }

    private void insertSuffix(CompactSuffixTreeNode current, int pos, int indexInWord) {
        CompactSuffixTreeNode matchedNode = null;
        //System.out.println("in");

        //System.out.println(pos);
        int inTree = 0;
        int idx = -1;

        ArrayList<CompactSuffixTreeNode> children = current.children;
        for (int i = 0; i < children.size(); i++) {
            CompactSuffixTreeNode child = children.get(i);

            if (string.charAt(pos + inTree) == child.substring.charAt(inTree)) {
                matchedNode = child;
                idx = i;
                while ((pos + inTree) < string.length() - 1 // Actual pos less then string length
                        && inTree < child.substring.length()  // Does not match all the node substring
                        && string.charAt(pos + inTree) == child.substring.charAt(inTree)) { // Match character
                    inTree++;
                }
            }

            if ((pos + inTree) == string.length() - 1) {
                //System.out.println("Esta " + string.substring(pos, string.length() - 1));
                return; // Already in tree
            } else if (matchedNode != null) {
                break;
            }
        }


        if (matchedNode == null) {
            //System.out.println("NUevo nodo full " + string.substring(pos, string.length() - 1));
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(pos, string.length(), false,
                    indexInWord, string.substring(pos, string.length() - 1), new HashSet<Integer>(){{add(currentWord);}});
            current.children.add(newNode);
        } else if (inTree < matchedNode.substring.length()) {
            //System.out.println("else");
            //System.out.println(matchedNode.substring);
            //System.out.println(inTree);
            int end = pos + inTree;
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(pos, end - 1, false,
                    indexInWord, matchedNode.substring.substring(0, inTree), matchedNode.listOfWords);
            newNode.listOfWords.add(currentWord);

            matchedNode.begin += inTree;
            matchedNode.substring = matchedNode.substring.substring(inTree);
            current.children.set(idx, newNode);
            //System.out.println(matchedNode);
            //System.out.println(newNode);

            newNode.children.add(matchedNode);

            insertSuffix(newNode, end, indexInWord);

        } else {
            insertSuffix(matchedNode, pos + inTree, indexInWord);
        }
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
                       ((child.begin + charactersInTree) < string.length()) &&
                        (string.charAt(child.begin + charactersInTree) ==
                                string.charAt(from + charactersInTree))) {
                    charactersInTree++;
                }
                break;
            }
        }

        // None child matched the character, insert new node
        if (idx == -1) {
            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(from, string.length() - 1,
                    false, startIndexInWord, string.substring(1, string.length() -1),
                    new HashSet<Integer>() {{add(currentWord);}});
            root.children.add(newNode);
        } else if (node.begin + charactersInTree <= node.end) {
            int end = from + charactersInTree - 1;
            char leftChar = (node.indexStartPath < 1) ? ' ' : string.charAt(node.indexStartPath - 1);
            char rightChar = (startIndexInWord < 1) ? ' ' : string.charAt(startIndexInWord - 1);
            boolean flag = node.isLeftDiverse || leftChar != rightChar;

            CompactSuffixTreeNode newNode = new CompactSuffixTreeNode(from, end, flag, startIndexInWord,
                    string.substring(from, end), node.listOfWords);
            newNode.listOfWords.add(currentWord);

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
            System.out.println(node.substring);
            node.substring = node.substring.substring(end + 1);
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
        //System.out.println(node);
        //System.out.println();
        while (node.children.size() == 1) {
            node = node.children.get(0);
            sb.append(node.character);
            //System.out.println(node.toString());
        }
        //System.out.println();

        int end = node.position;

        result = new CompactSuffixTreeNode(start, end, node.isLeftDiverse, node.indexStartPath, sb.toString(), node.listOfWords);

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
            int start = nodeLongestSubstring.begin;
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
            int start = node.begin;
            int end =  node.end;
            int len = end - start + 1;
            result.add(string.substring(start, start + len));
        }
        return result;
    }

    void print(CompactSuffixTreeNode current) {
        for (CompactSuffixTreeNode child : current.children) {
            System.out.println(child);
            print(child);
        }
        //System.out.println();
    }
}