package model.object;

import lombok.Getter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Map {
    @Getter
    private int X, Y; // map size
    private ArrayList<ArrayList<Integer>> map;
    @Getter
    private int empties, painted;
    public Map(Parser parser) {
        empties = 0;
        painted = 0;
        createMap(parser.parseSizeMap());
        createObstacles(parser.parseWallPosition());
//            createBoosters(parser.parseBoosters());

    }

    boolean isInside(List<Point<Integer, Integer>> points, Point<Double, Double> p) {
        boolean result = false;
        double x = p.getX();
        double y = p.getY();
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            double xp = points.get(i).getX();
            double yp = points.get(i).getY();
            double xp_prev = points.get(j).getX();
            double yp_prev = points.get(j).getY();
            if (((yp <= y && y < yp_prev) || (yp_prev <= y && y < yp))
                    && (x > (xp_prev - xp) * (y - yp) / (yp_prev - yp) + xp))
                result = !result;
            j = i;
        }
        return result;
    }


    private void createWalls(List<Point<Integer, Integer>> coordinates, int value) {
        for (double i = 0.5; i < Y; i += 1) {
            for (double j = 0.5; j < X; j += 1) {
                if (isInside(coordinates, new Point<>(j, i))) {
                    if(value == 0) empties++;
                    else empties--;
                    map.get((int) (i - 0.5)).set((int) (j - 0.5), value);
                }
            }
        }
    }

    public void createMap(List<Point<Integer, Integer>> coordinates) {

        map = new ArrayList<>();
        Y = coordinates.get(0).getY();
        X = coordinates.get(0).getX();
        for (double i = 0; i < Y; i++) {
            ArrayList<Integer> row = new ArrayList<>(X);
            for (double j = 0; j < X; j++) {
                row.add(-1);
            }
            map.add(row);
        }
        createWalls(coordinates.subList(1, coordinates.size()), 0);
    }

    public void createObstacles(List<List<Point<Integer, Integer>>> coordinates) {
        for (List<Point<Integer, Integer>> obstacle : coordinates) {
            createWalls(obstacle, -1); // тут как с картой ток наборами делаем
        }
    }

    public int value(int x, int y) {
        if (x >= X || y >= Y || x < 0 || y < 0)
            return -1;
        else
            return map.get(y).get(x);
    }

    public void setValue(int x, int y, int value) {  // delete нинада больше
        if (x >= X || y >= Y || x < 0 || y < 0)
            throw new IndexOutOfBoundsException();
        else
            map.get(y).set(x, value);
    }

    public void createBoosters(List<Point<Character, Point<Integer, Integer>>> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            Point<Character, Point<Integer, Integer>> booster = coordinates.get(i);
            int X = booster.getY().getX();
            int Y = booster.getY().getY();
            int index = 10;
            //надо кучу ifов todo для индекса
            map.get(Y).set(X, index);
        }
    }

    public void paint(List<Point<Integer, Integer>> listCoordinates) {
        for (Point<Integer, Integer> point : listCoordinates) {
            paint(point);
        }
    }

    public void paint(Point<Integer, Integer> coordinate) {
        if (coordinate.getX() >= 0 && coordinate.getY() >= 0 && coordinate.getX() < X && coordinate.getY() < Y) {
            int value = map.get(coordinate.getY()).get(coordinate.getX());
            if (value < 10 && value >= 0) {
                value += 10;
                painted++;
                map.get(coordinate.getY()).set(coordinate.getX(), value);
            }
        }
    }

    private void checkBounding(List<Point<Integer, Integer>> coordinates) { // это нам уже не надо (вроде)
        if (
                !coordinates.get(0).getX().equals(coordinates.get(3).getX()) ||
                        !coordinates.get(0).getY().equals(coordinates.get(1).getY()) ||
                        !coordinates.get(1).getX().equals(coordinates.get(2).getX()) ||
                        !coordinates.get(2).getY().equals(coordinates.get(3).getY())
        ) throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        for (int i = map.size() - 1; i >= 0; i--) {
            ArrayList<Integer> row = map.get(i);
            for (int j : row) {
                if (j == -1) System.out.print("ZZ");
                else if (j == 0) System.out.print("__");
            }
            System.out.println();
        }
        return "";
    }
}
