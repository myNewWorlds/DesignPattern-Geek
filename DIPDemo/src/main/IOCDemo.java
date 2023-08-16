package src.main;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制反转 模板设计模式
 * 模板设计模式：一个抽象类公开定义了执行它的方法的方式/模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行
 */
public class IOCDemo {
    /**
     * 框架内部的程序
     */
    private static final List<Testcase> testCases = new ArrayList<>();
    public static void register(Testcase testcase) {
        testCases.add(testcase);
    }

    public static void main(String[] args) {
        IOCDemo.register(new UserServiceTest());

        for (Testcase testCase : testCases) {
            testCase.run();
        }
    }
}

/**
 * 框架内部的程序
 */
abstract class Testcase {
    public void run() {
        if (doTest()) {
            System.out.println("Test Success");
        } else {
            System.out.println("Test Fail");
        }
    }
    //抽象模板
    public abstract boolean doTest();
}

/**
 * 程序员实现的程序
 */
class UserServiceTest extends Testcase {
    @Override
    public boolean doTest() {
        return true;
    }
}