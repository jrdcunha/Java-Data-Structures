import java.util.ArrayList;
import java.util.Random;

/**
 * Lab 6: Java Collection Framework, Skip List and Apache ANT <br />
 * The {@code SkipList} class
 * @param <K>           {@code K} key of each skip list node
 * @param <V>           {@code V} value of each skip list node
 */
public class SkipList<K extends Comparable<K>, V> {

    /**
     * The {@code Node} class for {@code SkipList}
     */
    private class Node {
        public K key;
        public V value;
        public ArrayList<Node> forwards = new ArrayList<Node>();
        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            for (int i = 0; i < level; i++)
                forwards.add(null);
        }
        public String toString() {
            return String.format("%s(%s,%d)", value, key, forwards.size());
        }
    }

    private Node head;

    private ArrayList<Node> updates = new ArrayList<Node>();

    /**
     * Level of the skip list. An empty skip list has a level of 1
     */
    private int level = 1;

    /**
     * Size of the skip list
     */
    private int size = 0;
    
    private static final double CHANCE = 0.5;

    Random rand = new Random();

    public SkipList() {
        head = new Node(null, null, 1);
    }

    public boolean coinFlipHeads() {
        return rand.nextFloat() > CHANCE ? true : false;
    }

    public int calcLevel() {
        int num = 1;
        while (coinFlipHeads())
            num++;
        return num;
    }

    public void resetUpdates() {
        updates = new ArrayList<Node>();

        for (int i = 0; i < level; i++) {
            updates.add(null);
        }

        return;
    }

    /**
     * Insert an new element into the skip list
     * @param key       {@code K} key of the new element
     * @param value     {@code V} value of the new element
     */
    public void insert(K key, V value) {
        // TODO: Lab 5 Part 1-1 -- skip list insertion
        if (search(key) != null) {
            findNode(key).value = value;
            return;
        }
        else {
            int newLevel = calcLevel();
            Node node = new Node(key, value, newLevel);
            
            for (int i = 0; i < Math.min(level, newLevel); i++) {
                node.forwards.set(i, updates.get(i).forwards.get(i));
                updates.get(i).forwards.set(i, node);
            }

            if (newLevel > level) {
                for (int i = head.forwards.size(); i < newLevel; i++)
                    head.forwards.add(node);
                level = newLevel;
            }

        }
    }

    /**
     * Remove an element by the key
     * @param key       {@code K} key of the element
     * @return          {@code V} value of the removed element
     */
    public V remove(K key) {
        // TODO: Lab 5 Part 1-2 -- skip list deletion
        if (search(key) != null) {
            Node forward = findNode(key);

            for (int i = 0; i < forward.forwards.size(); i++) {
                if (updates.get(i).forwards.get(i) != null)
                    updates.get(i).forwards.set(i, updates.get(i).forwards.get(i).forwards.get(i));
            }
            for (int i = level - 1; i >= 0; i--) {
                if (head.forwards.get(i) == null)
                    level--;
            }

            return forward.value;
        }
        else 
            return null;
    }

    /**
     * Search for an element by the key
     * @param key       {@code K} key of the element
     * @return          {@code V} value of the target element
     */
    public V search(K key) {
        // TODO: Lab 5 Part 1-3 -- skip list node search
        return findNode(key) != null ? findNode(key).value : null;
    }

    public Node findNode(K key) {
        resetUpdates();
        
        Node curr = head;

        for (int i = level - 1; i >= 0; i--) {
            while (curr.forwards.get(i) != null && key.compareTo(curr.forwards.get(i).key) > 0) {
                curr = curr.forwards.get(i);
            }
            updates.set(i, curr);
        }

        return curr.forwards.get(0) != null && curr.forwards.get(0).key.compareTo(key) == 0 ? curr.forwards.get(0) : null;
    }

    /**
     * Get the level of the skip list
     * @return          {@code int} level of the skip list
     */
    public int level() {
        return level;
    }

    /**
     * Get the size of the skip list
     * @return          {@code int} size of the skip list
     */
    public int size() {
        return size;
    }

    public String levelToString(Node node, int l) {
        return 
            node == null
            ? "null"
            : String.format("%s -> %s", node.toString(), levelToString(node.forwards.get(l), l));
    }

    /**
     * Print the skip list
     * @return          {@code String} the string format of the skip list
     */
    public String toString() {
        // TODO: Lab 5 Part 1-4 -- skip list printing
        String out = "";
        for (int i = level - 1; i >= 0; i--)
            out += String.format("Level %d: ", i + 1) + levelToString(head, i) + "\n";
        return out;
    }

    /**
     * Main entry
     * @param args      {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        SkipList<Integer, String> list = new SkipList<Integer, String>();
        int[] keys = new int[10];
        for (int i = 0; i < 10; i++) {                          // Insert elements
            keys[i] = (int) (Math.random() * 200);
            list.insert(keys[i], "\"" + keys[i] + "\"");
        }

        System.out.println(list);

        for (int i = 0; i < 10; i += 3) {
            int key = keys[i];
            // Search elements
            System.out.println(String.format("Find element             %3d: value=%s", key, list.search(key)));
            // Remove some elements
            System.out.println(String.format("Remove element           %3d: value=%s", key, list.remove(key)));
            // Search the removed elements
            System.out.println(String.format("Find the removed element %3d: value=%s", key, list.search(key)));
        }

        System.out.println(list);
    }

}
