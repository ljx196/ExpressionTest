import Model.CommonExp;
import Model.ExpOrigin;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        CommonExp commonExp = new CommonExp();
        System.out.println(commonExp.minvs(3, 4));
    }
}
