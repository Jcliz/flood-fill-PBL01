public class Main {
    public static void main(String[] args) {
        System.out.println("PILHA: \n");
        Stack<String> stack = new Stack<>();
        Queue<Integer> queue = new Queue<>();

        System.out.println("pilha vazia: " + stack.isEmpty());

        stack.push("10");
        stack.push("20");
        stack.push("30");

        System.out.println("tamanho: " + stack.size());
        System.out.println("elemento do topo: " + stack.top().getElement());

        stack.pop();
        System.out.println("topo removido, novo tamanho: " + stack.size());
        System.out.println("novo elemento do topo: " + stack.top().getElement());

        stack.clear();
        System.out.println("clear na pilha. pilha vazia: " + stack.isEmpty());

        System.out.println("\nFILA:\n");

        System.out.println("fila vazia: " + queue.isEmpty());

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        System.out.println("tamanho depois das insercoes: " + queue.size());
        System.out.println("elemento na frente: " + queue.front().getElement());

        queue.dequeue();
        System.out.println("novo tamanho: " + queue.size());
        System.out.println("novo elemento da frente: " + queue.front().getElement());

        queue.clear();
        System.out.println("clear na fila. fila vazia: " + queue.isEmpty());
    }
}