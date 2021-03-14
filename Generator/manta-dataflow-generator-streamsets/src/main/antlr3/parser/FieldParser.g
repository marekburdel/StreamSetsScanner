parser grammar FieldParser;

options {
    output = AST;
    memoize=true;
    language = Java;
    tokenVocab = FieldLexer;
}
tokens {
    AST_FIELD_PATH;
    AST_FIELD_NAME;
}

@header {
    package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.parser;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
}

@members {
    @Override
    public void reportError(RecognitionException e) {
        throw new RuntimeException(e);
    }

    @Override
    public void emitErrorMessage(String msg) {
        throw new RuntimeException(msg);
    }

}


//// Begin ////////////////////////////////////////////////////////////////////////////////////////////////////////////

start_rule :
        field_path EOF              -> ^(AST_FIELD_PATH field_path)
    ;

field_path:
        (SLASH IDENTIFIER)+         -> ^(AST_FIELD_NAME IDENTIFIER+)
    |   SLASH
    ;
