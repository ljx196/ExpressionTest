package Engine;

import Model.CommonExp;
import Model.ExpOrigin;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//todo 过滤不满足条件的映射 ✔
//todo 加强变量匹配规则 x^2 x_1 ✔
//todo 表达式搜索匹配， 类似于a^e+c e^f是匹配的 ✔
//todo 表达式转换
//todo 增加表达式合并同类项等功能
//todo 重写解析ExpOrigin
//todo 完善边界条件 >= 和 +- /*等是不同的
//todo 兼容f(x) g(x) e(g)等形式
//todo 兼容特殊形式例如数列那边
//todo 兼容不完全匹配例如 a*x^2+c 与 a*x^2+d*x+c 是能匹配上的此时d=0
//todo 兼容数列的情况例如T_n=a_1*a_2\cdots a_n需要兼容拿到里面的信息，连乘连加
public class ExpEngine implements Engine {

//    匹配映射
    List<Map<ExpOrigin, ExpOrigin>> matchMap = new ArrayList<Map<ExpOrigin, ExpOrigin>>();

//    变量映射
    List<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>> matchedCase = new ArrayList<Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>>();

    public boolean Match(String Exp, String Exp1) {
        ExpOrigin expOrigin = new CommonExp(Exp);
        ExpOrigin expOrigin1 = new CommonExp(Exp1);

        boolean RT = matchExp(expOrigin, expOrigin1, 0, 0, 0) || searchMatch(expOrigin, expOrigin1);

        filteMap(expOrigin, expOrigin1);

        return RT;
    }

    public boolean searchMatch(ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        for (ExpOrigin e : expOrigin.getExpCon()) {
            clearMap();
            if (matchExp(e, expOrigin1, 0, 0, 0)) {
                return true;
            } else if (e.getExpCon().size() != 0) {
                if (searchMatch(e, expOrigin1)) {
                    return true;
                }
            }
        }

        return false;
    }

//    第一步，先匹配type，若type相同，则说明这两个Exp处于同一个层级，接下来匹配子串，在同层的式子可以部分匹配，但是同层之下的必须精确匹配，即匹配上的式子环境应该一致
//    匹配之后需要同步变量，a*x+b与d*c+e中各变量的映射，a->d,c->x,e->d或者a->c,x->d,b->e;
    public boolean matchExp(ExpOrigin expOrigin, ExpOrigin expOrigin1, int iexp1, int iexp2, int idx) {
        if (!expOrigin.getHierarchy().equals(expOrigin1.getHierarchy())) {
            return false;
        }

        boolean RT = false;
        boolean USED = false;

        if (matchedCase.size() == 0) {
            matchMap.add(new HashMap<ExpOrigin, ExpOrigin>());
            matchedCase.add(new HashMap<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>());
        }

        for (; iexp1 < expOrigin.getExpCon().size(); iexp1++) {
            if (iexp2 >= expOrigin1.getExpCon().size() && !USED) {
                USED = true;
                continue;
            }
            for (iexp2 = !USED ? iexp2 : 0; iexp2 < expOrigin1.getExpCon().size(); iexp2++) {
                USED = true;
                if (expOrigin.getExpCon().get(iexp1).getHierarchy().equals(expOrigin1.getExpCon().get(iexp2).getHierarchy())) {
                    if (preciseMatch(expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2), idx)) {
                        if (judgeIsHave(matchMap.get(idx), expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2))) {
                            Map<ExpOrigin, ExpOrigin> TMP = new HashMap<ExpOrigin, ExpOrigin>(matchMap.get(idx));
                            if (matchMap.get(idx).containsKey(expOrigin.getExpCon().get(iexp1))) {
                                removeConnected(TMP, expOrigin.getExpCon().get(iexp1));
                            }
                            if (matchMap.get(idx).containsKey(expOrigin1.getExpCon().get(iexp2))) {
                                removeConnected(TMP, expOrigin1.getExpCon().get(iexp2));
                            }
                            matchMap.add(TMP);
                            addConnected(TMP, expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                            RT = matchExp(expOrigin, expOrigin1, iexp1, iexp2 + 1, matchMap.indexOf(TMP)) || RT;
                            continue;
                        }
                        addConnected(matchMap.get(idx), expOrigin.getExpCon().get(iexp1), expOrigin1.getExpCon().get(iexp2));
                    }
                }
            }
        }

        return matchMap.get(idx).size() == 2 * Math.min(expOrigin.getExpCon().size(), expOrigin1.getExpCon().size()) || RT;
    }

