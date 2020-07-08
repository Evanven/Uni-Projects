public class MyNode<T> {
    protected T item;
    protected MyNode<T> next = null;

    MyNode(T item,MyNode<T> next) {
        this.item = item;
        this.next = next;
    }

    public T getItem() {
        return item;
    }

    public MyNode<T> getNext() {
        return next;
    }

    public void setNext(MyNode<T> next) {
        this.next = next;
    }
  }
