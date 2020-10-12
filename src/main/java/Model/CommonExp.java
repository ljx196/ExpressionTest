package Model;

import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

public class CommonExp extends ExpOrigin{

    public CommonExp() {

    }

    public CommonExp(String Exp) {
        this();
        this.Exp = Exp;
        this.Operator = "#e?x?p";
        if (!paraseExp()) {
            if (isInteger(this.Exp)) {
                this.Type = "Constant";
            } else{
                this.Type = "Variable";
            }
            this.Hierarchy = "Variable";
        } else {
            this.Type = "Express";
        }
        collectVariables(this);
    }

    @Override
    public void simp() {

    }

    @Override
    public void ShowStructure() {
        ShowStructure(this);
    }

    public void ShowPlainStru() {
        for (ExpOrigin exp: this.ExpCon) {
            System.out.print(exp.Operator.replaceAll("e\\?x\\?p", exp.Exp) + " ");
        }
        System.out.println();
    }

    public void ShowVariables() {
        for (String v : this.Variables) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    public void ShowStructure(ExpOrigin expOrigin) {
        if (expOrigin.ExpCon.size() == 0) {
            System.out.println(expOrigin.Operator.replaceAll("e\\?x\\?p", expOrigin.Exp));
            return;
        }
        for (ExpOrigin expO : expOrigin.ExpCon) {
            ShowStructure(expO);
        }
    }

    @Override
    public void add(ExpOrigin Exp) {

    }

    @Override
    public void substract(ExpOrigin Exp) {

    }

    @Override
    public void plus(ExpOrigin Exp) {

    }

    @Override
    public void divide(ExpOrigin Exp) {

    }

    @Override
    public boolean contain(String Exp) {

        return false;
    }

    @Override
    public String query(String query) {
        return "";
    }

    @Override
    public String getOperator() {
        return Operator;
    }

    @Override
    public boolean paraseExp() {
//        怎么解析 目前先直接 通过加减乘除后面可以添加；
        boolean RT = false;
        for (List<String> ops : this.Operators) {
            if (loadExpLevel(ops)) {
                this.Hierarchy = this.Hierarchys.get(this.Operators.indexOf(ops));
                RT = true;
                break;
            }
        }
        return RT;
    }

    public void collectVariables(ExpOrigin expOrigin) {
        if (expOrigin.Type != null && expOrigin.Type.equals("Variable")) {
            if (!this.Variables.contains(expOrigin.Exp)) {
                this.Variables.add(expOrigin.Exp);
            }
            return ;
        }
        for (ExpOrigin expOrigin1 : expOrigin.ExpCon) {
            collectVariables(expOrigin1);
        }
    }

//    从SIDX的位置开始，寻找this.Exp中第一个存在符号的位置
    public String getOperator(int SIDX) {
        for (int i = SIDX; i < this.Exp.length(); ++i) {
            for (List<String> ops : this.Operators) {
                for (String op : ops) {
                    String ExpChar = String.valueOf(this.Exp.charAt(i));
                    if (ExpChar.equals(op)) {
                        return ExpChar;
                    }
                }
            }
        }
        return "+";
    }

//    按照分句的等级来划分 如果有操作匹配上了则返回true
    public boolean loadExpLevel(List<String> ops) {
        boolean RT = false;
        boolean FIRST = false;
        String TMP = "";
        String NOWOP = null;
        int i = 0;
        while (i <= this.Exp.length()) {
            String TOP = findOP(i);
            if (!TOP.equals("#")) {
                i += TOP.length();
            }
            if (NOWOP == null) {
                NOWOP = TOP;
//                TMP = TOP;
                TOP = "";
            }
            if (NOWOP.equals("#") && TOP.equals("#") || !ops.contains(NOWOP) && TOP.equals("#")) {
                break;
            }
            if (ops.contains(NOWOP) && !RT && TOP.equals("#")) {
                break;
            }
            if ((ops.contains(TOP) || TOP.equals("#")) && FIRST) {
                loadExpCon(NOWOP, TMP, ops);
                NOWOP = TOP;
                if (TOP.equals("#")) {
                    break;
                }
                TMP = "";
                TOP = "";
                RT = true;
            }
            String PAT = findPAT(i);
            TMP += TOP + PAT;
            i += PAT.length();
            FIRST = true;
        }

        return RT;
    }

//    找到IDX为起点的OP 如果没有则返回#
    public String findOP(int IDX) {
        for (List<String> ops : this.Operators) {
            for (String op : ops) {
                if (this.Exp.indexOf(op, IDX) == IDX) {
                    return op;
                }
            }
        }
        return "#";
    }

//    找到IDX起点的TYPE
    public String findPAT(int IDX) {
        int EIDX;
        for (String type : this.Patterns) {
            String ftype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$1");
            String ltype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$2");
            if (this.Exp.indexOf(ftype, IDX) != IDX) {
                continue;
            }
            EIDX = matchPattern(IDX, type);
            return this.Exp.substring(IDX, EIDX);
        }
        return null;
    }

//  匹配Pattern数据输入索引 Pattern类型，然后返回下一个OP的IDX
    public int matchPattern(int IDX, String type) {
        String ftype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$1");
        String ltype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$2");
        if (type.indexOf("(") == -1) {
            int TMP = indexFirstOP(IDX);
            if (TMP == -1) {
                return this.Exp.length();
            }
            return TMP;
        } else {
            int PIT = IDX + ftype.length();
            PIT = matchBracket(PIT, 1);
            return PIT;
        }
    }

//    找到IDX开始第一个OP,如果没有则返回-1
    public int indexFirstOP(int IDX) {
        int MIN = 9999;
        for (List<String> ops : this.Operators) {
            for (String op : ops) {
                int TMP = this.Exp.indexOf(op, IDX);
                if (TMP != -1) {
                    MIN = Math.min(MIN, TMP);
                }
            }
        }
        if (MIN == 9999) {
            return -1;
        } else {
            return MIN;
        }
    }

//  括号匹配
    public int matchBracket(int PIT, int count) {
        while (count != 0) {
            String TMP = String.valueOf(this.Exp.charAt(PIT));
            if (TMP.equals("(")) {
                count++;
            }
            if (TMP.equals(")")) {
                count--;
            }
            PIT++;
        }
        return PIT;
    }

//    输入符号位置信息，创建下一个对象。
    public void loadExpCon(String OP, String PAT, List<String> ops) {
        String Exp = "";
        String pat = "";
        for (String type : this.Patterns) {
            String ftype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$1");
            String ltype = type.replaceAll("(.*)e\\?x\\?p(.*)", "$2");
            if (PAT.indexOf(ftype) == 0) {
                String TPAT = type.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("e\\?x\\?p", "(.*)");
                Exp = PAT.replaceAll(TPAT, "$1");
                pat = type;
                break;
            }
        }
        if (OP.equals("+") || OP.equals("-")) {
            Exp = OP + Exp;
        }
        ExpOrigin expOrigin = new CommonExp(Exp);
        expOrigin.Operator = OP + pat;
        this.ExpCon.add(expOrigin);
    }

    @Override
    public String toString() {
        return this.Operator.replaceAll("e\\?x\\?p", this.Exp);
    }

    @Override
    public String getHierarchy() {
        return this.Hierarchy;
    }

    @Override
    public List<ExpOrigin> getExpCon() {
        return this.ExpCon;
    }

    @Override
    public String getType() {
        return this.Type;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
