package Model;

import org.junit.Test;

import java.net.CookieHandler;
import java.util.List;
import java.util.TimerTask;

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

//    按照分句的等级来划分 如果有操作匹配上了则返回true
    public boolean loadExpLevel(List<String> ops) {
        boolean RT = false;
        boolean FIRTST = true;
        int i = 0;
        while (i < this.Exp.length()) {
            String TOP = findOP(i);
            if (!TOP.equals("#")) {
                i += TOP.length();
            }
            String PAT = findPAT(i);
            i = nextPAT(PAT, i);
            if (FIRTST) {
                FIRTST = false;
                continue;
            }
            if (ops.contains(TOP)) {
                loadExpCon(TOP, PAT);
            }
        }

        return RT;
    }

//    找到IDX为起点的OP 如果没有则返回
    public String findOP(int IDX) {
        for (List<String> ops : this.Operators) {
            for (String op : ops) {
                if (this.Exp.indexOf(op) == IDX) {
                    return op;
                }
            }
        }
        return null;
    }

//    找到IDX起点的TYPE
    public String findPAT(int IDX) {
        for (String type : this.Patterns) {

        }
        return null;
    }

//    以IDX为起点的，找到下一个OP的位置
    public int nextPAT(String TYPE, int IDX) {

        return 0;
    }

//    输入符号位置信息，创建下一个对象。
    public void loadExpCon(String OP, String TTYPE) {
    }

    @Override
    public String toString() {
        return this.Exp;
    }

}
