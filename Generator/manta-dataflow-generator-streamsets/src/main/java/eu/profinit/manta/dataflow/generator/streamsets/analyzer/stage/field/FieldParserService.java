package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field;

import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.parser.FieldLexer;
import eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.parser.FieldParser;
import eu.profinit.manta.dataflow.generator.streamsets.helper.el.ExpressionLanguageHelper;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service to help parse fields.
 * @author mburdel
 */
public class FieldParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldParserService.class);

    private static final String ROOT_NAME = "ROOT";

    /**
     * Helper to resolve JSP 2.0 Expression Language
     */
    private ExpressionLanguageHelper expressionLanguageHelper;

    /**
     * Method constructs field's name from field's path.
     *
     * @param fieldPath field's path
     * @return field's name
     */
    String constructFieldName(String fieldPath) {
        List<String> parsedFieldPath = parseFieldPath(fieldPath);
        if (parsedFieldPath == null) {
            return null;
        }
        return parsedFieldPath.get(parsedFieldPath.size() - 1);
    }

    /**
     * Method removes expressions from field's path and then parses it.
     *
     * @param fieldPath field's path
     * @return parsed field's path
     */
    public List<String> parseFieldPath(String fieldPath) {
        if (fieldPath == null || fieldPath.isEmpty()) {
            return null;
        }

        List<String> parsedFieldPath = new ArrayList<>();
        parsedFieldPath.add(ROOT_NAME);

        fieldPath = expressionLanguageHelper.replaceRuntimeValues(fieldPath, Collections.emptyMap(), false);

        CommonTree tree;
        try {
            tree = getTree(fieldPath);
        } catch (RecognitionException | RuntimeException e) {
            LOGGER.warn("Cannot parse field path: {}", fieldPath);
            return null;
        }

        tree = (CommonTree) tree.getChild(0);
        if (tree.getChildren() != null) {
            for (Object t : tree.getChildren()) {
                parsedFieldPath.add(((CommonTree) t).getText());
            }
        }

        return parsedFieldPath;
    }

    private CommonTree getTree(String expression) throws RecognitionException {
        CharStream charStream = new ANTLRStringStream(expression);

        FieldLexer lexer = new FieldLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FieldParser parser = new FieldParser(tokens);

        return (CommonTree) parser.start_rule().getTree();
    }

    public ExpressionLanguageHelper getExpressionLanguageHelper() {
        return expressionLanguageHelper;
    }

    public void setExpressionLanguageHelper(ExpressionLanguageHelper expressionLanguageHelper) {
        this.expressionLanguageHelper = expressionLanguageHelper;
    }
}
