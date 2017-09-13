package site.kason.tempera.util;

/**
 *
 * @author Kason Yang
 */
public class MathUtil {

  public static Object add(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() + ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() + ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() + ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() + ((Number) o2).intValue();
      }
    } else {
      return String.valueOf(o1) + String.valueOf(o2);
    }
  }

  public static Object sub(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() - ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() - ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() - ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() - ((Number) o2).intValue();
      }
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }

  public static Object mul(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() * ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() * ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() * ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() * ((Number) o2).intValue();
      }
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }

  public static Object div(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() / ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() / ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() / ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() / ((Number) o2).intValue();
      }
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }

  public static Object mod(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() % ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() % ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() % ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() % ((Number) o2).intValue();
      }
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Object le(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() <= ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() <= ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() <= ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() <= ((Number) o2).intValue();
      }
    } else if(o1 instanceof Comparable && o2 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) <= 0;
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Object lt(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() < ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() < ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() < ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() < ((Number) o2).intValue();
      }
    } else if(o1 instanceof Comparable && o2 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) < 0;
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Object ge(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() >= ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() >= ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() >= ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() >= ((Number) o2).intValue();
      }
    } else if(o1 instanceof Comparable && o2 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) >= 0;
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Object gt(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() > ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() > ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() > ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() > ((Number) o2).intValue();
      }
    } else if(o1 instanceof Comparable && o2 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2) > 0;
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Boolean eq(Object o1, Object o2) {
    if ((o1 instanceof Number) && (o2 instanceof Number)) {
      if (o1 instanceof Double || o2 instanceof Double) {
        return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
      } else if (o1 instanceof Float || o2 instanceof Float) {
        return ((Number) o1).floatValue() == ((Number) o2).floatValue();
      } else if (o1 instanceof Long || o2 instanceof Long) {
        return ((Number) o1).longValue() == ((Number) o2).longValue();
      } else {
        return ((Number) o1).intValue() == ((Number) o2).intValue();
      }
    } else {
      throw new IllegalArgumentException(
              String.format("unsupported types:%s,%s", o1.getClass().getName(), o2.getClass().getName())
      );
    }
  }
  
  public static Object ne(Object o1, Object o2) {
    return !eq(o1,o2);
  }

}
