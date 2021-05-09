package com.example.aop.advisor;

/**
 * @author : jeymingwu
 * @date : 2021-05-09
 **/

public class WaiterDelegate {

    private Waiter waiter;

    public WaiterDelegate() {
    }

    public WaiterDelegate(Waiter waiter) {
        this.waiter = waiter;
    }

    public void service(String clientName) {
        this.waiter.greetTo(clientName);
        this.waiter.serveTo(clientName);
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
}
