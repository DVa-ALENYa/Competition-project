package model.mainAlg;


import model.object.Map;
import model.object.Pair;

import java.util.ArrayList;
import java.util.List;

public class MainAlg {

    private StringBuilder ans = new StringBuilder("");
    static Map map = new Map();

    public static void main(String[] args) {

        List<Pair<Integer, Integer>> mapSize = new ArrayList<>();
        mapSize.add(new Pair<>(0, 0));
        mapSize.add(new Pair<>(7, 0));
        mapSize.add(new Pair<>(7, 5));
        mapSize.add(new Pair<>(0, 5));
        map.createMap(mapSize);

        List<Pair<Integer, Integer>> obstacles = new ArrayList<>();
        obstacles.add(new Pair<>(3, 2));
        obstacles.add(new Pair<>(4, 2));
        obstacles.add(new Pair<>(4, 4));
        obstacles.add(new Pair<>(3, 4));

        obstacles.add(new Pair<>(2, 3));
        obstacles.add(new Pair<>(3, 3));
        obstacles.add(new Pair<>(3, 4));
        obstacles.add(new Pair<>(2, 4));
        map.createObstacles(obstacles);

        System.out.println(map.getX() + " " + map.getY());

        AStar star = new AStar(map);
        System.out.println(star.GetPath(1, 2, 5, 3));
    }




}
