package com.qingtian.mockito_demo;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Data
class MethodInfo {
	private final MyCGLibInterceptor interceptor;
	private final Method method;
	private final Object[] args;

	@Override
	public String toString() {
		return "{interceptor: " + interceptor + ", Method: " + method + ", args: " + Arrays.toString(args) + "}";
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof MethodInfo) {
			final MethodInfo otherMethodInfo = (MethodInfo) other;
			return interceptor.equals(otherMethodInfo.interceptor) && method.equals(otherMethodInfo.method)
					&& Arrays.equals(args, otherMethodInfo.args);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return interceptor.hashCode() + method.hashCode() + Arrays.hashCode(args);
	}
}
