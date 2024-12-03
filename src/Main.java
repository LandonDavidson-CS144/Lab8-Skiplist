import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SkipList skipList = new SkipList();

        //Test insert
        int size = 64;
        for (int i = 0; i < size; i++) {
            skipList.insert(skipList.rand.nextInt(1000));
        }

        //Test duplicate insert
        skipList.insert(1001);
        skipList.insert(1001);

        //Test delete
        skipList.insert(500);
        skipList.delete(500);
        System.out.println(skipList.search(500));

        //Test search
        System.out.println(skipList.search(1001));

        skipList.printList();
    }
}

class Node {
    ArrayList<Node> right = new ArrayList<>();
    int element;
    public Node(int element) {
        this.element = element;
    }
}

class SkipList {
    Node head = new Node(Integer.MIN_VALUE);
    int height = 0;
    Random rand = new Random();
    public void insert(int value) {
        Node[] prevNodes = new Node[height + 1];
        Node curNode = head;
        for (int i = height; i >= 0; i--) {
            while (curNode.right.size() > i && curNode.right.get(i).element <= value) {
                curNode = curNode.right.get(i);
            }
            prevNodes[i] = curNode;
        }
        if (curNode.element == value) return;

        Node newNode = new Node(value);
        int i = 0;
        do {
            if (i > height) {
                head.right.add(newNode);
                height++;
                i++;
                continue;
            }
            if (i < prevNodes[i].right.size()) {
                newNode.right.add(prevNodes[i].right.get(i));
                prevNodes[i].right.set(i, newNode);
            } else {
                prevNodes[i].right.add(newNode);
            }
            i++;
        } while (rand.nextBoolean());
    }
    public boolean delete(int value) {
        Node[] prevNodes = new Node[height + 1];
        Node curNode = head;
        for (int i = height; i >= 0; i--) {
            while (curNode.right.size() > i && curNode.right.get(i).element < value) {
                curNode = curNode.right.get(i);
            }
            prevNodes[i] = curNode;
        }
        if (curNode.right == null || curNode.right.getFirst().element != value) return false;

        Node target = curNode.right.getFirst();
        for (int i = 0; i < target.right.size(); i++) {
            prevNodes[i].right.set(i, target.right.get(i));
        }
        height = head.right.size();
        return true;
    }
    public Node search(int key) {
        Node curNode = head;
        for (int i = height; i >= 0; i--) {
            while (curNode.right.size() > i && curNode.right.get(i).element < key) {
                curNode = curNode.right.get(i);
            }
        }
        if (curNode.right == null || curNode.right.getFirst().element != key) return null;
        return curNode.right.getFirst();
    }
    public void printList() {
        StringBuilder line = new StringBuilder();
        for (int i = height; i >= 0; i--) {
            line.append("L").append(i).append(": ");
            Node curNode = head;
            while (curNode.right.size() > i) {
                line.append(curNode.right.get(i).element).append(" ");
                curNode = curNode.right.get(i);
            }
            System.out.println(line);
            line = new StringBuilder();
        }
    }
}
