package model.mainAlg;

import model.object.Map;
import model.object.Pair;

import java.util.*;

public class AStar {
    private final Map map;
    public Set<Node> closed;


    public AStar(Map map) {
        this.map = map;
    }

    public List<Pair<Integer, Integer>> GetPath(int x, int y, int X, int Y) {
        List<Pair<Integer, Integer>> pathToTarget = new ArrayList<>();
        closed = new HashSet<>();
        Queue<Node> open = new PriorityQueue<>(Comparator.comparingInt((Node node) -> node.F));

        Pair<Integer, Integer> startPosition = new Pair<>(x, y);
        Pair<Integer, Integer> targetPosition = new Pair<>(X, Y);

        if (startPosition == targetPosition) return pathToTarget;

        Node startNode = new Node(0, startPosition, targetPosition, null);
        closed.add(startNode);
        open.addAll(getNeighbours(startNode));
        while (!open.isEmpty()) {
            Node nodeToCheck = open.poll();

            if (nodeToCheck.position.equals(targetPosition)) {
                return calculatePathFromNode(nodeToCheck);
            }
            if (map.value(nodeToCheck.position.getFirst(), nodeToCheck.position.getSecond()) < 0) {
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

    public List<Pair<Integer, Integer>> calculatePathFromNode(Node node) {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Node currentNode = node;

        while (currentNode.parent != null) {
            path.add(new Pair<>(currentNode.position.getFirst(), currentNode.position.getSecond()));
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }


    private List<Node> getNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();

        neighbours.add(new Node(node.G + 1, new Pair<>(
                node.position.getFirst() - 1, node.position.getSecond()),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Pair<>(
                node.position.getFirst() + 1, node.position.getSecond()),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Pair<>(
                node.position.getFirst(), node.position.getSecond() - 1),
                node.finish,
                node));
        neighbours.add(new Node(node.G + 1, new Pair<>(
                node.position.getFirst(), node.position.getSecond() + 1),
                node.finish,
                node));
        return neighbours;
    }


    public class Node {
        public Pair<Integer, Integer> position;
        public Pair<Integer, Integer> finish;
        public Node parent;
        public int F; // F=G+H
        public int G; // расстояние от старта до ноды
        public int H; // расстояние от ноды до цели

        public Node(int g, Pair<Integer, Integer> nodePosition, Pair<Integer, Integer> targetPosition, Node previousNode) {
            position = nodePosition;
            finish = targetPosition;
            parent = previousNode;
            G = g;
            H = Math.abs(targetPosition.getFirst() - position.getFirst())
                    + Math.abs(targetPosition.getSecond() - position.getSecond());
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
