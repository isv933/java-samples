/*
   Найти все вхождения подстроки в строке. KMP алгоритм
 */

package org.isv.samples.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Kmp {

    static List<Integer> findAllPos(String s, String t) {
        var res = new ArrayList<Integer>();
        var p = prefix(t);
        var j = 0;
        for (var i = 0; i < s.length(); i++) {
            while (j > 0 && t.charAt(j) != s.charAt(i)) {
                j = p[j - 1];
            }
            if (t.charAt(j) == s.charAt(i)) {
                j++;
            }
            if (j == t.length()) {
                res.add(i - j + 1);
                j = 0;
            }
        }
        return res;
    }

    static int[] prefix(String a) {
        var p = new int[a.length()];

        for (var i = 1; i < a.length(); i++) {
            var j = p[i - 1];
            while (j > 0 && a.charAt(j) != a.charAt(i)) {
                j = p[j - 1];
            }
            if (a.charAt(i) == a.charAt(j)) {
                p[i] = j + 1;
            }
        }
        return p;
    }
}
