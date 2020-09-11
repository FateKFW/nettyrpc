package org.rdz.netty.client.inter;

import org.rdz.common.struct.RPCStruct;

@FunctionalInterface
public interface RequestIDCreator {
    String getRequestID(RPCStruct struct);
}
