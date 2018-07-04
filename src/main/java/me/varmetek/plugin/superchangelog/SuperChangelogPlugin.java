package me.varmetek.plugin.superchangelog;

import me.varmetek.plugin.superchangelog.command.CommandChangelog;
import me.varmetek.plugin.superchangelog.file.ChangelogReaderType;
import me.varmetek.plugin.superchangelog.file.ReaderManager;
import me.varmetek.plugin.superchangelog.gui.ChangelogGui;
import me.varmetek.plugin.superchangelog.utility.ReflectionTool;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SuperChangelogPlugin extends JavaPlugin
{


  private static SuperChangelogPlugin superChangelogPlugin;
  private ReaderManager readerManager;
  private ChangelogGui changelogGui;


  public static SuperChangelogPlugin getSuperChangelogPlugin (){
    return superChangelogPlugin;
  }

  public SuperChangelogPlugin(){
    superChangelogPlugin = this;
  }

  public void onLoad() {
    ReflectionTool.init();

  }
  public void onEnable() {
    this.readerManager = new ReaderManager(this);
    readerManager.chooseReader();
    superChangelogPlugin = this;

    resetGui();


    {
     new CommandChangelog(this);

    }
    this.getServer().getPluginManager().registerEvents(changelogGui,this);

  }

  public void onDisable() {
    superChangelogPlugin = null;
  }




  public ChangelogManager getChangeLogManager (){
    return readerManager.getChangelogReader().getChangeLogManager();
  }


  public ChangelogGui getChangelogGui (){
    return changelogGui;
  }




  public ReaderManager getReaderManager(){
   return  readerManager;
  }


  public void reloadType() throws IOException, InvalidConfigurationException{
    readerManager.reloadChangeLogs();
    resetGui();

  }

  public void loadReader (ChangelogReaderType type) throws IOException, InvalidConfigurationException{
    readerManager.loadType(type);
    this.getConfig().set("use-yaml-config", type  == ChangelogReaderType.YAML);
    this.saveConfig();
    resetGui();
  }

  public void resetGui(){
    changelogGui = new ChangelogGui(readerManager.getChangelogReader().getChangeLogManager());
  }





 /* public void convertChangeLog(ChangelogReaderType type) throws IOException{
    if(changelogReader.getReaderType() == type){
      throw new IllegalArgumentException("Cant transfer to same type");
    }

    ChangelogReader newReader;
    switch (type){
      case HJSON:{ newReader = new ChangelogReaderHJSON(this);}break;
      case YAML:{newReader = new ChangelogReaderYAML(this);}break;
      default:{      throw new IllegalArgumentException("Unrecognized changelog reader type");}
    }

    changelogReader.transferTo(newReader);

  }*/

  /*public ChangelogTransferResult transferChanglog(){
    if(changelogReader == null) throw new IllegalStateException("ChangelogReader cannot be null");

      try{

      }catch (Exception ex){

      }

    if(changelogReader instanceof ChangelogReaderYAML){
      final ChangelogTransferResult.TransferType type = ChangelogTransferResult.TransferType.HJSON;
      //transfer from HJSON
      if(new File(getDataFolder(), Constants.CHANGELOGFILE_HJSON).exists()){
        try {
          ChangelogReaderHJSON clrHJSON = new ChangelogReaderHJSON(this);
          clrHJSON.readFile();
          changelogReader.write(clrHJSON.getChangeLogManager());
          changelogReader.readFile();
          return ChangelogTransferResult.getSuccess(type);
        }catch (Exception ex){
          return ChangelogTransferResult.getFailure(type,
            new ChangelogTransferResult.ErrorContext(false,type,ex));

        }


      }else{
        return ChangelogTransferResult.getFailure(type,
          new ChangelogTransferResult.ErrorContext(true,type));
      }

    }else if(changelogReader instanceof ChangelogReaderHJSON){
      //transfer from YAML
      final ChangelogTransferResult.TransferType type = ChangelogTransferResult.TransferType.YAML;
        if(new File(getDataFolder(), Constants.CHANGELOGFILE_YML).exists()){
          try {
          ChangelogReaderYAML clrYAML = new ChangelogReaderYAML(this);
          clrYAML.readFile();
          changelogReader.write(clrYAML.getChangeLogManager());
          changelogReader.readFile();
          }catch (Exception ex){
            return ChangelogTransferResult.getFailure(type,
              new ChangelogTransferResult.ErrorContext(false,type,ex));

          }

          return ChangelogTransferResult.getSuccess(type);
        }else{
          return ChangelogTransferResult.getFailure(type,
            new ChangelogTransferResult.ErrorContext(true,type));
        }

    }else{
      throw new IllegalArgumentException("Unrecognized ChangelogReader");

    }



  }*/
}
