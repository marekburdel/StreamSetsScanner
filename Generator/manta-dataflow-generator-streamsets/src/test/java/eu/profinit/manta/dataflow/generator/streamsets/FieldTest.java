package eu.profinit.manta.dataflow.generator.streamsets;

import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.EFieldType;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.Field;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.FieldParserService;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mburdel
 */
public class FieldTest {

    private static final String STREAMSETS_TEST_FLOW_XML = "spring/streamsets-test-flow.xml";

    private Field testField;

    private FieldParserService parserService;

    @Before public void setUp() {
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(STREAMSETS_TEST_FLOW_XML);
        ExpressionLanguageHelper expressionLanguageHelper = springContext
                .getBean(eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper.class);

        parserService = new FieldParserService();
        parserService.setExpressionLanguageHelper(expressionLanguageHelper);
        testField = new Field("/", EFieldType.IO, parserService);
    }

    @Test public void test_AddChildFieldToField_SimpleName_00() {
        Field addedField = testField.add("/test/field/name01", EFieldType.IO);
        Assert.assertEquals("name01", addedField.getName());
        Assert.assertEquals("/test/field/name01", addedField.getFieldPath());
        Assert.assertEquals(EFieldType.IO, addedField.getFieldType());
    }

    @Test public void test_AddChildFieldToField_SimpleName_01() {
        Field addedField = testField.add("/test/field/name02", EFieldType.BEGIN);
        Assert.assertEquals("name02", addedField.getName());
        Assert.assertEquals("/test/field/name02", addedField.getFieldPath());
        Assert.assertEquals(EFieldType.BEGIN, addedField.getFieldType());
    }

    @Test public void test_AddChildFieldToField_SimpleName_02() {
        Field addedField = testField.add("/field/name03", EFieldType.I);
        Assert.assertEquals("name03", addedField.getName());
        Assert.assertEquals("/field/name03", addedField.getFieldPath());
        Assert.assertEquals(EFieldType.I, addedField.getFieldType());
    }

    @Test public void test_AddChildFieldToField_SimpleName_03() {
        Field addedField = testField.add("/field/name04", EFieldType.O);
        Assert.assertEquals("name04", addedField.getName());
        Assert.assertEquals("/field/name04", addedField.getFieldPath());
        Assert.assertEquals(EFieldType.O, addedField.getFieldType());
    }

    @Test public void test_FindAddedFieldByPath_00() {
        testField.add("/test/field/name01", EFieldType.IO);
        Assert.assertNull(testField.findFieldByPath("/test/field/name00"));
        Assert.assertNotNull(testField.findFieldByPath("/test/field/name01"));
    }

    @Test public void test_FindAddedFieldByPath_01() {
        testField.add("/test/field/name01", EFieldType.IO);
        testField.add("/test/field/name02", EFieldType.IO);
        testField.add("/test/field/ads", EFieldType.IO);
        Assert.assertEquals("name01", testField.findFieldByPath("/test/field/name01").getName());
        Assert.assertNotEquals("name01", testField.findFieldByPath("/test/field/name02").getName());
    }

    @Test public void test_MergeFields() {
        Field rootField00 = new Field("/", EFieldType.IO, parserService);
        Field rootField01 = new Field("/", EFieldType.IO, parserService);
        Field rootField02 = new Field("/", EFieldType.IO, parserService);

        rootField00.add("/age/salary", EFieldType.IO);
        rootField00.add("/test", EFieldType.IO);
        rootField00.add("/test/test", EFieldType.IO);
        rootField00.add("/test/first/second/third", EFieldType.IO);

        Assert.assertTrue(rootField00.merge(rootField01));

        rootField01.add("/age", EFieldType.IO);
        rootField01.add("/why/not", EFieldType.IO);
        rootField01.add("/why", EFieldType.IO);

        rootField02.add("/test/first/new", EFieldType.IO);
        rootField02.add("/why/not/so/", EFieldType.IO);

        Assert.assertTrue(rootField00.merge(rootField01));
        Assert.assertFalse(rootField00.merge(rootField01));

        Assert.assertTrue(rootField00.merge(rootField02));
        Assert.assertFalse(rootField00.merge(rootField02));

        Assert.assertTrue(rootField00.merge(rootField01));
        Assert.assertFalse(rootField01.merge(rootField02));

        rootField02.add("/test/first/something", EFieldType.IO);

        Assert.assertTrue(rootField02.merge(rootField00));
        Assert.assertTrue(rootField00.merge(rootField01));
        Assert.assertFalse(rootField01.merge(rootField02));
    }

    @Test public void test_MergeCaseSensitive() {
        Field rootField00 = new Field("/", EFieldType.IO, parserService);
        Field rootField01 = new Field("/", EFieldType.IO, parserService);

        rootField00.add("/test", EFieldType.IO);
        rootField01.add("/Test", EFieldType.IO);

        Assert.assertFalse(rootField00.merge(rootField01));
    }

    @Test public void test_FindCaseSensitive() {
        Field rootField00 = new Field("/", EFieldType.IO, parserService);
        rootField00.add("/test", EFieldType.IO);

        Assert.assertEquals("/test", rootField00.findFieldByPath("/TEST").getFieldPath());
    }

    @Test public void test_AddCaseSensitive() {
        Field rootField00 = new Field("/", EFieldType.IO, parserService);
        rootField00.add("/test", EFieldType.IO);
        Field addedField = rootField00.add("/TEST", EFieldType.IO);

        Assert.assertEquals("/test", rootField00.findFieldByPath("/TEST").getFieldPath());
    }

    @Test public void test_FieldWithBackslash() {
        Field rootField00 = new Field("/", EFieldType.IO, parserService);
        rootField00.add("/*\\n", EFieldType.IO);

        Assert.assertEquals("/*\\n", rootField00.findFieldByPath("/*\\n").getFieldPath());
    }
}
