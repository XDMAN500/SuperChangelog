package me.varmetek.plugin.superchangelog.file;

import me.varmetek.plugin.superchangelog.SuperChangelogPlugin;
import me.varmetek.plugin.superchangelog.customconfig.hjson.HjsonConfiguration;
import me.varmetek.plugin.superchangelog.utility.Constants;
import org.bukkit.configuration.file.FileConfiguration;


public class ChangelogReaderHJSON extends AbstractChangelogReader
{



  public ChangelogReaderHJSON (SuperChangelogPlugin plugin){
    super(plugin, Constants.CHANGELOGFILE_HJSON,ChangelogReaderType.HJSON);
  }

  @Override
  protected FileConfiguration createConfig (){
    return new HjsonConfiguration();
  }


}
