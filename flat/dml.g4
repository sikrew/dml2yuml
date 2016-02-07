grammar dml;

/** DML parser
 * Many bits are stolen from the java.g grammar that came with 
 * ANTLR 2.7.2 in the examples/java/java directory.
 * (updated to antlr4 by prs)
 */

dml: ( domainDefinition )* EOF ;

domainDefinition: classDefinition
		| relationDefinition
		| externalDeclaration
		| valueTypeDeclaration
		| packageDeclaration
		;

externalDeclaration: 'external' 'class' entityTypeId aliasId ';' ;

valueTypeDeclaration: enumTypeDeclaration | compositeValueTypeDeclaration ;

enumTypeDeclaration: 'enum' identifier aliasId ';' ;

compositeValueTypeDeclaration: 'valueType' typeSpec aliasId valueTypeBlock ;

aliasId: ( 'as' identifier )? ;

valueTypeBlock: '{' externalizationClause ( internalizationClause )? '}' ;

externalizationClause: 'externalizeWith' '{' externalizationElements '}' ;

externalizationElements: externalizationElement+ ;

externalizationElement: typeSpec identifier '(' ')' ';' ;

internalizationClause: 'internalizeWith' identifier '(' ')' ';' ;

packageDeclaration: 'package' ( identifier )? ';' ;

classDefinition: modifiers 'class' entityTypeId
		// it _might_ have a superclass...
		superClassClause
		// it might implement some interfaces...
		implementsClause
		// now parse the body of the class
		classBlock
		;

superClassClause: ( 'extends' entityTypeId )? ;

implementsClause: ( 'implements' entityTypeId ( ',' entityTypeId )*)? ;

modifiers: modifier* ;

modifier: 'public' | 'protected' ;

classBlock: '{' classSlot* '}' | ';' ;

classSlot: modifiers typeSpec classSlotInternal | METH ;

classSlotInternal: ID slotOptions ';' ;

slotOptions: ( '(' 'REQUIRED' ')' )? ;

typeSpec: identifier ( typeArguments )? ;

typeArguments: '<' typeArgument ( ',' typeArgument )* '>' ;

typeArgument: typeSpec | wildcard ;

wildcard: '?' ( wildcardBounds )? ;

wildcardBounds: 'extends' typeSpec | 'super' typeSpec ;

relationDefinition: 'relation' identifier '{' rolesAndSlots '}' ;

rolesAndSlots: ( modifiers entityTypeId ( role | classSlotInternal ))* ;

role: 'playsRole' roleName roleOptions ;

roleName: ID? ;

roleOptions: '{' ( roleOption ';' )* '}' | ';' ;

roleOption: 'multiplicity' multiplicityRange
	  | 'indexed' 'by' identifier ( '#' '(' multiplicityUpperOnly? ')' )? 
	  | 'ordered'
	  ;

multiplicityRange: INT '..' multiplicityUpperOnly | multiplicityUpperOnly ;

multiplicityUpperOnly: INT | '*' ; 

// A (possibly-qualified) java identifier.
identifier: ID ( '.' ID )* ;

// With the addition of packages, names of entities may start with a dot
entityTypeId: identifier | '.' identifier ; 

ID: [a-zA-Z_$][a-zA-Z_0-9$]* ;
INT: [0-9]+ ;
METH: ( '///' (~[\n\r])* ) ;

WS: [ \t\f\r\n]+ -> skip; // Whitespace -- ignored
JCOM: ( '//' (~[\n\r])* ) -> skip; // Single-line comments
CCOM: '/*' .*? '*/' -> skip ; // multiple-line comments
STR: '"' (~('"'))* '"' ; // string literals

// a dummy rule to force vocabulary to be all characters
// (except special ones that ANTLR uses internally: 0 to 2)
VOCAB: [\3-\377] ;
