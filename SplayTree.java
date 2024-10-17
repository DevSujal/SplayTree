import java.util.Scanner;

class SplayTree {
    // Node class representing a node in the splay tree
    class Node {
        int key;
        Node left, right;

        public Node(int key) {
            this.key = key;
            this.left = this.right = null;
        }
    }

    private Node root;

    // Function to perform a right rotation
    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Function to perform a left rotation
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Splay function to bring the key to root if exists, else bring the last accessed node to root
    private Node splay(Node root, int key, int[] steps) {
        if (root == null || root.key == key) {
            return root;
        }

        steps[0]++; // Increment step count

        // Key lies in the left subtree
        if (key < root.key) {
            // Key is not in the tree
            if (root.left == null) return root;

            // Zig-Zig (Left Left)
            if (key < root.left.key) {
                root.left.left = splay(root.left.left, key, steps);
                root = rotateRight(root);
            }
            // Zig-Zag (Left Right)
            else if (key > root.left.key) {
                root.left.right = splay(root.left.right, key, steps);
                if (root.left.right != null) {
                    root.left = rotateLeft(root.left);
                }
            }

            return (root.left == null) ? root : rotateRight(root);
        } else {
            // Key is not in the tree
            if (root.right == null) return root;

            // Zag-Zig (Right Left)
            if (key < root.right.key) {
                root.right.left = splay(root.right.left, key, steps);
                if (root.right.left != null) {
                    root.right = rotateRight(root.right);
                }
            }
            // Zag-Zag (Right Right)
            else if (key > root.right.key) {
                root.right.right = splay(root.right.right, key, steps);
                root = rotateLeft(root);
            }

            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    // Function to insert a key
    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = splay(root, key, new int[1]);

        if (root.key == key) return; // Key already exists

        Node newNode = new Node(key);

        if (key < root.key) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        root = newNode;
    }

    // Function to search a key and return the number of steps taken
    public boolean search(int key, int[] steps) {
        root = splay(root, key, steps);
        return root != null && root.key == key;
    }

    // Function to delete a key
    public void delete(int key) {
        if (root == null) return;

        root = splay(root, key, new int[1]);

        if (root.key != key) return; // Key not found

        if (root.left == null) {
            root = root.right;
        } else {
            Node temp = root.right;
            root = root.left;
            splay(root, key, new int[1]);
            root.right = temp;
        }
    }

    // Function to perform level order traversal of the tree
    public void levelOrderTraversal() {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node temp = queue.poll();
            System.out.print(temp.key + " ");
            if (temp.left != null) queue.add(temp.left);
            if (temp.right != null) queue.add(temp.right);
        }
        System.out.println();
    }

    // Main function to test the SplayTree
    public static void main(String[] args) {
        SplayTree tree = new SplayTree();
        Scanner scanner = new Scanner(System.in);
        int choice, key;
        
        // Insert elements
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Insert");
            System.out.println("2. Search");
            System.out.println("3. Delete");
            System.out.println("4. Level Order Traversal");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter key to insert: ");
                    key = scanner.nextInt();
                    tree.insert(key);
                    break;
                case 2:
                    System.out.print("Enter key to search: ");
                    key = scanner.nextInt();
                    int[] steps = new int[1];
                    boolean found = tree.search(key, steps);
                    if (found) {
                        System.out.println("Key " + key + " found in " + steps[0] + " steps.");
                    } else {
                        System.out.println("Key " + key + " not found. Took " + steps[0] + " steps to determine.");
                    }
                    break;
                case 3:
                    System.out.print("Enter key to delete: ");
                    key = scanner.nextInt();
                    tree.delete(key);
                    System.out.println("Key " + key + " deleted (if existed).");
                    break;
                case 4:
                    System.out.println("Level order traversal:");
                    tree.levelOrderTraversal();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        
        scanner.close();
    }
}
