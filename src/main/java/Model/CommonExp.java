package Model;

import com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader;
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
        boolean FIRST = false;
        String TMP = "";
        String NOWOP = null;
        int i = 0;
        while (i < this.Exp.length()) {
            String TOP = findOP(i);
            if (!TOP.equals("#")) {
                i += TOP.length();
            }
            if (NOWOP == null) {
                NOWOP = TOP;
            }
            if ((ops.contains(TOP) || !(NOWOP.equals("#") && TOP.equals("#"))) && FIRST) {
                loadExpCon(NOWOP, TMP);
                NOWOP = TOP;
                TMP = "";
                FIRST = true;
                RT = true;
            }
            String PAT = findPAT(i);
            TMP += TOP + TMP;
            i = nextPAT(PAT, i);
        }

        return RT;
    }

//    找到IDX为起点的OP 如果没有则返回#
    public String findOP(int IDX) {
        for (List<String> ops : this.Operators) {
            for (String op : ops) {
                if (this.Exp.indexOf(op) == IDX) {
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
            return indexFirstOP(IDX);
        } else {
            int PIT = IDX + ftype.length();
            PIT = matchBracket(PIT, 1);
            return PIT;
        }
    }

//    找到IDX开始第一个OP,如果没有则返回-1
    public int indexFirstOP(int IDX) {
        int MIN = 999;
        for (List<String> ops : this.Operators) {
            for (String op : ops) {
                int TMP = this.Exp.indexOf(op, IDX);
                if (TMP != -1) {
                    MIN = minvs(MIN, TMP);
                }
            }
        }
        return MIN;
    }

//    返回两个参数中最小值
    public int minvs(int F, int S) {
        return F > S ? S : F;
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
