package model.mainAlg;

import model.object.Map;
import model.object.Pair;

import java.util.*;

public class AStar {
    private final Map map;
    public List<Node> closed;


    public AStar(Map map) {
        this.map = map;
    }

    public List<Pair<Integer, Integer>> GetPath(int x, int y, int X, int Y) {
        List<Pair<Integer, Integer>> pathToTarget = new ArrayList<>();
        closed = new ArrayList<>();
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
                open.remove(nodeToCheck);
                closed.add(nodeToCheck);
            } else {
                open.remove(nodeToCheck);
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
        public boolean equals(Object obj) {
            Node node = (Node) obj;
            return this.position.equals(node.position);
        }
    }
}
