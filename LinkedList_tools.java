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
        // Check conditions
        if (xOwner.charAt(0) == 'B' || xPrice > 100) return;
        Node newNode = new Node(new Car(xOwner, xPrice));
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }
    void addFirst(String xOwner, int xPrice) {
        // Check conditions
        if (xOwner.charAt(0) == 'B' || xPrice > 100) return;
        Node newNode = new Node(new Car(xOwner, xPrice));
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }

    void f1() throws Exception {/* You do not need to edit this function. Your task is to complete the addLast  function
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
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        addFirst("X", 1);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
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

        Node firstNodeWithPrice5 = findFirstByPrice(5);
        deleteNode(firstNodeWithPrice5);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
    Node findFirstByPrice(int xPrice) {
        Node p = head;
        while (p != null) {
            if (p.info.price == xPrice) return p;
            p = p.next;
        }
        return null;
    }
    
    Node findLastByPrice(int xPrice) {
        Node result = null;
        Node p = head;
        while (p != null) {
            if (p.info.price == xPrice) result = p;
            p = p.next;
        }
        return result;
    }
    
    // Xóa node chỉ định (node cần xóa là 'target')
    void deleteNode(Node target) {
        if (target == null || head == null) return;

        // Nếu node cần xóa là head
        if (target == head) {
            head = head.next;
            if (head == null) tail = null;
            return;
        }

        // Nếu node cần xóa không phải là head
        Node prev = head;
        while (prev != null && prev.next != target) {
            prev = prev.next;
        }
        // Nếu tìm thấy node trước target
        if (prev != null && prev.next == target) {
            prev.next = target.next;
            if (target == tail) tail = prev;
        }
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
        sortAscByPrice();
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
    // Sắp xếp danh sách tăng dần theo price
void sortAscByPrice() {
    if (head == null) return;
    for (Node pi = head; pi != null; pi = pi.next) {
        for (Node pj = pi.next; pj != null; pj = pj.next) {
            if (pi.info.price > pj.info.price) {
                // Hoán đổi thông tin xe
                Car tmp = pi.info;
                pi.info = pj.info;
                pj.info = tmp;
            }
        }
    }
}

// Sắp xếp danh sách giảm dần theo price
void sortDescByPrice() {
    if (head == null) return;
    for (Node pi = head; pi != null; pi = pi.next) {
        for (Node pj = pi.next; pj != null; pj = pj.next) {
            if (pi.info.price < pj.info.price) {
                // Hoán đổi thông tin xe
                Car tmp = pi.info;
                pi.info = pj.info;
                pj.info = tmp;
            }
        }
    }
}

Node getNodeAt(int k) {
    Node p = head;
    int count = 0;
    while (p != null && count < k) {
        p = p.next;
        count++;
    }
    return p;
}

void sortAscByPrice(int k, int h) {
    Node start = getNodeAt(k);
    Node end = getNodeAt(h);
    if (start == null || end == null) return;

    for (Node pi = start; pi != null; pi = pi.next) {
        if (pi == end.next) break;
        for (Node pj = pi.next; pj != null && pj != end.next; pj = pj.next) {
            if (pi.info.price > pj.info.price) {
                Car tmp = pi.info;
                pi.info = pj.info;
                pj.info = tmp;
            }
        }
    }
}

void sortDescByPrice(int k, int h) {
    Node start = getNodeAt(k);
    Node end = getNodeAt(h);
    if (start == null || end == null) return;

    for (Node pi = start; pi != null; pi = pi.next) {
        if (pi == end.next) break;
        for (Node pj = pi.next; pj != null && pj != end.next; pj = pj.next) {
            if (pi.info.price < pj.info.price) {
                Car tmp = pi.info;
                pi.info = pj.info;
                pj.info = tmp;
            }
        }
    }
}
//==================================================================

    void f5() throws Exception {
        clear();
        loadData(13);
        String fname = "f5.txt";
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

sortAscByPrice(1, 3);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }

    void f6() throws Exception {//Delete the 4th element in the list.
        clear();
        loadData(13);
        String fname = "f6.txt";
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
        Node nodeToDelete = getNodeAt(3); // lấy node thứ 4 (index 3)
deleteNode(nodeToDelete);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
        void f7() throws Exception {//Delete the 4th element in the list.
        clear();
        loadData(13);
        String fname = "f7.txt";
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
        deleteNode(tail);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }

    void f8() throws Exception {//Suppose the list contains at least 3 elements. Delete the last node having price=5
        clear();
        loadData(13);
        String fname = "f8.txt";
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
        if (hasAtLeastNElements(3)) {
    Node lastNodeWithPrice5 = findLastByPrice(5);
    deleteNode(lastNodeWithPrice5);
}
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
    boolean hasAtLeastNElements(int n) {
    int count = 0;
    Node p = head;
    while (p != null) {
        count++;
        if (count >= n) return true;
        p = p.next;
    }
    return false;
}
    boolean hasAtMostNElements(int n) {
    int count = 0;
    Node p = head;
    while (p != null) {
        count++;
        if (count > n) return false;
        p = p.next;
    }
    return true;
}
    
    void f9() throws Exception {//Add 1 element (X,2) to the 3rd position in the list
        clear();
        loadData(13);
        String fname = "f9.txt";
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
        insertAtPosition(2, new Car("X", 2));
        //Car newCar = new Car("X", 2);
        //insertAtPosition(2, newCar);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
void insertAtPosition(int k, Car c) {
    Node newNode = new Node(c);
    if (k <= 0 || head == null) { // Chèn đầu danh sách
        newNode.next = head;
        head = newNode;
        if (tail == null) tail = newNode;
        return;
    }
    Node prev = head;
    int count = 0;
    while (prev != null && count < k - 1) {
        prev = prev.next;
        count++;
    }
    if (prev == null) { // Chèn cuối danh sách
        tail.next = newNode;
        tail = newNode;
    } else {
        newNode.next = prev.next;
        prev.next = newNode;
        if (newNode.next == null) tail = newNode;
    }
}

    void f10() throws Exception {//Calculate the average price of the elements in the list. 
        clear();
        loadData(13);
        String fname = "f10.txt";
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
        double avg=0;
avg = avgPrice();
        //------------------------------------------------------------------------------------
        f.writeBytes(avg + "\r\n");
        ftraverse(f);
        f.close();
    }
double avgPrice() {
    double sum = 0;
    int count = 0;
    Node p = head;
    while (p != null) {
        sum += p.info.price;
        count++;
        p = p.next;
    }
    if (count == 0) return 0;
    return sum / count;
}
    void f11() throws Exception {//Calculate the average price of the elements in the list (from 2nd to 4th element)
        clear();
        loadData(13);
        String fname = "f11.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        double avg=0;
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        avg = avgPriceFromTo(1, 3);
        //------------------------------------------------------------------------------------
        f.writeBytes(avg + "\r\n");
        ftraverse(f);
        f.close();
    }
    
    double avgPriceFromTo(int k, int h) {
    double sum = 0;
    int count = 0;
    Node p = head;
    int idx = 0;
    while (p != null) {
        if (idx >= k && idx <= h) {
            sum += p.info.price;
            count++;
        }
        if (idx > h) break; // Dừng duyệt khi qua vị trí cuối
        p = p.next;
        idx++;
    }
    if (count == 0) return 0;
    return sum / count;
}
    /*double avgPriceFromTo(int from, int to) {
    double sum = 0;
    int count = 0;
    Node p = getNodeAt(from); // lấy node bắt đầu
    int idx = from;
    while (p != null && idx <= to) {
        sum += p.info.price;
        count++;
        p = p.next;
        idx++;
    }
    if (count == 0) return 0;
    return sum / count;
}*/
    
    void f12() throws Exception {//Sorting elements in the list (DESC and from 2nd to 4th element)
        clear();
        loadData(13);
        String fname = "f12.txt";
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
        sortDescByPrice(1, 3);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }     
    
    void f13() throws Exception {//Sorting elements in the list (DESC and from 2nd to 4th element)
        clear();
        loadData(13);
        String fname = "f13.txt";
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
        deleteEvenPrices();
        removeNodesWithPrice(5);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }   
    
    void deleteEvenPrices() {
    // Xóa các node đầu có price chẵn
    while (head != null && head.info.price % 2 == 0) {
        head = head.next;
    }
    // Nếu list đã hết thì return
    if (head == null) return;

    Node prev = head;
    Node curr = head.next;
    while (curr != null) {
        if (curr.info.price % 2 == 0) {
            prev.next = curr.next;
            curr = prev.next;
        } else {
            prev = curr;
            curr = curr.next;
        }
    }
}
    void removeNodesWithPrice(int price) {
    // Xóa các node đầu có price đúng
    while (head != null && head.info.price == price) {
        head = head.next;
    }
    // Nếu list đã hết thì return
    if (head == null) return;

    Node prev = head;
    Node curr = head.next;
    while (curr != null) {
        if (curr.info.price == price) {
            prev.next = curr.next;
            curr = prev.next;
        } else {
            prev = curr;
            curr = curr.next;
        }
    }
}
    
    void f14() throws Exception {//Sorting elements in the list (DESC and from 2nd to 4th element)
        clear();
        loadData(13);
        String fname = "f14.txt";
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
        removeNodesWithPriceInRange(2, 6);
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }   
    
    /*void removeNodesWithPriceInRange(int minPrice, int maxPrice) {
    // Xóa các node đầu có giá trong khoảng
    while (head != null && head.info.price >= minPrice && head.info.price <= maxPrice) {
        head = head.next;
    }
    // Nếu list đã hết thì return
    if (head == null) return;

    Node prev = head;
    Node curr = head.next;
    while (curr != null) {
        if (curr.info.price >= minPrice && curr.info.price <= maxPrice) {
            prev.next = curr.next;
            curr = prev.next;
        } else {
            prev = curr;
            curr = curr.next;
        }
    }
}*/
    void removeNodesWithPriceInRange(int minPrice, int maxPrice) {
    Node p = head;
    while (p != null) {
        Node next = p.next; // lấy node tiếp theo trước khi xóa
        if (p.info.price >= minPrice && p.info.price <= maxPrice) {
            deleteNode(p);
        }
        p = next; // tiếp tục duyệt node tiếp theo
    }
}
    void f15() throws Exception {//Sorting elements in the list (DESC and from 2nd to 4th element)
        clear();
        loadData(13);
        String fname = "f15.txt";
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
    double max = findMaxPrice();
    double min = findMinPrice();
    double avg = (max + min) / 2.0;

    Node p = head;
    while (p != null) {
        Node next = p.next; // Lấy trước node tiếp theo
        if (p.info.price >= avg) {
            deleteNode(p);
        }
        p = next; // Sang node tiếp theo
    }
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }  
    
    double findMaxPrice() {
    double max = Double.NEGATIVE_INFINITY;
    Node p = head;
    while (p != null) {
        if (p.info.price > max) max = p.info.price;
        p = p.next;
    }
    return max;
}

double findMinPrice() {
    double min = Double.POSITIVE_INFINITY;
    Node p = head;
    while (p != null) {
        if (p.info.price < min) min = p.info.price;
        p = p.next;
    }
    return min;
}
//Đếm số lượng node trong list
int size() {
    int count = 0;
    Node p = head;
    while (p != null) {
        count++;
        p = p.next;
    }
    return count;
}
//Tìm node theo owner (theo tên)
Node findFirstByOwner(String xOwner) {
    Node p = head;
    while (p != null) {
        if (p.info.owner.equals(xOwner)) return p;
        p = p.next;
    }
    return null;
}
//Tìm tất cả node theo điều kiện (giá hoặc tên)
List<Node> findAllByPrice(int xPrice) {
    List<Node> res = new ArrayList<>();
    Node p = head;
    while (p != null) {
        if (p.info.price == xPrice) res.add(p);
        p = p.next;
    }
    return res;
}
/*List<Node> nodesToDelete = findAllByPrice(xPrice);
for (Node node : nodesToDelete) {
    deleteNode(node);
}*/
//Xóa node theo owner
void removeNodesByOwner(String xOwner) {
    while (head != null && head.info.owner.equals(xOwner)) {
        head = head.next;
    }
    if (head == null) return;
    Node prev = head;
    Node curr = head.next;
    while (curr != null) {
        if (curr.info.owner.equals(xOwner)) {
            prev.next = curr.next;
            curr = prev.next;
        } else {
            prev = curr;
            curr = curr.next;
        }
    }
}
//In list ra màn hình (console) cho debug
void printList() {
    Node p = head;
    while (p != null) {
        System.out.print(p.info + " ");
        p = p.next;
    }
    System.out.println();
}
//Đảo ngược danh sách liên kết
void reverseList() {
    Node prev = null;
    Node curr = head;
    tail = head;
    while (curr != null) {
        Node next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
    }
    head = prev;
}
/**
 * Thay thế node target bằng một node mới chứa (xOwner, xPrice).
 * Nếu target == null hoặc danh sách rỗng => không làm gì.
 * Nếu xOwner.charAt(0)=='B' hoặc xPrice>100 => không thay thế.
 */
void replaceNode(Node target, String xOwner, int xPrice) {
    // Kiểm tra điều kiện đầu vào
    if (target == null || head == null) return;
    if (xOwner.charAt(0) == 'B' || xPrice > 100) return;
    
    // Tạo node mới
    Node newNode = new Node(new Car(xOwner, xPrice));
    
    // Trường hợp thay thế head
    if (target == head) {
        newNode.next = head.next;
        head = newNode;
        // Nếu list chỉ có 1 phần tử, cập nhật luôn tail
        if (tail == target) {
            tail = newNode;
        }
        return;
    }
    
    // Tìm node trước target
    Node prev = head;
    while (prev != null && prev.next != target) {
        prev = prev.next;
    }
    
    // Nếu không tìm thấy target thì thoát
    if (prev == null) return;
    
    // Liên kết newNode vào vị trí của target
    newNode.next = target.next;
    prev.next = newNode;
    
    // Nếu target là tail thì cập nhật tail
    if (target == tail) {
        tail = newNode;
    }
}

/** Trả về số lượng phần tử trong danh sách */
public int size() {
    int cnt = 0;
    Node p = head;
    while (p != null) {
        cnt++;
        p = p.next;
    }
    return cnt;
}

/** Tính tổng giá của tất cả xe */
public int sumPrices() {
    int sum = 0;
    Node p = head;
    while (p != null) {
        sum += p.info.price;
        p = p.next;
    }
    return sum;
}

/** Tìm giá nhỏ nhất (trả về null nếu list rỗng) */
public Car minPriceCar() {
    if (head == null) return null;
    Node p = head;
    Car minCar = p.info;
    while (p != null) {
        if (p.info.price < minCar.price) {
            minCar = p.info;
        }
        p = p.next;
    }
    return minCar;
}

/** Tìm giá lớn nhất (trả về null nếu list rỗng) */
public Car maxPriceCar() {
    if (head == null) return null;
    Node p = head;
    Car maxCar = p.info;
    while (p != null) {
        if (p.info.price > maxCar.price) {
            maxCar = p.info;
        }
        p = p.next;
    }
    return maxCar;
}

/** Tính giá trung bình (0 nếu rỗng) */
public double averagePrice() {
    int cnt = size();
    return cnt == 0 ? 0 : (double) sumPrices() / cnt;
}
/** Phát hiện xem có chu trình (cycle) không */
public boolean hasCycle() {
    Node slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) return true;
    }
    return false;
}

