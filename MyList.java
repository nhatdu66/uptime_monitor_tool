/* This program contains 2 parts: (1) and (2)
   YOUR TASK IS TO COMPLETE THE PART  (2)  ONLY
 */
//(1)==============================================================
import java.util.*;
import java.io.*;

class MyList {
    
    Node head, tail;
    
    MyList() {
        head = tail = null;
    }
    
    boolean isEmpty() {
        return (head == null);
    }
    
    void clear() {
        head = tail = null;
    }
    
    void fvisit(Node p, RandomAccessFile f) throws Exception {
        if (p != null) {
            f.writeBytes(p.info + " ");
        }
    }
    
    void ftraverse(RandomAccessFile f) throws Exception {
        Node p = head;
        while (p != null) {
            fvisit(p, f); // You will use this statement to write information of the node p to the file
            p = p.next;
        }
        f.writeBytes("\r\n");
    }
    
    void loadData(int k) //do not edit this function
    {
        String[] a = Lib.readLineToStrArray("data.txt", k);
        int[] b = Lib.readLineToIntArray("data.txt", k + 1);
        int n = a.length;
        for (int i = 0; i < n; i++) {
            addLast(a[i], b[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
    void addLast(String xOwner, int xPrice) {//You should write here appropriate statements to complete this function.

        if (xOwner.charAt(0) == 'B' || xPrice > 100) {
            return;
        }
        Node lastNode = new Node(new Car(xOwner, xPrice));


        if (isEmpty()) {
            head = tail = lastNode;
            return;
        }


        tail.next = lastNode;
        tail = lastNode;
    }
    
    void f1() throws Exception {
        /* You do not need to edit this function. Your task is to complete the addLast  function
        above only.
         */
        clear();
        loadData(1);
        String fname = "f1.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        f.close();
    }

//==================================================================
    void f2() throws Exception {
        clear();
        loadData(4);
        String fname = "f2.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        Car x = new Car("X", 1);
        Car y = new Car("Y", 2);
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        
        addFirst(x);
        
        //------------------------------------------------------------------------------------
        
        ftraverse(f);
        f.close();
    }


    void addFirst(Car x) {
        Node firstNode = new Node(x);


        if (isEmpty()) {
            head = tail = firstNode;
            return;
        }


        firstNode.next = head;
        head = firstNode;
        
    }
//==================================================================

    void f3() throws Exception {
        clear();
        loadData(7);
        String fname = "f3.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/

        Node fNode = findNodeByNumber(5);
        removeNode(fNode);
        
        //------------------------------------------------------------------------------------

        ftraverse(f);
        f.close();
    }


    Node findNodeByNumber(int price) {


        if (isEmpty()) {
            return null;
        }
        
        Node currentNode = head;
        
        while (currentNode != null) {
            if (currentNode.info.price == price) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        
        return null;
    }


    void removeNode(Node q) {

        if (isEmpty() || q == null) {
            return;
        }


        if (q == head) {
            removeFirst();
            return;
        }

        if (q == tail) {
            removeLast();
            return;
        }


        Node preNode = findNodeBefore(q);


        if (preNode == null) {
            return;
        }


        preNode.next = q.next;
    }


    void removeFirst() {


        if (isEmpty()) {
            return;
        }


        if (head == tail) {
            head = tail = null;
            return;
        }

        head = head.next;
        
    }


    void removeLast() {

        if (isEmpty()) {
            return;
        }


        if (head == tail) {
            head = tail = null;
            return;
        }


        Node preNode = findNodeBefore(tail);


        preNode.next = null;
        tail = preNode;
    }


    Node findNodeBefore(Node q) {
        if (q == head) {
            return null;
        }
        
        Node preNode = head;
        while (preNode != null && preNode.next != q) {
            preNode = preNode.next;
        }
        
        return preNode;
    }

//==================================================================
    void f4() throws Exception {
        clear();
        loadData(10);
        String fname = "f4.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        
        sortByNumber();
        
        //------------------------------------------------------------------------------------

        ftraverse(f);
        f.close();
    }
    

    void sortByNumber() {
    ArrayList<Car> list = new ArrayList<>();
    for (Node cur = head; cur != null; cur = cur.next) {
        list.add(cur.info);
    }

    list.sort((o1, o2) -> o2.price - o1.price);

    clear();

    for (Car car : list) {
        addLast(car);
    }
}
    
    void addLast(Car x) {
        Node lastNode = new Node(x);


        if (isEmpty()) {
            head = tail = lastNode;
            return;
        }


        tail.next = lastNode;
        tail = lastNode;
    }
    
}
