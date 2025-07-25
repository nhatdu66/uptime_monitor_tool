// File: StringMatching.java

import java.util.*;
import java.io.*;

public class StringMatching {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter text: ");
        String text = sc.nextLine();
        System.out.print("Enter pattern: ");
        String pattern = sc.nextLine();
        System.out.println();

        Runtime runtime = Runtime.getRuntime();
        // ==== 1. Brute-Force ====
        System.gc();
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        long t0 = System.nanoTime();
        List<Integer> bf = bruteForceSearch(text, pattern);
        long t1 = System.nanoTime();
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("Brute-Force", bf, t1 - t0, memAfter - memBefore);

        // ==== 2. KMP ====
        System.gc();
        memBefore = runtime.totalMemory() - runtime.freeMemory();
        t0 = System.nanoTime();
        List<Integer> kmp = KMPSearch(text, pattern);
        t1 = System.nanoTime();
        memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("KMP", kmp, t1 - t0, memAfter - memBefore);

        // ==== 3. Rabin-Karp ====
        System.gc();
        memBefore = runtime.totalMemory() - runtime.freeMemory();
        t0 = System.nanoTime();
        List<Integer> rk = rabinKarpSearch(text, pattern);
        t1 = System.nanoTime();
        memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("Rabin-Karp", rk, t1 - t0, memAfter - memBefore);

        // ==== 4. Boyer-Moore ====
        System.gc();
        memBefore = runtime.totalMemory() - runtime.freeMemory();
        t0 = System.nanoTime();
        List<Integer> bm = boyerMooreSearch(text, pattern);
        t1 = System.nanoTime();
        memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("Boyer-Moore", bm, t1 - t0, memAfter - memBefore);

        // ==== 5. Aho–Corasick ====
        System.gc();
        memBefore = runtime.totalMemory() - runtime.freeMemory();
        t0 = System.nanoTime();
        List<Integer> ac = ahoCorasickSearch(text, pattern);
        t1 = System.nanoTime();
        memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("Aho–Corasick", ac, t1 - t0, memAfter - memBefore);

        // ==== 6. Z-Algorithm ====
        System.gc();
        memBefore = runtime.totalMemory() - runtime.freeMemory();
        t0 = System.nanoTime();
        List<Integer> za = ZSearch(text, pattern);
        t1 = System.nanoTime();
        memAfter = runtime.totalMemory() - runtime.freeMemory();
        printStats("Z-Algorithm", za, t1 - t0, memAfter - memBefore);

