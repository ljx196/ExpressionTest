package Engine;

import Model.CommonExp;
import Model.ExpOrigin;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpEngine implements Engine {

//    变量映射
    List<Map<ExpOrigin, ExpOrigin>> matchMap = new ArrayList<Map<ExpOrigin, ExpOrigin>>();


//    匹配映射
    List<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<String, String>>>> matchedCase = new ArrayList<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<String, String>>>>();

    public boolean matchExp(String Exp, String Exp1) {
        return matchExp(new CommonExp(Exp1), new CommonExp(Exp), 0, 0, 0);
    }

    //    第一步，先匹配type，若type相同，则说明这两个Exp处于同一个层级，接下来匹配子串，在同层的式子可以部分匹配，但是同层之下的必须精确匹配，即匹配上的式子环境应该一致
//    匹配之后需要同步变量，a*x+b与d*c+e中各变量的映射，a->d,c->x,e->d或者a->c,x->d,b->e;
    public boolean matchExp(ExpOrigin expOrigin, ExpOrigin expOrigin1, int iexp1, int iexp2, int idx) {
        if (!expOrigin.getHierarchy().equals(expOrigin1.getHierarchy())) {
            return false;
        }
        boolean RT = false;
        for (; iexp1 < expOrigin.getExpCon().size(); iexp1++) {
            for (; iexp2 < expOrigin1.getExpCon().size(); iexp2++) {
                if (expOrigin.getExpCon().get(iexp1).getHierarchy().equals(expOrigin1.getExpCon().get(iexp2).getHierarchy())) {
                    if (preciseMatch(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2), idx)) {
                        if (matchMap.get(idx).containsKey(expOrigin.getExpCon().get(iexp1))) {
                            Map<ExpOrigin, ExpOrigin> TMP = new HashMap<ExpOrigin, ExpOrigin>(matchMap.get(idx));
                            TMP.remove(expOrigin.getExpCon().get(iexp1));
                            TMP.remove(expOrigin1.getExpCon().get(iexp2));
                            TMP.put(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                            TMP.put(expOrigin1.getExpCon().get(iexp2), expOrigin.getExpCon().get(iexp1));
                            matchMap.add(TMP);
                            RT = matchExp(expOrigin, expOrigin1, iexp1, iexp2 + 1, matchMap.indexOf(TMP));
                            continue;
                        }
                        matchMap.get(idx).put(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                        matchMap.get(idx).put(expOrigin1.getExpCon().get(iexp2), expOrigin.getExpCon().get(iexp1));
                    }
                }
            }
        }
        if (matchMap.get(idx).size() == 2 * Math.min(expOrigin.getExpCon().size(), expOrigin1.getExpCon().size()) || RT) {
            return true;
        }
        return false;
    }

//    匹配同一层级的表达式
    public boolean preciseMatch(ExpOrigin expOrigin, ExpOrigin expOrigin1, int idx) {
        if (expOrigin.getExpCon().size() != expOrigin1.getExpCon().size()) {
            return false;
        }
        for (ExpOrigin exp : expOrigin.getExpCon()) {
            for (ExpOrigin exp1 : expOrigin1.getExpCon()) {
                if (preciseMatch(exp, exp1, idx)) {
                    if (true) {

                    }
                }
            }
        }
        return false;
    }
}
