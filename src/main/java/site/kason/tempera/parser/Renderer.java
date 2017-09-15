package site.kason.tempera.parser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import site.kason.tempera.engine.Template;
import site.kason.tempera.model.IterateContext;
import site.kason.tempera.util.MathUtil;

/**
 *
 * @author Kason Yang
 */
public abstract class Renderer {

  private StringBuilder sb;

  public Map<String, Object> data = new HashMap();

  public Renderer() {
  }

  public void append(Object str) {
    sb.append(str);
  }

  public StringBuilder append(String str) {
    return sb.append(str);
  }

  public StringBuilder append(boolean b) {
    return sb.append(b);
  }

  public StringBuilder append(char c) {
    return sb.append(c);
  }

  public StringBuilder append(int i) {
    return sb.append(i);
  }

  public StringBuilder append(long lng) {
    return sb.append(lng);
  }

  public StringBuilder append(float f) {
    return sb.append(f);
  }

  public StringBuilder append(double d) {
    return sb.append(d);
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

  public String render(Map<String, Object> values) {
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
    this.sb = new StringBuilder();
    this.execute();
    return sb.toString();
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
  
  public Object lt(Object o1,Object o2){
    return MathUtil.lt(o1, o2);
  }
  
  public Object le(Object o1,Object o2){
    return MathUtil.le(o1, o2);
  }
  
  public Object gt(Object o1,Object o2){
    return MathUtil.gt(o1, o2);
  }
  
  public Object ge(Object o1,Object o2){
    return MathUtil.ge(o1, o2);
  }
  
  public Object eq(Object o1,Object o2){
    return MathUtil.eq(o1, o2);
  }
  
  public Object ne(Object o1,Object o2){
    return MathUtil.ne(o1, o2);
  }

}
