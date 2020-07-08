import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringStackImpl<T> implements StringStack<T> {

    MyNode<T> pseudo = new MyNode("0", null);
    private MyNode<T> head;

    private int stacksize;

    public StringStackImpl() {
        stacksize = 0;
        head = pseudo;
    }

    public void push(T item) {
        stacksize++;

        MyNode<T> dot = new MyNode(item, head);
        head = dot;
    }

    public T pop() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        stacksize--;

        T v = head.getItem();
        head = head.getNext();
        return v;
    }

    public T peek() throws NoSuchElementException {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return head.getItem();
    }

    public boolean isEmpty(){
        return (head == pseudo);
    }

    public void printStack(PrintStream stream){
        if(isEmpty()){
          throw new NoSuchElementException();
        }
      	MyNode<T> first = head;
      	T item;

      	while(first.next != pseudo){
      		item = first.getItem();
      		stream.print(item + ", ");

      		first = first.next;
      	}
      	item = first.getItem();
      	stream.println(item);
    }

    public int size(){
        return stacksize;
    }
  }
