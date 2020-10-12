package Engine;

import Model.ExpOrigin;

import java.util.Map;

public interface Engine {

    boolean matchExp(ExpOrigin expOrigin, ExpOrigin expOrigin1, int iexp1, int iexp2, int idx);

    boolean Match(String Exp, String Exp1);

}
