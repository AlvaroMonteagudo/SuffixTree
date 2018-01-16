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
        char leftChar = (this.indexStartPath < 1) ? ' ' : word.charAt(this.indexStartPath);
        char rightChar = word.charAt(firstChar - 1);
        /*System.out.println(firstChar);
        System.out.println(this.indexStartPath);
        System.out.println(rightChar);
        System.out.println(leftChar);
        System.out.println(word);*/
        if (leftChar != rightChar) {
            isLeftDiverse = true;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<SuffixTreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SuffixTreeNode> children) {
        this.children = children;
    }

    public boolean isLeftDiverse() {
        return isLeftDiverse;
    }

    public void setLeftDiverse(boolean leftDiverse) {
        isLeftDiverse = leftDiverse;
    }

    public int getIndexStartPath() {
        return indexStartPath;
    }

    public void setIndexStartPath(int indexStartPath) {
        this.indexStartPath = indexStartPath;
    }
}

class CompactSuffixTreeNode {

    private int begin, end;

    private ArrayList<CompactSuffixTreeNode> children = new ArrayList<>();

    private boolean isLeftDiverse = false;

    private int indexStartPath = 0;


    public CompactSuffixTreeNode(int begin, int end, boolean isLeftDiverse, int indexStartPath) {
        this.begin = begin;
        this.end = end;
        this.isLeftDiverse = isLeftDiverse;
        this.indexStartPath = indexStartPath;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public ArrayList<CompactSuffixTreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CompactSuffixTreeNode> children) {
        this.children = children;
    }

    public boolean isLeftDiverse() {
        return isLeftDiverse;
    }

    public void setLeftDiverse(boolean leftDiverse) {
        isLeftDiverse = leftDiverse;
    }

    public int getIndexStartPath() {
        return indexStartPath;
    }

    public void setIndexStartPath(int indexStartPath) {
        this.indexStartPath = indexStartPath;
    }
}
