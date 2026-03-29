public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {}

    public void push(T elemento) {
        Node<T> newNode = new Node<>(elemento);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new NullPointerException();
        }
        T e = top.getElement();
        top = top.getNext();
        size--;
        return e;
    }

    public Node<T> top() {
        return top;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}