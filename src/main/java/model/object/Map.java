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

    public Map(Parser parser) throws IllegalAccessException {
        createMap(parser.parseSizeMap());
        createObstacles(parser.parseWallPosition());
        createBoosters(parser.parseBoosters());
    }

    private void createWalls(List<Pair<Integer, Integer>> coordinates) throws IllegalAccessException {
        Pair<Integer, Integer> startOfWall = coordinates.get(1), endOfWall; // вынес метод етот отдельно т.к. одно и тоже.
        int from, to;                                                       // чекни его, ну и остальные... Вроде верно все.
        for (int i = 2; i < coordinates.size(); i++) {
            endOfWall = coordinates.get(i);
            if (startOfWall.getFirst().equals(endOfWall.getFirst())) { // тут тип если иксы равны то заполняем по игрекам
                from = Math.min(startOfWall.getSecond(), endOfWall.getSecond());
                to = Math.max(startOfWall.getSecond(), endOfWall.getSecond());
                for (int j = from; j < to; j++) {
                    setValue(startOfWall.getFirst(), j, -1);
                }
            } else if (startOfWall.getSecond().equals(endOfWall.getSecond())) { // тут аналогично ток с иксами
                from = Math.min(startOfWall.getFirst(), endOfWall.getFirst());
                to = Math.max(startOfWall.getFirst(), endOfWall.getFirst());
                for (int j = from; j < to; j++) {
                    setValue(j, startOfWall.getSecond(), -1);
                }
            } else throw new IllegalAccessException(); // это если точки не на одной линии (перпендикулярной осям)
            startOfWall = endOfWall; // двигаемся к следующему отрезку
        }
    }

    public void createMap(List<Pair<Integer, Integer>> coordinates) throws IllegalAccessException {

        map = new ArrayList<>();
        Y = coordinates.get(0).getSecond();
        X = coordinates.get(0).getFirst();
        for (int i = 0; i < Y; i++) {
            ArrayList<Integer> row = new ArrayList<>(X);
            for (int j = 0; j < X; j++) {
                row.add(0); // тут я сделал карту и заполнил нулями
            }
            map.add(row);
        }
        createWalls(coordinates);// затем мы уже тут стены мутим внутри матрицы
    }

    public void createObstacles(List<List<Pair<Integer, Integer>>> coordinates) throws IllegalAccessException {
        for(List<Pair<Integer, Integer>> obstacle : coordinates){
            createWalls(obstacle); // тут как с картой ток наборами делаем
        }
    }

    public int value(int x, int y) {  // delete нинада больше
        if (x >= X || y >= Y || x < 0 || y < 0) return -2;
        else
            return map.get(y).get(x);
    }

    public void setValue(int x, int y, int value) {  // delete нинада больше
        if (x >= X || y >= Y || x < 0 || y < 0) throw new IndexOutOfBoundsException();
        else
            map.get(y).set(x, value);
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

    public void paint(Pair<Integer, Integer> coordinate) {
        int value = map.get(coordinate.getSecond()).get(coordinate.getFirst());
        value += 10;
        map.get(coordinate.getSecond()).set(coordinate.getFirst(), value);
    }

    private void checkBounding(List<Pair<Integer, Integer>> coordinates) { // это нам уже не надо (вроде)
        if (
                !coordinates.get(0).getFirst().equals(coordinates.get(3).getFirst()) ||
                        !coordinates.get(0).getSecond().equals(coordinates.get(1).getSecond()) ||
                        !coordinates.get(1).getFirst().equals(coordinates.get(2).getFirst()) ||
                        !coordinates.get(2).getSecond().equals(coordinates.get(3).getSecond())
        ) throw new IllegalArgumentException();
    }

}
