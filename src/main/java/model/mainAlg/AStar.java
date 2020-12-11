package model.mainAlg;

import model.object.Map;
import model.object.Point;

import java.util.*;

public class AStar {
    private final Map map;
    public Set<Node> closed;


    public AStar(Map map) {
        this.map = map;
    }

    public List<Point<Integer, Integer>> GetPath(int x, int y, int X, int Y) {
        List<Point<Integer, Integer>> pathToTarget = new ArrayList<>();
        closed = new HashSet<>();
        Queue<Node> open = new PriorityQueue<>(Comparator.comparingInt((Node node) -> node.F));

        Point<Integer, Integer> startPosition = new Point<>(x, y);
        Point<Integer, Integer> targetPosition = new Point<>(X, Y);

        if (startPosition == targetPosition) return pathToTarget;

        Node startNode = new Node(0, startPosition, targetPosition, null);
        closed.add(startNode);
        open.addAll(getNeighbours(startNode));
        while (!open.isEmpty()) {
            Node nodeToCheck = open.poll();

            if (nodeToCheck.position.equals(targetPosition)) {
                return calculatePathFromNode(nodeToCheck);
            }
            if (map.value(nodeToCheck.position.getX(), nodeToCheck.position.getY()) < 0) {
                closed.add(nodeToCheck);
            } else {
                if (!closed.contains(nodeToCheck)) {
                    closed.add(nodeToCheck);
                    open.addAll(getNeighbours(nodeToCheck));
                }
            }
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


    private List<Node> getNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();

        neighbours.add(new Node(node.G + 1, new Point<>(
                node.position.getX() - 1, node.position.getY()),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Point<>(
                node.position.getX() + 1, node.position.getY()),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Point<>(
                node.position.getX(), node.position.getY() - 1),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Point<>(
                node.position.getX(), node.position.getY() + 1),
                node.finish,
                node));
        return neighbours;
    }


    public class Node {
        public Point<Integer, Integer> position;
        public Point<Integer, Integer> finish;
        public Node parent;
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
