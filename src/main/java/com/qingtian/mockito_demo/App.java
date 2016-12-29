package com.qingtian.mockito_demo;

import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		final List<String> myMockList1 = (List<String>) MyMockito.getInterceptor().getInstance(List.class);
		final List<String> myMockList2 = (List<String>) MyMockito.getInterceptor().getInstance(List.class);
		final Map<Integer, String> myMockMap = (Map<Integer, String>) MyMockito.getInterceptor().getInstance(Map.class);

		MyMockito.when(myMockList1.get(0)).thenReturn("Hello, I am James");
		MyMockito.when(myMockList1.get(2)).thenReturn("Hello, I am Billy");
		MyMockito.when(myMockList2.get(0)).thenReturn("Hello, I am Tom");
		MyMockito.when(myMockMap.get(10)).thenReturn("Hello, I am Bob");
		System.out.println("myMockList1.get(0) = " + myMockList1.get(0));
		System.out.println("myMockList1.get(2) = " + myMockList1.get(2));
		System.out.println("myMockList2.get(0) = " + myMockList2.get(0));
		System.out.println("myMockMap.get(10) = " + myMockMap.get(10));
	}
}
