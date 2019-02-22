package com.yun.rpc.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 18:13
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -8954519833833364500L;
    private String className;
    private String methodName;
    private Object[] parameters;
    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                ", version='" + version + '\'' +
                '}';
    }
}
