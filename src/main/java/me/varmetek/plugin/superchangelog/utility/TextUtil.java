package me.varmetek.plugin.superchangelog.utility;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.regex.Pattern;


public final class TextUtil
  {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)(&|§)[0-9A-FK-OR]");

    public static String  color(String text){
      return text == null ? null :ChatColor.translateAlternateColorCodes('&',text);
    }
    public static String  stripColor(String text){ return text == null ? null : STRIP_COLOR_PATTERN.matcher(text).replaceAll("");}

    public static String  reverseColor(String text){
      if(text == null) return null;

      char[] b = text.toCharArray();

      for(int i = 0; i < b.length - 1; ++i) {
        if (b[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
          b[i] = '&';
          b[i + 1] = Character.toLowerCase(b[i + 1]);
        }
      }

      return new String(b);
    }


    public static void sendMessage(CommandSender sender, String... messages){
      Preconditions.checkNotNull(sender,"Sender cannot be null");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.length == 0){
        sender.sendMessage(" ");
      }else{
        for(String msg: messages){
          if(msg == null) continue;
          sender.sendMessage(color(msg));
        }
      }
    }

    public static void sendMessage(CommandSender[] senders, String... messages){
      Preconditions.checkNotNull(senders,"Senders cannot be null");
      Preconditions.checkArgument(senders.length != 0,"Senders cannot be empty");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.length == 0){
        for(CommandSender s: senders){
          s.sendMessage(" ");
        }

      }else{
        for(String msg: messages){
          if(msg == null) continue;
          for(CommandSender s: senders){
            s.sendMessage(color(msg));
          }
        }
      }
    }


    public static void sendMessage(Collection<CommandSender> senders, String... messages){
      Preconditions.checkNotNull(senders,"Senders cannot be null");
      Preconditions.checkArgument(!senders.isEmpty(),"Senders cannot be empty");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.length == 0){
        for(CommandSender s: senders){
          s.sendMessage(" ");
        }

      }else{
        for(String msg: messages){
          if(msg == null) continue;
          for(CommandSender s: senders){
            s.sendMessage(color(msg));
          }
        }
      }
    }

    public static void sendMessage(CommandSender sender, Collection<String> messages){
      Preconditions.checkNotNull(sender,"Sender cannot be null");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.size() == 0){
        sender.sendMessage(" ");
      }else{
        for(String msg: messages){
          if(msg == null) continue;
          sender.sendMessage(color(msg));
        }
      }
    }

    public static void sendMessage(CommandSender[] senders, Collection<String> messages){
      Preconditions.checkNotNull(senders,"Senders cannot be null");
      Preconditions.checkArgument(senders.length != 0,"Senders cannot be empty");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.size() == 0){
        for(CommandSender s: senders){
          s.sendMessage(" ");
        }

      }else{
        for(String msg: messages){
          if(msg == null) continue;
          for(CommandSender s: senders){
            s.sendMessage(color(msg));
          }
        }
      }
    }


    public static void sendMessage(Collection<CommandSender> senders, Collection<String> messages){
      Preconditions.checkNotNull(senders,"Senders cannot be null");
      Preconditions.checkArgument(!senders.isEmpty(),"Senders cannot be empty");
      Preconditions.checkNotNull(messages,"Cannot send null messages");

      if(messages.size() == 0){
        for(CommandSender s: senders){
          s.sendMessage(" ");
        }

      }else{
        for(String msg: messages){
          if(msg == null) continue;
          for(CommandSender s: senders){
            s.sendMessage(color(msg));
          }
        }
      }
    }
  }

