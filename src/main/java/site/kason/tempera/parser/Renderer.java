package site.kason.tempera.parser;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import site.kason.tempera.extension.Function;
import site.kason.tempera.model.IterateContext;
import site.kason.tempera.util.MathUtil;

/**
 *
 * @author Kason Yang
 */
public abstract class Renderer {

  //TODO fix to protected
  public Writer writer;

  public Map<String, Object> data = new HashMap();
  
  public Map<String,Function> functions = new HashMap();

  public Renderer() {
  }

  public Writer append(Object str) throws IOException {
    return writer.append(Objects.toString(str, ""));
  }

  public Writer append(String str) throws IOException {
    return writer.append(str);
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

  public RuntimeException nonIterableValueException(Object obj) {
    return new RuntimeException("iterable value required.");
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

  public void render(Map<String, Object> values, Writer writer,Map<String,Function> functions) {
    this.functions = functions;
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
        throw new RuntimeException(ex);
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
    Function fn = this.functions.get(funcName);
    if(fn==null){
      throw new RuntimeException("function not found:" + funcName);
    }
    return fn.execute(arguments);
  }

}
