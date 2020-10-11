import Engine.Engine;
import Model.CommonExp;
import Model.ExpOrigin;
import javafx.util.Pair;
import org.junit.Test;
import Engine.ExpEngine;

import java.lang.reflect.Array;
import java.util.*;

public class TestClass {

    @Test
    public void test() {
        String str = "123+";
        System.out.println(str.substring(0, 4));
    }

    @Test
    public void test1() {
        String str;
        str = "213";
        System.out.println(str);
    }

    @Test
    public void test2() {
        ExpOrigin expOrigin = new CommonExp("-a+c+d-e/f*d");
        System.out.println(expOrigin.toString());
        expOrigin.ShowStructure();
    }

    @Test
    public void test3() {
        String str = "e?x?p";
        System.out.println(str.replaceAll("(.*)e\\?x\\?p(.*)", "$1").length());
        System.out.println(str.split("e?x?p"));
        System.out.println(str.indexOf("cos("));
        System.out.println(str.indexOf(""));
        System.out.println(str.indexOf(""));
}

    @Test
    public void test4() {
        List<String> A = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
        List<String> B = new ArrayList<String>(Arrays.asList("b", "c", "d"));
        System.out.println(A.containsAll(B));
        List<String> C = A;
        System.out.println(C.containsAll(B));
        System.out.println(C);
    }

    @Test
    public void test5() {
        String str = "(e?x?p)";
        String mstr = "111122233223";
        System.out.println(mstr.indexOf("22", 10));
        List<String> strs1 = new ArrayList<String>(Arrays.asList("1", "2", "3"));
        List<String> strss = Arrays.asList("1", "3", "4");
        System.out.println(strs1.indexOf("3"));
        System.out.println(mstr.substring(2, 4));
        String strs = str.replaceAll("(.*)e\\?x\\?p(.*)", "$1");
        System.out.println(strs);
        System.out.println(mstr.indexOf(strs, 2));
    }

    @Test
    public void test6() {
        String PAT = "e?x?p";
        String pat = PAT.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("e\\?x\\?p", "(.*)");
        System.out.println(pat);
        String str = "123+4";
        System.out.println(str.replaceAll(pat, "$1"));
    }

    @Test
    public void test7() {
        CommonExp commonExp = new CommonExp("a+b+(c+d+a)");
        commonExp.ShowStructure();
        commonExp.ShowPlainStru();
        commonExp.ShowVariables();
    }

    @Test
    public void test8() {
        CommonExp expOrigin = new CommonExp("a");
        CommonExp expOrigin1 = new CommonExp("f(x)>=cos(a+b)");
        String s = "123";
        String b = s;
        b = "345";
//        test.ShowStructure();
        expOrigin.ShowStructure();
        expOrigin.ShowVariables();
    }

    @Test
    public void test9() {
//        Map<Pair<ExpOrigin, ExpOrigin>,
//        List<Map<Pair<ExpOrigin, ExpOrigin>, Map<String, String>>>
    }

    public void changemap(Map<String, String> maps) {
        for (String key : maps.keySet()) {
            System.out.println(maps.get(key));
            maps.put(key, "changed");
        }
    }

    @Test
    public void test10() {
        List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
        lm.add(new HashMap<String, String>());
        lm.get(0).put("1", "2");
        System.out.println(lm.get(0).get("1"));
        Map<String, String> lms = new HashMap<String, String>(lm.get(0));
        lms.put("1", "3");
        System.out.println(lm.get(0).get("1"));
    }

    public void changem(List<Map<String, String>> lm) {
        lm.add(new HashMap<String, String>());
    }

    @Test
    public void test11() {
        List<String> ls = new ArrayList<String>();
        List<List<String>> lss = new ArrayList<List<String>>();
        lss.add(ls);
        System.out.println(lss.get(0).size());
        changels(ls);
        System.out.println(lss.get(0).size());
    }

    public void changels(List<String> ls) {
        ls.add("123");
    }

    @Test
    public void test12() {
        Map<String, List<String>> m = new HashMap<String, List<String>>();
        m.put("1", new ArrayList<String>());
        m.get("1").add("2");
        List<String> sss = m.get("1");
        System.out.println(m.get("1").size());
        sss.add("3");
        System.out.println(m.get("1").size());
    }

    @Test
    public void test13() {
        List<List<String>> lss = Arrays.asList(Arrays.asList("3", "4"));
        List<String> ls = Arrays.asList("1", "2");
        lss.set(0, ls);
        System.out.println(lss.get(0).get(0));
    }

    @Test
    public void test14() {
        CommonExp c1 = new CommonExp("a+b");
        List<CommonExp> c2 = new ArrayList<CommonExp>();
        c2.add(c1);
        List<CommonExp> c3 = new ArrayList<CommonExp>();
        c3.add(c1);
        if (c2.get(0).equals(c3.get(0))) {
            System.out.println("123");
        }
    }

    @Test
    public void test15() {
        ExpEngine expEngine = new ExpEngine();
        System.out.println(expEngine.matchExp("a+b", "c+d"));
        System.out.println("123");
    }

    @Test
    public void test16() {
        Map<String, String> m = new HashMap<String, String>();
        List<String> l = new ArrayList<String>();
        m.put("1", "1");
        m.put("2", "2");
        m.put("3", "3");
        m.put("4", "4");
        m.put("5", "5");
        for (String ss : m.keySet()) {
            if (ss.equals("1") || ss.equals("3")) {
                l.add(ss);
            }
        }
        for (String ss : l) {
            m.remove(ss);
        }
        for (String ss : m.keySet()) {
            System.out.println(ss);
        }
    }

    @Test
    public void test17() {
        List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
        Map<String, String> m = new HashMap<String, String>();
        m.put("1", "2");
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("2", "2");
        lm.add(m);
        lm.add(m1);
        Map<String, String> m2 = new HashMap<String, String>(lm.get(1));
        System.out.println(lm.indexOf(m2));
        lm.get(1).put("2", "3");
        System.out.println(m2.get("3"));
    }

    public boolean p1() {
        System.out.println("1");
        return true;
    }
}
