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
    @Getter
    private final StringBuilder answer = new StringBuilder();

    private final List<Manipulator> manipulators;

    private Position position = Position.D;

    private enum Position {
        W(0),
        A(1),
        S(2),
        D(3);

        private final int number;

        Position(int i) {
            this.number = i;
        }
    }

    public Robot(Parser parser) {
        Point<Integer, Integer> point = parser.parseStartPositionRobot();
        this.X = point.getX();
        this.Y = point.getY();
        manipulators = new ArrayList<>(3);
        manipulators.add(0, new Manipulator(X + 1, Y - 1));
        manipulators.add(1, new Manipulator(X + 1, Y));
        manipulators.add(2, new Manipulator(X + 1, Y + 1));
    }

    public List<Point<Integer, Integer>> getManipulators() {
        List<Point<Integer, Integer>> out = new ArrayList<>();
        for (Manipulator manipulator : manipulators) {
            out.add(manipulator.getPoint());
        }
        return out;
    }

    public Point<Integer, Integer> getPoint() {
        return new Point<>(X, Y);
    }

    public void moveW() {
        this.Y--;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveW();
        }
        switch (position) {
            case A:
                turnQ();
                break;
            case D:
                turnE();
                break;
            case S:
                turnE();
                turnE();
                break;
        }
        answer.append('S');
    }

    public void moveS() {
        this.Y++;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveS();
        }
        switch (position) {
            case A:
                turnE();
                break;
            case D:
                turnQ();
                break;
            case W:
                turnE();
                turnE();
                break;
        }
        answer.append('W');
    }

    public void moveA() {
        this.X--;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveA();
        }
        switch (position) {
            case D:
                turnE();
                turnE();
                break;
            case S:
                turnQ();
                break;
            case W:
                turnE();
                break;
        }
        answer.append('A');
    }

    public void moveD() {
        this.X++;
        for (Manipulator manipulator : manipulators) {
            manipulator.moveD();
        }
        switch (position) {
            case A:
                turnE();
                turnE();
                break;
            case S:
                turnE();
                break;
            case W:
                turnQ();
                break;
        }
        answer.append('D');
    }

    public void turnQ() {
        position = Position.values()[(position.number + 3) % 4];
        for (Manipulator manipulator : manipulators) {
            manipulator.turnE(this.X, this.Y);
        }
        answer.append('Q');
    }

    public void turnE() {
        position = Position.values()[(position.number + 1) % 4];
        for (Manipulator manipulator : manipulators) {
            manipulator.turnQ(this.X, this.Y);
        }
        answer.append('E');
    }

    public void moveTo(Point<Integer, Integer> point) {
        int helpX = point.getX() - X;
        int helpY = point.getY() - Y;
        if (helpX == 0) {
            if (helpY == 1) {
                moveS();
            } else {
                moveW();
            }
        } else if (helpX == 1) {
            moveD();
        } else {
            moveA();
        }
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

        public Point<Integer, Integer> getPoint() {
            return new Point<>(X, Y);
        }

        private void moveW() {
            this.Y--;
        }

        private void moveS() {
            this.Y++;
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
