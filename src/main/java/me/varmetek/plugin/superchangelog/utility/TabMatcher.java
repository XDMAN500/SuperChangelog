package me.varmetek.plugin.superchangelog.utility;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public  class TabMatcher
{
  protected final List<String> options;

  public TabMatcher(List<String> options){
    this.options = ImmutableList.copyOf(options);
  }

  public TabMatcher(String[] options){
    this.options = ImmutableList.copyOf(options);
  }


  public List<String>  match(String param){
    return match(param, true);
  }

  public List<String>  match(String param, boolean ignorecase){
    Preconditions.checkNotNull(param);

    if(ignorecase) param = param.toLowerCase();

    List<String> result= new ArrayList<>(options.size());
    for (String op : options) {
      if(op.length() < param.length()) continue;
      String check = op.substring(0,  param.length());
      if(ignorecase) check = check.toLowerCase();
      if (param.startsWith(check)){
        result.add(op);
      }
    }
    return result;
  }

}
