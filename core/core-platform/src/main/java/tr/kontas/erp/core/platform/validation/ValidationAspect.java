package tr.kontas.erp.core.platform.validation;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import tr.kontas.fluentvalidation.annotations.Validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(com.netflix.graphql.dgs.DgsMutation)")
    public Object aroundDgsMutation(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        for (Object arg : args) {
            if (arg == null) continue;

            if (arg instanceof DataFetchingEnvironment) continue;

            Class<?> argClass = arg.getClass();
            if (argClass.isPrimitive() || argClass == String.class || Number.class.isAssignableFrom(argClass)
                    || Boolean.class.isAssignableFrom(argClass)) {
                continue;
            }

            try {
                Annotation[] annotations = argClass.getAnnotations();
                boolean hasValidate = false;
                for (Annotation a : annotations) {
                    if (a.annotationType().getName().equals(Validate.class.getName())) {
                        hasValidate = true;
                        break;
                    }
                }

                if (hasValidate) {
                    try {
                        Method validateMethod = argClass.getMethod("validate");
                        validateMethod.invoke(arg);
                    } catch (NoSuchMethodException e) {
                        log.warn("Class {} is annotated with @Validate but has no validate() method", argClass.getName());
                    } catch (InvocationTargetException ite) {
                        Throwable cause = ite.getCause();
                        if (cause != null) throw cause;
                        throw ite;
                    }
                }
            } catch (Exception ex) {
                log.error("Validation aspect failure", ex);
                throw ex;
            }
        }

        return pjp.proceed();
    }
}
