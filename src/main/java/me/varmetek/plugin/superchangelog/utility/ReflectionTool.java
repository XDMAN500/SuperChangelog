package me.varmetek.plugin.superchangelog.utility;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.*;

public final class ReflectionTool
{
  private static String VERSION;
  private static  String NMS_ROOT;
  private static  String OBC_ROOT;
  private static final String NMS= "net.minecraft.server.";
  private static final String OBC= "org.bukkit.craftbukkit.";


  public static void init(){
    Server server = Bukkit.getServer();
    String clazz = server.getClass().getName();
    int start = clazz.indexOf("v");
    int end = clazz.lastIndexOf(".");
    VERSION = clazz.substring(start,end);
    NMS_ROOT = new StringBuilder(NMS).append(VERSION).append('.').toString();
    OBC_ROOT = new StringBuilder(OBC).append(VERSION).append('.').toString();
    System.out.println(   "Starting Relfection on Version: "+ VERSION );
  }


  public static ClassAccessor getNmsClass(String clazz){
   return ClassAccessor.forClass(NMS_ROOT+clazz);
  }

  public static ClassAccessor getObcClass(String clazz){
    return ClassAccessor.forClass(OBC_ROOT+clazz);

  }


  private static abstract class  MemberAccessor{
    protected final boolean accessible;
    public MemberAccessor(AccessibleObject obj){
      Preconditions.checkNotNull(obj);
      accessible = obj.isAccessible();
    }



  }

  public static final class ClassAccessor{
    private final Class clazz;

    public static ClassAccessor forClass(String name){
      Class clazz;
      try{
        clazz  = Class.forName(name);
      }catch (ClassNotFoundException e ){
        throw new ReflectionFindException(String.format("Could not find class \"%s\"",name));

      }
      return new ClassAccessor(clazz);
    }

    public ClassAccessor(Class cl){
      this.clazz = cl;
    }

    public Class getContainedClass(){
      return clazz;
    }

    public ClassAccessor findSubClass(String name){
      Class subClazz = null;
      try{
        Class[] classes = clazz.getDeclaredClasses();
        if(classes.length == 0){
          subClazz = null;
        }else{
          for(Class cl: classes){
            if(cl.getSimpleName().equals(name)){
              subClazz = cl;
              break;
            }
          }
        }
      }catch ( SecurityException ignored ){}


      if (subClazz == null){


          Class[] classes = clazz.getClasses();
          if(classes.length == 0){
            subClazz = null;
          }else{
            for(Class cl: classes){
            if(cl.getSimpleName().equals(name)){
              subClazz = cl;
              break;
            }
          }}

        if(subClazz == null){
          throw new ReflectionFindException(String.format("Could not find sublass \"%s\"", name));
        }

      }


      return new ClassAccessor(subClazz);


    }

    public FieldAccessor findField(String name){
      Field f = null;

      try{
        f = clazz.getDeclaredField(name);
      }catch (NoSuchFieldException| SecurityException e ){
        try{
          f = clazz.getField(name);
        }catch (NoSuchFieldException| SecurityException ex ){
          throw new ReflectionFindException(String.format("Could not find field \"%s\"  (%s)",name,ex.getClass().getSimpleName()));
        }
      }

      return new FieldAccessor(f);
    }

    public MethodAccessor findMethod(String name, Class... args){
      Method meth = null;

      try{
        meth = clazz.getDeclaredMethod(name,args);
      }catch (NoSuchMethodException| SecurityException e ){
        try{
          meth = clazz.getMethod(name);
        }catch (NoSuchMethodException| SecurityException ex ){
          throw new ReflectionFindException(String.format("Could not find method \"%s\"  (%s)",name,ex.getClass().getSimpleName()));
        }
      }

      return new MethodAccessor(meth);
    }

    public ConstructorAccessor findConstructor( Class... args){
      Constructor constr = null;

      try{
        constr= clazz.getDeclaredConstructor(args);
      }catch (NoSuchMethodException| SecurityException e ){
        try{
          constr= clazz.getConstructor(args);
        }catch (NoSuchMethodException| SecurityException ex ){
          throw new ReflectionFindException("Could not find constructor");
        }
      }

      return new ConstructorAccessor(constr);
    }
  }

  public static  class FieldAccessor{
    private final Field field;
    private final boolean accessible;

    public FieldAccessor(Field field){
      Preconditions.checkNotNull(field);
      accessible = field.isAccessible();
      this.field = field;
    }

    public Field getField(){
      return field;
    }

    public Object getValue(Object src){
      try {
        field.setAccessible(true);
        Object ret =  field.get(src);
        field.setAccessible(accessible);
        return ret;
      } catch (IllegalAccessException e) {
        throw new ReflectionAccessException(e);

      }
    }

    public void setValue(Object src, Object value){
      try {
        field.setAccessible(true);
        field.set(src,value);
        field.setAccessible(accessible);
      } catch (IllegalAccessException e) {
        throw new ReflectionAccessException(e);

      }
    }


  }

  public static class MethodAccessor {
    private final Method method;
    private final boolean accessible;

    public MethodAccessor(Method meth){
      Preconditions.checkNotNull(meth);
      accessible = meth.isAccessible();
      this.method = meth;
    }

    public Method getMethod(){
      return method;
    }

    public Object invoke(Object src, Object... args){
      try {
        method.setAccessible(true);
        Object ret =  method.invoke(src,args);
        method.setAccessible(accessible);
        return ret;
      } catch (IllegalAccessException| InvocationTargetException e) {
        throw new ReflectionAccessException(e);

      }
    }

  }

  public static class ConstructorAccessor {
    private final Constructor constructor;
    private final boolean accessible;

    public ConstructorAccessor(Constructor constr){
      Preconditions.checkNotNull(constr);
      accessible = constr.isAccessible();
      this.constructor = constr;
    }

    public Object invoke( Object... args){
      try {

        constructor.setAccessible(true);
        Object ret = constructor.newInstance(args);
        constructor.setAccessible(accessible);
        return ret;

      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new ReflectionAccessException(e);

      }
    }

    public Constructor getConstructor(){
      return constructor;
    }

  }


  public static class ReflectionFindException extends RuntimeException{
    public ReflectionFindException() {
      super();
    }


    public ReflectionFindException(String message) {
      super(message);
    }

    public ReflectionFindException(String message, Throwable cause) {
      super(message, cause);
    }

    public ReflectionFindException(Throwable cause) {
      super(cause);
    }
  }

  public static class ReflectionAccessException extends RuntimeException{
    public ReflectionAccessException() {
      super();
    }


    public ReflectionAccessException(String message) {
      super(message);
    }

    public ReflectionAccessException(String message, Throwable cause) {
      super(message, cause);
    }

    public ReflectionAccessException(Throwable cause) {
      super(cause);
    }


  }

}
