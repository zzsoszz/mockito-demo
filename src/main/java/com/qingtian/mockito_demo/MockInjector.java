package com.qingtian.mockito_demo;

import lombok.Data;

@Data
class MockInjector {
	private final MethodInfo methodInfo;
	public void thenReturn(final Object mockResult) {
		MyMockito.MOCKED_METHODS.put(methodInfo, mockResult);
	}
}