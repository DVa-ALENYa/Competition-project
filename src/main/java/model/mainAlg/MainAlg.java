package model.mainAlg;

import model.object.Map;
import model.object.Point;
import model.object.Robot;
import model.parser.Parser;

import java.util.*;


public class MainAlg {

    private static final String file = "src/main/resources/example-01.desc";

    private static Map map;
    private static Robot robot;
    private static int X;
    private static int Y;

    static void init() {
        Parser parser = new Parser(file);
        map = new Map(parser);
        robot = new Robot(parser);
        X = robot.getX();
        Y = robot.getY();
    }

    public static class Node {
        public Point<Integer, Integer> position;
        public Node parent;

        public Node(Point<Integer, Integer> nodePosition, Node previousNode) {
            position = nodePosition;
            parent = previousNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(position, node.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }


    private static List<Node> getNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();
        if (map.value(node.position.getX() - 1, node.position.getY()) >= 0 &&
                map.value(node.position.getX() - 1, node.position.getY()) < 1)
            neighbours.add(new Node(
                    new Point<>(node.position.getX() - 1, node.position.getY()),
                    node));
//        if (map.value(node.position.getX() - 2, node.position.getY()) >= 0 &&
//                map.value(node.position.getX() - 2, node.position.getY()) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() - 2, node.position.getY()),
//                    node));
//        if (map.value(node.position.getX() - 2, node.position.getY() + 1) >= 0 &&
//                map.value(node.position.getX() - 2, node.position.getY()) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() - 2, node.position.getY() + 1),
//                    node));
//        if (map.value(node.position.getX() - 2, node.position.getY() - 1) >= 0 &&
//                map.value(node.position.getX() - 2, node.position.getY()) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() - 2, node.position.getY() - 1),
//                    node));


        if (map.value(node.position.getX() + 1, node.position.getY()) >= 0 &&
                map.value(node.position.getX() + 1, node.position.getY()) < 1)
            neighbours.add(new Node(
                    new Point<>(node.position.getX() + 1, node.position.getY()),
                    node));
//        if (map.value(node.position.getX() + 2, node.position.getY()) >= 0 &&
//                map.value(node.position.getX() + 2, node.position.getY()) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() + 2, node.position.getY()),
//                    node));
//        if (map.value(node.position.getX() + 2, node.position.getY() - 1) >= 0 &&
//                map.value(node.position.getX() + 2, node.position.getY() - 1) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() + 2, node.position.getY() - 1),
//                    node));
//        if (map.value(node.position.getX() + 2, node.position.getY() + 1) >= 0 &&
//                map.value(node.position.getX() + 2, node.position.getY() + 1) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX() + 1, node.position.getY() + 1),
//                    node));


        if (map.value(node.position.getX(), node.position.getY() - 1) >= 0&&
                map.value(node.position.getX(), node.position.getY() - 1) < 1)
            neighbours.add(new Node(
                    new Point<>(node.position.getX(), node.position.getY() - 1),
                    node));
//        if (map.value(node.position.getX(), node.position.getY() - 2) >= 0&&
//                map.value(node.position.getX(), node.position.getY() - 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX(), node.position.getY() - 2),
//                    node));
//        if (map.value(node.position.getX()-1, node.position.getY() - 2) >= 0&&
//                map.value(node.position.getX()-1, node.position.getY() - 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX()-1, node.position.getY() - 2),
//                    node));
//        if (map.value(node.position.getX()+1, node.position.getY() - 2) >= 0&&
//                map.value(node.position.getX()+1, node.position.getY() - 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX()+1, node.position.getY() - 2),
//                    node));


        if (map.value(node.position.getX(), node.position.getY() + 1) >= 0 &&
                map.value(node.position.getX(), node.position.getY() + 1) < 1)
            neighbours.add(new Node(
                    new Point<>(node.position.getX(), node.position.getY() + 1),
                    node));
//        if (map.value(node.position.getX(), node.position.getY() + 2) >= 0 &&
//                map.value(node.position.getX(), node.position.getY() + 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX(), node.position.getY() + 2),
//                    node));
//        if (map.value(node.position.getX()-1, node.position.getY() + 2) >= 0 &&
//                map.value(node.position.getX()-1, node.position.getY() + 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX()-1, node.position.getY() + 2),
//                    node));
//        if (map.value(node.position.getX()+1, node.position.getY() + 2) >= 0 &&
//                map.value(node.position.getX()+1, node.position.getY() + 2) < 9)
//            neighbours.add(new Node(
//                    new Point<>(node.position.getX()+1, node.position.getY() + 2),
//                    node));

        return neighbours;
    }


    public static void main(String[] args) {
        init();
        Set<Node> closed = new HashSet<>();
        Queue<Node> open = new PriorityQueue<>(Comparator.comparingInt((Node node) ->
                Math.abs(node.position.getX() - robot.getX()) + Math.abs(node.position.getY() - robot.getY()))
        );
        Node startNode = new Node(new Point<>(robot.getX(), robot.getY()), null);
//        map.paint(robot.getManipulators());
        map.paint(robot.getPoint());
        closed.add(startNode);
        open.addAll(getNeighbours(startNode));
        while (!open.isEmpty()) {
            Node current = open.poll();
            if (!closed.contains(current)) {
                List<Point<Integer, Integer>> pointsList =
                        new AStar(map).GetPath(robot.getX(), robot.getY(), current.position.getX(), current.position.getY());
                for (Point<Integer, Integer> point : pointsList) {
                    robot.moveTo(point);
                    System.out.println(point);
//                    System.out.println(robot.getManipulators());
//                    map.paint(robot.getManipulators());
                    map.paint(robot.getPoint());
                }
                closed.add(current);
                open.addAll(getNeighbours(current));
            }
        }
        System.out.println(robot.getAnswer().toString());
        System.out.println(map);
    }
}