package be.kdg.simulator;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestSuite.class);

        System.out.printf("%sFailures: %d%s%n", ANSI_RED, result.getFailureCount(), ANSI_RESET);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.printf("%sSuccessful: %s%s%n", ANSI_GREEN, result.wasSuccessful(), ANSI_RESET);
        System.out.printf("Amount of testcases: %d%n", result.getRunCount());
        System.out.printf("Time: %d ms%n", result.getRunTime());
        System.exit(0);
    }
}
