package me.varmetek.plugin.superchangelog.file;

import me.varmetek.plugin.superchangelog.SuperChangelogPlugin;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Level;

public final  class ReaderManager
{
  private final  EnumMap<ChangelogReaderType,ClReaderCreator> typeMap = new EnumMap<ChangelogReaderType,ClReaderCreator>(ChangelogReaderType.class);
  private final SuperChangelogPlugin plugin;


  private ChangelogReader changelogReader;
  public ReaderManager(SuperChangelogPlugin plugin){
    this.plugin = plugin;
    {
      typeMap.put(ChangelogReaderType.YAML, ()-> new ChangelogReaderYAML(this.plugin));
      typeMap.put(ChangelogReaderType.HJSON, ()-> new ChangelogReaderHJSON(this.plugin));
    }
  }

  public void chooseReader(){
    plugin.saveDefaultConfig();
    try {
      Object yaml = plugin.getConfig().get("use-yaml-config",null);
      if(yaml == null || !(yaml instanceof Boolean)){

        loadType(ChangelogReaderType.YAML);
        plugin.getLogger().log(Level.WARNING, String.format("Setting \"%s\" was not set. Defaulting to true"));
      }else{
        if( ((Boolean)yaml).booleanValue()){

         loadType(ChangelogReaderType.YAML);

          plugin.getLogger().log(Level.INFO, String.format("Using the YAML Config Reader"));
        }else{
          loadType(ChangelogReaderType.HJSON);
          plugin.getLogger().log(Level.INFO, String.format("Using the HJSON Config Reader"));
        }


      }
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, "Cannot access " + getChangelogReader().getFileName() + " : "+ ex.getMessage());

      return;
    } catch (InvalidConfigurationException ex) {
      plugin.getLogger().log(Level.SEVERE, String.format("File %s is not formmated correctly : %s", getChangelogReader().getFileName(),ex.getMessage()));
      return;
    }
  }

  public void chooseReader(ChangelogReaderType type){
    if(!typeMap.containsKey(type)) throw new IllegalArgumentException("Unrecognized reader type");
    this.changelogReader = typeMap.get(type).createReader();

  }

  public void loadType(ChangelogReaderType type) throws IOException, InvalidConfigurationException{

    chooseReader(type);
    this.changelogReader.readFile();
    plugin.resetGui();
  /*  try {

    } catch (IOException ex) {
      Bukkit.getLogger().log(Level.SEVERE, "Cannot access " + changelogReader.getFileName() + " : "+ ex.getMessage());
    } catch (InvalidConfigurationException ex) {
      Bukkit.getLogger().log(Level.SEVERE, String.format("File %s is not formmated correctly : %s",changelogReader.getFileName(),ex.getMessage()));


    }*/
  }

  public void saveType() throws IOException{
    this.changelogReader.write();
  }



  public ChangelogReader getChangelogReader(){
    return changelogReader;
  }


  public ChangelogReader convertToType(ChangelogReaderType type) throws IOException{
    if(changelogReader.getReaderType() == type){
      throw new IllegalArgumentException("Cant transfer to same type");
    }
    if(!typeMap.containsKey(type)) throw new IllegalArgumentException("Unrecognized reader type");
    ChangelogReader newReader = typeMap.get(type).createReader();
    changelogReader.transferTo(newReader);
    return newReader;

  }

  private interface  ClReaderCreator{
    ChangelogReader createReader();
  }

  public void reloadChangeLogs() throws IOException, InvalidConfigurationException{
  //  changelogReader.write();
    changelogReader.readFile();
    plugin.resetGui();

  }

}
