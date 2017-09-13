# What is Tempera?

Tempera is a high-performance and type-safe template engine.

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

# miscellaneous

    class IterateContext{
      isFirst()
      isLast()
      getIndex()
      hasNext()
      next()
    }
