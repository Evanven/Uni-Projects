import java.util.NoSuchElementException;

public class List<T> {

    private Node<T> head = null;
    private Node<T> tail = null;
    int listSize;

    public List() {
        listSize = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void insertAtFront(T data) {
        listSize += 1;
        Node<T> dot = new Node<>(data);

        if (isEmpty()) {
            head = dot;
            tail = dot;
        } else {
            dot.setNext(head);
            head = dot;
        }
    }

    public void insertAtBack(T data) {
        listSize += 1;
        Node<T> dot = new Node<>(data);

        if (isEmpty()) {
            head = dot;
        } else {
            tail.setNext(dot);
        }
        tail = dot;
    }

   public T removeFromBack() throws NoSuchElementException {
       if (isEmpty())
           throw new NoSuchElementException();

       T data = tail.getData();

       if (head == tail)
           head = tail = null;
       else {
           Node<T> iterator = head;
           while (iterator.getNext() != tail)
               iterator = iterator.getNext();

           iterator.setNext(null);
           tail = iterator;
       }

       return data;
   }

    public Node<T> getHead() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return head;
    }

    public int size() {
        return listSize;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Empty List";
        }
        else{
            Node<T> current = head;

            StringBuilder ret = new StringBuilder();

            while (current != null) {
                ret.append(current.getData().toString());

                if (current.getNext() != null)
                    ret.append(" ");

                current = current.getNext();
            }

            return ret.toString();
        }
    }

}
