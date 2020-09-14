package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//所有表达式的基础父类
public abstract class ExpOrigin {

    List<ExpOrigin> ExpCon = new ArrayList<ExpOrigin>();

    String Exp;

//    List<String> Operators = new ArrayList<String>(Arrays.asList("+", "-", "*", "/"));
    List<List<String>> Operators = new ArrayList<List<String>>(Arrays.asList(Arrays.asList("+", "-"),
        Arrays.asList("*", "/")));

    List<String> Patterns = new ArrayList<String>(Arrays.asList("sin(e?x?p)", "cos(e?x?p)", "tan(e?x?p)", "(e?x?p)", "e?x?p"));

    String Operator;

    String Type;

    abstract public void simp();

    abstract public void ShowStructure();

    abstract public void add(ExpOrigin Exp);

    abstract public void substract(ExpOrigin Exp);

    abstract public void plus(ExpOrigin Exp);

    abstract public void divide(ExpOrigin Exp);

    abstract public String query(String query);

    abstract public void paraseExp();

}
