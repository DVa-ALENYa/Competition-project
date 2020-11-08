package model.mainAlg;

import model.parser.Parser;

public class MainAlg {
    public static void main(String[] args) {
        Parser parser = new Parser("src/main/resources/example-01.desc");
        System.out.println(parser.parseSizeMap());
        System.out.println(parser.parseStartPositionRobot());
        System.out.println(parser.parseWallPosition());
        System.out.println(parser.parseBoosters());
    }
}
