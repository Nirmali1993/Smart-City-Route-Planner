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


// ============================================================================
// MEMBER 1: Graph Data Structure (Adjacency List)
// ============================================================================

class Graph {
    private Map<Integer, List<Integer>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public boolean addVertex(int locationId) {
        if (!adjacencyList.containsKey(locationId)) {
            adjacencyList.put(locationId, new ArrayList<>());
            return true;
        }
        return false;
    }

    public boolean removeVertex(int locationId) {
        if (adjacencyList.containsKey(locationId)) {
            adjacencyList.remove(locationId);

            for (List<Integer> edges : adjacencyList.values()) {
                edges.remove(Integer.valueOf(locationId));
            }
            return true;
        }
        return false;
    }

    public boolean addEdge(int source, int destination) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            if (!adjacencyList.get(source).contains(destination)) {
                adjacencyList.get(source).add(destination);
                adjacencyList.get(destination).add(source);
                return true;
            }
        }
        return false;
    }

    public boolean removeEdge(int source, int destination) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            adjacencyList.get(source).remove(Integer.valueOf(destination));
            adjacencyList.get(destination).remove(Integer.valueOf(source));
            return true;
        }
        return false;
    }

    public List<int[]> getAllConnections() {
        List<int[]> connections = new ArrayList<>();
        Set<String> visitedEdges = new HashSet<>();

        for (int source : adjacencyList.keySet()) {
            for (int destination : adjacencyList.get(source)) {
                int min = Math.min(source, destination);
                int max = Math.max(source, destination);
                String edge = min + "-" + max;

                if (!visitedEdges.contains(edge)) {
                    connections.add(new int[]{source, destination});
                    visitedEdges.add(edge);
                }
            }
        }
        return connections;
    }

    public List<Integer> bfsTraversal(int startLocation) {
        if (!adjacencyList.containsKey(startLocation)) {
            return new ArrayList<>();
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> traversalOrder = new ArrayList<>();

        queue.add(startLocation);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                traversalOrder.add(vertex);

                for (int neighbor : adjacencyList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return traversalOrder;
    }
}
// ============================================================================
// MEMBER 2: Location and Road Management
// ============================================================================

class LocationManager {
    private AVLTree avlTree;
    private Graph graph;
    private int nextId;

    public LocationManager() {
        this.avlTree = new AVLTree();
        this.graph = new Graph();
        this.nextId = 1;
    }

    public int addLocation(String name) {
        int locationId = nextId;
        avlTree.addLocation(locationId, name);
        graph.addVertex(locationId);
        nextId++;
        return locationId;
    }

    public boolean removeLocation(int locationId) {
        if (avlTree.findLocation(locationId) != null) {
            avlTree.removeLocation(locationId);
            graph.removeVertex(locationId);
            return true;
        }
        return false;
    }

    public boolean addRoad(int sourceId, int destId) {
        String sourceExists = avlTree.findLocation(sourceId);
        String destExists = avlTree.findLocation(destId);

        if (sourceExists != null && destExists != null) {
            return graph.addEdge(sourceId, destId);
        }
        return false;
    }

    public boolean removeRoad(int sourceId, int destId) {
        return graph.removeEdge(sourceId, destId);
    }

    public List<Location> getAllLocations() {
        return avlTree.getAllLocations();
    }

    public List<int[]> getAllConnections() {
        return graph.getAllConnections();
    }

    public String getLocationName(int locationId) {
        return avlTree.findLocation(locationId);
    }

    public List<Integer> bfsTraversal(int startLocation) {
        return graph.bfsTraversal(startLocation);
    }
}
