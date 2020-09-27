import Model.CommonExp;
import Model.ExpOrigin;
import org.junit.Test;

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
        CommonExp expOrigin = new CommonExp("f(x)>=a*x^2+b*x+c");
        String s = "123";
        String b = s;
        b = "345";
//        test.ShowStructure();
        expOrigin.ShowStructure();
        expOrigin.ShowVariables();
    }

    @Test
    public void test9() {
        ExpOrigin expOrigin = new CommonExp("1");
        System.out.println(expOrigin);
        List<String> s = Arrays.asList("1", "2");
        if (s.contains(expOrigin.toString())) {
            System.out.println("123");
        }
    }

    public void changemap(Map<String, String> maps) {
        for (String key : maps.keySet()) {
            System.out.println(maps.get(key));
            maps.put(key, "changed");
        }
    }

}
