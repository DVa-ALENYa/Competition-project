package model.mainAlg;

import model.object.Map;
import model.object.Robot;
import model.parser.Parser;


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


        System.out.println(map);
    }

}