/** Trả về node giữa (middle) theo Floyd; nếu chẵn thì trả phần tử thứ  n/2 */
public Node findMiddle() {
    Node slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    return slow;
}

/** Lấy phần tử thứ k từ cuối (1-based). Nếu không đủ trả về null */
public Node getNthFromEnd(int k) {
    Node p = head, q = head;
    for (int i = 0; i < k; i++) {
        if (q == null) return null;
        q = q.next;
    }
    while (q != null) {
        p = p.next;
        q = q.next;
    }
    return p;
}

/** Kiểm tra danh sách có phải palindrome theo price không */
public boolean isPalindrome() {
    if (head == null || head.next == null) return true;
    // Tìm giữa, đảo nửa sau, so sánh
    Node mid = findMiddle();
    Node second = reverse(mid.next);
    Node p1 = head, p2 = second;
    boolean ok = true;
    while (p2 != null) {
        if (p1.info.price != p2.info.price) {
            ok = false;
            break;
        }
        p1 = p1.next;
        p2 = p2.next;
    }
    // Khôi phục lại list
    mid.next = reverse(second);
    return ok;
}

/** Đảo ngược danh sách và trả về head mới */
private Node reverse(Node p) {
    Node prev = null;
    while (p != null) {
        Node nxt = p.next;
        p.next = prev;
        prev = p;
        p = nxt;
    }
    return prev;
}
/** Nối hai danh sách (this nối đuôi với other); trả về this */
public MyList concat(MyList other) {
    if (this.isEmpty()) {
        this.head = other.head;
        this.tail = other.tail;
    } else if (!other.isEmpty()) {
        this.tail.next = other.head;
        this.tail = other.tail;
    }
    return this;
}

