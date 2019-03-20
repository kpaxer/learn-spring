package demo05;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@ComponentScan
public class Main05 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Main05.class);

        UserManager userManager = ctx.getBean(UserManager.class);
        PlatformTransactionManager txManager = ctx.getBean(PlatformTransactionManager.class);
        TransactionDefinition txDef = ctx.getBean(TransactionDefinition.class);
        TransactionTemplate txTemplate = ctx.getBean(TransactionTemplate.class);

        System.out.println(userManager.queryAllUser());

        try {
            txTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    System.out.println("插入新数据，查看数据");
                    userManager.add("test", "test@lttdev.com", "test");
                    System.out.println(userManager.queryAllUser());
                    throw new RuntimeException("抛出异常");
                }
            });
        } catch (Exception ex) {
            System.out.println("异常信息: "+ex.getMessage());
        }

        System.out.println("事务已经回滚，再查看数据");
        System.out.println(userManager.queryAllUser());

    }

}
