package model.mainAlg;

import model.object.Map;
import model.object.Point;
import model.object.Robot;

import model.parser.Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class MainAlg {

    private static final String file = "src/main/resources/example-01.desc";

    private static Map map;
    private static Robot robot;
    private static Set<Point<Integer, Integer>> closed;
    private static Queue<Point<Integer, Integer>> open;

    static void init() {
        Parser parser = new Parser(file);
        map = new Map(parser);
        robot = new Robot(parser, map);
    }


    public static void main(String[] args) {
        init();
        List<Point<Integer, Integer>> path;
        //System.out.println(map);
        map.paint(robot.getPoint());
        Point<Integer, Integer> current;
        do {
            System.out.println("Robot " + robot.getPoint() + " E - " + map.getEmpties() + " P - " + map.getPainted());
            current = findEmpty(robot.getX(), robot.getY());
            System.out.println(current);
            if (current.getX().equals(robot.getX()) && current.getY().equals(robot.getY())) {
                break;
            }
            path = new AStar(map).getPath(robot.getX(), robot.getY(), current.getX(), current.getY());
            for (Point<Integer, Integer> p : path) {
                robot.moveTo(p);
                map.paint(new Point<>(robot.getX(), robot.getY()));
                    map.paint(robot.getManipulators());
            }
            //System.out.println(robot.getAnswer().toString());
        } while (map.getEmpties() > map.getPainted());
        //System.out.println(robot.getAnswer());
        try (FileWriter writer = new FileWriter("src/main/resources/ans.txt", false)) {
            writer.write(robot.getAnswer().toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Point<Integer, Integer> findEmpty(int x, int y) {//X=284, Y=343
        open = new LinkedList<>();
        closed = new HashSet<>();
        Point<Integer, Integer> start = new Point<>(x, y);
        open.addAll(neighbours(start));
        closed.add(start);
        while (!open.isEmpty()) {
            int n = open.size();
            for (int i = 0; i < n; i++) {
                Point<Integer, Integer> current = open.poll();
                System.out.println(closed.size());
                if (map.getEmpties() <= map.getPainted()) return start;
                if (map.value(current.getX(), current.getY()) < 10) {
                    return current;
                }
                List<Point<Integer, Integer>> neig;
                neig = neighbours(current);
                open.addAll(neighbours(current));
                closed.add(current);
            }
        }
        return start;

    }

    private static List<Point<Integer, Integer>> neighbours(Point<Integer, Integer> point) {
        List<Point<Integer, Integer>> ans = new ArrayList<>();
        int x = point.getX(), y = point.getY();
        Point<Integer, Integer> add;
        if (map.value(x + 1, y) >= 0) {
            add = new Point<>(x + 1, y);
            if (!cont(add))
                ans.add(new Point<>(x + 1, y));
        }
        if (map.value(x - 1, y) >= 0) {
            add = new Point<>(x - 1, y);
            if (!cont(add))
                ans.add(new Point<>(x - 1, y));
        }
        if (map.value(x, y + 1) >= 0) {
            add = new Point<>(x, y + 1);
            if (!cont(add))
                ans.add(new Point<>(x, y + 1));
        }
        if (map.value(x, y - 1) >= 0) {
            add = new Point<>(x, y - 1);
            if (!cont(add))
                ans.add(new Point<>(x, y - 1));
        }
        return ans;
    }

    private static boolean cont(Point<Integer, Integer> point) {
        for (Point<Integer, Integer> p : closed) {
            if (point.getX().equals(p.getX()) && point.getY().equals(p.getY())) return true;
        }
        for (Point<Integer, Integer> p : open) {
            if (point.getX().equals(p.getX()) && point.getY().equals(p.getY())) return true;
        }
        return false;
    }

}