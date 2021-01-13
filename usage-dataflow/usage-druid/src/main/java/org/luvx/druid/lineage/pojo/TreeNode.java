package org.luvx.druid.lineage.pojo;

import lombok.Getter;
import org.luvx.druid.lineage.TreeNodeIterator;

import java.util.*;

public class TreeNode<T> implements Iterable<TreeNode<T>> {
    @Getter
    public  T                 data;
    public  TreeNode<T>       parent;
    @Getter
    public  List<TreeNode<T>> children;
    private List<TreeNode<T>> elementsIndex;

    public TreeNode(T data) {
        this.data = data;
        children = new LinkedList<>();
        elementsIndex = new LinkedList<>();
        elementsIndex.add(this);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children == null || children.size() == 0;
    }

    /**
     * 添加一个子节点
     *
     * @param child
     * @return
     */
    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        children.add(childNode);
        registerChildForSearch(childNode);
        return childNode;
    }

    public TreeNode<T> addChild(TreeNode<T> childNode) {
        childNode.parent = this;
        children.add(childNode);
        registerChildForSearch(childNode);
        return childNode;
    }

    /**
     * 获取当前节点的层
     *
     * @return
     */
    public int getLevel() {
        if (isRoot()) {
            return 0;
        } else {
            return parent.getLevel() + 1;
        }
    }

    /**
     * 递归为当前节点以及当前节点的所有父节点增加新的节点
     *
     * @param node
     */
    private void registerChildForSearch(TreeNode<T> node) {
        elementsIndex.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }

    /**
     * 从当前节点及其所有子节点中搜索某节点
     *
     * @param cmp
     * @return
     */
    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
        for (TreeNode<T> element : elementsIndex) {
            T elData = element.data;
            if (cmp.compareTo(elData) == 0) {
                return element;
            }
        }
        return null;
    }

    public TreeNode<T> findChildNode(Comparable<T> cmp) {
        for (TreeNode<T> element : children) {
            T elData = element.data;
            if (cmp.compareTo(elData) == 0) {
                return element;
            }
        }
        return null;
    }

    /**
     * 获取当前节点的迭代器
     *
     * @return
     */
    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<T>(this);
    }

    /**
     * 获取所有叶子节点的数据
     *
     * @return
     */
    public Set<TreeNode<T>> getAllLeafs() {
        Set<TreeNode<T>> leafNodes = new HashSet<>();
        if (children.isEmpty()) {
            leafNodes.add(this);
        } else {
            for (TreeNode<T> child : children) {
                leafNodes.addAll(child.getAllLeafs());
            }
        }
        return leafNodes;
    }

    /**
     * 获取所有叶子节点的数据
     *
     * @return
     */
    public Set<T> getAllLeafData() {
        Set<T> leafNodes = new HashSet<T>();
        if (children.isEmpty()) {
            leafNodes.add(data);
        } else {
            for (TreeNode<T> child : children) {
                leafNodes.addAll(child.getAllLeafData());
            }
        }
        return leafNodes;
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[tree data null]";
    }
}
