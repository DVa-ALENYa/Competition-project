package model.mainAlg;

import model.object.Map;
import model.object.Pair;
import model.object.Robot;
import model.parser.Parser;

import java.util.Collections;
import java.util.List;

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

    public static void main(String[] args) {
        init();
        if (X != 0 || Y != 0) {
            List<Pair<Integer, Integer>> path = new AStar(map).GetPath(X, Y, 0, 0);
            for (Pair<Integer, Integer> pair : path) {
                robot.moveTo(pair);
            }
        }
        System.out.println(map);
        int h = map.getY() + 1;
        int w = map.getX() + 1;
        int maxW = map.getX()-1;
        int maxH = map.getY()-1;
        int minW = 0;
        int minH = 1;
        int countOfFrames = Math.max(maxH, maxW) / 2;
        int move = 0;
        boolean findEmpty = false;
        int x = 0, y = 0;
        for (int i = 0; i < countOfFrames; i++) {
            h--;
            h--;
            w--;
            w--;
            System.out.println(2 * (h + w));
            for (int j = 0; j < 2 * (h + w)-1; j++) {
                if (move == 0) {
                    if (map.value(++x, y) != -1) {
                        if (findEmpty) {
                            findEmpty = false;
                            List<Pair<Integer, Integer>> path = new AStar(map).GetPath(robot.getX(), robot.getY(), x, y);
                            for (Pair<Integer, Integer> pair : path) {
                                robot.moveTo(pair);
                            }
                        } else {
                            robot.moveD();
                        }
                    } else {
                        findEmpty = true;
                        x++;
                    }

                    if (x == maxW) {
                        maxW--;
                        move = 1;
                    }
                }
                else if (move == 1) {
                    if (map.value(x, ++y) != -1) {
                        if (findEmpty) {
                            findEmpty = false;
                            List<Pair<Integer, Integer>> path = new AStar(map).GetPath(robot.getX(), robot.getY(), x, y);
                            for (Pair<Integer, Integer> pair : path) {
                                robot.moveTo(pair);
                            }
                        } else {
                            robot.moveS();
                        }
                    } else {
                        findEmpty = true;
                        y++;
                    }
                    if (y == maxH) {
                        maxH--;
                        move = 2;
                    }
                }
                else if (move == 2) {
                    if (map.value(--x, y) != -1) {
                        if (findEmpty) {
                            findEmpty = false;
                            List<Pair<Integer, Integer>> path = new AStar(map).GetPath(robot.getX(), robot.getY(), x, y);
                            for (Pair<Integer, Integer> pair : path) {
                                robot.moveTo(pair);
                            }
                        } else {
                            robot.moveA();
                        }
                    } else {
                        findEmpty = true;
                        x--;
                    }
                    if (x == minW) {
                        minW++;
                        move = 3;
                    }
                }
                else if (move == 3) {
                    if (map.value(x, --y) != -1) {
                        if (findEmpty) {
                            findEmpty = false;
                            List<Pair<Integer, Integer>> path = new AStar(map).GetPath(robot.getX(), robot.getY(), x, y);
                            for (Pair<Integer, Integer> pair : path) {
                                robot.moveTo(pair);
                            }
                        } else {
                            robot.moveW();
                        }
                    } else {
                        findEmpty = true;
                        y--;
                    }
                    if (y == minH) {
                        minH++;
                        move = 0;
                    }
                }
            }
        }
        System.out.println(robot.getAnswer());
    }

}
