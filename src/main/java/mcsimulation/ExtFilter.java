package mcsimulation; /**
 *  Monte Carlo Simulations
 *  Copyright (C) 2008  Francesco Ficarola
 *  E-Mail: francesco.ficarola<at>gmail.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

import java.io.File;
import javax.swing.filechooser.*;

public class ExtFilter extends FileFilter
{
  String[] extensions;
  String description;
  
  public ExtFilter(String ext) {
      this (new String[] {ext},null);
  }
  
  public ExtFilter(String[] exts, String descr) {
    extensions = new String[exts.length];
    for (int i=exts.length-1;i>=0;i--)
        {extensions[i]=exts[i].toLowerCase();}
    description=(descr==null?exts[0]+" files":descr);
  }
  
  public boolean accept(File f) {
    if (f.isDirectory()) {return true;}
    String name = f.getName().toLowerCase();
    for (int i=extensions.length-1;i>=0;i--)
      {if (name.endsWith(extensions[i])) {return true;} }
    return false;
  }
  
  public String getDescription() {
      return description;
  }
  
}