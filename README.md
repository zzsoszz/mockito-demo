# mockito-demo
http://blog.csdn.net/jamesdoctor/article/details/50019103


mockito-demo
 Mockito实现原理探析 -- Mockito.when(...).thenReturn(...)的一个简化实现
标签： 编程java单元测试测试框架



        Mockito是一套非常强大的测试框架，被广泛的应用于Java程序的unit test中，而在其中被使用频率最高的恐怕要数"Mockito.when(...).thenReturn(...)"了。那这个用起来非常便捷的"Mockito.when(...).thenReturn(...)"，其背后的实现原理究竟为何呢？为了一探究竟，笔者实现了一个简单的MyMockito，也提供了类似的"MyMockito.when(...).thenReturn(...)"支持。当然这个实现只是一个简单的原型，完备性和健壮性上都肯定远远不及mockito（比如没有annotation支持和thread safe），但应该足以帮助大家理解"Mockito.when(...).thenReturn(...)"的核心实现原理。

       在阅读以下代码之前，希望读者能简单的了解一下Java动态代理和cglib，没接触过的至少应该写两个简单的小程序感受一下（这篇博文中就提供了一些简短的示例）。cglib是一个强大的高性能的代码生成包，被许多AOP的框架所使用，Mockito也使用了这个库，我们的MyMockito自然也不例外。


P.S.:

示例代码中还使用了lombok，省去了构造函数的编写。


[java] view plain copy
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
            final MethodInfo otherMethodInfo = (MethodInfo)other;  
            return interceptor.equals(otherMethodInfo.interceptor) && method.equals(otherMethodInfo.method) && Arrays.equals(args, otherMethodInfo.args);  
        }  
          
        return false;  
    }  
      
    @Override  
    public int hashCode() {  
        return interceptor.hashCode() + method.hashCode() + Arrays.hashCode(args);  
    }  
}  
  
@Data  
class MockInjector {  
    private final MethodInfo methodInfo;  
    public void thenReturn(final Object mockResult) {  
        MyMockito.MOCKED_METHODS.put(methodInfo, mockResult);  
    }  
}  
  
class MyCGLibInterceptor implements MethodInterceptor {  
    @Override  
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {          
        final MethodInfo key = new MethodInfo(this, method, args);  
        final boolean hasMocked = MyMockito.MOCKED_METHODS.containsKey(key);  
        if (!hasMocked) {  
            // When called for the first time (by MyMockito.when(...)),  
            // return a MethodInfo object used as key,  
            // so that the later MethodInfo.thenReturn(...) will use this key  
            // to insert mock result into the MyMockito.MOCKED_METHODS.  
            System.out.println("Initializing the mock for " + key.toString());  
            return key;  
        } else {  
            // Now that MyMockito.MOCKED_METHODS already contains the mock result  
            // for this method call, just return the mock result.  
            System.out.println("Returns the mock result:");  
            return MyMockito.MOCKED_METHODS.get(key);  
        }  
    }  
      
    public Object getInstance(final Class<?> t) {  
        final Enhancer enhancer = new Enhancer();    
        enhancer.setSuperclass(t);    
        enhancer.setCallback(this);    
        return enhancer.create();  
    }  
}  
  
public class MyMockito {  
    public static final Map<MethodInfo, Object> MOCKED_METHODS = new HashMap<MethodInfo, Object>();  
      
    public static MockInjector when(Object methodCall) {  
        return new MockInjector((MethodInfo)methodCall);  
    }  
      
    private static MyCGLibInterceptor getInterceptor() {  
        return new MyCGLibInterceptor();  
    }  
  
    public static void main(String[] args) {  
        final List<String> myMockList1 =   
                (List<String>)getInterceptor().getInstance(List.class);   
        final List<String> myMockList2 =   
                (List<String>)getInterceptor().getInstance(List.class);   
        final Map<Integer, String> myMockMap =   
                (Map<Integer, String>)getInterceptor().getInstance(Map.class);   
          
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
