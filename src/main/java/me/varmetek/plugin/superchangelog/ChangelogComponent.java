package me.varmetek.plugin.superchangelog;

import com.google.common.base.Preconditions;
import me.varmetek.plugin.superchangelog.utility.TextUtil;

public class ChangelogComponent
{
  protected final String message;
  protected final String messageNoColor;
  protected final String messageUndoColor;

  public ChangelogComponent (String message){
    this.message = TextUtil.color(Preconditions.checkNotNull(message));
    this.messageNoColor = TextUtil.stripColor(this.message);
    this.messageUndoColor = TextUtil.reverseColor(this.message);
  }

  public String getMessage(){
    return message;
  }

  public String getMessageNoColor(){
    return messageNoColor;
  }

  public String getMessageUndoColor(){
    return messageUndoColor;
  }

}
