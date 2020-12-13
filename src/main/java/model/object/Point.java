package model.object;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Point<T, T1> {
    @Getter @Setter
    private int dist = 0;
    @Getter @Setter
    private int g = 0;
    @Getter @Setter
    private int F = 0;
    @Getter
    private T X;
    @Getter
    private T1 Y;
    public Point(T X, T1 Y){
        this.X = X;
        this.Y = Y;
    }

    public Point(T X, T1 Y, int dist, int g){
        this.X = X;
        this.Y = Y;
        this.dist = dist;
        this.g = g;
        F = g + dist;
    }

    public void set(T first, T1 second){
        this.X = first;
        this.Y = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point<?, ?> point = (Point<?, ?>) o;
        return Objects.equals(X, point.X) &&
                Objects.equals(Y, point.Y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
