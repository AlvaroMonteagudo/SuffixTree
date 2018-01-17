import java.util.ArrayList;

class SuffixTreeNode {

    private final char DEFAULT_SYMBOL = 0;

    private int position;

    private ArrayList<SuffixTreeNode> children = new ArrayList<>();

    private boolean isLeftDiverse = false;

    private int indexStartPath = DEFAULT_SYMBOL;


    SuffixTreeNode(int position) {
        this.position = position;
    }

    // Add new children and return it to keep adding new children to it
    SuffixTreeNode addChildren(int position, int indexStartPath) {
        children.add(new SuffixTreeNode(position));
        SuffixTreeNode lastChildren = children.get(children.size() - 1);
        lastChildren.indexStartPath = indexStartPath;
        return lastChildren;
    }

    SuffixTreeNode getChildren(char character, String word) {
        for (SuffixTreeNode n: children) {
            if (word.charAt(n.position) == character) {
                return n;
            }
        }
        return null;
    }

    void updateLeftDiverse(int firstChar, String word) {
        char leftChar = (this.indexStartPath < 1) ? ' ' : word.charAt(this.indexStartPath - 1);
        char rightChar = word.charAt(firstChar - 1);
        if (leftChar != rightChar) {
            isLeftDiverse = true;
        }
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<SuffixTreeNode> getChildren() {
        return children;
    }

    public boolean isLeftDiverse() {
        return isLeftDiverse;
    }

    public int getIndexStartPath() {
        return indexStartPath;
    }
}

class CompactSuffixTreeNode {

    private int begin, end;

    private ArrayList<CompactSuffixTreeNode> children = new ArrayList<>();

    private boolean isLeftDiverse = false;

    private int indexStartPath = 0;


    CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
    }

    public int getEnd() {
        return end;
    }

    public ArrayList<CompactSuffixTreeNode> getChildren() {
        return children;
    }

    public boolean isLeftDiverse() {
        return isLeftDiverse;
    }

    public int getIndexStartPath() {
        return indexStartPath;
    }
}
