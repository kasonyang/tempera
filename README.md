 [![Build Status](https://travis-ci.org/kasonyang/tempera.svg?branch=master)](https://travis-ci.org/kasonyang/tempera)
[![Codecov](https://img.shields.io/codecov/c/github/kasonyang/tempera.svg)](https://codecov.io/gh/kasonyang/tempera)
![Maven Central](https://img.shields.io/maven-central/v/site.kason/tempera.svg)

# What is Tempera?

Tempera is a high-performance and type-safe template engine.

# Installation

gradle

    compile 'site.kason:tempera:VERSION'


# Get started

    Engine engine = new Engine();
    Template tpl = engine.compileInline("{{var name:String}}hello,{{name}}!", "hello.template", null);
    String result = tpl.render(Collections.singletonMap("name", "world"));
    assertEquals("hello,world!", result);

# Syntax

    {{var name:String}}
    {{if expr}}  {{else}} {{/if}}
    {{for i,ctx in expr}} {{/for}}
    {{layout masterName}}  {{/layout}}
    {{placeholder placeHolderName}} {{/placeholder}}
    {{replace placeHolderName}}  {{/replace}}
    {{expr}}
    {{* here is comment *}}
    {{expr | js }}
    {{hello('world')}}

# miscellaneous

    class IterateContext{
      isFirst()
      isLast()
      getIndex()
      hasNext()
      next()
    }
