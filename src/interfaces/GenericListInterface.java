package interfaces;

public interface GenericListInterface<T> {

    public boolean add(T item) throws InterruptedException;

    public boolean remove(T item) throws InterruptedException;

    public boolean contains(T item);

    public int size();
}