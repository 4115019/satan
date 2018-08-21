//package com.satan.tools.distribute.lock;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.stereotype.Component;
//import java.lang.reflect.Method;
//
//
///**
// * Created by xiaozhujun on 15/10/9.
// */
//@Aspect
//@Component
//@Slf4j
//public class DistributeLockAspect {
//
//    @Autowired
//    RedissonClient redissonClient;
//
//    @Pointcut(value="execution(public * *(..))")
//    public void anyPublicMethod() {
//    }
//
//    @Around("anyPublicMethod() && @annotation(distributeLock)")
//    public Object redisLock(ProceedingJoinPoint pjp, DistributeLock distributeLock) throws Throwable {
//        Object result = null;
//
//        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
//        //得到参数上下文
//		ParameterEvaluationContext evaluationContext = new ParameterEvaluationContext(new LocalVariableTableParameterNameDiscoverer(),methodSignature.getMethod(), pjp.getArgs(), pjp.getTarget().getClass());
//
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression exp = parser.parseExpression(distributeLock.key());
//        //springEL计算表达式得到key
//        String keyValue = (String) exp.getValue(evaluationContext);
//        log.info("redisLock() keyvalue={}",keyValue);
//
//
//        if(StringUtils.isBlank(keyValue)) {
//            result = pjp.proceed();
//            logMethod("lockKey is null",pjp);
//            return result;
//        }
//
//        log.info("{}.{}({}) lock with key : {}",
//                pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), StringUtils.join(pjp.getArgs(), ","), keyValue);
//
//        RLock lock = null;
//        try{
//            lock = redissonClient.getLock(keyValue);
//            lock.lock();
//
//            result = pjp.proceed();
//
//        }catch (Exception e){
//            log.error("处理结果失败",e);
//            throw e;
//        }finally {
//            try {
//                if (lock != null) {
//                    lock.unlock();
//                }
//            }catch (Exception e){
//                log.error("解锁失败",e);
//            }
//        }
//
//        return result;
//    }
//
//    public Object getFiledByMethod(String method,Object obj){
//        try{
//            Method methodObj = obj.getClass().getMethod(method, new Class[] {});
//            Object value = methodObj.invoke(obj,new Object[] {});
//            return value;
//            }
//        catch(Exception e)
//        {
//            return null;
//        }
//    }
//
//    private String getField(ProceedingJoinPoint pjp,String filed){
//        if(StringUtils.isBlank(filed)) return null;
//        Object[] params = pjp.getArgs();
//        if(!filed.contains(".")){
//            for(Object o:params){
//                if(o.getClass().getName().equals(filed)){
//                    return filed.toString();
//                }
//            }
//        }
//        else{
//            String[] keyInfo = filed.split("\\.");
//            for(Object o:params){
//                if(o.getClass().getSimpleName().equals(keyInfo[0])){
//                    Object keyO = getFiledByMethod(keyInfo[1], o);
//                    if(keyO!=null) return keyO.toString();
//                }
//            }
//        }
//        return null;
//    }
//
//    private void logMethod(String message,ProceedingJoinPoint pjp){
//        //方法参数类型，转换成简单类型
//        log.error("{}.{}({}) lock fail for: {}",
//                pjp.getTarget().getClass().getName(),pjp.getSignature().getName(), StringUtils.join(pjp.getArgs(), ","),message);
//    }
//}
