package model;

public class Pair<E, F> {
    private E first;
    private F second;

    public Pair(E first, F second) {
        this.first = first;
        this.second = second;
    }

    public boolean firstEquals(Pair<E,F> pair){
        return pair.getFirst().equals(this.first);
    }

    public boolean secondEquals(Pair<E,F> pair){
        return pair.getSecond().equals(this.second);
    }

    public E getFirst() {
        return first;
    }

    public F getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Pair<E, F>) obj).first.equals(this.first) && ((Pair<E, F>) obj).second.equals(this.second);
    }


}
