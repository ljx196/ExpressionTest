package Engine;

import Model.ExpOrigin;

import java.util.Map;

public interface Engine {

    public boolean matchExp(String Exp, String Exp1);

    public boolean matchExp(ExpOrigin expOrigin, ExpOrigin expOrigin1);

}
