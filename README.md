jackCompiler
============
Java implementation of the Jack (a simple object-oriented language) compiler: The compiler parses Jack programs, analyzes the syntax, and generates executable VM code according to the Jack grammar.


The overall compiler is constructed using five modules: 

JackCompiler: top-level driver that sets up and invokes the other modules; 
JackTokenizer: tokenizer; 
SymbolTable: symbol table;  
VMWriter: output module for generating VM code; 
CompilationEngine: recursive top-down compilation engine.


The JackCompiler Module:

The compiler operates on a given source file name Xxx.jack, and creates a JackTokenizer and and output Xxx.jack input file. Next, the compiler uses the CompilationEngine, SymbolTable, and VMWriter modules to write the output file.


The JackTokenizer Module:

Removes all comments and white space from the input stream and breaks it into Jack-language tokens, as specified by the Jack grammar.


The SymbolTable Module:

This module provides services for creating and using a symbol table. Each symbol has a scope from which it is visible in the source code. The symbol table implements this abstraction by giving each symbol a running number (index) within the scope. The index starts at 0, increments by 1 each time an identifier is added to the table, and resets to 0 when starting a new scope. 

The following kinds of identifiers may appear in the symbol table: 
Static: Scope: class. 
Field: Scope: class. 
Argument: Scope: subroutine (method/function/constructor). 
Var: Scope: subroutine (method/function/constructor). 

When compiling error-free Jack code, any identifier not found in the symbol table may be assumed to be a subroutine name or a class name. 

SymbolTable: Provides a symbol table abstraction. The symbol table associates the identifier names found in the program with identifier properties needed for compilation: type, kind, and running index. The symbol table for Jack programs has two nested scopes (class/subroutine).

The symbol table abstraction and API is implemented using two separate hash tables: one for the class scope and another one for the subroutine scope. When a new subroutine is started, the subroutine scope table will be cleared.

The VMWriter Module:

Emits VM commands into a file, using the VM command syntanx.


The CompilationEngine Module 

This class does the compilation itself. It reads its input from a JackTokenizer and writes its output into a VMWriter. It is organized as a series of compilexxx() routines, where xxx is a syntactic element of the Jack language. The contract between these routines is that each compilexxx() routine should read the syntactic construct xxx from the input, advance() the tokenizer exactly beyond xxx, and emit to the output VM code effecting the semantics of xxx. Thus compilexxx() may only be called if indeed xxx is the next syntactic element of the input. If xxx is a part of an expression and thus has a value, the emitted code should compute this value and leave it at the top of the VM stack. 
