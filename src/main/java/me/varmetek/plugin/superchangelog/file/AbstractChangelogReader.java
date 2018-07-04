package me.varmetek.plugin.superchangelog.file;

import com.google.common.base.Preconditions;
import me.varmetek.plugin.superchangelog.Changelog;
import me.varmetek.plugin.superchangelog.ChangelogComponent;
import me.varmetek.plugin.superchangelog.ChangelogManager;
import me.varmetek.plugin.superchangelog.SuperChangelogPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractChangelogReader implements ChangelogReader
{

  protected final SuperChangelogPlugin plugin;
  protected final ChangelogManager null_manager;
  protected final String fileName;
  protected final ChangelogReaderType type;

  protected ChangelogManager changeLogManager;

  public AbstractChangelogReader (SuperChangelogPlugin plugin, String fileName, ChangelogReaderType type){
    this.plugin = Preconditions.checkNotNull(plugin);
    this.fileName = Preconditions.checkNotNull(fileName);
    this.type = Preconditions.checkNotNull(type);
    null_manager = new ChangelogManager(plugin, Collections.emptyList());
    this.changeLogManager = null_manager;
  }



  @Override
  public ChangelogManager getChangeLogManager (){
    return changeLogManager;
  }

  @Override
  public void readFile () throws IOException, InvalidConfigurationException{


    File file = new File(plugin.getDataFolder(), fileName);



    if (!file.exists()){
      plugin.saveResource(fileName,false);
    }


    FileConfiguration config = createConfig();
    config.load(file);
    this.changeLogManager = deserialize(config);

  /*  try {

    } catch (FileNotFoundException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "File " + file.getName()+ "was not created nor replaced");
      this.changeLogManager = null_manager;
      return;
    } catch (IOException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "Cannot access " + file.getName() + " : "+ ex.getMessage());
      this.changeLogManager = null_manager;
      return;
    } catch (InvalidConfigurationException ex) {
      Bukkit.getLogger().log(Level.SEVERE, String.format("File %s is not formmated correctly : %s", file.getName(),ex.getMessage()));
      this.changeLogManager = null_manager;
      return;
    }*/








  }

  protected ChangelogManager deserialize(MemorySection config){
    List<Changelog>  listsCL = new LinkedList<>();
    ConfigurationSection root = config.getConfigurationSection("changelogs");
    Set<String > changelogs = root.getKeys(false);

    for(String title: changelogs ){

      ConfigurationSection titleSection = root.getConfigurationSection(title);
      Preconditions.checkNotNull(titleSection,"Changelog \"" + title+ "\" is not formatted correctly");
      try {
        Changelog log = getCLFromKey(title,titleSection );
        listsCL.add(log);
      }catch (Exception e){
        throw new IllegalArgumentException("Error interpreting Changelog \""+ title + "\" "+ e.getMessage() );
      }


    }
    Collections.sort(listsCL);

    ChangelogManager clm = new ChangelogManager(plugin,listsCL);
    return  clm;
  }


  protected FileConfiguration serialize(ChangelogManager clm){
    FileConfiguration config = createConfig();
    ConfigurationSection root = config.createSection("changelogs");
    for(Changelog cl : changeLogManager){

      ConfigurationSection section = root.createSection(cl.getTitle().getMessageUndoColor());

      section.set("update",cl.getUpdate());

      List<String>  strs = new ArrayList<>(cl.getComponents().size());
      for(ChangelogComponent clc: cl.getComponents()){
        strs.add(clc.getMessageUndoColor());
      }
      section.set("changes", strs);

    }
    return config;
  }

  @Override
  public void transferTo(ChangelogReader read) throws IOException{
    AbstractChangelogReader reader = (AbstractChangelogReader)read;
    FileConfiguration configg = serialize(this.changeLogManager);
    reader.changeLogManager = reader.deserialize(configg);
    reader.write();
  }

  @Override
  public void write () throws IOException{


   serialize(this.changeLogManager).save(new File(plugin.getDataFolder(),fileName));
  }


  public String getFileName(){
    return fileName;
  }

  public File getFile(){
    return new File(plugin.getDataFolder(),fileName);
  }

  protected Changelog getCLFromKey(String key, ConfigurationSection config){
    Preconditions.checkNotNull(config, "path "+key);
    Preconditions.checkArgument(config.contains("update"), "attribute 'update' is not formatted correctly");
    int update = config.getInt("update");
    List<String> changes = config.getStringList("changes");
    Preconditions.checkNotNull(changes,"attribute 'changes' is not formatted correctly");

    Changelog.ChangeLogBuilder builder = Changelog.builder();
    builder.setTitle(key);
    builder.setUpdate(update);
    for(String s: changes){
      if(s == null) continue;
      builder.addComponent(new ChangelogComponent(s));
    }

    return builder.build();

  }

  protected abstract FileConfiguration createConfig();

  public ChangelogReaderType getReaderType(){
    return this.type;
  }
}
