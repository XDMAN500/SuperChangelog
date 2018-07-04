package me.varmetek.plugin.superchangelog.utility;

public final class StringKey
{
  private final String value;

  public StringKey(String val){
    this.value = val.toLowerCase();
  }

  @Override
  public boolean equals(Object o){
    if(o == null) return false;
    if(o instanceof StringKey) return equals((StringKey) o);
    if(o instanceof String) return equals( new StringKey((String)o));
    return false;

  }


  @Override
  public int hashCode(){
    return value.hashCode();
  }

  private boolean equals(StringKey o){
      return  this.value.equals( o.value);
  }


}
