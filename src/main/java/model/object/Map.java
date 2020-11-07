package model.object;

import java.util.ArrayList;

public class Map {

    private int X, Y; // map size
    private ArrayList<ArrayList<Integer>> map;
    private int xOffset, yOffset;

    public Map(ArrayList<ArrayList<Pair<Integer, Integer>>> parsed) {
        createMap(parsed.get(0));
        createObstacles(parsed.get(2));
        createBoosters(parsed.get(3));
    }

    public void createMap(ArrayList<Pair<Integer, Integer>> coordinates){
        checkBounding(coordinates);
        xOffset = coordinates.get(0).getFirst();
        yOffset = coordinates.get(0).getSecond(); //не знаю надо ли...

        map = new ArrayList<>();
        int n = coordinates.get(2).getSecond() - coordinates.get(0).getSecond(),
                m = coordinates.get(2).getFirst() - coordinates.get(0).getFirst();
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(0);
            }
            map.add(row);
        }
    }

    public void createObstacles(ArrayList<Pair<Integer, Integer>> coordinates){
        // тута траблы с входными т.к. там несколько препятствий, и каждый с набором вершин. Надо обсудить.
        //TODO
    }

    public void createBoosters(ArrayList<Pair<Integer, Integer>> coordinates){
        //TODO
    }

    private void checkBounding(ArrayList<Pair<Integer, Integer>> coordinates){
        if(coordinates.size() != 4 ||
                !coordinates.get(0).getFirst().equals(coordinates.get(3).getFirst()) ||
                !coordinates.get(0).getSecond().equals(coordinates.get(1).getSecond()) ||
                !coordinates.get(1).getFirst().equals(coordinates.get(2).getFirst()) ||
                !coordinates.get(2).getSecond().equals(coordinates.get(3).getSecond())
        ) throw new IllegalArgumentException();
    }
}
