package model.object;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Robot {

    @Getter @Setter
    private int X;
    @Getter @Setter
    private int Y;
    private List<Manipulator> manipulators;

    public Robot(int x, int y) {
        X = x;
        Y = y;
        manipulators = new ArrayList<>(3);
        manipulators.add(new Manipulator(x + 1, y));
        manipulators.add(new Manipulator(x + 1, y + 1));
        manipulators.add(new Manipulator(x + 1, y - 1));
    }

    public class Manipulator{

        @Getter @Setter
        private int X;

        @Getter @Setter
        private int Y;

        public Manipulator(int x, int y) {
            X = x;
            Y = y;
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
}
