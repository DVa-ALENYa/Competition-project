package model.object;

import java.util.Objects;

public class Pair<T, T1> {
    private T first;
    private T1 second;
    public Pair(T first, T1 second){
        this.first = first;
        this.second = second;
    }
    public T getFirst(){
        return this.first;
    }

    public T1 getSecond(){
        return this.second;
    }

    public void setFirst(T first){
        this.first = first;
    }

    public void setSecond(T1 second){
        this.second = second;
    }

    public void set(T first, T1 second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