//    匹配同一层级的表达式，要求精确匹配，并且需要返回对应的变量
    public boolean preciseMatch(ExpOrigin expOrigin, ExpOrigin expOrigin1, int idx) {
        if (expOrigin.getExpCon().size() != expOrigin1.getExpCon().size()) {
            return false;
        }

        Pair<ExpOrigin, ExpOrigin> TP = new Pair<ExpOrigin, ExpOrigin>(expOrigin, expOrigin1);
        Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>> TMP = new HashMap<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>>(matchedCase.get(idx));

        boolean FLAG = false;
        if (judgeIsHave(matchMap.get(idx), expOrigin, expOrigin1)) {
            removeMap(TMP, TP);
            TMP.put(TP, new ArrayList<Map<ExpOrigin, ExpOrigin>>());
            FLAG = true;
        }

        if (TMP.get(TP) == null) {
            TMP.put(TP, new ArrayList<Map<ExpOrigin, ExpOrigin>>());
        }

        boolean RT = preciseMatchs(expOrigin, expOrigin1, 0, 0, 0, TMP.get(TP), null);

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
            TMP.add(new HashMap<>());
        }

        boolean USED = false;
        boolean FLAG = false;

        if (tmatched == null) {
            tmatched = new HashMap<>();
        }

        if (judgeEqual(expOrigin, expOrigin1, iidx, TMP)) {
            addConnected(TMP.get(iidx), expOrigin, expOrigin1);
            return true;
        }

        for (; e1 < expOrigin.getExpCon().size(); ++e1) {
            if (e2 >= expOrigin1.getExpCon().size() && !USED) {
                USED = true;
                continue;
            }
            for (e2 = !USED ? e2 : 0 ; e2 < expOrigin1.getExpCon().size(); ++e2) {
                USED = true;
                if (judgeEqual(expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2), iidx, TMP) || preciseMatchs(expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2), 0, 0, iidx, TMP, null)) {
                    if (judgeIsHave(TMP.get(iidx), expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2)) || judgeIsHave(tmatched, expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2))) {
                        Map<ExpOrigin, ExpOrigin> TMPss = new HashMap<ExpOrigin, ExpOrigin>(TMP.get(iidx));
                        Map<ExpOrigin, ExpOrigin> ttmatched = new HashMap<>(tmatched);
                        if (expOrigin.getExpCon().get(e1).getHierarchy().equals("Variable")) {
                            constructNewMatch(TMPss, expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                            TMP.add(TMPss);
                        }
                        constructNewMatch(ttmatched, expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                        FLAG = preciseMatchs(expOrigin, expOrigin1, e1, e2 + 1, TMP.indexOf(TMPss), TMP, ttmatched) || FLAG;
                        continue;
                    }
                    if (expOrigin.getExpCon().get(e1).getHierarchy().equals("Variable")) {
                        addConnected(TMP.get(iidx), expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                    }
                    addConnected(tmatched, expOrigin.getExpCon().get(e1), expOrigin1.getExpCon().get(e2));
                }
            }
        }
        return (expOrigin.getExpCon().size() * 2 == tmatched.size() || FLAG) && expOrigin.getExpCon().size() != 0;
    }

    //判断两个exp是否是相等的变量如果传入的不是两个相等的变量返回false
    private boolean judgeEqual(ExpOrigin expOrigin, ExpOrigin expOrigin1, int iidx, List<Map<ExpOrigin, ExpOrigin>> TMP) {
        if (!expOrigin.getHierarchy().equals("Variable") || !expOrigin.getHierarchy().equals("Variable")) {
            return false;
        }
        if (judgeOper(expOrigin, expOrigin1)) {
//            if (TMP.get(iidx).get(expOrigin) == null || TMP.get(iidx).get(expOrigin).equals(expOrigin1)) {
//                return true;
//            }
            return true;
        }
        return false;
    }

//    去掉TMP中含有TP的部分
    private void removeMap(Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>> TMP, Pair<ExpOrigin, ExpOrigin> TP) {
        List<Pair<ExpOrigin, ExpOrigin>> LTMP = new ArrayList<Pair<ExpOrigin, ExpOrigin>>();
        for (Pair<ExpOrigin, ExpOrigin> PAIR : TMP.keySet()) {
            if (PAIR.getKey() == TP.getKey() || PAIR.getKey() == TP.getValue() || PAIR.getValue() == TP.getKey() || PAIR.getValue() == TP.getValue()) {
                LTMP.add(PAIR);
            }
        }
        for (Pair<ExpOrigin, ExpOrigin> PAIR : LTMP) {
            TMP.remove(PAIR);
        }
    }

    //移除现有的映射
    private void removeConnected(Map<ExpOrigin, ExpOrigin> TMP, ExpOrigin expOrigin) {
        TMP.remove(TMP.get(expOrigin));
        TMP.remove(expOrigin);
    }
//添加映射
    private void addConnected(Map<ExpOrigin, ExpOrigin> TMP, ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        TMP.put(expOrigin, expOrigin1);
        TMP.put(expOrigin1, expOrigin);
    }

//    重构一个映射
    private void constructNewMatch(Map<ExpOrigin, ExpOrigin> TMP, ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        removeConnected(TMP, expOrigin);
        removeConnected(TMP, expOrigin1);
        addConnected(TMP, expOrigin, expOrigin1);
    }

    private boolean judgeIsHave(Map<ExpOrigin, ExpOrigin> TMP, ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        return TMP.containsKey(expOrigin) || TMP.containsKey(expOrigin1);
    }

    //判断两个变量是否相等
    private boolean judgeOper(ExpOrigin o1, ExpOrigin o2) {
        String so1 = o1.getOperator().replaceAll("(.*)e\\?x\\?p(.*)", "$1$2").replace("#", "+");
        String so2 = o2.getOperator().replaceAll("(.*)e\\?x\\?p(.*)", "$1$2").replace("#", "+");
        if (o1.getType().equals("Constant")) {
            if (o1.toString().equals(o2.toString())) {
                return true;
            } else {
                return false;
            }
        }
        if (so1.equals(so2)) {
            return true;
        }
        return false;
    }

    private void filteMap(ExpOrigin expOrigin, ExpOrigin expOrigin1) {
        matchMap.removeIf(map -> {
           return map.size() != expOrigin1.getExpCon().size() * 2;
        });
        matchedCase.removeIf(map -> {
           return map.size() != expOrigin1.getExpCon().size();
        });
        for (Map<Pair<ExpOrigin, ExpOrigin>, List<Map<ExpOrigin, ExpOrigin>>> mp : matchedCase) {
            for (Pair<ExpOrigin, ExpOrigin> p : mp.keySet()) {
                int minSize = Math.max(Math.min(p.getKey().getExpCon().size(), p.getValue().getExpCon().size()), 1);
                mp.get(p).removeIf(l -> {
                   return l.size() != minSize * 2;
                });
            }
        }
    }

    private void clearMap() {
        matchedCase.clear();
        matchMap.clear();
    }
}
