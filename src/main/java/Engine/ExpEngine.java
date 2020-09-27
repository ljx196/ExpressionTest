package Engine;

import Model.CommonExp;
import Model.ExpOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpEngine implements Engine {

//    变量映射
    List<Map<String, String>> matchMap = new ArrayList<Map<String, String>>();

//    匹配映射
    List<Map<String, String>> matchedCase = new ArrayList<Map<String, String>>();

    public boolean matchExp(String Exp, String Exp1) {
        return matchExp(new CommonExp(Exp1), new CommonExp(Exp));
    }

    //    第一步，先匹配type，若type相同，则说明这两个Exp处于同一个层级，接下来匹配子串，在同层的式子可以部分匹配，但是同层之下的必须精确匹配，即匹配上的式子环境应该一致
//    匹配之后需要同步变量，a*x+b与d*c+e中各变量的映射，a->d,c->x,e->d或者a->c,x->d,b->e;
    public boolean matchExp(ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        Map<ExpOrigin, Boolean> TMP = new HashMap<ExpOrigin, Boolean>();
        if (expOrigin.getHierarchy().equals(expOrigin1.getHierarchy())) {
            for (ExpOrigin exp1 : expOrigin.getExpCon()) {
                boolean FLAG = false;
                for (ExpOrigin exp2 : expOrigin.getExpCon()) {
                    if (!exp1.getHierarchy().equals(exp2.getHierarchy()) || TMP.get(exp2)) {
                        continue;
                    }
                    if (preciseMatch(exp1, exp2, new HashMap<String, String>(), new HashMap<String, String>())) {
                        TMP.put(exp2, true);
                        FLAG = true;
                    }
                }
                if (!FLAG) {
                    break;
                }
            }
        }
        return false;
    }

//    匹配同一层级的表达式
    public boolean preciseMatch(ExpOrigin expOrigin, ExpOrigin expOrigin1, Map<String, String> matched, Map<String, String> matchedv) {
        if (expOrigin.getExpCon().size() != expOrigin1.getExpCon().size()) {
            return false;
        }
        if (expOrigin.getType().equals(expOrigin.getType()) && expOrigin.equals("Variable")) {
            if (!matchedv.containsKey(expOrigin.toString())) {
                matchedv.put(expOrigin.toString(), expOrigin1.toString());
                matchedv.put(expOrigin1.toString(), expOrigin.toString());
                return true;
            }
            if (!matchedv.get(expOrigin.toString()).equals(expOrigin1.toString())) {
                return false;
            } else {
                return true;
            }
        }

        for (ExpOrigin exp1 : expOrigin.getExpCon()) {
            for (ExpOrigin exp2 : expOrigin1.getExpCon()) {
                if (exp1.getHierarchy().equals(exp2.getHierarchy())) {
                    if (!preciseMatch(exp1, exp2, new HashMap<String, String>(matched), new HashMap<String, String>(matchedv))) {

                    } else {

                    }
                }
            }
        }
        return true;
    }
}
