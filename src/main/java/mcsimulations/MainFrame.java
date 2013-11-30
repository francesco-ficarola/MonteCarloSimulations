package mcsimulations; /**
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

import java.awt.*;
import javax.swing.*;
import java.io.File;

public class MainFrame extends JFrame {
    
    public MainFrame(String verbose, File f){
        
        setSize(new Dimension(840,580));
	setTitle("Monte Carlo Simulations");
	setResizable(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	MainPanel panel = new MainPanel(verbose, f);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(820,560));
	
        Container contentPane = getContentPane();
	contentPane.add(scroll);
        setVisible(true);
    }

}