import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *  Implementation for basic and compacted tree nodes.
 *
 *  @authors Silvia Usón: 681721 at unizar dot es
 *           Álvaro Monteagudo: 681060 at unizar dot es
 *
 *  @version 1.0
 *
 */
class SuffixTreeNode {

    // Default value for node
    private final char DEFAULT_SYMBOL = 0;

    // Position of the node
    public int position;

    // List containing all the children of this node
    public ArrayList<SuffixTreeNode> children = new ArrayList<>();

    // Flag if node is left diverse
    public boolean isLeftDiverse = false;

    // Helpful parameter to determine if a node is left diverse
    public int indexStartPath = DEFAULT_SYMBOL;

    public char character = ' ';

    public Set<Integer> listOfWords = new HashSet<>();


    /**
     * Suffix tree node constructor
     * @param position value of the node
     */
    SuffixTreeNode(int position) {

        this.position = position;
    }

    /**
     * @param position value for this node
     * @param indexStartPath start path of the new node
     * @return new added node as children of this
     */
    SuffixTreeNode addChildren(int position, int indexStartPath, char character) {
        children.add(new SuffixTreeNode(position));
        SuffixTreeNode lastChildren = children.get(children.size() - 1);
        lastChildren.indexStartPath = indexStartPath;
        lastChildren.character =  character;
        return lastChildren;
    }

    /**
     * @param character to look for
     * @param word to look in
     * @return null if does not exist a children node with character as value,
     *          otherwise return the found children.
     */
    SuffixTreeNode getChildren(char character, String word) {
        for (SuffixTreeNode n: children) {
            if (word.charAt(n.position) == character) {
                return n;
            }
        }
        return null;
    }

    /**
     * Update the node setting left diversity if proceeds
     * @param firstChar char to compare if it is left diverse
     * @param word string
     */
    void updateLeftDiverse(int firstChar, String word) {
        char leftChar = (this.indexStartPath < 1) ? ' ' : word.charAt(this.indexStartPath - 1);
        char rightChar = word.charAt(firstChar - 1);
        if (leftChar != rightChar) {
            isLeftDiverse = true;
        }
    }

    @Override
    public String toString() {
        return  "position=" + position +
                ", isLeftDiverse=" + isLeftDiverse +
                ", indexStartPath=" + indexStartPath +
                ", character=" + character +
                ", words=" + listOfWords;
    }
}

/**
 * Class that represents a basic compact suffix tree node.
 * It keeps begin and end of compacted characters in the word, children and left diverse flag.
 */
class CompactSuffixTreeNode {

    // Values of the node
    public int begin, end;

    // List containing all the children of this node
    public ArrayList<CompactSuffixTreeNode> children = new ArrayList<>();

    // Flag if node is left diverse
    public boolean isLeftDiverse;

    // Helpful parameter to determine if a node is left diverse
    public int indexStartPath;

    public String substring;

    public Set<Integer> listOfWords = new HashSet<>();


    /**
     * Constructor for compacted suffix tree node
     * @param begin value of the node
     * @param end value of the node
     * @param isLeftDiverse flag
     * @param indexStartPath extra parameter for internal management
     */
    CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
    }

    /**
     * Constructor for compacted suffix tree node
     * @param begin value of the node
     * @param end value of the node
     * @param isLeftDiverse flag
     * @param indexStartPath extra parameter for internal management
     * @param substring fragment of the word to keep in the node
     * @param listOfWords list of words the node belongs to
     */
    CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath, String substring, Set<Integer> listOfWords) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
        this.substring = substring;
        this.listOfWords = listOfWords;
    }

    @Override
    public String toString() {
        return "" +
                "begin=" + begin +
                ", end=" + end +
                ", isLeftDiverse=" + isLeftDiverse +
                ", indexStartPath=" + indexStartPath +
                ", substring=" + substring +
                ", words=" + listOfWords;
    }
}

