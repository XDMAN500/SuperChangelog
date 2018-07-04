package me.varmetek.plugin.superchangelog;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public final class Changelog implements Comparable<Changelog>
{



  private final ChangelogComponent changeLogName;
  private final ImmutableList<ChangelogComponent> components;
  private final int update;


  public static ChangeLogBuilder  builder(){
    return new ChangeLogBuilder();
  }

  private Changelog (ChangeLogBuilder ee){
        this.changeLogName = new ChangelogComponent(ee.changeLogName);
        this.components = ImmutableList.copyOf(ee.components);
        this.update  = ee.update;
  }




  public ChangelogComponent getTitle(){
    return changeLogName;
  }
  public int getUpdate(){
    return update;
  }


  public ImmutableList<ChangelogComponent>  getComponents (){
    return components;
  }

  @Override
  public int compareTo (Changelog o){
    return this.update- o.update;
  }


  public static class ChangeLogBuilder{
    private String changeLogName = new StringBuilder("Update ").append(Instant.now().toString()).toString();
    private List<ChangelogComponent> components;
    private int update;


    private ChangeLogBuilder(){
      components = new LinkedList<>();
    }

    public ChangeLogBuilder setTitle(String title){
      changeLogName = title;
      return this;

    }

    public ChangeLogBuilder setUpdate(int up){
      this.update = up;
      return this;
    }


    public ChangeLogBuilder  addComponent(ChangelogComponent hi){
      Preconditions.checkNotNull(hi);
      components.add(hi);
      return this;
    }


    public ChangeLogBuilder clearComponents(){
      components.clear();
      return this;
    }

    public Changelog build(){
      return new Changelog(this);
    }
  }
}
