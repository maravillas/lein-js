# lein-js

lein-js is a Leiningen plugin for concatenating and compiling JavaScript files using Google's [Closure Compiler](http://code.google.com/closure/compiler/).

## Usage

Syntax:

    lein js [mode]

where *mode* is `devel` or `prod` (defaults to `devel`). 

prod results in minified files, whereas devel pretty prints the result. Both modes use the compiler for optimizations, though this is configurable in the options below.

### Leiningen project options

The lein-js plugin configuration is specified in a :js entry in your project.clj. This entry should be a map with the following possible options:

**:src**

Optional. Default: "src/js"

A string specifying the path relative to the project root that contains JavaScript source files.

**:deploy**

Optional. Default: "war/js"

A string specifying the path relative to the project root that should contain the compiled JavaScript files.

**:bundles**

Required.

A vector specifying bundles consisting of the JavaScript files to compile and name of the resulting file. The input and output filenames should be relative to the :src and :deploy options above, respectively.

Format: 

>     [output1 [inputA inputB ...] 
>      ... 
>      outputN [inputA inputB ...]]

**:options**

**:devel-options**

**:prod-options**

Optional. Default: []

Vectors of options to provide to the Closure compiler. See below for details. 

Settings in :options are set in both modes, while settings in :devel-options and :prod-options are only set in their respective modes. These mode-specific settings override any conflicting settings in :options.

###Closure compiler options

These options are a subset of the options available in the Closure compiler application. 

**:compilation-level**

Sets the compilation level. See [Compilation Levels](http://code.google.com/closure/compiler/docs/compilation_levels.html).

* :whitespace-only
    * Removes comments, line breaks, unnecessary spaces, and other whitespace.
* :simple-optimizations (default)
    * Performs the same optimizations as :whitespace-only, and adds some basic optimizations, including renaming local variables and function parameters.
* :advanced-optimizations
    * Performs the same optimizations as :simple-optimizations, and adds more aggressive optimizations.

**:warning-level**

Sets the compiler's warning level.

* :quiet
* :default (default)
* :verbose

**:pretty-print**

Controls pretty print formatting. 

Boolean value. Default: false

**:print-input-delimeter**

Adds comments that label each input in the compiled file.

Boolean value. Default: false

**:process-closure-primitives**

Processes built-ins from the Closure library, such as goog.require(), goog.provide(), and goog.exportSymbol().

Boolean value. Default: false

**:coding-convention**

The set of style rules and conventions to enforce.

* :closure
    * Enforce the Closure style rules and conventions.
* :default (default)
    * Do not enforce any particular style rules or conventions.

**:summary-detail**

Controls how detailed the summary printed after each bundle's compilation is.

* :never-print
    * Never print the summary, regardless of errors or warnings.
* :print-on-errors (default)
    * Print the summary only when errors or warnings occur.
* :print-if-type-checking
    * Print the summary if type checking is turned on.
* :always-print
    * Always print the summary.

**:manage-closure-deps**

Automatically sort dependencies as specified by the goog.provides and goog.requires built-ins in each file. See also :process-closure-primitives. See [Manage Closure Dependencies](http://code.google.com/p/closure-compiler/wiki/ManageClosureDependencies).

Boolean value. Default: false

**:compilation-errors**

**:compilation-warnings**

**:compilation-ignored**

Controls the errors and warnings displayed during compilation, with finer-grained control than :warning-level. See [Warnings](http://code.google.com/p/closure-compiler/wiki/Warnings).

* "DEPRECATED" 
    * Warnings when non-deprecated code accesses code that's marked @deprecated 
* "VISIBILITY" 
    * Warnings when @private and @protected are violated. 
* "NON_STANDARD_JSDOC"
    * Warnings about non-conforming JSDoc markup
* "ACCESS_CONTROLS" 
    * Warnings when @deprecated, @private, or @protected are violated. 
* "INVALID_CASTS" 
    * Warnings about invalid type casts
* "FILEOVERVIEW_JSDOC" 
    * Warnings about duplicate @fileoverview tags 
* "STRICT_MODULE_DEP_CHECK" 
    * Warnings about all references potentially violating module dependencies 
* "UNKNOWN_DEFINES" 
    * Warnings when unknown @define values are specified. 
* "MISSING_PROPERTIES" 
    * Warnings about whether a property will ever be defined on an object. Part of type-checking. 
* "UNDEFINED_VARIABLES" 
    * Warnings about undefined variables
* "CHECK_REGEXP"
    * Look for references to the global RegExp object that would cause regular expressions to be unoptimizable. 
* "CHECK_TYPES" 
    * Type-checking 
* "CHECK_VARIABLES"
    * Warnings about undefined or redefined variables

Vector of strings.

Defaults:

Errors: []

Warnings: ["FILEOVERVIEW_JSDOC" "UNKNOWN_DEFINES"]

Ignored: ["DEPRECATED" "VISIBILITY" "ACCESS_CONTROLS" "STRICT_MODULE_DEP_CHECK" "MISSING_PROPERTIES" "CHECK_TYPES"]

**:externs**

The files containing extern declarations. See [Externs](http://code.google.com/closure/compiler/docs/api-tutorial3.html#externs).

Vector of strings. Default: []

**:charset**

The character set of the input files. See [java.nio.charset.Charset](http://java.sun.com/javase/6/docs/api/java/nio/charset/Charset.html) for options.

String. Default: "UTF-8"

## Installation

Add the following to your project.clj :dev-dependencies:

`[lein-js "0.1.1-SNAPSHOT"]`

Download the plugin with `lein deps`.

## License

Copyright (c) 2010 Matthew Maravillas

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.