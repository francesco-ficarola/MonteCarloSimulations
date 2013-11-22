package mcsimulations;

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
        
        if(args.length == 1) {
            if(args[0].equals("-v") || args[0].equals("--verbose")) {
                MainFrame window = new MainFrame(args[0]);
                window.setLocationRelativeTo(null);
            } else {
                System.out.println();
                System.out.println("ERROR: Invalid Parameter!");
                System.out.println();
                System.out.println("TARGET SPECIFICATION:");
                System.out.println("-v or --verbose: Getting verbose output");
                System.out.println();
            }
        } else {
            if(args.length > 1) {
                System.out.println();
                System.out.println("ERROR: Invalid Parameter!");
                System.out.println();
                System.out.println("TARGET SPECIFICATION:");
                System.out.println("-v or --verbose: Getting verbose output");
                System.out.println();
            } else {
                MainFrame window = new MainFrame("");
                window.setLocationRelativeTo(null);
            }
        }
        
    }

}
