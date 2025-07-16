/* This program contains 2 parts: (1) and (2)
   YOUR TASK IS TO COMPLETE THE PART  (2)  ONLY
 */
//(1)==============================================================
import java.io.*;
import java.util.*;

class BSTree {

    Node root;

    BSTree() {
        root = null;
    }

    boolean isEmpty() {
        return (root == null);
    }

    void clear() {
        root = null;
    }

    void fvisit(Node p, RandomAccessFile f) throws Exception {
        if (p != null) {
            f.writeBytes(p.info + " ");
        }
    }

    void preOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        fvisit(p, f);
        preOrder(p.left, f);
        preOrder(p.right, f);
    }

    void inOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        inOrder(p.left, f);
        fvisit(p, f);
        inOrder(p.right, f);
    }

    void postOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        postOrder(p.left, f);
        postOrder(p.right, f);
        fvisit(p, f);
    }

    void breadth(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        Queue q = new Queue();
        q.enqueue(p);
        Node r;
        while (!q.isEmpty()) {
            r = q.dequeue();
            fvisit(r, f);
            if (r.left != null) {
                q.enqueue(r.left);
            }
            if (r.right != null) {
                q.enqueue(r.right);
            }
        }
    }

    void loadData(int k) //do not edit this function
    {
        String[] a = Lib.readLineToStrArray("data.txt", k);
        int[] b = Lib.readLineToIntArray("data.txt", k + 1);
        int n = a.length;
        for (int i = 0; i < n; i++) {
            insert(a[i], b[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
    void insert(String xOwner, int xPrice) {//You should insert here statements to complete this function

        if (xOwner.charAt(0) == 'A' || xPrice > 100) {
            return;
        }

        if (isEmpty()) {
            Node node = new Node(new Car(xOwner, xPrice));
            root = node;
            return;
        }

        Car x = new Car(xOwner, xPrice);

        Node father = null;
        Node children = root;


        while (children != null) {

            if (children.info.price == x.price) {
                return;
            }

            father = children;


            if (x.price < children.info.price) {
                children = children.left;
            } else {
                children = children.right;
            }
        }

        if (x.price < father.info.price) {
            father.left = new Node(x);
        } else {
            father.right = new Node(x);
        }
    }

    void f1() throws Exception {/* You do not need to edit this function. Your task is to complete insert  function
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
        preOrder(root, f);
        f.writeBytes("\r\n");
        inOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

//=============================================================
    void f2() throws Exception {
        clear();
        loadData(4);
        String fname = "f2.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        preOrder(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/

        preOrder2(root, f);
        
        //------------------------------------------------------------------------------------


        f.writeBytes("\r\n");
        f.close();
    }

    void preOrder2(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }

        if (p.info.price >= 4 && p.info.price <= 7) {
            fvisit(p, f);
        }
        preOrder2(p.left, f);
        preOrder2(p.right, f);

    }

// f.writeBytes(" k = " + k + "\r\n");
//=============================================================
    void f3() throws Exception {
        clear();
        loadData(7);
        String fname = "f3.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        
        Node fNode = breadth(root);
        System.out.println(fNode.info);
        deleteByCopy(fNode.info);
        breadth(root, f);
        
        //------------------------------------------------------------------------------------

        f.writeBytes("\r\n");
        f.close();
    }


    Node breadth(Node q) {
        if (q == null) return null;

        Queue queue = new Queue();
        queue.enqueue(q);
    
        while (!queue.isEmpty()) {
            Node tmp = queue.dequeue();

            if (tmp.left != null && tmp.right != null && tmp.left.info.price <= 7 && tmp.right.info.price <= 7) {
                return tmp;
                }

            if (tmp.left != null) queue.enqueue(tmp.left);
            if (tmp.right != null) queue.enqueue(tmp.right);
        }
        return null;
    }


    void deleteByCopy(Car x) {
    if (isEmpty()) return;

    Node parent = null, current = root;
    while (current != null && current.info != x) {
        parent = current;
        current = (x.price < current.info.price) ? current.left : current.right;
    }

    if (current == null) return; // not found

    // Case 1 & 2: 0 or 1 child
    Node replacement = null;
    if (current.left == null)
        replacement = current.right;
    else if (current.right == null)
        replacement = current.left;

    if (replacement != null || (current.left == null && current.right == null)) {
        if (parent == null) root = replacement;
        else if (current == parent.left) parent.left = replacement;
        else parent.right = replacement;
        return;
    }

    // Case 3: 2 children
    Node maxLeft = current.left, parentMaxLeft = null;
    while (maxLeft.right != null) {
        parentMaxLeft = maxLeft;
        maxLeft = maxLeft.right;
    }

    current.info = maxLeft.info;
    if (parentMaxLeft == null)
        current.left = maxLeft.left;
    else
        parentMaxLeft.right = maxLeft.left;
}

//=============================================================
    void f4() throws Exception {
        clear();
        loadData(10);
        String fname = "f4.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        Node fNode = breadth3(root);
        Node newRoot = rotateRight(fNode);
        Node faRoot = findFatherOfNode(fNode);
        
        
        if (faRoot == null) root = newRoot;
        else if (faRoot.left == fNode) faRoot.left = newRoot;
        else if (faRoot.right == fNode) faRoot.right = newRoot;
        
        
        breadth(root, f);
        //------------------------------------------------------------------------------------

        f.writeBytes("\r\n");
        f.close();
    }


    Node breadth3(Node q) {
    if (q == null) return null;
    Queue queue = new Queue();
    queue.enqueue(q);
    while (!queue.isEmpty()) {
        Node tmpNode = queue.dequeue();
        if (tmpNode.left != null && tmpNode.info.price < 7) return tmpNode;
        if (tmpNode.left != null) queue.enqueue(tmpNode.left);
        if (tmpNode.right != null) queue.enqueue(tmpNode.right);
    }
    return null;
}


    Node rotateRight(Node oldNode) {
        if (oldNode == null || oldNode.left == null) return oldNode;
        Node newNode = oldNode.left;
        oldNode.left = newNode.right;
        newNode.right = oldNode;
        return newNode;
    }
    
    Node findFatherOfNode(Node qNode) {
        if (qNode == root || isEmpty()) return null;
        Node parentNode = root;
        while (parentNode != null) {
            if (parentNode.left == qNode || parentNode.right == qNode) return parentNode;
            parentNode = (qNode.info.price < parentNode.info.price) ? parentNode.left : parentNode.right;
        }
    return null;
}
}
