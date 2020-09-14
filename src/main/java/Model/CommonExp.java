package Model;

import org.junit.Test;

import java.net.CookieHandler;
import java.util.List;

public class CommonExp extends ExpOrigin{

    public CommonExp() {

    }

    public CommonExp(String Exp) {
        this();
        this.Exp = Exp;
        this.Operator = "+";
        paraseExp();
    }

    @Override
    public void simp() {

    }

    @Override
    public void ShowStructure() {
        ShowStructure(this);
    }

    public void ShowStructure(ExpOrigin expOrigin) {
        if (expOrigin.ExpCon.size() == 0) {
            System.out.println(expOrigin.Operator + expOrigin.Exp);
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

    public void add(String Exp) {

    }

    @Override
    public String query(String query) {
        return "";
    }

    @Override
    public void paraseExp() {
//        怎么解析 目前先直接 通过加减乘除后面可以添加；
        for (List<String> ops : this.Operators) {
            if (loadExpLevel(ops)) {
                break;
            }
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

//    按照分句的等级来划分
    public boolean loadExpLevel(List<String> ops) {
//        boolean RT = false;
//        boolean RT = false;
//        boolean SIGNAL = false;
//        int SIDX = 0;
//        String NOWOP = "+";
//        for (int i = 0; i < this.Exp.length(); i++) {
//            for (String op : ops) {
//                if (String.valueOf(this.Exp.charAt(i)).equals(op)) {
////                    i = patternMatch(i);
//                    loadExpCon(SIDX, i, NOWOP);
//                    NOWOP = op;
//                    SIDX = i + 1;
//                    RT = true;
//                }
//            }
//        }
//        if (RT) {
//            loadExpCon(SIDX, this.Exp.length(), NOWOP);
//        }
//        String NOWOP = "+";
//        for (int i = 0; i < this.Exp.length(); ++i) {
//            for (String op : ops) {
//                String CTMP = String.valueOf(this.Exp.charAt(i));
//                if (CTMP.equals(op)) {
//                    if (i == 0) {
//                        NOWOP = CTMP;
//                        i = loadExpCon(NOWOP, i, ops);
//                        continue;
//                    }
//                    RT = true;
//                }
//            }
//        }
        boolean RT = false;
        String NOWOP = getFirstOp(ops);
        int i;
        if (NOWOP.equals("#")) {
            i = 0;
        } else {
            i = matchOps(0, ops);
        }
        while (i < this.Exp.length()) {
            i = loadExpCon(i, NOWOP, ops);
            if (i != -1) {
                RT = true;
            }
        }
        return RT;
    }
//    获取Exp中第一个字符的符号，如果第一个字符前没符号则返回#
    public String getFirstOp(List<String> ops) {
        return "";
    }

//    从IDX开始的位置匹配ops中的符号，如果IDX开始的字符串没有匹配的ops则返回-1
    public int matchOps(int IDX, List<String> ops) {

        return 0;
    }

//    输入符号位置信息，创建下一个对象。
    public int loadExpCon(int SIDX, String NOWOP, List<String> ops) {
//        if (SIDX >= EIDX) {
//            return;
//        }
//        String TmpExp = this.Exp.substring(SIDX, EIDX);
//        ExpOrigin exp = new CommonExp(TmpExp);
//        exp.Operator = NOWOP;
//        this.ExpCon.add(exp);
        int IDX = SIDX + 1;
        for (; IDX < this.Exp.length(); IDX++) {
            for (String op : ops) {
                String CTMP = String.valueOf(this.Exp.charAt(IDX));
                if (op.equals(CTMP)) {

                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return this.Exp;
    }

}
