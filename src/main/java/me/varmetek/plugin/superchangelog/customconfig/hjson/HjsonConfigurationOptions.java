/*
 * Copyright (c) 2018 Varmetek - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.varmetek.plugin.superchangelog.customconfig.hjson;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.file.FileConfigurationOptions;

public class HjsonConfigurationOptions extends FileConfigurationOptions
{
  protected String integerIdentifier = "$i$";

  protected HjsonConfigurationOptions(HjsonConfiguration configuration) {
    super(configuration);
  }

  public HjsonConfiguration configuration() {
    return (HjsonConfiguration)super.configuration();
  }

  public HjsonConfigurationOptions copyDefaults(boolean value) {
    super.copyDefaults(value);
    return this;
  }

  @Override
  public HjsonConfigurationOptions pathSeparator(char value) {
    super.pathSeparator(value);
    return this;
  }

  @Override
  public HjsonConfigurationOptions header(String value) {
    super.header(value);
    return this;
  }

  @Override
  public HjsonConfigurationOptions copyHeader(boolean value) {
    super.copyHeader(value);
    return this;
  }

  public void setIntegerTag(String id){
    integerIdentifier = Preconditions.checkNotNull(id);

  }

  public String getIntegerTag(){
    return integerIdentifier;

  }

}
