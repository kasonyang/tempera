title=Tempera document
date=2017-10-11
type=post
tags=
status=published
~~~~~~

# Installation

gradle

    compile 'site.kason:tempera:VERSION'


# Get started

    Engine engine = new Engine();
    Template tpl = engine.compileInline("{{var name:String}}hello,{{name}}!", "hello.template", null);
    String result = tpl.render(Collections.singletonMap("name", "world"));
    assertEquals("hello,world!", result);

# Declaring variables

Variables should be declared at the beginning of template.

    {{var name:String}}
    {{var user:User}}

# Output variable

    Hello,my name is {{name}}.
    Hello,my name is {{user.name}}.

The variable`{{user.name}}` will attempt the following techniques to access the`name` attribute of the `user` variable:

* user.name
* user.getName()
* user.isName() 

# Operators

    == , != , < , > , <= , >=
    && , || , !
    +  , -  , * , / , %
    ?: 

# Control structures

## If

    {{if EXPR}}
    ...
    {{else}}
    ...
    {{/if}}

## For

    {{for i in list}}
    ...
    {{/for}}

or
  
    {{for i,ctx in list}}
    ...
    index:{{ctx.index}}
    isFirst:{{ctx.isFirst}}
    isLast:{{ctx.isLast}}
    ...
    {{/for}}

# Inheritance

Template `parent`:

    <html>
    <body>
    {{placeholder body}}Here is the default content of body.{{/placeholder}}
    </body>
    </html>

Template `child`:

    {{layout parent}}
    {{replace body}}Here is the new content of body.{{/body}}
    {{/layout}}

The output of `child` will be(refomatted):

    <html>
    <body>
    Here is the new content of body.
    </body>
    </html>

# Comments

    {{* 
    Here is comment.
    The content would be ignored.
     *}}

# Filters

    {{name|upper}}

# Functions

    {{length(name)}}

# Miscellaneous

The class of context variable in for statement:

    class IterateContext{
      isFirst()
      isLast()
      getIndex()
      hasNext()
      next()
    }
