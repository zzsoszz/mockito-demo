package com.qingtian.mockito_demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMockito {
	public static final Map<MethodInfo, Object> MOCKED_METHODS = new HashMap<MethodInfo, Object>();

	public static MockInjector when(Object methodCall) {
		return new MockInjector((MethodInfo) methodCall);
	}

	public static MyCGLibInterceptor getInterceptor() {
		return new MyCGLibInterceptor();
	}
}