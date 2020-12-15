package model.mainAlg;

import model.object.Map;
import model.object.Point;
import model.object.Robot;
import model.parser.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class MainAlg {

    //private static final String file = "src/main/resources/example-01.desc";

    private static Map map;
    private static Point<Integer, Integer> startPosition;
    private static Robot robot1;
    private static List<Robot> robots;
    private static Queue<Point<Integer, Integer>> open;
    private static ArrayList<ArrayList<Integer>> closed;
    private static Point<Integer, Integer> start;

    static void init(File input) {
        Parser parser = new Parser(input);
        map = new Map(parser);
        startPosition = parser.parseStartPositionRobot();
        robot1 = new Robot(startPosition, map);
        robots = new ArrayList<>();
    }

    static void newRobot() {
        robots.add(new Robot(robot1.getPoint(), map));
    }

    public static void main(String[] args) {
        String output;
        File file = new File("src/main/resources/input");
        final File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            init(files[i]);
            output = String.format("src/main/resources/output/prob-%03d.sol", i + 1);
            try (FileWriter writer = new FileWriter(output, false)) {
                writer.write(algo());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String algo() {
        List<Point<Integer, Integer>> path;
        map.paint(robot1.getPoint());
        map.paint(robot1.getManipulators());
        Point<Integer, Integer> current;
        start = new Point<>(robot1.getX(), robot1.getY());
        robots.add(robot1);

        if (!map.getClone().isEmpty()) {
            for (Point<Integer, Integer> c : map.getClone()) {
                path = new AStar(map).getPath(robots.get(0).getX(), robots.get(0).getY(), c.getX(), c.getY());
                for (Point<Integer, Integer> pa : path) {
                    robots.get(0).moveTo(pa, false);
                    map.paint(new Point<>(robots.get(0).getX(), robots.get(0).getY()));
                    map.paint(robots.get(0).getManipulators());
                }
            }
            path = new AStar(map).getPath(robots.get(0).getX(), robots.get(0).getY(), map.getXPoint().getX(), map.getXPoint().getY());
            for (Point<Integer, Integer> pa : path) {
                robots.get(0).moveTo(pa, false);
                map.paint(new Point<>(robots.get(0).getX(), robots.get(0).getY()));
                map.paint(robots.get(0).getManipulators());
            }
            for (int i = 0; i < map.getClone().size(); i++) {
                robots.get(0).useClone();
                newRobot();
            }
        }

        do {
            for (Robot robot : robots) {
                current = findEmpty(robot1.getX(), robot1.getY());
                if (current.getX().equals(robot1.getX()) && current.getY().equals(robot1.getY())) {
                    return getAns();
                }
                path = new AStar(map).getPath(robot1.getX(), robot1.getY(), current.getX(), current.getY());
                for (Point<Integer, Integer> p : path) {
                    if (map.value(current.getX(), current.getY()) >= 10)
                        break;
                    robot1.moveTo(p, path.size() <= 2);
                    map.paint(new Point<>(robot1.getX(), robot1.getY()));
                    map.paint(robot1.getManipulators());
                }
            }
        } while (true);
    }

    private static String getAns() {
        StringBuilder ans = new StringBuilder();
        boolean flag = false;
        for (Robot robot : robots) {
            if (flag) ans.append("#");
            flag = true;
            ans.append(robot.getAnswer().toString());
        }
        return ans.toString();
    }

    public static Point<Integer, Integer> findEmpty(int x, int y) {
        open = new PriorityQueue<>((o1, o2) -> {
            if (o1.getG() == o2.getG()) return Integer.compare(o1.getF(), o2.getF());
            return Integer.compare(o1.getG(), o2.getG());
        });

        closed = new ArrayList<>();
        for (int i = 0; i < map.getY(); i++) {
            ArrayList<Integer> row = new ArrayList<>(map.getX());
            for (int j = 0; j < map.getX(); j++) {
                row.add(0);
            }
            closed.add(row);
        }
        Point<Integer, Integer> start = new Point<>(x, y, 0, 0);
        open.addAll(neighbours(start));
        while (!open.isEmpty()) {
            Point<Integer, Integer> current = open.poll();
            assert current != null;
            if (map.value(current.getX(), current.getY()) < 10) {
                return current;
            }
            closed.get(current.getY()).set(current.getX(), 1);
            open.addAll(neighbours(current));
        }
        return start;
    }

    private static List<Point<Integer, Integer>> neighbours(Point<Integer, Integer> point) {
        List<Point<Integer, Integer>> ans = new ArrayList<>();
        int x = point.getX(), y = point.getY();
        if (map.value(x + 1, y) >= 0) {
            if (containPoint(x + 1, y))
                ans.add(new Point<>(x + 1, y, distant(x + 1, y, start), point.getG() + 1));
        }
        if (map.value(x - 1, y) >= 0) {
            if (containPoint(x - 1, y))
                ans.add(new Point<>(x - 1, y, distant(x - 1, y, start), point.getG() + 1));
        }
        if (map.value(x, y + 1) >= 0) {
            if (containPoint(x, y + 1))
                ans.add(new Point<>(x, y + 1, distant(x, y + 1, start), point.getG() + 1));
        }
        if (map.value(x, y - 1) >= 0) {
            if (containPoint(x, y - 1))
                ans.add(new Point<>(x, y - 1, distant(x, y - 1, start), point.getG() + 1));
        }
        return ans;
    }

    private static boolean containPoint(int x, int y) {
        if (closed.get(y).get(x) == 1) return false;
        for (Point<Integer, Integer> p : open) {
            if (Objects.equals(x, p.getX()) && Objects.equals(y, p.getY())) return false;
        }
        return true;
    }

    public static int distant(int x, int y, Point<Integer, Integer> point) {
        return (int) Math.pow(x - point.getX(), 2)
                + (int) Math.pow(y - point.getY(), 2);
    }

}