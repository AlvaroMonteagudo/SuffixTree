import java.util.ArrayList;

class SuffixTreeNode {

    private int position;

    private ArrayList<SuffixTreeNode> children;

    private boolean isLeftDiverse = false;

    private int indexStartPath = 0;


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
        if (word.charAt(this.indexStartPath - 1) != word.charAt(firstChar - 1)) {
            isLeftDiverse = true;
        }

    }

    public boolean isLeftDiverse() {
        return isLeftDiverse;
    }
}