/** Tách thành hai danh sách tại vị trí k (0-based). 
 *  Phần sau k chuyển vào list2 và trả về list2 */
public MyList splitAt(int k) {
    MyList list2 = new MyList();
    if (k < 0 || head == null) {
        list2.head = head; 
        list2.tail = tail;
        head = tail = null;
        return list2;
    }
    Node p = head;
    for (int i = 0; i < k && p.next != null; i++) {
        p = p.next;
    }
    list2.head = p.next;
    p.next = null;
    list2.tail = list2.head;
    while (list2.tail != null && list2.tail.next != null) {
        list2.tail = list2.tail.next;
    }
    if (p == tail) tail = p;
    return list2;
}
/** Loại bỏ các node có cùng price (giữ lại lần đầu) */
public void removeDuplicatesByPrice() {
    Set<Integer> seen = new HashSet<>();
    Node prev = null, curr = head;
    while (curr != null) {
        if (seen.contains(curr.info.price)) {
            prev.next = curr.next;
            if (curr == tail) tail = prev;
        } else {
            seen.add(curr.info.price);
            prev = curr;
        }
        curr = curr.next;
    }
}

/** Lọc giữ lại chỉ những node thỏa predicate, bỏ các node khác */
public void filter(Predicate<Car> pred) {
    // Loại đầu
    while (head != null && !pred.test(head.info)) {
        head = head.next;
    }
    if (head == null) {
        tail = null;
        return;
    }
    Node prev = head, curr = head.next;
    while (curr != null) {
        if (!pred.test(curr.info)) {
            prev.next = curr.next;
            if (curr == tail) tail = prev;
        } else {
            prev = curr;
        }
        curr = curr.next;
    }
}
/** Trả về ArrayList<Car> theo thứ tự hiện tại */
public List<Car> toList() {
    List<Car> out = new ArrayList<>();
    Node p = head;
    while (p != null) {
        out.add(p.info);
        p = p.next;
    }
    return out;
}

/** In ngược danh sách ra console (dùng đệ quy) */
public void printReverse() {
    printReverse(head);
    System.out.println();
}
private void printReverse(Node p) {
    if (p == null) return;
    printReverse(p.next);
    System.out.print(p.info + " ");
}
/** Quay vòng phải cả list lên k vị trí */
public void rotateRight(int k) {
    int n = size();
    if (n == 0 || k % n == 0) return;
    k %= n;
    // Tách tại n-k-1
    Node p = head;
    for (int i = 1; i < n - k; i++) {
        p = p.next;
    }
    Node newHead = p.next;
    p.next = null;
    tail.next = head;
    head = newHead;
    tail = p;
}


}
