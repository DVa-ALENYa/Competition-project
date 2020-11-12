package model.object;

import lombok.Getter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Map {

    private int X, Y; // map size
    private ArrayList<ArrayList<Integer>> map;
    private int xOffset, yOffset;
    private String fileName = "";

    public Map() {
        Parser parser = new Parser(fileName);
        createMap(parser.parseSizeMap());
        createObstacles(parser.parseWallPosition());
        createBoosters(parser.parseBoosters());
    }

    public void createMap(List<Pair<Integer, Integer>> coordinates) {
        checkBounding(coordinates);
        xOffset = coordinates.get(0).getFirst();
        yOffset = coordinates.get(0).getSecond(); //не знаю надо ли...

        map = new ArrayList<>();
        Y = coordinates.get(2).getSecond() - coordinates.get(0).getSecond();
        X = coordinates.get(2).getFirst() - coordinates.get(0).getFirst();
        for (int i = 0; i < Y; i++) {
            ArrayList<Integer> row = new ArrayList<>(X);
            map.add(row);
        }
    }

    public void createObstacles(List<Pair<Integer, Integer>> coordinates) {
        for (int i = 0; i < coordinates.size(); i+=4) {
            List<Pair<Integer, Integer>> obstacle = coordinates.subList(i, i + 4);
            checkBounding(obstacle);
            int x = obstacle.get(0).getFirst() - xOffset;
            int y = obstacle.get(0).getSecond() - yOffset;
            int Y = obstacle.get(2).getSecond() - yOffset;
            int X = obstacle.get(2).getFirst() - xOffset;
            for (int j = y; j < Y; j++) {
                for (int k = x; k < X; k++) {
                    map.get(j).set(k, -1);  //мозги плывут чекни плз
                }
            }
        }
    }

    public void createBoosters(List<Pair<Character, Pair<Integer, Integer>>> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            Pair<Character, Pair<Integer, Integer>> booster = coordinates.get(i);
            int X = booster.getSecond().getFirst() - xOffset;
            int Y = booster.getSecond().getSecond() - yOffset;
            int index = 10;
            //надо кучу ifов todo для индекса
            map.get(Y).set(X, index);
        }
    }

    public void paint(Pair<Integer, Integer> coordinate){
        int value = map.get(coordinate.getSecond()).get(coordinate.getFirst());
        value += 10;
        map.get(coordinate.getSecond()).set(coordinate.getFirst(), value);
    }

    private void checkBounding(List<Pair<Integer, Integer>> coordinates) {
        if (coordinates.size() != 4 ||
                !coordinates.get(0).getFirst().equals(coordinates.get(3).getFirst()) ||
                !coordinates.get(0).getSecond().equals(coordinates.get(1).getSecond()) ||
                !coordinates.get(1).getFirst().equals(coordinates.get(2).getFirst()) ||
                !coordinates.get(2).getSecond().equals(coordinates.get(3).getSecond())
        ) throw new IllegalArgumentException();
    }

}
