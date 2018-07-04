package me.varmetek.plugin.superchangelog.file;

import me.varmetek.plugin.superchangelog.SuperChangelogPlugin;
import me.varmetek.plugin.superchangelog.utility.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ChangelogReaderYAML extends AbstractChangelogReader
{



  public ChangelogReaderYAML (SuperChangelogPlugin plugin){
    super(plugin, Constants.CHANGELOGFILE_YML,ChangelogReaderType.YAML);
  }

  @Override
  protected FileConfiguration createConfig (){
    return new YamlConfiguration();
  }


}
