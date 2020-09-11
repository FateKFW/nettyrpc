package org.rdz.common.struct;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
@ToString
public class RPCStruct implements Serializable {
    private static final long serialVersionUID = -2160822461099422891L;

    //唯一消息ID
    private String requestID;

    //需要调用的接口
    private String className;

    //调用方法
    private String methodName;

    //参数类型
    private Class<?>[] parameterType;

    //参数值
    private Object[] parameterValues;

    //返回结果
    private Object result;
}