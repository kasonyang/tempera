title=Tempera document
date=2017-10-11
type=post
tags=
status=published
~~~~~~

# Overview

Tempera is a high-performance and type-safe template engine for java. 

# Performance

The performance test could be found here:[https://github.com/kasonyang/template-benchmark](https://github.com/kasonyang/template-benchmark)

![performance](https://github.com/kasonyang/template-benchmark/raw/master/results.png)

# Installation

![Maven Central](https://img.shields.io/maven-central/v/site.kason/tempera.svg)

gradle

    compile "site.kason:tempera:$VERSION"


# Get started

compile template from string:

    Engine engine = new Engine();
    Template tpl = engine.compileInline("{{var name:String}}hello,{{name}}!", "hello");
    String result = tpl.render(Collections.singletonMap("name", "world"));
    assertEquals("hello,world!", result);

compile template from resource:

    Engine engine = new Engine();
    Template tpl = engine.compile("templates/main.tpr");//compile template from resource:/templates/main.tpr
    tpl.render(Collections.singletonMap("names", list),new StringWriter());

compile template from file system:

    Configuration conf = new Configuration(Configuration.DEFAULT);
    conf.setTemplateLoader(new FileTemplateLoader("."));
    Template tpl = engine.compile("templates/main.tpr");//compile template from file: ./templates/main.tpr
    tpl.render(Collections.singletonMap("names", list),new StringWriter());

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

Using filters in template:

    {{name|upper}}

Specify the default filter:

    Configuration conf = new Configuration(Configuration.DEFAULT);
    conf.setDefaultFilter("html");

Register custom filters:

    Configuration conf = new Configuration(Configuration.DEFAULT);
    conf.registerFilter("html", new HtmlFilter());

# Functions

Using functions in template:

    {{length(name)}}

Register custom functions:

    Configuration conf = new Configuration(Configuration.DEFAULT);
    conf.registerFunction("XXX", new XXXFunction());

# Spring integration

Implements ViewResolver:

    public class TemperaView implements View {

      private final Template template;

      public TemperaView(Template template) {
        this.template = template;
      }

      @Override
      public String getContentType() {
        return null;
      }

      @Override
      public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap(model);
        template.render(data, response.getWriter());
      }

    }
    
    public class TemperaViewResolver implements ViewResolver {

      private final Engine engine;

      public TemperaViewResolver(Engine engine) {
        this.engine = engine;
      }

      private final Map<Template, TemperaView> views = new ConcurrentHashMap();

      @Override
      public View resolveViewName(String viewName, Locale locale) throws Exception {
        Template tpl = engine.compile(viewName);
        TemperaView view = views.get(tpl);
        if (view == null) {
          synchronized (views) {
            view = views.get(tpl);
            if (view == null) {
              view = new TemperaView(tpl);
              views.put(tpl, view);
            }
          }
        }
        return view;
      }

    }

Create `viewResolver` bean in your `Application` class:

    @SpringBootApplication
    public class Application extends SpringBootServletInitializer {

      public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
      }

      @Bean
      public ViewResolver viewResolver() {
        TemperaViewResolver vr = new TemperaViewResolver(new Engine());
        return vr;
      }
      
    }


# Miscellaneous

The class of context variable in for statement:

    class IterateContext{
      isFirst()
      isLast()
      getIndex()
      hasNext()
      next()
    }
