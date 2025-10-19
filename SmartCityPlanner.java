import java.util.*;

// ============================================================================
// MEMBER 3: AVL Tree for Location Management
// ============================================================================

class AVLNode {
    int locationId;
    String name;
    AVLNode left, right;
    int height;

    public AVLNode(int locationId, String name) {
        this.locationId = locationId;
        this.name = name;
        this.height = 1;
    }
}

class AVLTree {
    private AVLNode root;

    public AVLTree() {
        this.root = null;
    }

    private int getHeight(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rightRotate(AVLNode z) {
        AVLNode y = z.left;
        AVLNode T3 = y.right;

        y.right = z;
        z.left = T3;

        z.height = 1 + Math.max(getHeight(z.left), getHeight(z.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    private AVLNode leftRotate(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        y.left = z;
        z.right = T2;

        z.height = 1 + Math.max(getHeight(z.left), getHeight(z.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    private AVLNode insert(AVLNode node, int locationId, String name) {
        if (node == null) {
            return new AVLNode(locationId, name);
        }

        if (locationId < node.locationId) {
            node.left = insert(node.left, locationId, name);
        } else if (locationId > node.locationId) {
            node.right = insert(node.right, locationId, name);
        } else {
            return node; // Duplicate not allowed
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && locationId < node.left.locationId) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && locationId > node.right.locationId) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && locationId > node.left.locationId) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && locationId < node.right.locationId) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void addLocation(int locationId, String name) {
        root = insert(root, locationId, name);
    }

    private AVLNode getMinValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private AVLNode deleteNode(AVLNode node, int locationId) {
        if (node == null) {
            return node;
        }

        if (locationId < node.locationId) {
            node.left = deleteNode(node.left, locationId);
        } else if (locationId > node.locationId) {
            node.right = deleteNode(node.right, locationId);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            AVLNode temp = getMinValueNode(node.right);
            node.locationId = temp.locationId;
            node.name = temp.name;
            node.right = deleteNode(node.right, temp.locationId);
        }

        if (node == null) {
            return node;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void removeLocation(int locationId) {
        root = deleteNode(root, locationId);
    }

    private void inorderTraversal(AVLNode node, List<Location> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(new Location(node.locationId, node.name));
            inorderTraversal(node.right, result);
        }
    }

    public List<Location> getAllLocations() {
        List<Location> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private AVLNode search(AVLNode node, int locationId) {
        if (node == null || node.locationId == locationId) {
            return node;
        }
        if (locationId < node.locationId) {
            return search(node.left, locationId);
        }
        return search(node.right, locationId);
    }

    public String findLocation(int locationId) {
        AVLNode node = search(root, locationId);
        return (node != null) ? node.name : null;
    }
}

class Location {
    int id;
    String name;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }
}