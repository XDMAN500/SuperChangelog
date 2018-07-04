package me.varmetek.plugin.superchangelog.file;

import me.varmetek.plugin.superchangelog.ChangelogManager;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

public interface ChangelogReader
{

  /**
   * Returns the categories in the order that they are listed
   *
   * @return the categories for {@;ink ChangelogComponent}
   */

  ChangelogManager getChangeLogManager();



  void readFile() throws  IOException, InvalidConfigurationException;

  void write() throws IOException;

  ChangelogReaderType getReaderType();

  String getFileName();
  File getFile();

  void transferTo(ChangelogReader reader) throws IOException;

}
