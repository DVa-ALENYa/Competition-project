package model.object;

import lombok.Getter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Map {
    private int X, Y; // map size
    private ArrayList<ArrayList<Integer>> map;

    public Map(Parser parser) {
            createMap(parser.parseSizeMap());
            createObstacles(parser.parseWallPosition());
//            createBoosters(parser.parseBoosters());
    }



    boolean isInside(List<Pair<Integer, Integer>> pairs, Pair<Double, Double> p) {
        boolean result = false;
        double x = p.getFirst();
        double y = p.getSecond();
        int j = pairs.size() - 1;
        for(int i = 1; i < pairs.size(); i++) {
            double xp = pairs.get(i).getFirst();
            double yp = pairs.get(i).getSecond();
            double xp_prev = pairs.get(j).getFirst();
            double yp_prev = pairs.get(j).getSecond();
            if (((yp <= y && y <yp_prev) || (yp_prev <= y && y < yp))
            && (x > (xp_prev - xp) * (y - yp) / (yp_prev - yp) + xp))
            result = !result;
            j = i;
        }
        return result;
    }


    private void createWalls(List<Pair<Integer, Integer>> coordinates){
        for (double i = 0.5; i < Y; i+=1) {
            for (double j = 0.5; j < X; j+=1) {
                if(isInside(coordinates, new Pair<>(j, i))) map.get((int) (i - 0.5)).set((int) (j - 0.5), 0);
            }
        }
    }

    public void createMap(List<Pair<Integer, Integer>> coordinates) {

        map = new ArrayList<>();
        Y = coordinates.get(0).getSecond();
        X = coordinates.get(0).getFirst();
        for (double i = 0; i < Y; i++) {
            ArrayList<Integer> row = new ArrayList<>(X);
            for (double j = 0; j < X; j++) {
                row.add(-1);
            }
            map.add(row);
        }
        createWalls(coordinates);
    }

    public void createObstacles(List<List<Pair<Integer, Integer>>> coordinates) {
        for (List<Pair<Integer, Integer>> obstacle : coordinates) {
            createWalls(obstacle); // тут как с картой ток наборами делаем
        }
    }

    public int value(int x, int y) {
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
            int X = booster.getSecond().getFirst();
            int Y = booster.getSecond().getSecond();
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
        for (int i = map.size() - 1; i >= 0; i--) {
            ArrayList<Integer> row = map.get(i);
            for(int j : row){
                if(j == -1) System.out.print("ZZ");
                else if(j == 0) System.out.print("__");
                //System.out.print(j);
            }
            System.out.println();
        }
        return "";
    }
}