        sc.close();
    }

    private static void printStats(String name, List<Integer> positions, long nanoTime, long memBytes) {
        System.out.printf("%-15s : Positions=%s  | Time=%.3f ms  | Memory=%d bytes%n",
            name, positions, nanoTime / 1_000_000.0, memBytes);
    }

    // 1. Brute-Force
    public static List<Integer> bruteForceSearch(String text, String pat) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pat.length();
        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pat.charAt(j)) j++;
            if (j == m) res.add(i);
        }
        return res;
    }

    // 2. KMP
    public static List<Integer> KMPSearch(String text, String pat) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pat.length();
        int[] lps = buildLPS(pat);
        int i = 0, j = 0;
        while (i < n) {
            if (text.charAt(i) == pat.charAt(j)) {
                i++; j++;
                if (j == m) {
                    res.add(i - m);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return res;
    }
    private static int[] buildLPS(String pat) {
        int m = pat.length();
        int[] lps = new int[m];
        int len = 0, i = 1;
        while (i < m) {
            if (pat.charAt(i) == pat.charAt(len)) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    // 3. Rabin-Karp
    public static List<Integer> rabinKarpSearch(String text, String pat) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pat.length();
        if (m == 0 || n < m) return res;
        final int base = 256;
        final int mod = 101_467_4487; // a large prime
        long hashP = 0, hashT = 0, power = 1;
        for (int i = 0; i < m; i++) {
            hashP = (hashP * base + pat.charAt(i)) % mod;
            hashT = (hashT * base + text.charAt(i)) % mod;
            power = (power * base) % mod;
        }
        for (int i = 0; i + m <= n; i++) {
            if (hashP == hashT) {
                if (text.regionMatches(i, pat, 0, m)) {
                    res.add(i);
                }
            }
            if (i + m < n) {
                hashT = (hashT * base - text.charAt(i) * power % mod + mod) % mod;
                hashT = (hashT + text.charAt(i + m)) % mod;
            }
        }
        return res;
    }

    // 4. Boyer-Moore
    public static List<Integer> boyerMooreSearch(String text, String pat) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pat.length();
        if (m == 0 || n < m) return res;
        int[] bad = buildBadCharTable(pat);
        int[] good = buildGoodSuffixTable(pat);
        int shift = 0;
        while (shift <= n - m) {
            int j = m - 1;
            while (j >= 0 && pat.charAt(j) == text.charAt(shift + j)) j--;
            if (j < 0) {
                res.add(shift);
                shift += good[0];
            } else {
                int bcShift = j - bad[text.charAt(shift + j)];
                int gsShift = good[j];
                shift += Math.max(1, Math.max(bcShift, gsShift));
            }
        }
        return res;
    }
    private static int[] buildBadCharTable(String pat) {
        final int R = 256;
        int[] bad = new int[R];
        Arrays.fill(bad, -1);
        for (int i = 0; i < pat.length(); i++) {
            bad[pat.charAt(i)] = i;
        }
        return bad;
    }
    private static int[] buildGoodSuffixTable(String pat) {
        int m = pat.length();
        int[] good = new int[m];
        int[] suff = new int[m];
        // compute suffixes
        suff[m - 1] = m;
        int g = m - 1, f = 0;
        for (int i = m - 2; i >= 0; i--) {
            if (i > g && suff[i + m - 1 - f] < i - g) {
                suff[i] = suff[i + m - 1 - f];
            } else {
                if (i < g) g = i;
                f = i;
                while (g >= 0 && pat.charAt(g) == pat.charAt(g + m - 1 - f)) g--;
                suff[i] = f - g;
            }
        }
        // build good suffix
        Arrays.fill(good, m);
        int j = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (suff[i] == i + 1) {
                for (; j < m - 1 - i; j++) {
                    if (good[j] == m) good[j] = m - 1 - i;
                }
            }
        }
        for (int i = 0; i <= m - 2; i++) {
            good[m - 1 - suff[i]] = m - 1 - i;
        }
        return good;
    }

    // 5. Aho–Corasick (single pattern)
    public static List<Integer> ahoCorasickSearch(String text, String pat) {
        // build automaton for one pattern
        AhoNode root = new AhoNode();
        // insert pattern
        AhoNode node = root;
        for (char c : pat.toCharArray()) {
            node = node.next.computeIfAbsent(c, k -> new AhoNode());
        }
        node.out.add(pat);
        // build failure links
        Queue<AhoNode> q = new ArrayDeque<>();
        for (AhoNode child : root.next.values()) {
            child.fail = root;
            q.add(child);
        }
        while (!q.isEmpty()) {
            AhoNode curr = q.poll();
            for (Map.Entry<Character,AhoNode> kv : curr.next.entrySet()) {
                char ch = kv.getKey();
                AhoNode nxt = kv.getValue();
                AhoNode f = curr.fail;
                while (f != null && !f.next.containsKey(ch)) {
                    f = f.fail;
                }
                nxt.fail = (f == null ? root : f.next.get(ch));
                nxt.out.addAll(nxt.fail.out);
                q.add(nxt);
            }
        }
        // search
        List<Integer> res = new ArrayList<>();
        node = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (node != null && !node.next.containsKey(c)) {
                node = node.fail;
            }
            node = (node == null ? root : node.next.get(c));
            if (!node.out.isEmpty()) {
                for (String p : node.out) {
                    res.add(i - p.length() + 1);
                }
            }
        }
        return res;
    }

    private static class AhoNode {
        Map<Character, AhoNode> next = new HashMap<>();
        AhoNode fail = null;
        List<String> out = new ArrayList<>();
    }

    // 6. Z-Algorithm
    public static List<Integer> ZSearch(String text, String pat) {
        List<Integer> res = new ArrayList<>();
        String s = pat + "$" + text;
        int m = pat.length(), n = s.length();
        int[] Z = new int[n];
        int L = 0, R = 0;
        for (int i = 1; i < n; i++) {
            if (i > R) {
                L = R = i;
                while (R < n && s.charAt(R - L) == s.charAt(R)) R++;
                Z[i] = R - L; 
                R--;
            } else {
                int k = i - L;
                if (Z[k] < R - i + 1) {
                    Z[i] = Z[k];
                } else {
                    L = i;
                    while (R < n && s.charAt(R - L) == s.charAt(R)) R++;
                    Z[i] = R - L;
                    R--;
                }
            }
        }
        for (int i = m + 1; i < n; i++) {
            if (Z[i] >= m) {
                res.add(i - m - 1);
            }
        }
        return res;
    }
}
