package Model;

public class Function {
    private FunctionType functionType;
    private String function;
    private String target;

    public Function(FunctionType functionType, String function, String target) {
        this.functionType = functionType;
        this.function = function;
        this.target = target;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public String getFunction() {
        return function;
    }

    public String getTarget() {
        return target;
    }
}

enum FunctionType{
    OnDeath,
    OnSpawn,
    OnAttack,
    OnDefend,
    Passive,
    Combo
}
