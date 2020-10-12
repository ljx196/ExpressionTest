package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//所有表达式的基础父类
public abstract class ExpOrigin {

    List<ExpOrigin> ExpCon = new ArrayList<ExpOrigin>();

    List<String> Variables = new ArrayList<String>();

    String Exp;

    List<List<String>> Operators = Arrays.asList(Arrays.asList("=", ">=", "<=", ">", "<"),
        Arrays.asList("+", "-"),
        Arrays.asList("*", "/"),
        Arrays.asList("^", "_"));

    List<String> Hierarchys = Arrays.asList("relation", "priorityoper", "oper", "orientation", "Variable", "Constant");

    List<String> Patterns = Arrays.asList("sin(e?x?p)", "cos(e?x?p)", "tan(e?x?p)", "(e?x?p)", "e?x?p");

    String Operator;

    String Type;

    String Hierarchy;

    abstract public void simp();

    abstract public void ShowStructure();

    abstract public void add(ExpOrigin Exp);

    abstract public void substract(ExpOrigin Exp);

    abstract public void plus(ExpOrigin Exp);

    abstract public void divide(ExpOrigin Exp);

    abstract public String query(String query);

    abstract public boolean contain(String Exp);

    abstract public boolean paraseExp();

    abstract public String getHierarchy();

    abstract public List<ExpOrigin> getExpCon();

    abstract public String getType();

    abstract public String getOperator();

}
