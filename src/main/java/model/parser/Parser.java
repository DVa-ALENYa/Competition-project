package model.parser;

import model.object.Pair;

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
        List<List<Pair<Integer, Integer>>> out;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = in.readLine();
            this.splitArr = line.split("#");
        } catch (IOException e) {
            System.err.println("file not found");
        }
    }

    public List<Pair<Integer, Integer>> parseSizeMap() {
        Matcher matcher = pattern.matcher(splitArr[0]);
        List<Pair<Integer, Integer>> out = new ArrayList<>();
        Pair<Integer, Integer> added;
        int maxX = 0, maxY = 0;
        while (matcher.find()) {
            String[] split = matcher.group(1).split(",");
            added = new Pair<>(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
            if (maxX < added.getFirst()) {
                maxX = added.getFirst();
            } else if (maxY < added.getSecond()) {
                maxY = added.getSecond();
            }
            out.add(added);
        }
        out.add(0, new Pair<>(maxX, maxY));
        return out;
    }

    public Pair<Integer, Integer> parseStartPositionRobot() {
        Matcher matcher = pattern.matcher(splitArr[1]);
        if (matcher.find()){
            String[] split = matcher.group(1).split(",");
            return new Pair<>(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        } else 
            throw new IllegalArgumentException("Wrong of position Robot");
    }

    public List<List<Pair<Integer, Integer>>> parseWallPosition() { // парсер поменял на лист листов без проверки на 4
        List<List<Pair<Integer, Integer>>> out = new ArrayList<>();
        for (String splitStr: splitArr[2].split(";")) {
            List<Pair<Integer, Integer>> obstacle = new ArrayList<>();
            Matcher matcher = pattern.matcher(splitStr);
            while (matcher.find()) {
                String[] split = matcher.group(1).split(",");
                obstacle.add(new Pair<>(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
            }
            out.add(obstacle);
        }
        return out;
    }

    public List<Pair<Character, Pair<Integer, Integer>>> parseBoosters() {
        String[] split = splitArr[3].split(";");
        List<Pair<Character, Pair<Integer, Integer>>> out = new ArrayList<>();
        for (String splitStr : split) {
            Matcher matcher = pattern.matcher(splitStr);
            if (matcher.find()) {
                String[] splitPair = matcher.group(1).split(",");
                out.add(
                        new Pair<>(splitStr.charAt(0),
                                new Pair<>(Integer.valueOf(splitPair[0]), Integer.valueOf(splitPair[1])))
                );
            }
        }
        return out;
    }
}
