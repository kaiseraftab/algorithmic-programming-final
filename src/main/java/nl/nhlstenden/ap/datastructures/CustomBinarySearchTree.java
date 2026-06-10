package nl.nhlstenden.ap.datastructures;

public class CustomBinarySearchTree<T extends Comparable<T>> implements CustomTree<T> {

    private class Node {
        T data;
        Node left;
        Node right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private static class Holder<E> {
        E value;
    }

    private Node root;
    private int size;

    public CustomBinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void insert(T element) {
        root = insertRecursive(root, element);
        size++;
    }

    private Node insertRecursive(Node node, T element) {
        if (node == null) {
            return new Node(element);
        }

        int comparison = element.compareTo(node.data);

        if (comparison < 0) {
            node.left = insertRecursive(node.left, element);
        } else {
            node.right = insertRecursive(node.right, element);
        }

        return node;
    }

    @Override
    public boolean search(T element) {
        return searchRecursive(root, element);
    }

    private boolean searchRecursive(Node node, T element) {
        if (node == null) {
            return false;
        }

        int comparison = element.compareTo(node.data);

        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return searchRecursive(node.left, element);
        } else {
            return searchRecursive(node.right, element);
        }
    }

    @Override
    public T remove(T element) {
        Holder<Node> removedHolder = new Holder<>();
        root = removeRecursive(root, element, removedHolder);
        if (removedHolder.value != null) {
            size--;
            return removedHolder.value.data;
        }
        return null;
    }

    private Node removeRecursive(Node node, T element, Holder<Node> removedHolder) {
        if (node == null) {
            return null;
        }

        int comparison = element.compareTo(node.data);

        if (comparison < 0) {
            node.left = removeRecursive(node.left, element, removedHolder);
        } else if (comparison > 0) {
            node.right = removeRecursive(node.right, element, removedHolder);
        } else {
            removedHolder.value = node;

            if (node.left == null && node.right == null) {
                return null;
            }

            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            T successor = findMin(node.right);
            node.data = successor;
            node.right = removeRecursive(node.right, successor, new Holder<>());
        }

        return node;
    }

    private T findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public CustomList<T> inOrder() {
        CustomList<T> result = new CustomLinkedList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node node, CustomList<T> result) {
        if (node == null) {
            return;
        }
        inOrderRecursive(node.left, result);
        result.add(node.data);
        inOrderRecursive(node.right, result);
    }

    @Override
    public CustomList<T> preOrder() {
        CustomList<T> result = new CustomLinkedList<>();
        preOrderRecursive(root, result);
        return result;
    }

    private void preOrderRecursive(Node node, CustomList<T> result) {
        if (node == null) {
            return;
        }
        result.add(node.data);
        preOrderRecursive(node.left, result);
        preOrderRecursive(node.right, result);
    }

    @Override
    public CustomList<T> postOrder() {
        CustomList<T> result = new CustomLinkedList<>();
        postOrderRecursive(root, result);
        return result;
    }

    private void postOrderRecursive(Node node, CustomList<T> result) {
        if (node == null) {
            return;
        }
        postOrderRecursive(node.left, result);
        postOrderRecursive(node.right, result);
        result.add(node.data);
    }

    @Override
    public String toString() {
        return "BST{size=" + size + ", inOrder=" + inOrder() + "}";
    }
}
