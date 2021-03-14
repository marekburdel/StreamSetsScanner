package eu.profinit.manta.dataflow.generator.streamsets.el;

import eu.profinit.manta.dataflow.generator.streamsets.helper.el.Dependence;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import eu.profinit.manta.dataflow.model.Edge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author mburdel
 */
public class ELDependenciesResolverTest {

    private static final String STREAMSETS_TEST_FLOW_XML = "spring/streamsets-test-flow.xml";

    private ExpressionLanguageHelper expressionLanguageHelper;

    @Before public void setUp() {
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(STREAMSETS_TEST_FLOW_XML);
        expressionLanguageHelper = springContext
                .getBean(eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper.class);
    }

    @Test public void test_DependenciesResolverCorrectInput() {
        String expressionString = "manta${'manta'}manta${record:value('/age') +1}";
        List<Dependence> dependencies = expressionLanguageHelper.getDependenciesFromEL(expressionString);
        Assert.assertEquals("/age", dependencies.get(0).getFieldPath());
        Assert.assertEquals(Edge.Type.DIRECT, dependencies.get(0).getFlowType());
    }

    @Test public void test_DependenciesResolverCorrectInput_EscapeChar_DoubleQuotes() {
        String expressionString = "${record:value(\"/'test/te\\\"st'/'\\\\'\\\"test/test/\\\\'new\\\\''\")}";
        List<Dependence> dependencies = expressionLanguageHelper.getDependenciesFromEL(expressionString);

        Assert.assertEquals("/'test/te\"st'/'\\'\"test/test/\\'new\\''", dependencies.get(0).getFieldPath());
    }

    @Test public void test_DependenciesResolverCorrectInput_EscapeChar_SingleQuotes() {
        String expressionString = "${record:value('/\"test/te\\\\\"st\"/\"\\'\\\\\"test/test/\\'new\\'\"')}";
        List<Dependence> dependencies = expressionLanguageHelper.getDependenciesFromEL(expressionString);

        Assert.assertEquals("/\"test/te\\\"st\"/\"'\\\"test/test/'new'\"", dependencies.get(0).getFieldPath());
        Assert.assertEquals(Edge.Type.DIRECT, dependencies.get(0).getFlowType());
    }

    @Test public void test_DependenciesResolverCorrectInput_RecursivelyInAnotherFunction_00() {
        String expressionString = "${str:concat(record:value('/hello'), 'world')}";
        List<Dependence> dependencies = expressionLanguageHelper.getDependenciesFromEL(expressionString);

        Assert.assertEquals("/hello", dependencies.get(0).getFieldPath());
        Assert.assertEquals(Edge.Type.DIRECT, dependencies.get(0).getFlowType());
    }

    @Test public void test_DependenciesResolverCorrectInput_RecursivelyInAnotherFunction_01() {
        String expressionString = "${str:indexOf(record:value('/revenue'),'$')}";
        List<Dependence> dependencies = expressionLanguageHelper.getDependenciesFromEL(expressionString);

        Assert.assertEquals("/revenue", dependencies.get(0).getFieldPath());
        Assert.assertEquals(Edge.Type.FILTER, dependencies.get(0).getFlowType());
    }

}
