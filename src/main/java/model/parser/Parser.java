package model.parser;

import model.object.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private Pattern pattern = Pattern.compile("(\\d+,\\d+)");

    private String[] splitArr;

    public Parser(String fileName) {
        List<List<Point<Integer, Integer>>> out;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = in.readLine();
            this.splitArr = line.split("#");
        } catch (IOException e) {
            System.err.println("file not found");
        }
    }

    public List<Point<Integer, Integer>> parseSizeMap() {
        Matcher matcher = pattern.matcher(splitArr[0]);
        List<Point<Integer, Integer>> out = new ArrayList<>();
        Point<Integer, Integer> added;
        int maxX = 0, maxY = 0;
        while (matcher.find()) {
            String[] split = matcher.group(1).split(",");
            added = new Point<>(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
            if (maxX < added.getX()) {
                maxX = added.getX();
            } else if (maxY < added.getY()) {
                maxY = added.getY();
            }
            out.add(added);
        }
        out.add(0, new Point<>(maxX, maxY));
        return out;
    }

    public Point<Integer, Integer> parseStartPositionRobot() {
        Matcher matcher = pattern.matcher(splitArr[1]);
        if (matcher.find()){
            String[] split = matcher.group(1).split(",");
            return new Point<>(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        } else 
            throw new IllegalArgumentException("Wrong of position Robot");
    }

    public List<List<Point<Integer, Integer>>> parseWallPosition() { // парсер поменял на лист листов без проверки на 4
        List<List<Point<Integer, Integer>>> out = new ArrayList<>();
        for (String splitStr: splitArr[2].split(";")) {
            List<Point<Integer, Integer>> obstacle = new ArrayList<>();
            Matcher matcher = pattern.matcher(splitStr);
            while (matcher.find()) {
                String[] split = matcher.group(1).split(",");
                obstacle.add(new Point<>(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
            }
            out.add(obstacle);
        }
        return out;
    }

    public List<Point<Character, Point<Integer, Integer>>> parseBoosters() {
        String[] split = splitArr[3].split(";");
        List<Point<Character, Point<Integer, Integer>>> out = new ArrayList<>();
        for (String splitStr : split) {
            Matcher matcher = pattern.matcher(splitStr);
            if (matcher.find()) {
                String[] splitPair = matcher.group(1).split(",");
                out.add(
                        new Point<>(splitStr.charAt(0),
                                new Point<>(Integer.valueOf(splitPair[0]), Integer.valueOf(splitPair[1])))
                );
            }
        }
        return out;
    }
}
