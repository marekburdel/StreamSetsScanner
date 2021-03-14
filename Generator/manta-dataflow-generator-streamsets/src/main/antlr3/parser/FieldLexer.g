lexer grammar FieldLexer;

options {
    language = Java;
}

@header {
	package eu.profinit.manta.dataflow.generator.streamsets.analyzer.stage.field.parser;

}

@members {

    @Override
	public void emitErrorMessage(String msg) {
        throw new RuntimeException(msg);
    }
}


WHITESPACE
    : (NEWLINE | ' ' | '\f' | '\t' | '\u00a0')+
    { $channel = HIDDEN; }
    ;

SLASH :                 '/';

SINGLE_QUOTE :          '\'';
DOUBLE_QUOTES :         '"';

IDENTIFIER :
        SINGLE_QUOTE  (~ SINGLE_QUOTE | ESC_SINGLE_QUOTE)+ SINGLE_QUOTE
    |   DOUBLE_QUOTES (~ DOUBLE_QUOTES | ESC_DOUBLE_QUOTES)+ DOUBLE_QUOTES
    |   (CHARACTER | REGEX_CHARACTER | BACKSLASH)+
    ;

fragment ESC_SINGLE_QUOTE :      BACKSLASH SINGLE_QUOTE;
fragment ESC_DOUBLE_QUOTES :     BACKSLASH DOUBLE_QUOTES;

fragment BACKSLASH :             '\\';

fragment NEWLINE :              CR? LF;
fragment CR :                   '\r';
fragment LF :                   '\n';

fragment UNDERSCORE :		'_';

fragment CHARACTER :		'a'..'z' | 'A'..'Z' | '0'..'9' | UNDERSCORE;
fragment REGEX_CHARACTER :  CHARACTER | '*' | '[' | ']' | '(' | ')' | '.';
