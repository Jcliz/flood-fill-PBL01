public class Node<T> {
    private final T element;
    private Node<T> next;

    public Node(T elemento) {
        this.element = elemento;
    }

    public T getElement() { return element; }
    public Node<T> getNext() { return next; }
    public void setNext(Node<T> next) { this.next = next; }
}