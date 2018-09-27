package be.kdg.simulator;

import be.kdg.simulator.config.GeneratorConfigTest;
import be.kdg.simulator.messaging.generators.RandomMessageGeneratorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RandomMessageGeneratorTest.class, GeneratorConfigTest.class })
public class TestSuite {
}