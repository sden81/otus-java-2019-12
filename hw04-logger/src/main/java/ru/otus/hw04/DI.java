package ru.otus.hw04;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

public class DI {
    public OriginalInterface createOriginal(){
        ClassLoader classLoader = Original.class.getClassLoader();
        Class<?>[] interfaces = Original.class.getInterfaces();
        CustomInvocationHandler invocationHandler = new CustomInvocationHandler(new Original());

        return (OriginalInterface) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    }

    class CustomInvocationHandler implements InvocationHandler
    {
        private Original original;
        private ArrayList<String> methodsNeedLogging = new ArrayList<>();

        /**
         * @param original
         */
        public CustomInvocationHandler(Original original) {

            this.original = original;

            Class thisClass = Original.class;
            Method[] methods = thisClass.getDeclaredMethods();
            for (Method method : methods){
                if (method.getAnnotation(Log.class) instanceof Log){
                    methodsNeedLogging.add(getMethodSignature(method));
                }
            }
        }

        /**
         * @param proxy
         * @param method
         * @param args
         * @return
         * @throws Throwable
         */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            if (methodsNeedLogging.contains(getMethodSignature(method))){
                logging(method.getName());
            }

            return method.invoke(original, args);
        }

        /**
         * @param methodName
         */
        protected void logging(String methodName){
            System.out.println(String.format("Logging '%s' method", methodName));
        }

        /**
         * @param method
         * @return
         */
        protected String getMethodSignature(Method method){
            String params = "";
            for (Parameter parameter : method.getParameters()){
                params += parameter.toString();
            }
            String signature = String.format("%s|%s|%s",
                    method.getName().toString(),
                    params,
                    method.getReturnType().toString()
            );

            return signature;
        }
    }
}
