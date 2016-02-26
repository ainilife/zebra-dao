package com.dianping.zebra.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class ProxyTest {

	@SuppressWarnings("unchecked")
   public static Set<String> getSetProxy1(final Set<String> s) {
		return (Set<String>) Proxy.newProxyInstance(s.getClass().getClassLoader(), new Class[] { Set.class },
		      new InvocationHandler() {
			      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			      	System.out.println("invoke1 "  + method.getName());
				      return method.invoke(s, args);
			      }
		      });
	}
	
	@SuppressWarnings("unchecked")
   public static Set<String> getSetProxy2(final Set<String> s) {
		return (Set<String>) Proxy.newProxyInstance(s.getClass().getClassLoader(), new Class[] { Set.class },
		      new InvocationHandler() {
			      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			      	System.out.println("invoke2 "  + method.getName());
				      return method.invoke(s, args);
			      }
		      });
	}
	
	public static class InvocationHandler1 implements InvocationHandler{

		private final Set<String> s;
	
		private InvocationHandler1(final Set<String> s){
			this.s = s;
		}
		@Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("invoke1 "  + method.getName());
	      return method.invoke(s, args);
		}
		
	}

	public static void main(String[] args) {
	   Set<String> set = new HashSet<String>();
	   
	   set = getSetProxy1(set);
	   set = getSetProxy2(set);
	   
	   set.add("init");
   }

}
