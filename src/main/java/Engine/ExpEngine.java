package Engine;

import Model.CommonExp;
import Model.ExpOrigin;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpEngine implements Engine {

//    匹配映射
    List<Map<ExpOrigin, ExpOrigin>> matchMap = new ArrayList<Map<ExpOrigin, ExpOrigin>>();

//    变量映射
    List<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>> matchedCase = new ArrayList<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>>();

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
        boolean USED = false;

        for (; iexp1 < expOrigin.getExpCon().size(); iexp1++) {
            for (iexp2 = !USED ? iexp2 : 0; iexp2 < expOrigin1.getExpCon().size(); iexp2++) {
                USED = true;
                if (expOrigin.getExpCon().get(iexp1).getHierarchy().equals(expOrigin1.getExpCon().get(iexp2).getHierarchy())) {
                    if (matchedCase.size() == 0) {
                        matchMap.add(new HashMap<ExpOrigin, ExpOrigin>());
                        matchMap.get(0).put(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                        matchedCase.add(new HashMap<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>());
                    }
                    if (preciseMatch(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2), idx)) {
                        if (matchMap.get(idx).containsKey(expOrigin.getExpCon().get(iexp1))) {
                            Map<ExpOrigin, ExpOrigin> TMP = new HashMap<ExpOrigin, ExpOrigin>(matchMap.get(idx));
                            TMP.remove(expOrigin.getExpCon().get(iexp1));
                            TMP.remove(expOrigin1.getExpCon().get(iexp2));
                            TMP.put(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                            TMP.put(expOrigin1.getExpCon().get(iexp2), expOrigin.getExpCon().get(iexp1));
                            matchMap.add(TMP);
                            RT = RT || matchExp(expOrigin, expOrigin1, iexp1, iexp2 + 1, matchMap.indexOf(TMP));
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
        Pair<ExpOrigin, ExpOrigin> TP = new Pair<ExpOrigin, ExpOrigin>(expOrigin, expOrigin1);
        if (matchedCase.get(idx).size() == 0) {

        }
        Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>> TMP = new HashMap<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>(matchedCase.get(idx));
        boolean FLAG = false;
        if (matchMap.get(idx).containsKey(expOrigin)) {
            FLAG = true;
        }

        boolean RT = preciseMatchs(expOrigin, expOrigin1, 0, 0, 0, TMP.get(new Pair<ExpOrigin, ExpOrigin>(expOrigin, expOrigin1)), null);
        if (RT) {
            if (FLAG) {
                matchedCase.add(TMP);
            } else {
                matchedCase.set(idx, TMP);
            }
        }
        return RT;
    }

//    递归判断Exp是否等价
    public boolean preciseMatchs(ExpOrigin expOrigin, ExpOrigin expOrigin1, int e1, int e2, int iidx, List<Map<ExpOrigin, ExpOrigin>> TMP, Map<ExpOrigin, ExpOrigin> tmatched) {
        if (expOrigin.getExpCon().size() != expOrigin1.getExpCon().size()) {
            return false;
        }
        if (TMP.size() == 0) {
            TMP.add(new HashMap<ExpOrigin, ExpOrigin>());
        }
        if (judgeEqual(expOrigin, expOrigin1, iidx, TMP)) {
            return true;
        }

        boolean USED = false;
        boolean FLAG = false;
        if (tmatched == null) {
            tmatched = new HashMap<ExpOrigin, ExpOrigin>();
        }
        for (; e1 < expOrigin.getExpCon().size(); ++e1) {
            for (e2 = !USED ? e2 : 0 ; e2 < expOrigin1.getExpCon().size(); ++e2) {
                USED = true;

                if (preciseMatchs(expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2), 0, 0, iidx, TMP, null)) {
                    if (expOrigin.getExpCon().get(e1).getHierarchy().equals("Variable")) {
                        if (TMP.get(iidx).containsKey(expOrigin.getExpCon().get(e1))) {
                            Map<ExpOrigin, ExpOrigin> TMPss = new HashMap<ExpOrigin, ExpOrigin>(TMP.get(iidx));
                            Map<ExpOrigin, ExpOrigin> ttmatched = new HashMap<ExpOrigin, ExpOrigin>(tmatched);
                            ttmatched.remove(expOrigin.getExpCon().get(e1));
                            ttmatched.remove(expOrigin.getExpCon().get(e2));
                            ttmatched.put(expOrigin.getExpCon().get(e1), expOrigin.getExpCon().get(e2));
                            ttmatched.put(expOrigin.getExpCon().get(e2), expOrigin.getExpCon().get(e1));
                            TMPss.remove(expOrigin.getExpCon().get(e1));
                            TMPss.remove(expOrigin1.getExpCon().get(e2));
                            TMPss.put(expOrigin.getExpCon().get(e1), expOrigin.getExpCon().get(e2));
                            TMPss.put(expOrigin.getExpCon().get(e2), expOrigin.getExpCon().get(e1));
                            TMP.add(TMPss);
                            FLAG = preciseMatchs(expOrigin, expOrigin1, e1, e2 + 1, TMP.indexOf(TMPss), TMP, ttmatched) || FLAG;
                            continue;
                        }
                        TMP.get(iidx).put(expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                        TMP.get(iidx).put(expOrigin.getExpCon().get(e2), expOrigin1.getExpCon().get(e1));
                    }
                    tmatched.put(expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                    tmatched.put(expOrigin.getExpCon().get(e2), expOrigin1.getExpCon().get(e1));
                }
            }
        }

        if (expOrigin.getExpCon().size() * 2 == tmatched.size() || FLAG) {
            return true;
        }
        return false;
    }

    //判断两个exp是否是相等的变量如果传入的不是两个相等的变量返回false
    public boolean judgeEqual(ExpOrigin expOrigin, ExpOrigin expOrigin1, int iidx, List<Map<ExpOrigin, ExpOrigin>> TMP) {
        if (!expOrigin.getHierarchy().equals("Variable") || !expOrigin.getHierarchy().equals("Variable")) {
            return false;
        }
        if (expOrigin.getOperator().equals(expOrigin1.getOperator()) && TMP.get(iidx).get(expOrigin).equals(expOrigin1)) {
            return true;
        }
        return false;
    }
}
