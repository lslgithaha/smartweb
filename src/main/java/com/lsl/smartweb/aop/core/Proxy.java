package com.lsl.smartweb.aop.core;

import com.lsl.smartweb.aop.proxy.ProxyChain;

public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
