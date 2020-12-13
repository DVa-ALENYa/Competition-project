package model.mainAlg;

import lombok.Getter;
import model.object.Map;
import model.object.Point;

import java.util.*;

public class AStar {
    private final Map map;
    public Set<Node> closed;
    Queue<Node> open;

    public AStar(Map map) {
        this.map = map;
    }

    public List<Point<Integer, Integer>> getPath(int x, int y, int X, int Y) {
        //System.out.println("thinking...");
        List<Point<Integer, Integer>> pathToTarget = new ArrayList<>();
        if(x == X && y == Y) return pathToTarget;
        closed = new HashSet<>();
        open = new PriorityQueue<>(Comparator.comparing(Node::getF));

        Point<Integer, Integer> startPosition = new Point<>(x, y);
        Point<Integer, Integer> targetPosition = new Point<>(X, Y);

        if (startPosition == targetPosition) return pathToTarget;

        Node startNode = new Node(0, startPosition, targetPosition, null);
        closed.add(startNode);
        open.addAll(getNeighbours(startNode));
        while (!open.isEmpty()) {
            Node nodeToCheck = open.poll();
           // System.out.println(nodeToCheck);
            if (nodeToCheck.position.equals(targetPosition)) {

                return calculatePathFromNode(nodeToCheck);
            }
            closed.add(nodeToCheck);
            getNeighbours(nodeToCheck).stream().filter(v -> !containsNode(v)).forEach(v -> open.add(v));
        }
        return pathToTarget;
    }

    public List<Point<Integer, Integer>> calculatePathFromNode(Node node) {
        List<Point<Integer, Integer>> path = new ArrayList<>();
        Node currentNode = node;
        while (currentNode.parent != null) {
            path.add(new Point<>(currentNode.position.getX(), currentNode.position.getY()));
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private Boolean containsNode(Node node){
        for(Node v : closed){
            if (node.position.getX().equals(v.position.getX()) &&
                    node.position.getY().equals(v.position.getY())
                    ) return true;
        }
        for(Node v : open){
            if (node.position.getX().equals(v.position.getX()) &&
                    node.position.getY().equals(v.position.getY())
                    && v.F >= node.F) return true;
        }
        return false;
    }


    private List<Node> getNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();
        int x = node.position.getX(), y = node.position.getY();
        if(map.value(x - 1, y) >= 0)
            neighbours.add(new Node(node.G + 1, new Point<>(
                    x - 1, y),
                    node.finish,
                    node));
        if(map.value(x + 1, y) >= 0)
            neighbours.add(new Node(node.G + 1, new Point<>(
                    x + 1, y),
                    node.finish,
                    node));
        if(map.value(x, y - 1) >= 0)
            neighbours.add(new Node(node.G + 1, new Point<>(
                    x, y - 1),
                    node.finish,
                    node));
        if(map.value(x, y + 1) >= 0)
            neighbours.add(new Node(node.G + 1, new Point<>(
                    x, y + 1),
                    node.finish,
                    node));
        return neighbours;
    }


    public class Node {
        public Point<Integer, Integer> position;
        public Point<Integer, Integer> finish;
        public Node parent;
        @Getter
        public int F; // F=G+H
        public int G; // расстояние от старта до ноды
        public int H; // расстояние от ноды до цели

        public Node(int g, Point<Integer, Integer> nodePosition, Point<Integer, Integer> targetPosition, Node previousNode) {
            position = nodePosition;
            finish = targetPosition;
            parent = previousNode;
            G = g;
            H = Math.abs(targetPosition.getX() - position.getX())
                    + Math.abs(targetPosition.getY() - position.getY());
            F = G + H;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "position=" + position +
                    ", finish=" + finish +
                    ", F=" + F +
                    ", G=" + G +
                    ", H=" + H +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return F == node.F &&
                    G == node.G &&
                    H == node.H &&
                    Objects.equals(position, node.position) &&
                    Objects.equals(finish, node.finish) &&
                    Objects.equals(parent, node.parent);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, finish, parent, F, G, H);
        }
    }
}
