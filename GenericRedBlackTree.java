/**
 * Lab 4: Generics <br />
 * The {@code GenericRedBlackTree} class <br />
 * Reference: <a href="https://en.wikipedia.org/wiki/Red%E2%80%93black_tree">
 *              https://en.wikipedia.org/wiki/Red%E2%80%93black_tree
 *            </a>
 */
public class GenericRedBlackTree<K extends Comparable<K>, V> {

    /**
     * Root node of the red black tree
     */
    private Node root = null;

    /**
     * Size of the tree
     */
    private int size = 0;

    private static final boolean RIGHT = true;
    private static final boolean LEFT = false;

    /**
     * Search for the node by key, and return the corresponding value
     * @param key       {@code K} the key for searching
     * @return          {@code V} the value of the node, or {@code NULL} if not found
     */
    public V find(K key) {
        // TODO: Lab 4 Part 3-1 -- find an element from the tree
        if (getNode(key) != null)
            return getNode(key).value;
        else
            return null;
    }

    public Node getNode(K key) {
        Node curr = root;   

        if (curr != null) {
            while (curr.value != null) {
                if (key.compareTo(curr.key) == 0) {
                    return curr;
                }
                else if (key.compareTo(curr.key) < 0) {
                    curr = curr.lChild;
                }
                else if (key.compareTo(curr.key) > 0) {
                    curr = curr.rChild;
                }
            }
        }

        return null;
    }

    public Node getGrandparent(Node node) {
        if (node.parent != null) {return node.parent.parent;}
        else {return null;}
    }

    public Node getUncle(Node node) {
        if (node.parent != null) {
            if (node.parent == node.parent.parent.lChild) {
                return node.parent.parent.rChild;
            }
            else {
                return node.parent.parent.lChild;
            }
        }
        else {return null;}
    }

    public Node getSibling(Node node) {
        if (node.parent != null) {
            if (node == node.parent.lChild) {
                return node.parent.rChild;
            }
            else {
                return node.parent.lChild;
            }
        }
        else 
            return null;
    }

    public void rotate(Node node, boolean dir) {
        Node parent = node.parent;
        Node gp = getGrandparent(node);
        node.parent = gp;
        
        if (gp != null) {
            if (parent == gp.lChild) {
                gp.lChild = node;
            }
            else if (parent == gp.rChild) {
                gp.rChild = node;
            }
        }
        else {root = node;}

        if (dir) {
            parent.lChild = node.rChild;
            if (node.rChild != null) {node.rChild.parent = parent;}
            node.rChild = parent;
        }
        else {
            parent.rChild = node.lChild;
            if (node.lChild != null) {node.lChild.parent = parent;} 
            node.lChild = parent;
        }

        parent.parent = node;
    }

    public void fixInsColor(Node node) {
        // case 1
        if (node == root) {
            node.color = Node.BLACK;
        }
        // case 2
        else if (node.parent.color) {
            return;
        }
        else {
            Node gp = getGrandparent(node);
            Node uncle = getUncle(node);
            
            // case 3
            if (!node.parent.color && !uncle.color) {
                node.parent.color = Node.BLACK;
                uncle.color = Node.BLACK;
                gp.color = Node.RED;
                fixInsColor(gp);
            }
            else {
                // case 4
                if (!node.parent.color && uncle.color) {
                    if (node == node.parent.rChild && node.parent == gp.lChild) {
                        rotate(node, LEFT);
                        node = node.lChild;
                    }
                    else if (node == node.parent.lChild && node.parent == gp.rChild) {
                        rotate(node, RIGHT);
                        node = node.rChild;
                    }
                }
                
                //case 5
                node.parent.color = Node.BLACK;
                gp.color = Node.RED;

                if (node == node.parent.lChild) {
                    rotate(node.parent, RIGHT);
                }
                else if (node == node.parent.rChild) {
                    rotate(node.parent, LEFT);
                }
            }
        }
    }

    /**
     * Insert an element to the tree
     * @param key       {@code K} the key of the new element
     * @param value     {@code V} the value of the new element
     */
    public void insert(K key, V value) {
        // TODO: Lab 4 Part 3-2 -- insert an element into the tree
        if (getNode(key) != null) {
            Node node = getNode(key);
            node.value = value;
            fixInsColor(node);
            return;
        }
        else {
            Node curr = root; 
            if (curr != null) {
                while (curr.value != null) {
                    if (key.compareTo(curr.key) < 0) {
                        curr = curr.lChild;
                    }
                    else if (key.compareTo(curr.key) > 0) {
                        curr = curr.rChild;
                    }
                } 
            }  

            Node node = new Node(key, value);

            if (size == 0) {
                root = node;
            }
            else {
                node.parent = curr.parent;
                if (curr == curr.parent.lChild) {
                    curr.parent.lChild = node;
                }
                else if (curr == curr.parent.rChild) {
                    curr.parent.rChild = node;
                }
            }

            size++;
            node.color = Node.RED;
            fixInsColor(node);
        }

        return;
    }

    public void swap(Node node, Node larger) {
        K nodeKey = node.key;
        V nodeValue = node.value;
        node.key = larger.key;
        node.value = larger.value;
        larger.key = nodeKey;
        larger.value = nodeValue;
    }

