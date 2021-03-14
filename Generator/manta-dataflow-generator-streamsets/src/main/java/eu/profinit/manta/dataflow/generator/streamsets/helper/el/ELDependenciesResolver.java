package eu.profinit.manta.dataflow.generator.streamsets.helper.el;

import eu.profinit.manta.dataflow.model.Edge;
import org.apache.commons.el.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.el.ELException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EL Resolver resolves JSP 2.0 Expression Language and returns all found fields paths stored in list of dependencies.
 * @author mburdel
 */
public class ELDependenciesResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELDependenciesResolver.class);

    /**
     * JSP 2.0 Expression Language Evaluator helps to parse string expression into convenient AST.
     */
    private ExpressionEvaluatorImpl evaluator;

    /**
     * Mapping for all used functions in StreamSets Data Collector
     */
    private Map<String, List<FunctionArg>> functionMapping;

    /**
     * Method parses JSP 2.0 Expression Language and analyzing functions where field's path can be mentioned.
     * If field's path is found then called methods add it into dependencies' list.
     *
     * @param expressionString JSP 2.0 Expression Language
     * @return dependencies with field's paths and edge's types
     */
    @SuppressWarnings("deprecation") public List<Dependence> getDependenciesFromEL(String expressionString) {
        List<Dependence> dependencies = new ArrayList<>();
        try {
            Object expression = evaluator.parseExpressionString(expressionString);
            analyzeObject(expression, Edge.Type.DIRECT, dependencies);
            // StreamSets is using JSP 2.0 so exception is deprecated, newer version JSP 2.1
        } catch (ELException e) {
            LOGGER.warn("Problem occurred while parsing expression language: " + expressionString, e);
        }
        return dependencies;
    }

    private void analyzeObject(Object object, Edge.Type edgeType, List<Dependence> dependencies) {
        if (object instanceof ExpressionString) {
            analyzeExpression((ExpressionString) object, edgeType, dependencies);
        } else if (object instanceof BinaryOperatorExpression) {
            analyzeExpression((BinaryOperatorExpression) object, edgeType, dependencies);
        } else if (object instanceof ComplexValue) {
            analyzeExpression((ComplexValue) object, edgeType, dependencies);
        } else if (object instanceof ConditionalExpression) {
            analyzeExpression((ConditionalExpression) object, edgeType, dependencies);
        } else if (object instanceof FunctionInvocation) {
            analyzeExpression((FunctionInvocation) object, edgeType, dependencies);

        } else if (object instanceof BooleanLiteral) {
            analyzeExpression((BooleanLiteral) object, edgeType, dependencies);
        } else if (object instanceof FloatingPointLiteral) {
            analyzeExpression((FloatingPointLiteral) object, edgeType, dependencies);
        } else if (object instanceof IntegerLiteral) {
            analyzeExpression((IntegerLiteral) object, edgeType, dependencies);
        } else if (object instanceof NullLiteral) {
            analyzeExpression((NullLiteral) object, edgeType, dependencies);
        } else if (object instanceof StringLiteral) {
            analyzeExpression((StringLiteral) object, edgeType, dependencies);

        } else if (object instanceof NamedValue) {
            analyzeExpression((NamedValue) object, edgeType, dependencies);
        } else if (object instanceof UnaryOperatorExpression) {
            analyzeExpression((UnaryOperatorExpression) object, edgeType, dependencies);
        }
    }

    private void analyzeExpression(ExpressionString expressionString, Edge.Type edgeType,
            List<Dependence> dependencies) {
        for (Object object : expressionString.getElements()) {
            analyzeObject(object, edgeType, dependencies);
        }
    }

    private void analyzeExpression(BinaryOperatorExpression binaryOperatorExpression, Edge.Type edgeType,
            List<Dependence> dependencies) {
        analyzeObject(binaryOperatorExpression.getExpression(), edgeType, dependencies);
        for (Object object : binaryOperatorExpression.getExpressions()) {
            analyzeObject(object, edgeType, dependencies);
        }
    }

    private void analyzeExpression(ComplexValue expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // StreamSets doesn't use ComplexValue.
    }

    private void analyzeExpression(ConditionalExpression expression, Edge.Type edgeType,
            List<Dependence> dependencies) {
        analyzeObject(expression.getCondition(), edgeType, dependencies);
        analyzeObject(expression.getTrueBranch(), edgeType, dependencies);
        analyzeObject(expression.getFalseBranch(), edgeType, dependencies);
    }

    /**
     * Method analyzes EL functions.
     * @param expression expression
     * @param edgeType edge type
     * @param dependencies dependencies
     */
    private void analyzeExpression(FunctionInvocation expression, Edge.Type edgeType, List<Dependence> dependencies) {
        String functionName = expression.getFunctionName();
        // get arguments list of analyzed function from mapping that contains type of argument and edge
        List<FunctionArg> args = functionMapping.get(functionName);
        if (args != null) {
            for (int index = 0; index < args.size(); ++index) {
                // get value of i-argument
                Object arg = expression.getArgumentList().get(index);
                // If expression pass through function's argument that hasn't direct connection,
                // then change edgeType for next analysis .
                Edge.Type nextEdgeType = (edgeType == args.get(index).getEdgeType()) ? edgeType : Edge.Type.FILTER;
                // If function's argument can contain field path and argument's value is instance of StringLiteral
                // then add this field's path to dependencies, else continue to analyze argument.
                if (args.get(index).getFunctionArgType() == EFunctionArgType.FIELD_PATH) {
                    if (arg instanceof StringLiteral) {
                        String expressionString = StringLiteral
                                .getValueFromToken(((StringLiteral) arg).getExpressionString());
                        dependencies.add(new Dependence(expressionString, nextEdgeType));
                    } else {
                        analyzeObject(arg, nextEdgeType, dependencies);
                    }
                } else {
                    analyzeObject(arg, nextEdgeType, dependencies);
                }
            }
        } else {
            for (Object arg : expression.getArgumentList()) {
                analyzeObject(arg, Edge.Type.FILTER, dependencies);
            }
        }
    }

    private void analyzeExpression(BooleanLiteral expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(FloatingPointLiteral expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(IntegerLiteral expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(NullLiteral expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(StringLiteral expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(NamedValue expression, Edge.Type edgeType, List<Dependence> dependencies) {
        // without addition to dependencies
    }

    private void analyzeExpression(UnaryOperatorExpression expression, Edge.Type edgeType,
            List<Dependence> dependencies) {
        analyzeObject(expression.getExpression(), edgeType, dependencies);
    }

    public void setFunctionMapping(Map<String, List<FunctionArg>> functionMapping) {
        this.functionMapping = functionMapping;
    }

    public void setEvaluator(ExpressionEvaluatorImpl evaluator) {
        this.evaluator = evaluator;
    }
}
