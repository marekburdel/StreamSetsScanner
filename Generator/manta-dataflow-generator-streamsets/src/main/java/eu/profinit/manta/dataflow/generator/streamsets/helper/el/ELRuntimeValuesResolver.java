package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

import org.apache.commons.el.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.el.ELException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * EL Runtime Values Resolver resolves JSP 2.0 Expression Language
 * and tries to evaluate expressions with Runtime Values.
 * @author mburdel
 */
public class ELRuntimeValuesResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELRuntimeValuesResolver.class);

    private static final String RUNTIME_PROPERTY_PATH = "runtime.conf_";

    private static final String FUNCTION_RECORD_VALUE = "record:value";
    private static final String FUNCTION_RUNTIME_CONF = "runtime:conf";
    private static final String FUNCTION_RUNTIME_LOAD_RESOURCE = "runtime:loadResource";
    private static final String FUNCTION_STR_TRIM = "str:trim";

    /**
     * JSP 2.0 Expression Language Evaluator helps to parse string expression into convenient AST.
     */
    private ExpressionEvaluatorImpl evaluator;

    /**
     * Path to Runtime Values (.properties file and .txt files).
     */
    private String runtimeValuesPath;

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Method replaces
     *      ${PARAMETER},
     *      ${record:value(PARAMETER)},
     *      ${runtime:conf('PROPERTY')},
     *      ${runtime:loadResource("file.txt", true/false)}
     * with value stored in
     *      pipeline's parameters,
     *      .properties file,
     *      resources files
     * and removes other EL.
     * Path to files is in runtimeValuesPath variable.
     * @param expressionString expression
     * @param pipelineParameters pipeline's parameters
     * @param remove true if EL should be remove if Runtime Value is not recognized,
     *               else only Runtime Values will be replaced
     * @return string with replaced Runtime Values with its value stored
     * in pipeline's parameters, .properties file and resources file.
     */
    @SuppressWarnings("deprecation") public String replaceRuntimeValues(String expressionString,
            Map<String, String> pipelineParameters, boolean remove) {
        try {
            Object expression = evaluator.parseExpressionString(expressionString);
            return replaceRuntimeValue(expression, pipelineParameters, remove);
        } catch (ELException e) {
            LOGGER.warn("Problem occurred while parsing expression language: " + expressionString, e);
        }
        return "";
    }

    private String replaceRuntimeValue(Object object, Map<String, String> pipelineParameters, boolean remove) {
        if (object instanceof ExpressionString) {
            return replaceRuntimeValue((ExpressionString) object, pipelineParameters, remove);
        } else if (object instanceof BinaryOperatorExpression) {
            return replaceRuntimeValue((BinaryOperatorExpression) object, pipelineParameters, remove);
        } else if (object instanceof ComplexValue) {
            return replaceRuntimeValue((ComplexValue) object, pipelineParameters, remove);
        } else if (object instanceof ConditionalExpression) {
            return replaceRuntimeValue((ConditionalExpression) object, pipelineParameters, remove);
        } else if (object instanceof FunctionInvocation) {
            return replaceRuntimeValue((FunctionInvocation) object, pipelineParameters, remove);

        } else if (object instanceof NamedValue) {
            return replaceRuntimeValue((NamedValue) object, pipelineParameters, remove);
        } else if (object instanceof UnaryOperatorExpression) {
            return replaceRuntimeValue((UnaryOperatorExpression) object, pipelineParameters, remove);

        } else if (object instanceof String) {
            return (String) object;
        }

        if (remove) {
            return StringUtils.EMPTY;
        } else {
            return ((Expression) object).getExpressionString();
        }
    }

    private String replaceRuntimeValue(ExpressionString expression, Map<String, String> pipelineParameters,
            boolean remove) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : expression.getElements()) {
            stringBuilder.append(replaceRuntimeValue(object, pipelineParameters, remove));
        }
        return stringBuilder.toString();
    }

    private String replaceRuntimeValue(BinaryOperatorExpression expression, Map<String, String> pipelineParameters,
            boolean remove) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean appendOperator;
        String replacement;

        replacement = replaceRuntimeValue(expression.getExpression(), pipelineParameters, remove);
        stringBuilder.append(replacement);
        appendOperator = !replacement.isEmpty();
        for (int i = 0; i < expression.getExpressions().size(); ++i) {
            Object operator = expression.getOperators().get(i);
            if (appendOperator) {
                if (operator instanceof PlusOperator) {
                    stringBuilder.append("+");
                } else {
                    stringBuilder.append("-");
                }
            }
            replacement = replaceRuntimeValue(expression.getExpressions().get(i), pipelineParameters, remove);
            appendOperator = !replacement.isEmpty();
            stringBuilder.append(replacement);
        }
        return stringBuilder.toString();
    }

    private String replaceRuntimeValue(ComplexValue expression, Map<String, String> pipelineParameters,
            boolean remove) {
        // StreamSets doesn't use ComplexValue.
        if (remove) {
            return StringUtils.EMPTY;
        }
        return expression.getExpressionString();
    }

    private String replaceRuntimeValue(ConditionalExpression expression, Map<String, String> pipelineParameters,
            boolean remove) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(replaceRuntimeValue(expression.getCondition(), pipelineParameters, remove));
        stringBuilder.append(replaceRuntimeValue(expression.getTrueBranch(), pipelineParameters, remove));
        stringBuilder.append(replaceRuntimeValue(expression.getFalseBranch(), pipelineParameters, remove));

        return stringBuilder.toString();
    }

    private String replaceRuntimeValue(FunctionInvocation expression, Map<String, String> pipelineParameters,
            boolean remove) {
        String functionName = expression.getFunctionName();
        switch (functionName) {
        case FUNCTION_RECORD_VALUE:
            return analyzeRuntimeValueInRecordValueFunction(expression, pipelineParameters, remove);
        case FUNCTION_RUNTIME_CONF:
            return analyzeRuntimeValueInConfFunction(expression, remove);
        case FUNCTION_RUNTIME_LOAD_RESOURCE:
            return analyzeRuntimeValueInLoadResourceFunction(expression, remove);
        case FUNCTION_STR_TRIM:
            return analyzeStrTrimFunction(expression, pipelineParameters, remove);
        default:
            return analyzeRuntimeValueInDefaultFunction(expression, pipelineParameters, remove);
        }
    }

    private String analyzeStrTrimFunction(FunctionInvocation expression, Map<String, String> pipelineParameters,
            boolean remove) {
        String stringToTrim;
        Object arg0 = expression.getArgumentList().get(0);
        if (arg0 instanceof NamedValue) {
            stringToTrim = replaceRuntimeValue((NamedValue) arg0, pipelineParameters, remove);
        } else if (arg0 instanceof FunctionInvocation) {
            stringToTrim = replaceRuntimeValue((FunctionInvocation) arg0, pipelineParameters, remove);
        } else if (arg0 instanceof StringLiteral) {
            stringToTrim = (String) ((StringLiteral) arg0).getValue();
        } else {
            stringToTrim = ((Expression) arg0).getExpressionString();
        }

        stringToTrim = stringToTrim.trim();

        return stringToTrim;
    }

    private String replaceRuntimeValue(NamedValue expression, Map<String, String> pipelineParameters, boolean remove) {
        if (pipelineParameters != null && pipelineParameters.containsKey(expression.getName())) {
            // parameter value may be expressed as a runtime value, so we need to replace it again
            return replaceRuntimeValues(pipelineParameters.get(expression.getName()), pipelineParameters, remove);
        }
        LOGGER.warn("Cannot recognize runtime value '{}' from pipeline's parameters. "
                + "Make sure that pipeline's parameter is set.", expression.getName());
        if (remove) {
            return StringUtils.EMPTY;
        }
        return expression.getExpressionString();
    }

    private String replaceRuntimeValue(UnaryOperatorExpression expression, Map<String, String> pipelineParameters,
            boolean remove) {
        return replaceRuntimeValue(expression.getExpression(), pipelineParameters, remove);
    }

    private String analyzeRuntimeValueInDefaultFunction(FunctionInvocation expression,
            Map<String, String> pipelineParameters, boolean remove) {
        if (remove) {
            return StringUtils.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(expression.getFunctionName()).append("(");
        for (int i = 0; i < expression.getArgumentList().size(); ++i) {
            Object argument = expression.getArgumentList().get(i);
            stringBuilder.append(replaceRuntimeValue(argument, pipelineParameters, remove));
            if (i < expression.getArgumentList().size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private String analyzeRuntimeValueInRecordValueFunction(FunctionInvocation expression,
            Map<String, String> pipelineParameters, boolean remove) {
        Object arg0 = expression.getArgumentList().get(0);
        if (arg0 instanceof NamedValue) {
            return replaceRuntimeValue((NamedValue) arg0, pipelineParameters, remove);
        }
        if (remove) {
            return StringUtils.EMPTY;
        }
        return expression.getExpressionString();
    }

    private String analyzeRuntimeValueInConfFunction(FunctionInvocation expression, boolean remove) {
        // read from .properties file
        Object argConf = expression.getArgumentList().get(0);
        String confName = (String) ((StringLiteral) argConf).getValue();

        // read all properties files that are stored in path
        File[] propertiesFiles = findPropertiesFiles(runtimeValuesPath);

        if (propertiesFiles != null) {
            for (File propertiesFile : propertiesFiles) {
                try (InputStream input = new FileInputStream(propertiesFile)) {
                    Properties prop = new Properties();
                    // load a properties file
                    prop.load(input);

                    String runtimeValue = prop.getProperty(RUNTIME_PROPERTY_PATH + confName);
                    if (runtimeValue != null) {
                        return runtimeValue;
                    } else {
                        LOGGER.warn("Cannot recognize runtime value '{}' from properties file '{}'.", confName,
                                propertiesFile.getName());
                    }
                } catch (IOException ex) {
                    LOGGER.warn("Cannot open properties file '{}'", propertiesFile.getName());
                }
            }
        } else {
            LOGGER.warn("Properties file(s) not found in '{}'", runtimeValuesPath);
        }

        if (remove) {
            return StringUtils.EMPTY;
        }
        return expression.getExpressionString();
    }

    private File[] findPropertiesFiles(String runtimeValuesPath) {
        File dir = new File(runtimeValuesPath);
        return dir.listFiles((dir1, filename) -> filename.endsWith(".properties"));
    }

    private String analyzeRuntimeValueInLoadResourceFunction(FunctionInvocation expression, boolean remove) {
        Object argLoad = expression.getArgumentList().get(0);
        String fileName = (String) ((StringLiteral) argLoad).getValue();
        try {
            return readFile(runtimeValuesPath + "/" + fileName, Charset.defaultCharset());
        } catch (IOException e) {
            LOGGER.warn("Cannot open file '{}'", runtimeValuesPath + "/" + fileName);
        }
        if (remove) {
            return StringUtils.EMPTY;
        }
        return expression.getExpressionString();
    }

    public void setRuntimeValuesPath(String runtimeValuesPath) {
        this.runtimeValuesPath = runtimeValuesPath;
    }

    public void setEvaluator(ExpressionEvaluatorImpl evaluator) {
        this.evaluator = evaluator;
    }
}
