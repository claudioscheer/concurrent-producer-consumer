package interfaces;

public interface GenericListInterface<T> {

    public boolean add(T item);

    public boolean remove(T item);

    public boolean contains(T item);

    public int size();
}