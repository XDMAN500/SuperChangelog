package me.varmetek.plugin.superchangelog.utility;

public final class Constants
{

  private Constants() throws Exception{
    throw new Exception("Can't instatiate Constants.class");
  }

  public static final String
    CHANGELOGFILE_YML = "changelogs.yml",
    CHANGELOGFILE_HJSON = "changelogs.hjson";
}
