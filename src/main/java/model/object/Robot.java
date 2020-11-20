package model.object;

import lombok.Getter;
import lombok.Setter;
import model.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Robot {

    @Getter
    @Setter
    private int X;
    @Getter
    @Setter
    private int Y;

    private final List<Manipulator> manipulators;

    public Robot(Parser parser) {
        Pair<Integer, Integer> pair = parser.parseStartPositionRobot();
        this.X = pair.getFirst();
        this.Y = pair.getSecond();
        manipulators = new ArrayList<>(3);
        manipulators.add(0, new Manipulator(X + 1, Y - 1));
        manipulators.add(1, new Manipulator(X + 1, Y));
        manipulators.add(2, new Manipulator(X + 1, Y + 1));
    }

    public char moveW() {
        Y++;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveW();
        }
        return 'W';
    }

    public char moveS() {
        Y--;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveS();
        }
        return 'S';
    }

    public char moveA() {
        X--;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveA();
        }
        return 'A';
    }

    public char moveD() {
        X++;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveD();
        }
        return 'D';
    }

    public char turnQ() {
        for (Manipulator manipulator : manipulators) {
            manipulator.turnQ(this.X, this.Y);
        }
        return 'Q';
    }

    public char turnE() {
        for (Manipulator manipulator : manipulators) {
            manipulator.turnE(this.X, this.Y);
        }
        return 'E';
    }

    private static class Manipulator {

        @Getter
        @Setter
        private int X;

        @Getter
        @Setter
        private int Y;

        public Manipulator(int x, int y) {
            this.X = x;
            this.Y = y;
        }

        private void moveW() {
            this.Y++;
        }

        private void moveS() {
            this.Y--;
        }

        private void moveA() {
            this.X--;
        }

        private void moveD() {
            this.X++;
        }

        private void turnQ(int robotX, int robotY) {
            int x = this.X;
            this.X = this.Y + robotX - robotY;
            this.Y = robotY + robotX - x;
        }

        private void turnE(int robotX, int robotY) {
            int y = this.Y;
            this.Y = this.X + robotY - robotX;
            this.X = robotX + robotY - y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Manipulator that = (Manipulator) o;
            return X == that.X &&
                    Y == that.Y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(X, Y);
        }

        @Override
        public String toString() {
            return "Manipulator{" +
                    "X=" + X +
                    ", Y=" + Y +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return X == robot.X &&
                Y == robot.Y &&
                Objects.equals(manipulators, robot.manipulators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y, manipulators);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "X=" + X +
                ", Y=" + Y +
                ", manipulators=" + manipulators +
                '}';
    }
}
