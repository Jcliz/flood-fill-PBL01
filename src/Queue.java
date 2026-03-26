public class Queue<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;

    public Queue() {}

    public void enqueue(T elemento) {
        Node<T> novoNode = new Node<>(elemento);
        if (isEmpty()) {
            first = novoNode;
        } else {
            last.setNext(novoNode);
        }
        last = novoNode;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NullPointerException();
        }
        var e = first.getElement();
        first = first.getNext();
        size--;

        if (isEmpty()) {
            last = null;
        }

        return e;
    }

    public Node<T> front() {
        return first;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }
}