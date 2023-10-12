package com.ljt;

public class Demo {
    public static void main(String[] args) {
        OrderController orderController = new OrderController();
        UserController userController = new UserController();
        orderController.order();
        userController.login();
    }
}
