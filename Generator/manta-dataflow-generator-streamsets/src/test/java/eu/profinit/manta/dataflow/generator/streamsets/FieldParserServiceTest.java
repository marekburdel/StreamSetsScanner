package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldParserService;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 *
 * @author mburdel
 */
public class FieldParserServiceTest {

    private static final String STREAMSETS_TEST_FLOW_XML = "spring/streamsets-test-flow.xml";

    private FieldParserService parserService = new FieldParserService();

    @Before public void setUp() {
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(STREAMSETS_TEST_FLOW_XML);
        ExpressionLanguageHelper expressionLanguageHelper = springContext
                .getBean(eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper.class);

        parserService = new FieldParserService();
        parserService.setExpressionLanguageHelper(expressionLanguageHelper);
    }

    @Test public void test_ParseFieldPath_RootOnly() {
        String fieldPath = "/";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(1, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
    }

    @Test public void test_ParseFieldPath_SimpleInput_00() {
        String fieldPath = "/test1/test2";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(3, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
        Assert.assertEquals("test1", parsedFieldPath.get(1));
        Assert.assertEquals("test2", parsedFieldPath.get(2));
    }

    @Test public void test_ParseFieldPath_SingleQuotes() {
        String fieldPath = "/'test1'/test2/'test3'";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(4, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
        Assert.assertEquals("'test1'", parsedFieldPath.get(1));
        Assert.assertEquals("test2", parsedFieldPath.get(2));
        Assert.assertEquals("'test3'", parsedFieldPath.get(3));
    }

    @Test public void test_ParseFieldPath_SingleQuotesWithEscapingChar() {
        String fieldPath = "/'test1\\''/test2";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(3, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
        Assert.assertEquals("'test1\\''", parsedFieldPath.get(1));
        Assert.assertEquals("test2", parsedFieldPath.get(2));
    }

    @Test public void test_ParseFieldPath_DoubleQuotes() {
        String fieldPath = "/\"test1'\"/test2";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(3, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
        Assert.assertEquals("\"test1'\"", parsedFieldPath.get(1));
        Assert.assertEquals("test2", parsedFieldPath.get(2));
    }

    @Test public void test_ParseFieldPath_DoubleQuotesWithEscapingChar() {
        String fieldPath = "/\"test1'\\\"\"/test2";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals(3, parsedFieldPath.size());
        Assert.assertEquals("ROOT", parsedFieldPath.get(0));
        Assert.assertEquals("\"test1'\\\"\"", parsedFieldPath.get(1));
        Assert.assertEquals("test2", parsedFieldPath.get(2));
    }

    @Test public void test_ParseFieldPath_WithSpecialChars_00() {
        String fieldPath = "/special_characters!@#$%^&*()-=+[]{}|;:,.\\?><`~";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertNull(parsedFieldPath);
    }

    @Test public void test_ParseFieldPath_WithRegexSpecialChars() {
        String fieldPath = "/allowed_special_characters*[]().";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals("allowed_special_characters*[]().", parsedFieldPath.get(1));
    }

    @Test public void test_ParseFieldPath_WithSpecialChars_01() {
        String fieldPath = "/not_allowed_special_characters!@#$%^&-=+{}|;:,\\?><`~";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertNull(parsedFieldPath);
    }

    @Test public void test_ParseFieldPath_WithSpecialChars_02() {
        String fieldPath = "/not_allowed_special_characters!@#$%^&-=+{}|;:,\\?><`~_extra_string";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertNull(parsedFieldPath);
    }

    @Test public void test_ParseFieldPath_WithSpecialChars_WithSingleQuotes_03() {
        String fieldPath = "/'special_characters_with_single_quotes!@#$%^&*()-=+[]{}|;:,./?><`~'";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals("'special_characters_with_single_quotes!@#$%^&*()-=+[]{}|;:,./?><`~'",
                parsedFieldPath.get(1));
    }

    @Test public void test_ParseFieldPath_WithSpecialChars_WithDoubleQuotes_03() {
        String fieldPath = "/\"special_characters_with_double_quotes!@#$%^&*()-=+[]{}|;:,./?><`~\"";
        List<String> parsedFieldPath = parserService.parseFieldPath(fieldPath);

        Assert.assertEquals("\"special_characters_with_double_quotes!@#$%^&*()-=+[]{}|;:,./?><`~\"",
                parsedFieldPath.get(1));
    }
}
