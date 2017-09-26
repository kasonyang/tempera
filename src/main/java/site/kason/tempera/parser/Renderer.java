package site.kason.tempera.parser;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;
import site.kason.tempera.model.IterateContext;
import site.kason.tempera.model.RenderContext;
import site.kason.tempera.util.MathUtil;

/**
 *
 * @author Kason Yang
 */
public abstract class Renderer {

  //TODO fix to protected
  public Writer writer;

  public Map<String, Object> data = new HashMap();
  
  public RenderContext renderContext;

  public Renderer() {
  }

  public Writer append(Object obj) throws IOException {
    Filter[] defaultFilters = renderContext.getDefaultFilters();
    for (Filter f : defaultFilters) {
      obj = f.filter(obj);
    }
    return this.rawAppend(obj);
  }
  
  public Writer rawAppend(Object obj) throws IOException{
    return writer.append(Objects.toString(obj,""));
  }

  public IterateContext createIterateContext(Object obj) {
    return new IterateContext(this.iterator(obj));
  }

  public Iterator iterator(Object list) {
    if (list == null) {
      return Collections.EMPTY_LIST.iterator();
    }
    if (list.getClass().isArray()) {
      return Arrays.asList((Object[]) list).iterator();
    } else if (list instanceof Iterable) {
      return ((Iterable) list).iterator();
    } else {
      throw this.nonIterableValueException(list);
    }
  }

  public RenderException nonIterableValueException(Object obj) {
    return new RenderException("iterable value required.");
  }

  public boolean toBoolean(int val) {
    return val != 0;
  }

  public boolean toBoolean(long val) {
    return val != 0;
  }

  public boolean toBoolean(float val) {
    return val != 0;
  }

  public boolean toBoolean(double val) {
    return val != 0;
  }

  public boolean toBoolean(char c) {
    return c != 0;
  }

  public boolean toBoolean(String val) {
    return val != null && !val.isEmpty();
  }

  public boolean toBoolean(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof Boolean) {
      return ((Boolean) obj);
    } else if (obj instanceof Number) {
      return ((Number) obj).longValue() != 0;
    }
    return true;
  }

  public abstract void execute();

  public void render(Map<String, Object> values, Writer writer,RenderContext renderContext) {
    this.renderContext = renderContext;
    if (values == null) {
      values = Collections.EMPTY_MAP;
    }
    this.data = values;
    Class<? extends Renderer> clazz = this.getClass();
    for (Map.Entry<String, Object> d : values.entrySet()) {
      String key = d.getKey();
      String fieldName = "this_" + key;
      try {
        Field f = clazz.getField(fieldName);
        f.set(this, d.getValue());
      } catch (NoSuchFieldException ex) {
        //do nothing
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        throw new RenderException(ex);
      }
    }
    this.writer = writer;
    this.execute();
  }

  public Object add(Object o1, Object o2) {
    return MathUtil.add(o1, o2);
  }

  public Object sub(Object o1, Object o2) {
    return MathUtil.sub(o1, o2);
  }

  public Object mul(Object o1, Object o2) {
    return MathUtil.mul(o1, o2);
  }

  public Object div(Object o1, Object o2) {
    return MathUtil.div(o1, o2);
  }

  public Object mod(Object o1, Object o2) {
    return MathUtil.mod(o1, o2);
  }

  public Object lt(Object o1, Object o2) {
    return MathUtil.lt(o1, o2);
  }

  public Object le(Object o1, Object o2) {
    return MathUtil.le(o1, o2);
  }

  public Object gt(Object o1, Object o2) {
    return MathUtil.gt(o1, o2);
  }

  public Object ge(Object o1, Object o2) {
    return MathUtil.ge(o1, o2);
  }

  public Object eq(Object o1, Object o2) {
    return MathUtil.eq(o1, o2);
  }

  public Object ne(Object o1, Object o2) {
    return MathUtil.ne(o1, o2);
  }
  
  public Object callFunction(String funcName,Object[] arguments){
    Function fn = this.renderContext.getFunction(funcName);
    if(fn==null){
      throw new RenderException("function not found:" + funcName);
    }
    return fn.execute(arguments);
  }
  
  public Object callFilter(String filterName,Object value){
    Filter filter = this.renderContext.getFilter(filterName);
    if(filter==null){
      throw new RenderException("filter not found:" + filterName);
    }
    return filter.filter(value);
  }
  
  public Object readProperty(Object obj,String property){
    Class<? extends Object> clazz = obj.getClass();
    try {
      Field field = clazz.getField(property);
      return field.get(obj);
    } catch (NoSuchFieldException|IllegalAccessException ex) {
      String firstUp = property.substring(0,1).toUpperCase() + property.substring(1);
      String[] methodName = new String[]{
        property,
        "get" + firstUp,
        "is" + firstUp,
        "has" + firstUp
      };
      for(String mn:methodName){
        try {
          Method m = clazz.getMethod(mn);
          return m.invoke(obj);
        } catch (NoSuchMethodException|IllegalAccessException ex1) {
          
        } catch(InvocationTargetException ex2){
          //TODO handle exception
          throw new RuntimeException(ex2);
        }
      }
    }catch(SecurityException|IllegalArgumentException ex){
      throw new RuntimeException(ex);
    }
    //TODO handle property not found
    throw new RuntimeException("property not found:" + property);
  }

}
