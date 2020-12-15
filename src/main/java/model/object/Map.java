package model.object;

import lombok.Getter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Map {
    @Getter
    private int X, Y; // map size
    @Getter
    private Point<Integer, Integer> xPoint;
    List<Point<Integer, Integer>> clone = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> map;

    public Map(Parser parser) {

        createMap(parser.parseSizeMap());
        createObstacles(parser.parseWallPosition());

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

    public void hasACloneBoost(List<Point<Character, Point<Integer, Integer>>> coordinates){
        for(Point<Character, Point<Integer, Integer>> p : coordinates ) {
            if(p.getX() == 'C') clone.add(p.getY()); //HINT: we use point class as a pair class.
            if(p.getX() == 'X') xPoint = p.getY();
        }
    }

    public Point<Integer, Integer> hasXPoint(List<Point<Character, Point<Integer, Integer>>> coordinates){
        for(Point<Character, Point<Integer, Integer>> p : coordinates ){
            if(p.getX() == 'X') return p.getY(); //HINT: we use point class as a pair class.
        }
        return new Point<>(-1, -1);
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
                map.get(coordinate.getY()).set(coordinate.getX(), value);
            }
        }
    }
}
