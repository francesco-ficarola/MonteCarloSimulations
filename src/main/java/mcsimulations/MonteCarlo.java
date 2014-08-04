package mcsimulations;

import  java.io.File;

/**
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

public class MonteCarlo {


    public static void main(String[] args) {
        
        boolean verbose = false;
        File f = null;
        
        for ( String arg : args ) {
            if( arg.equals("-v") || arg.equals("--verbose") )
                verbose = true;
            else if( arg.equals("-h") || arg.equals("--help") ) {
                showHelp();
                System.exit(-1);
            } else if (arg.startsWith("-")){
                showHelp("Unknown option " + arg);
                System.exit(-1);
            } else
                f = new File(arg);
        };
        
        MainFrame window = new MainFrame(verbose, f);
        window.setLocationRelativeTo(null);
        
    }

    private static void showHelp(String msg) {
        System.out.println("\n" + msg);
        showHelp();
    }

    private static void showHelp() {

        System.out.println("");
        System.out.println("usage:");
        System.out.println("    MonteCarlo.sh [-h] [-v] {file]");
        System.out.println("");
        System.out.println("-v or --verbose : Getting verbose output");
        System.out.println("   -h or --help : this help");
        System.out.println("           file : file path to open");
        System.out.println("");
    }

}
