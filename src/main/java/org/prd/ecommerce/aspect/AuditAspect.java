package org.prd.ecommerce.aspect;

import jakarta.persistence.Table;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.entities.entity.Audit;
import org.prd.ecommerce.entities.entity.UserEntity;
import org.prd.ecommerce.repository.AuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

@Aspect
@Component
public class AuditAspect {
    //private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditRepository auditoriaDao;
    //around>before>afterReturning>after
    //around siempre se ejecuta primero y hasta que no se inicialice el proceso around no se ejecuta.
    //pointcut es el metodo que se va a interceptar
    //returning es el parametro que se va a retornar
    //value es para indicar el metodo que se va a interceptar
    //argNames es para indicar el nombre del parametro que se va a retornar en este caso es resultado que es el parametro que se va a retornar en el metodo createUser de la clase AuthServiceImpl
    //para guardar en la auditoria el metodo que se ejecuto


    @AfterReturning(pointcut = "execution(* org.prd.ecommerce.services.impl.*ServiceImpl.create*(..))", returning = "resultado")
    public void logAfterReturning(JoinPoint joinPoint, Object resultado) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Object[] parameters = joinPoint.getArgs();
        String entityName = null;
        String tabla = null;
        String method = joinPoint.getSignature().getName();
        String nameDtoClass = resultado.getClass().getSimpleName();
        String nameEntityClass = nameDtoClass.substring(0, nameDtoClass.length() - 3);
        Long idRegister = null;
        String userLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Class<?> clase = null;
        Object entityClass = null;
        try {
            clase = Class.forName("org.prd.ecommerce.entities.entity."+nameEntityClass);
            entityClass = clase.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException e) {
            logger.error("Error al obtener la clase :"+e);
        }

        if(method.startsWith("create")){
            entityName = method.substring(6);
            if(entityClass!=null){
                tabla = entityClass.getClass().getAnnotation(Table.class).name();
            }
            idRegister = Long.parseLong(resultado.getClass().getMethod("getId").invoke(resultado).toString());
            logger.info(method + "(): audit created...");
            auditoriaDao.save(new Audit(userLogged, method, idRegister, Calendar.getInstance().getTime(), tabla, "New "+entityName+" created"));
        }
        if(method.startsWith("update")){
        }

    }
    @After("execution(* org.prd.ecommerce.services.impl.*ServiceImpl.delete*(..))")
    public void logAfterReturning(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Object[] parameters = joinPoint.getArgs();
        String tabla = null;
        String method = joinPoint.getSignature().getName();
        String nameEntityClass = method.substring(6);
        Long idRegister = (Long) parameters[0];
        String userLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Class<?> clase = null;
        Object entityClass = null;
        try {
            clase = Class.forName("org.prd.ecommerce.entities.entity."+nameEntityClass);
            entityClass = clase.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException e) {
            logger.error("Error en la auditoria al obtener la clase :"+e);
        }
        if(method.startsWith("delete")){
            tabla =  entityClass.getClass().getAnnotation(Table.class).name();
            logger.info(method + "(): audit created...");
            auditoriaDao.save(new Audit(userLogged, method, idRegister, Calendar.getInstance().getTime(), tabla, "Entity with id "+idRegister+" deleted"));
        }
    }
    
    //para loguear el tiempo de ejecucion de un metodo
//    @Around("execution(* org.prd.ecommerce.services.impl.AuthServiceImpl.createUser(..))")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object resultado = null;
//         resultado = joinPoint.proceed();// continua para interceptar al m�todo
//        //joinPoint.proceed(new Object[] { new Integer(10) });//se puede cambiar el argumento
//        //joinPoint.set$AroundClosure(null);//esta hace que  no se ejecute el metodo y se cierre el metodo logAround
//        //joinPoint.stack$AroundClosure(null);//esta hace que  no se ejecute el metodo y se cierre el metodo logAround
//
//        return resultado;
//    }

    /*
    * System.out.println("logAfterReturning() m�todo: "
                + joinPoint.getSignature().getName());
        System.out.println("logAfterReturning() respuesta : " + resultado);
        System.out.println("logAfterReturning() argumentos : "
                + Arrays.toString(joinPoint.getArgs()));
        System.out.println("logAfterReturning() clase largo : "
                + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("logAfterReturning() signature name (nombre metodo): " + joinPoint.getSignature().getName());
        System.out.println("logAfterReturning() signature long string (metodo entero): " + joinPoint.getSignature().toLongString());
        System.out.println("logAfterReturning() signature short string (clase y metodo): " + joinPoint.getSignature().toShortString());
        System.out.println("logAfterReturning() signature class : " + joinPoint.getSignature().getDeclaringType());//auqie se obtiene ela clase :)

        System.out.println("logAfterReturning() tipo : " + joinPoint.getKind());
        System.out.println("logAfterReturning() Modificadores : "
                + joinPoint.getSignature().getModifiers());

        System.out.println("logAfterReturning() toShortString : "
                + joinPoint.toShortString());*/
}
