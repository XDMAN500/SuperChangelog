package me.varmetek.plugin.superchangelog;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class ChangelogManager implements Iterable<Changelog>
{
  protected SuperChangelogPlugin plugin;

  protected ImmutableList<Changelog> changelogs;

  public ChangelogManager (SuperChangelogPlugin plugin, List<Changelog> changelogs){
    this.plugin = Preconditions.checkNotNull(plugin);
    this.changelogs = ImmutableList.copyOf(changelogs);
  }

  public ImmutableList<Changelog> getChangelogs (){
    return changelogs;
  }


  @Override
  public Iterator<Changelog> iterator (){
    return changelogs.iterator();
  }
}