    public void fixDelColor(Node node) {
        // case 1
        if (node.parent == null)
            return;
        else {
            Node sibling = getSibling(node);

            // case 2
            if (!sibling.color) {
                node.parent.color = Node.RED;
                sibling.color = Node.BLACK;
                if (node == node.parent.lChild) {
                    rotate(sibling, LEFT);
                }
                else {
                    rotate(sibling, RIGHT);
                }

                sibling = getSibling(node);
            }
            
            if (sibling.value != null) {
                // case 3
                if (node.parent.color && sibling.color && sibling.lChild.color && sibling.rChild.color) {
                    sibling.color = Node.RED;
                    fixDelColor(node.parent);
                }
                // case 4
                else if (!node.parent.color && sibling.color && sibling.lChild.color && sibling.rChild.color) {
                    sibling.color = Node.RED;
                    node.parent.color = Node.BLACK;
                }
                else {
                    // case 5
                    if (sibling.color) {
                        sibling.color = Node.RED;
                        if (node == node.parent.lChild && !sibling.lChild.color && sibling.rChild.color) {
                            sibling.lChild.color = Node.BLACK;
                            rotate(sibling.lChild, RIGHT);
                        }
                        else if (node == node.parent.rChild && sibling.lChild.color && !sibling.rChild.color) {
                            sibling.rChild.color = Node.BLACK;
                            rotate(sibling.rChild, LEFT);
                        }

                        sibling = getSibling(node);
                    }

                    // case 6
                    sibling.color = node.parent.color;
                    node.parent.color = Node.BLACK;
                    if (node == node.parent.lChild) {
                        sibling.rChild.color = Node.BLACK;
                        rotate(sibling, LEFT);
                    }
                    else {
                        sibling.lChild.color = Node.BLACK;
                        rotate(sibling, RIGHT);
                    }
                
                }
            }
        }
        
    }

    /**
     * Remove an element from the tree
     * @param key       {@code K} the key of the element
     * @return          {@code V} the value of the removed element
     */
    public V remove(K key) {
        // TODO: Lab 4 Part 3-3 -- remove an element from the tree
        if (find(key) == null)
            return null;
        else {
            Node node = getNode(key);

            if (node.lChild.value != null && node.rChild.value != null) {
                // find next larger node
                Node larger = node.rChild;
                
                while (larger.lChild.value != null) {
                    larger = larger.lChild;
                }

                swap(node, larger);
                node = larger;                
            }

            Node child;

            if (node.lChild.value == null) {
                child = node.rChild;
            }
            else {
                child = node.lChild;
            }

            if (node.parent != null) {
                child.parent = node.parent;
                if (node == node.parent.lChild) {
                    node.parent.lChild = child;
                }
                else {
                    node.parent.rChild = child;
                }
            }
            else {
                if (child.value == null) {
                    root = null;
                }
                else {
                    root = child;
                    child.parent = null;
                }
            }

            if (node.color) {
                if (!child.color) {
                    child.color = Node.BLACK;
                }
                else {
                    fixDelColor(child);
                }
            }

            size--;
            return node.value;
        }
    }

    /**
     * Get the size of the tree
     * @return          {@code int} size of the tree
     */
    public int size() {
        return size;
    }

    public String treeString(Node node) {
        if (node == null || node.value == null) {
            return "-";
        }
        else {
            return String.format("%s{%s}{%s}", node.toString(), treeString(node.lChild), treeString(node.rChild));
        }
    }

    /**
     * Cast the tree into a string
     * @return          {@code String} Printed format of the tree
     */
    @Override public String toString() {
        // TODO: Lab 4 Part 3-4 -- print the tree, where each node contains both value and color
        // You can print it by in-order traversal
        return treeString(root);
    }

    /**
     * Main entry
     * @param args      {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        GenericRedBlackTree<Integer, String> rbt = new GenericRedBlackTree<Integer, String>();
        int[] keys = new int[10];
        for (int i = 0; i < 10; i++) {
            keys[i] = (int) (Math.random() * 200);
            System.out.println(String.format("%2d Insert: %-3d ", i+1, keys[i]));
            rbt.insert(keys[i], "\"" + keys[i] + "\"");
        } // for (int i = 0; i < 10; i++)

        assert rbt.root.color == GenericRedBlackTree.Node.BLACK;
        System.out.println(rbt.root);                   // This helps to figure out the tree structure
        System.out.println(rbt);

        for (int i = 0; i < 10; i++) {
            System.out.println(keys[i]);
            System.out.println(String.format("%2d Delete: %3d(%s)", i+1, keys[i], rbt.remove(keys[i])));
            // if ((i + 1) % 5 == 0) {
                System.out.println(rbt);
            // } // if ((i + 1) % 5 == 0)
        } // for (int i = 0; i < 10; i++)
    }


    /**
     * The {@code Node} class for {@code GenericRedBlackTree}
     */
    private class Node {
        public static final boolean BLACK = true;
        public static final boolean RED = false;

        public K key;
        public V value;
        public boolean color = BLACK;
        public Node parent = null, lChild = null, rChild = null;

        public Node(K key, V value) {                   // By default, a new node is black with two NIL children
            this.key = key;
            this.value = value;
            if (value != null) {
                lChild = new Node(null, null);          // And the NIL children are both black
                lChild.parent = this;
                rChild = new Node(null, null);
                rChild.parent = this;
            }
        }

        /**
         * Print the tree node: red node wrapped by "<>"; black node by "[]"
         * @return          {@code String} The printed string of the tree node
         */
        @Override public String toString() {
            if (value == null)
                return "";
            return (color == RED) ? "<" + value + "(" + key + ")>" : "[" + value + "(" + key + ")]";
        }
    }

}
