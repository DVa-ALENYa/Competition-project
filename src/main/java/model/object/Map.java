package model.object;

import lombok.Getter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Map {
    private int X, Y; // map size
    private ArrayList<ArrayList<Integer>> map;
    private int xOffset, yOffset;

    public Map(Parser parser) {
        try {
            createMap(parser.parseSizeMap());
//            createObstacles(parser.parseWallPosition());
//            createBoosters(parser.parseBoosters());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    double getAzimuth(Pair<Integer, Integer> o, Pair<Integer, Integer> p) {
        return Math.atan2(p.getSecond() - o.getSecond(), p.getFirst() - o.getFirst());
    }

    double getAngle(Pair<Integer, Integer> o, Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        double result = getAzimuth(o, b) - getAzimuth(o, a);
        if (result > Math.PI) result -= 2 * Math.PI;
        if (result < -Math.PI) result += 2 * Math.PI;
        return result;
    }

    boolean isInside(List<Pair<Integer, Integer>> pairs, Pair<Integer, Integer> p, int n) {
        double sum = getAngle(p, pairs.get(n - 1), pairs.get(n));
        for (int i = n; i > 0; i--) {
            sum += getAngle(p, pairs.get(i - 1), pairs.get(i));
        }
        return Math.abs(sum) > 0;
    }

    private boolean inMap(List<Pair<Integer, Integer>> coordinates, Pair<Integer, Integer> point) {
        boolean result = false;
        int j = coordinates.size() - 1;
        for (int i = 0; i < coordinates.size(); i++) {
            if (thc(coordinates.get(i).getFirst(), coordinates.get(i).getSecond(),
                    coordinates.get(j).getFirst(), coordinates.get(j).getSecond(),
                    point.getFirst(), point.getSecond())) {
                return true;
            }
            if ((coordinates.get(i).getSecond() < point.getSecond()
                    && coordinates.get(j).getSecond() >= point.getSecond()
                    || coordinates.get(j).getSecond() < point.getSecond()
                    && coordinates.get(i).getSecond() >= point.getSecond())
                    && (coordinates.get(i).getFirst() + (point.getSecond() - coordinates.get(i).getSecond())
                    / (coordinates.get(j).getSecond() - coordinates.get(i).getSecond())
                    * (coordinates.get(j).getFirst() - coordinates.get(i).getFirst())
                    < point.getFirst()))
                result = !result;
            j = i;
        }
        return result;
    }

    private void createWalls(List<Pair<Integer, Integer>> coordinates) throws IllegalAccessException {
        Pair<Integer, Integer> startOfWall = coordinates.get(0), endOfWall; // вынес метод етот отдельно т.к. одно и тоже.
        int from, to;                                                       // чекни его, ну и остальные... Вроде верно все.
        for (int i = 1; i < coordinates.size(); i++) {
            endOfWall = coordinates.get(i);
            if (startOfWall.getFirst().equals(endOfWall.getFirst())) { // тут тип если иксы равны то заполняем по игрекам
                from = Math.min(startOfWall.getSecond(), endOfWall.getSecond()) - 1;
                to = Math.max(startOfWall.getSecond(), endOfWall.getSecond()) - 1;
                for (int j = from; j < to; j++) {
                    setValue(startOfWall.getFirst(), j, 0);
                }
            } else if (startOfWall.getSecond().equals(endOfWall.getSecond())) { // тут аналогично ток с иксами
                from = Math.min(startOfWall.getFirst(), endOfWall.getFirst());
                to = Math.max(startOfWall.getFirst(), endOfWall.getFirst());
                for (int j = from; j < to; j++) {
                    setValue(j, startOfWall.getSecond(), 0);
                }
            } else throw new IllegalAccessException(); // это если точки не на одной линии (перпендикулярной осям)
            startOfWall = endOfWall; // двигаемся к следующему отрезку
        }
    }

    boolean thc(double x1, double y1, double x2, double y2, double x, double y) {
        double AB = (int) Math.sqrt(((Math.max(x2, x1) - Math.min(x2, x1))*(Math.max(x2, x1) - Math.min(x2, x1)))
                +((Math.max(y2, y1) - Math.min(y2, y1))*(Math.max(y2, y1) - Math.min(y2, y1))));
        double AP = (int) Math.sqrt(((Math.max(x, x1) - Math.min(x, x1))*(Math.max(x, x1) - Math.min(x, x1)))
                +((Math.max(y, y1) - Math.min(y, y1))*(Math.max(y, y1) - Math.min(y, y1))));
        double PB = (int) Math.sqrt(((Math.max(x2, x) - Math.min(x2, x)) * (Math.max(x2, x) - Math.min(x2, x)))
                +((Math.max(y2, y) - Math.min(y2, y))*(Math.max(y2, y) - Math.min(y2, y))));
        if(AB == AP + PB)
            return true;
        else return false;

    }

    public void createMap(List<Pair<Integer, Integer>> coordinates) throws IllegalAccessException {

        map = new ArrayList<>();
        Y = coordinates.get(0).getSecond();
        X = coordinates.get(0).getFirst();
        for (int i = 0; i <= Y; i++) {
            ArrayList<Integer> row = new ArrayList<>(X);
            for (int j = 0; j <= X; j++) {
                if (inMap(coordinates, new Pair<>(i, j)/*, coordinates.size()-1*/))
                    row.add(0);
                else
                    row.add(-1);
            }
            map.add(row);
        }
        //createWalls(coordinates.subList(1,coordinates.size()));// затем мы уже тут стены мутим внутри матрицы
    }

    public void createObstacles(List<List<Pair<Integer, Integer>>> coordinates) throws IllegalAccessException {
        for (List<Pair<Integer, Integer>> obstacle : coordinates) {
            createWalls(obstacle); // тут как с картой ток наборами делаем
        }
    }

    public int value(int x, int y) {  // delete нинада больше
        if (x >= X || y >= Y || x < 0 || y < 0)
            return -2;
        else
            return map.get(y).get(x);
    }

    public void setValue(int x, int y, int value) {  // delete нинада больше
        if (x >= X || y >= Y || x < 0 || y < 0)
            throw new IndexOutOfBoundsException();
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

    @Override
    public String toString() {
        for (List<Integer> row : map) {
            System.out.println(row);
        }
        return "";
    }
}
