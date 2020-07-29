package spring.aop.xml;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author jeymingwu
 * @date 2020/7/29 15:28
 */
public class Audience {

    public void silenceCellPhones() {
        System.out.println("Silencing cell phones");
    }

    public void takeSeats() {
        System.out.println("Taking seats");
    }

    public void applause() {
        System.out.println("CLAP CLAP CLAP!!!");
    }

    public void demandRefund() {
        System.out.println("Demanding a refund");
    }

    public void watchPerformance(ProceedingJoinPoint joinPoint) {
        try {
            silenceCellPhones();
            takeSeats();
            joinPoint.proceed();
            applause();
        } catch (Throwable e) {
            demandRefund();
        }
    }
}
