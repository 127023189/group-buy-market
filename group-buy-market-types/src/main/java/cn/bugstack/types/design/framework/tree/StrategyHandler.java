package cn.bugstack.types.design.framework.tree;

public interface StrategyHandler <T,D,R>{
    /**
     * 泛型 T - 入参、D - 上下文、R - 返参
     */
    StrategyHandler DEFAULT = (T, D) -> null;

    R apply(T requestParameter, D dynamicContext) throws Exception;
}
