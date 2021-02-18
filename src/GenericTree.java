import java.util.*;

public class GenericTree<E> {

    private GenericTreeNode<E> root;

    public GenericTree() {
        super();
    }

    public GenericTreeNode<E> getRoot() {
        return this.root;
    }

    public void setRoot(GenericTreeNode<E> root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        int numberOfNodes = 0;

        if(root != null) {
            numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; //1 for the root!
        }

        return numberOfNodes;
    }

    private int auxiliaryGetNumberOfNodes(GenericTreeNode<E> node) {
        int numberOfNodes = node.getNumberOfChildren();

        for(GenericTreeNode<E> child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }

        return numberOfNodes;
    }

    public boolean exists(E dataToFind) {
        return (find(dataToFind) != null);
    }


    // TEST
    public GenericTreeNode<FileData> find(int dataToFind) {
        GenericTreeNode<FileData> returnNode = null;

        if(root != null) {
            returnNode = auxiliaryFind((GenericTreeNode<FileData>) root, dataToFind);
        }

        return returnNode;
    }

    private GenericTreeNode<FileData> auxiliaryFind(GenericTreeNode<FileData> currentNode, int dataToFind) {
        GenericTreeNode<FileData> returnNode = null;
        int i = 0;

        if (currentNode.getData().getId() == dataToFind) {
            returnNode = currentNode;
        }

        else if(currentNode.hasChildren()) {
            while(returnNode == null && i < currentNode.getNumberOfChildren()) {
                returnNode = auxiliaryFind(currentNode.getChildAt(i), dataToFind);
                i++;
            }
        }

        return returnNode;
    }

    // TEST

    public GenericTreeNode<E> find(E dataToFind) {
        GenericTreeNode<E> returnNode = null;

        if(root != null) {
            returnNode = auxiliaryFind(root, dataToFind);
        }

        return returnNode;
    }

    private GenericTreeNode<E> auxiliaryFind(GenericTreeNode<E> currentNode, E dataToFind) {
        GenericTreeNode<E> returnNode = null;
        int i = 0;

        if (currentNode.getData().equals(dataToFind)) {
            returnNode = currentNode;
        }

        else if(currentNode.hasChildren()) {
            while(returnNode == null && i < currentNode.getNumberOfChildren()) {
                returnNode = auxiliaryFind(currentNode.getChildAt(i), dataToFind);
                i++;
            }
        }

        return returnNode;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public List<GenericTreeNode<E>> build(GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<E>> returnList = null;

        if(root != null) {
            returnList = build(root, traversalOrder);
        }

        return returnList;
    }

    public List<GenericTreeNode<E>> build(GenericTreeNode<E> node, GenericTreeTraversalOrderEnum traversalOrder) {
        List<GenericTreeNode<E>> traversalResult = new ArrayList<>();

        if(traversalOrder == GenericTreeTraversalOrderEnum.PRE_ORDER) {
            buildPreOrder(node, traversalResult);
        }

        else if(traversalOrder == GenericTreeTraversalOrderEnum.POST_ORDER) {
            buildPostOrder(node, traversalResult);
        }

        return traversalResult;
    }

    private void buildPreOrder(GenericTreeNode<E> node, List<GenericTreeNode<E>> traversalResult) {
        traversalResult.add(node);

        for(GenericTreeNode<E> child : node.getChildren()) {
            buildPreOrder(child, traversalResult);
        }
    }

    private void buildPostOrder(GenericTreeNode<E> node, List<GenericTreeNode<E>> traversalResult) {
        for(GenericTreeNode<E> child : node.getChildren()) {
            buildPostOrder(child, traversalResult);
        }

        traversalResult.add(node);
    }

    public String toString() {
        /*
        We're going to assume a pre-order traversal by default
         */

        String stringRepresentation = "";

        if(root != null) {
            stringRepresentation = build(GenericTreeTraversalOrderEnum.PRE_ORDER).toString();

        }

        return stringRepresentation;
    }
}