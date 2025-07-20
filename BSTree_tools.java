/* This program contains 2 parts: (1) and (2)
   YOUR TASK IS TO COMPLETE THE PART  (2)  ONLY
 */
//(1)==============================================================
import java.io.*;
import java.util.*;

class BSTree
  {Node root;
   BSTree() {root=null;}
   boolean isEmpty()
      {return(root==null);
      }
   void clear()
      {root=null;
      }
   void fvisit(Node p, RandomAccessFile f) throws Exception
     {if(p != null) f.writeBytes(p.info + " ");
     }
   void preOrder(Node p, RandomAccessFile f) throws Exception//Node → trái → phải
     {if(p==null) return;
      fvisit(p,f);
      preOrder(p.left,f);
      preOrder(p.right,f);
     }
   void inOrder(Node p, RandomAccessFile f) throws Exception//Trái → node → phải (giúp in ra các giá trị theo thứ tự tăng dần)
     {if(p==null) return;
      inOrder(p.left,f);
      fvisit(p,f);
      inOrder(p.right,f);
     }
   void postOrder(Node p, RandomAccessFile f) throws Exception//Trái → phải → node
     {if(p==null) return;
      postOrder(p.left,f);
      postOrder(p.right,f);
      fvisit(p,f);
     }
  void breadth(Node p, RandomAccessFile f) throws Exception//Duyệt theo từng tầng (sử dụng queue)
    {if(p==null) return;
     Queue q = new Queue();
     q.enqueue(p);Node r;
     while(!q.isEmpty())
       {r = q.dequeue();
        fvisit(r,f);
        if(r.left!=null) q.enqueue(r.left);
        if(r.right!=null) q.enqueue(r.right);
       }
    }
   void loadData(int k)  //do not edit this function
     {String [] a = Lib.readLineToStrArray("data.txt", k);
      int [] b = Lib.readLineToIntArray("data.txt", k+1);
      int n = a.length;
      for(int i=0;i<n;i++) insert(a[i],b[i]);
     }

//===========================================================================
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
  void insert(String xOwner, int xPrice)
     {//You should insert here statements to complete this function
    // Kiểm tra điều kiện không chèn
    if(xOwner.charAt(0) == 'B' || xPrice > 100) return;

    Car xCar = new Car(xOwner, xPrice);
    Node q = new Node(xCar);

    // Cây rỗng
    if(root == null) {
        root = q;
        return;
    }

    Node p = root;
    while(true) {
        // So sánh theo giá tiền
        if(xPrice < p.info.price) {
            if(p.left == null) {
                p.left = q;
                return;
            }
            p = p.left;
        } else {
            if(p.right == null) {
                p.right = q;
                return;
            }
            p = p.right;
        }
    }
     }

  void f1() throws Exception
    {/* You do not need to edit this function. Your task is to complete insert  function
        above only.
     */
     clear();
     loadData(1);
     String fname = "f1.txt";
     File g123 = new File(fname);
     if(g123.exists()) g123.delete();
     RandomAccessFile  f = new RandomAccessFile(fname, "rw"); 
     preOrder(root,f);
     f.writeBytes("\r\n");
     inOrder(root,f);
     f.writeBytes("\r\n");
     f.close();
    }  
  
//=============================================================
  void f2() throws Exception
    {clear();
     loadData(4);
     String fname = "f2.txt";
     File g123 = new File(fname);
     if(g123.exists()) g123.delete();
     RandomAccessFile  f = new RandomAccessFile(fname, "rw"); 
     preOrder(root,f);
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
    if(p == null) return;
    // Chỉ ghi node nếu giá nằm trong [3,5]
    if(p.info.price >= 3 && p.info.price <= 5) {
        f.writeBytes(p.info + " ");
    }
    preOrder2(p.left, f);
    preOrder2(p.right, f);
}

void deleteByPrice(int xPrice) {
    Node p = root, parent = null;
    while (p != null && p.info.price != xPrice) {
        parent = p;
        if (xPrice < p.info.price) {
            p = p.left;
        } else {
            p = p.right;
        }
    }
    if (p == null) return; // Không tìm thấy node cần xóa

    // Xóa nút p
    // Trường hợp 1: p không có con trái
    if (p.left == null) {
        if (parent == null) root = p.right;
        else if (parent.left == p) parent.left = p.right;
        else parent.right = p.right;
    }
    // Trường hợp 2: p không có con phải
    else if (p.right == null) {
        if (parent == null) root = p.left;
        else if (parent.left == p) parent.left = p.left;
        else parent.right = p.left;
    }
    // Trường hợp 3: p có cả hai con
    else {
        // Tìm node nhỏ nhất ở cây con phải
        Node minRight = p.right, minRightParent = p;
        while (minRight.left != null) {
            minRightParent = minRight;
            minRight = minRight.left;
        }
        // Thay thế thông tin
        p.info = minRight.info;
        // Xóa nút minRight
        if (minRightParent.left == minRight)
            minRightParent.left = minRight.right;
        else
            minRightParent.right = minRight.right;
    }
}

// Tìm node theo price
Node searchByPrice(int xPrice) {
    Node p = root;
    while (p != null) {
        if (p.info.price == xPrice) return p;
        if (xPrice < p.info.price) p = p.left;
        else p = p.right;
    }
    return null;
}
// Xóa node target
void deleteNode(Node target) {
    if (target == null) return;
    deleteByPrice(target.info.price);
}

// f.writeBytes(" k = " + k + "\r\n");
//=============================================================
  void f3() throws Exception
    {clear();
     loadData(7);
     String fname = "f3.txt";
     File g123 = new File(fname);
     if(g123.exists()) g123.delete();
     RandomAccessFile  f = new RandomAccessFile(fname, "rw"); 
     breadth(root,f);
     f.writeBytes("\r\n");
    //------------------------------------------------------------------------------------
     /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/

    Node target = null;
    Queue q = new Queue();
    if (root != null) q.enqueue(root);
    while (!q.isEmpty()) {
        Node n = q.dequeue();
        if (n.left != null) q.enqueue(n.left);
        if (n.right != null) q.enqueue(n.right);
        if (n.left != null && n.right != null && n.info.price < 7) {
            target = n;
            break;
        }
    }
    if (target != null) {
        deleteByCopying(target);
    }

    //------------------------------------------------------------------------------------
     breadth(root,f);
     f.writeBytes("\r\n");
     f.close();
    }  

  void deleteByCopying(Node node) {
    // Node có 2 con
    Node parent = node, p = node.left;
    if (p == null) return; // không có con trái
    // Tìm node lớn nhất bên trái
    while (p.right != null) {
        parent = p;
        p = p.right;
    }
    node.info = p.info; // copy thông tin sang
    // Xóa nút đã copy
    if (parent == node) parent.left = p.left;
    else parent.right = p.left;
}
  
//=============================================================
  void f4() throws Exception
    {clear();
     loadData(10);
     String fname = "f4.txt";
     File g123 = new File(fname);
     if(g123.exists()) g123.delete();
     RandomAccessFile  f = new RandomAccessFile(fname, "rw"); 
     breadth(root,f);
     f.writeBytes("\r\n");
    //------------------------------------------------------------------------------------
     /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/



    //------------------------------------------------------------------------------------
     breadth(root,f);
     f.writeBytes("\r\n");
     f.close();
    }
    int height(Node p){
        if(p==null)
            return 0;
        if(height(p.left)>=height(p.right))
            return height(p.left)+1;
        else return height(p.right)+1;
    }
    void f16() throws Exception
    {clear();
     loadData(10);
     String fname = "f16.txt";
     File g123 = new File(fname);
     if(g123.exists()) g123.delete();
     RandomAccessFile  f = new RandomAccessFile(fname, "rw"); 
     breadth(root,f);
     f.writeBytes("\r\n");
    //------------------------------------------------------------------------------------
     /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
    int h=height(root);
    f.writeBytes(" "+h);
    
    NodePair cand = findRotationCandidate();
    if (cand != null) {
      rotateRight(cand.parent, cand.node);
    }

    //------------------------------------------------------------------------------------
     breadth(root,f);
     f.writeBytes("\r\n");
     f.close();
    }
  // ======= (2) – CHỈ EDIT PHẦN NÀY =======

  // Giúp lưu tạm node và parent tương ứng
  private class NodePair {
    Node node, parent;
    NodePair(Node node, Node parent) {
      this.node = node;
      this.parent = parent;
    }
  }

  // 1. Tìm node đầu tiên (duyệt BFS) có left son và price < 7
  private NodePair findRotationCandidate() {
    Queue nq = new Queue();   // queue cho các node
    Queue pq = new Queue();   // queue cho parent của từng node

    if (root != null) {
      nq.enqueue(root);
      pq.enqueue(null);
    }

    while (!nq.isEmpty()) {
      Node u  = nq.dequeue();
      Node up = pq.dequeue();

      if (u.left != null && u.info.price < 7) {
        return new NodePair(u, up);
      }
      if (u.left != null) {
        nq.enqueue(u.left);
        pq.enqueue(u);
      }
      if (u.right != null) {
        nq.enqueue(u.right);
        pq.enqueue(u);
      }
    }
    return null;
  }

  // 2. Quay phải tại node p, biết trước parent của p
  private void rotateRight(Node parent, Node p) {
    Node L = p.left;
    // Bước 1: chuyển L.right thành p.left
    p.left  = L.right;
    // Bước 2: gắn p thành L.right
    L.right = p;
    // Bước 3: nối L vào đúng chỗ cũ của p
    if (parent == null) {
      root = L;
    } else if (parent.left == p) {
      parent.left = L;
    } else {
      parent.right = L;
    }
  }
/** Trả về số node trong cây */
public int size() {
    return size(root);
}
private int size(Node p) {
    if (p == null) return 0;
    return 1 + size(p.left) + size(p.right);
}

/** Node có giá nhỏ nhất (càng trái cùng) */
public Node findMin() {
    if (root == null) return null;
    Node p = root;
    while (p.left != null) p = p.left;
    return p;
}

/** Node có giá lớn nhất (càng phải cùng) */
public Node findMax() {
    if (root == null) return null;
    Node p = root;
    while (p.right != null) p = p.right;
    return p;
}
/** Kiểm tra xem cấu trúc có đúng là BST không */
public boolean isValidBST() {
    return isValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
}
private boolean isValidBST(Node p, int min, int max) {
    if (p == null) return true;
    int v = p.info.price;
    if (v <= min || v >= max) return false;
    return isValidBST(p.left,  min, v) 
        && isValidBST(p.right, v, max);
}
/** Trả về danh sách Car từ root đến node price = xPrice (nếu có) */
public List<Car> pathTo(int xPrice) {
    List<Car> path = new ArrayList<>();
    Node p = root;
    while (p != null) {
        path.add(p.info);
        if (xPrice == p.info.price) break;
        p = xPrice < p.info.price ? p.left : p.right;
    }
    if (p == null) path.clear();  // không tìm thấy
    return path;
}
/** In ra màn hình (console) toàn bộ root→leaf paths */
public void printAllRootToLeaf() {
    List<Car> buffer = new ArrayList<>();
    dfsPrint(root, buffer);
}
private void dfsPrint(Node p, List<Car> buf) {
    if (p == null) return;
    buf.add(p.info);
    if (p.left == null && p.right == null) {
        System.out.println(buf);
    } else {
        dfsPrint(p.left, buf);
        dfsPrint(p.right, buf);
    }
    buf.remove(buf.size() - 1);
}
/** Chuyển cây thành mảng (in-order) */
public List<Car> toListInOrder() {
    List<Car> res = new ArrayList<>();
    inOrderCollect(root, res);
    return res;
}
private void inOrderCollect(Node p, List<Car> out) {
    if (p == null) return;
    inOrderCollect(p.left, out);
    out.add(p.info);
    inOrderCollect(p.right, out);
}
/** Trả về giá trị Car nhỏ thứ k (1-based). Nếu không đủ k node, trả về null. */
public Car kthSmallest(int k) {
    Counter c = new Counter();
    return kth(root, k, c);
}
private Car kth(Node p, int k, Counter cnt) {
    if (p == null) return null;
    Car left = kth(p.left, k, cnt);
    if (left != null) return left;
    if (++cnt.val == k) return p.info;
    return kth(p.right, k, cnt);
}
private static class Counter { int val; }

/** Tìm Lowest Common Ancestor của hai node có giá a và b */
public Node lowestCommonAncestor(int a, int b) {
    return lca(root, a, b);
}
private Node lca(Node p, int a, int b) {
    if (p == null) return null;
    if (p.info.price > Math.max(a,b))  return lca(p.left,  a, b);
    if (p.info.price < Math.min(a,b))  return lca(p.right, a, b);
    return p;
}
/** Đảo ngược toàn bộ con trái ↔ phải */
public void mirror() {
    mirror(root);
}
private void mirror(Node p) {
    if (p == null) return;
    Node tmp = p.left; 
    p.left = p.right; 
    p.right = tmp;
    mirror(p.left);
    mirror(p.right);
}
/** Quay trái tại node p (cập nhật parent bên ngoài nếu cần) */
public Node rotateLeft(Node p) {
    if (p == null || p.right == null) return p;
    Node R = p.right;
    p.right = R.left;
    R.left = p;
    return R;
}

/** Quay phải tại node p */
public Node rotateRight(Node p) {
    if (p == null || p.left == null) return p;
    Node L = p.left;
    p.left = L.right;
    L.right = p;
    return L;
}
// Đếm số lá (leaf nodes)
public int countLeaves() {
    return countLeaves(root);
}
private int countLeaves(Node p) {
    if (p == null) return 0;
    if (p.left == null && p.right == null) return 1;
    return countLeaves(p.left) + countLeaves(p.right);
}

// Tính tổng giá của tất cả node
public int sumPrices() {
    return sumPrices(root);
}
private int sumPrices(Node p) {
    if (p == null) return 0;
    return p.info.price + sumPrices(p.left) + sumPrices(p.right);
}

// Đếm node có giá nằm trong đoạn [lo, hi]
public int countInRange(int lo, int hi) {
    return countInRange(root, lo, hi);
}
private int countInRange(Node p, int lo, int hi) {
    if (p == null) return 0;
    if (p.info.price < lo) 
        return countInRange(p.right, lo, hi);
    if (p.info.price > hi) 
        return countInRange(p.left, lo, hi);
    return 1 
         + countInRange(p.left, lo, hi) 
         + countInRange(p.right, lo, hi);
}
// Giá trị lớn nhất nhỏ hơn xPrice (predecessor)
public Node predecessor(int xPrice) {
    Node p = root, pred = null;
    while (p != null) {
        if (p.info.price < xPrice) {
            pred = p;
            p = p.right;
        } else {
            p = p.left;
        }
    }
    return pred;
}

// Giá trị nhỏ nhất lớn hơn xPrice (successor)
public Node successor(int xPrice) {
    Node p = root, succ = null;
    while (p != null) {
        if (p.info.price > xPrice) {
            succ = p;
            p = p.left;
        } else {
            p = p.right;
        }
    }
    return succ;
}
// Trả về chiều cao node (dùng để tính cân bằng)
private int height(Node p) {
    return p == null ? 0 : 1 + Math.max(height(p.left), height(p.right));
}

// Kiểm tra xem cây có phải AVL (mỗi node chênh ≤1) không
public boolean isAVL() {
    return isAVL(root);
}
private boolean isAVL(Node p) {
    if (p == null) return true;
    int hl = height(p.left), hr = height(p.right);
    if (Math.abs(hl - hr) > 1) return false;
    return isAVL(p.left) && isAVL(p.right);
}

// Phương thức “cân bằng” gốc đơn giản (right-rotate / left-rotate)  
public void balance() {
    root = balanceNode(root);
}
private Node balanceNode(Node p) {
    if (p == null) return null;
    p.left  = balanceNode(p.left);
    p.right = balanceNode(p.right);
    int hl = height(p.left), hr = height(p.right);
    if (hl - hr > 1)       return rotateRight(p);
    else if (hr - hl > 1)  return rotateLeft(p);
    return p;
}
// In-order lặp bằng Stack
public void inOrderIterative(RandomAccessFile f) throws Exception {
    Stack<Node> st = new Stack<>();
    Node p = root;
    while (p != null || !st.isEmpty()) {
        while (p != null) { st.push(p); p = p.left; }
        p = st.pop();
        fvisit(p, f);
        p = p.right;
    }
    f.writeBytes("\r\n");
}

// Pre-order lặp
public void preOrderIterative(RandomAccessFile f) throws Exception {
    if (root == null) { f.writeBytes("\r\n"); return; }
    Stack<Node> st = new Stack<>();
    st.push(root);
    while (!st.isEmpty()) { 
        Node u = st.pop();
        fvisit(u, f);
        if (u.right != null) st.push(u.right);
        if (u.left  != null) st.push(u.left);
    }
    f.writeBytes("\r\n");
}
/** Trả về List<Car> của tất cả node tại độ sâu k (root ở depth=0) */
public List<Car> nodesAtLevel(int k) {
    List<Car> ans = new ArrayList<>();
    nodesAtLevel(root, k, 0, ans);
    return ans;
}
private void nodesAtLevel(Node p, int target, int depth, List<Car> out) {
    if (p == null) return;
    if (depth == target) {
        out.add(p.info);
    } else {
        nodesAtLevel(p.left,  target, depth+1, out);
        nodesAtLevel(p.right, target, depth+1, out);
    }
}
/** Chuyển BST thành DLL theo order (in-order), trả về head của danh sách kép */
public Node flattenToDLL() {
    if (root == null) return null;
    // Dummy head
    Node dummy = new Node(new Car("", 0));
    Node prev = dummy;
    Stack<Node> st = new Stack<>();
    Node p = root;
    while (p != null || !st.isEmpty()) {
        while (p != null) { st.push(p); p = p.left; }
        p = st.pop();
        // nối vào DLL
        prev.right = p;
        p.left = prev;
        prev = p;
        p = p.right;
    }
    Node headDLL = dummy.right;
    headDLL.left = null;
    return headDLL;
}
/** In cây ra console với indent (phải→node→trái) */
public void print2D() {
    print2D(root, 0);
}
private void print2D(Node p, int level) {
    if (p == null) return;
    print2D(p.right, level+1);
    System.out.println("    ".repeat(level) + p.info.price);
    print2D(p.left,  level+1);
}
/** Trả về đường kính (số node tối đa trên một đường root→leaf bất kỳ) */
public int diameter() {
    return diameter(root).diam;
}
private Pair diameter(Node p) {
    if (p == null) return new Pair(0, 0);
    Pair L = diameter(p.left), R = diameter(p.right);
    // chiều cao = max(hl, hr)+1
    int h = 1 + Math.max(L.h, R.h);
    // diam = max(diamL, diamR, hl+hr+1)
    int d = Math.max(Math.max(L.diam, R.diam), L.h + R.h + 1);
    return new Pair(h, d);
}
private static class Pair {
    int h, diam;
    Pair(int h, int diam) { this.h = h; this.diam = diam; }
}


 }
