package me.varmetek.plugin.superchangelog.command;

import me.varmetek.plugin.superchangelog.Changelog;
import me.varmetek.plugin.superchangelog.ChangelogComponent;
import me.varmetek.plugin.superchangelog.ChangelogManager;
import me.varmetek.plugin.superchangelog.SuperChangelogPlugin;
import me.varmetek.plugin.superchangelog.file.ChangelogReaderType;
import me.varmetek.plugin.superchangelog.gui.ChangelogGui;
import me.varmetek.plugin.superchangelog.utility.TabMatcher;
import me.varmetek.plugin.superchangelog.utility.TextUtil;
import org.bukkit.command.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class CommandChangelog implements CommandExecutor, TabCompleter
{
  public static final String LABEL = "changelog";


  private final SuperChangelogPlugin plugin;

  public CommandChangelog (final SuperChangelogPlugin plugin){
    this.plugin = plugin;

    PluginCommand cmd =  plugin.getCommand(CommandChangelog.LABEL);
    cmd.setExecutor(this);
    cmd.setTabCompleter(this);
  }

  @Override
  public boolean onCommand (CommandSender sender, Command command, String label, String[] args){

    ChangelogManager clm = plugin.getChangeLogManager();
    ChangelogGui clg = plugin.getChangelogGui();
    if (args.length == 0){
      if (sender instanceof ConsoleCommandSender){

        Changelog cl = clm.getChangelogs().get(clm.getChangelogs().size() - 1);
        sender.sendMessage("Most Recent Changelog - '" + cl.getTitle() + "'");
        for (ChangelogComponent clc : cl.getComponents()) {
          sender.sendMessage("  " + clc.getMessage());
        }
      } else if (sender instanceof Player){
        ((Player) sender).openInventory(clg.getNewGui().getInventory());
      } else {
        sender.sendMessage("What are you?");
      }

      return true;
    }


    if (sender.hasPermission("supercl.admin")){

        switch (args[0].toLowerCase()) {
          case "help":{
            TextUtil.sendMessage(sender, "&a SuperChangelog Help Menu",
              "",
              "  &7/"+ label+ " reload  &a reloads the config file to read new changes",
              "  &7/"+ label+ " convert &a converts settings to selected format ",
              ""

            );
          }
          case "reload": {
            try {
              reload();
            }catch (IOException ex){
              TextUtil.sendMessage(sender,"&c An error occurred when reading file");
              ex.printStackTrace();
              return false;
            } catch (InvalidConfigurationException e) {
              TextUtil.sendMessage(sender,"&c The file is not configured correctly");
              e.printStackTrace();
            }
            TextUtil.sendMessage(sender, "&a Changelog Files reloaded");
            return true;
          }
          case "type":
          case "config":
          case "readertype":
          case "reader": {

            TextUtil.sendMessage(sender, "&a Current reader type is "+ plugin.getReaderManager().getChangelogReader().getReaderType().name());
            return true;
          }

          case "switch":
          case "load": {
            if(args.length <= 1){
              TextUtil.sendMessage(sender, "&a Convert options are Hjson and Yaml");
              return true;
            }

            String selection = args[1].toLowerCase();
            ChangelogReaderType type = ChangelogReaderType.NONE;
            switch(selection){
              case "yml":
              case "yaml":{type = ChangelogReaderType.YAML;}break;
              case "hjson":{type = ChangelogReaderType.HJSON;}break;
              default:{
                TextUtil.sendMessage(sender,"&c Unrecognized reader type "+ selection);
                return false;
              }
            }

            if(type == plugin.getReaderManager().getChangelogReader().getReaderType()){
              TextUtil.sendMessage(sender,"&c Already using reader type "+ type.name());
              return false;
            }

            TextUtil.sendMessage(sender,"&a Switching to reader "+ type.name());
            try {
              plugin.loadReader(type);
              TextUtil.sendMessage(sender,"&a Successfully switched to reader "+ type.name());
            }catch (IOException ex){
              TextUtil.sendMessage(sender,"&c An error occurred when reading file");
              ex.printStackTrace();
              return false;
            } catch (InvalidConfigurationException e) {
              TextUtil.sendMessage(sender,"&c The file is not configured correctly");
              e.printStackTrace();
            }
          }break;

          case "convert": {
            if(args.length <= 1){
              TextUtil.sendMessage(sender, "&a Convert options are Hjson and Yaml");
              return true;
            }

            String selection = args[1].toLowerCase();
            ChangelogReaderType type = ChangelogReaderType.NONE;
            switch(selection){
              case "yml":
              case "yaml":{type = ChangelogReaderType.YAML;}break;
              case "hjson":{type = ChangelogReaderType.HJSON;}break;
              default:{
                TextUtil.sendMessage(sender,"&c Unrecognized reader type "+ selection);
                return false;
              }
            }

            if(type == ChangelogReaderType.NONE){
                TextUtil.sendMessage(sender,"&c Unrecognized config type");
                return false;
            }

            if(type == plugin.getReaderManager().getChangelogReader().getReaderType()){
              TextUtil.sendMessage(sender,"&c Already using reader type "+ type.name());
              return false;
            }

            try {
              TextUtil.sendMessage(sender,"&a Attempting Data transfer");
              plugin.getReaderManager().convertToType(type);
              TextUtil.sendMessage(sender,"&a Data transfer complete");
              TextUtil.sendMessage(sender,"&a Use /"+label +" swtich  to switch to config type "+ type.name());
            }catch (IOException ex){
              TextUtil.sendMessage(sender,"&c An error occurred when transferring data");
              ex.printStackTrace();
              return false;
            }



            return true;
          }
        }


    }
    return false;
  }



  private void reload() throws IOException, InvalidConfigurationException{
    plugin.getReaderManager().reloadChangeLogs();
  }

  private static final TabMatcher INITIAL_OP = new TabMatcher(new String[]{"help","reload","convert"});
  private static final TabMatcher CONFIG_OP = new TabMatcher(new String[]{"yaml", "hjson"});

  @Override
  public List<String> onTabComplete (CommandSender commandSender, Command command, String s, String[] args){
    if (args.length == 0 ) return Collections.EMPTY_LIST;
    if(!commandSender.hasPermission("supercl.admin")) return Collections.EMPTY_LIST;


    if(args.length ==1 ){
      return INITIAL_OP.match(args[0]);
    }


    switch (args[0].toLowerCase()) {
      case "load":
      case "convert": {
        if(args.length == 2){

          return CONFIG_OP.match(args[1]);
        }
      }
    }

    return Collections.emptyList();
  }




}
