public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {}

    public boolean push(T elemento) {
        Node<T> newNode = new Node<>(elemento);
        newNode.setNext(top);
        top = newNode;
        size++;
        return true;
    }

    public boolean pop() {
        if (isEmpty()) {
            return false;
        }
        top = top.getNext();
        size--;
        return true;
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