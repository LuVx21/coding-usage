package org.luvx.algorithm.tree;

public class BTDepth {
    /**
     * 树的深度
     */
    public int TreeDepth(Node root) {
        if (root == null) {
            return 0;
        }
        int left = TreeDepth(root.getLeft());
        int right = TreeDepth(root.getRight());
        return Math.max(left, right) + 1;
    }

    /**
     * 非递归写法:层次遍历
     */
    public int TreeDepthByLoop(Node root) {
        return 0;
    }
}
