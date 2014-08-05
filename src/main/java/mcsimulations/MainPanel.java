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

import mcsimulations.simulation.MCSimulation;	
import mcsimulations.simulation.SimulationResults;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.lang.Enum;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class MainPanel extends JPanel {
    
    //---------------------------------------------------------------------
    //Dichiarazione Variabili
    //---------------------------------------------------------------------
    
    //activitiesNumber: tiene conto del numero delle attività presenti
    private int activitiesNumber;
    //startSimulations: calcola il tempo di inizio delle simulazioni
    private long startSimulations;
    //stopSimulations: calcola il tempo di fine delle simulazioni
    private long stopSimulations;
    //results: risultati di calcolo
    private SimulationResults results;
    //verbose: Stringa per la modalità verbose
    private boolean verbose;
    
    //limTable: per il GridBagLayout del tablePanel
    private GridBagConstraints limTable;
    //limRepetitions: per il GridBagLayout del repetitionsPanel
    private GridBagConstraints limRepetitions;
        
    //northPanel: pannello nord del MainPanel
    private JPanel northPanel;
    //centerPanel: pannello centrale del MainPanel
    private JPanel centerPanel;
    //southPanel: pannello sud del MainPanel
    private JPanel southPanel;
    //tableSuperPanel: pannello contenuto in tableScroll (JScrollPane)
    private JPanel tableSuperPanel;
    //tablePanel: pannello nord del tableSuperPanel
    private JPanel tablePanel;
    //repetitionsPanel: pannello delle ripetitizioni e delle attività correnti
    private JPanel repetitionsPanel;
    //menuButtonPanel: pannello contenente i pulsanti del menu principale
    private JPanel menuButtonPanel;
    //activityButtPanel: pannello contenente i pulsanti per le attività
    private JPanel activityButtPanel;
    //resultsPanel: pannello dei risultati di calcolo
    private JPanel resultsPanel;
    //fillerPanel: pannello di riempimento centrale nel northPanel
    private JPanel fillerPanel;
    //parametersPanel: pannello contenente i campi dei parametri
    private JPanel parametersPanel;
    
    //addActivityButton: pulsante che permette l'inserimento di attività
    private JButton addActivityButton;
    //remActivityButton: pulsante che permette di rimuovere le attività
    private JButton remActivityButton;
    //resetButton: pulsante per il reset dei campi
    private JButton resetButton;
    //computeButton: pulsante per l'avvio del calcolo
    private JButton computeButton;
    //newButton: pulsante per avviare un nuovo progetto
    private JButton newButton;
    //openButton: pulsante per aprire un progetto esistente
    private JButton openButton;
    //saveButton: pulsante per salvare il progetto corrente
    private JButton saveButton;
    //helpButton: pulsante per ricevere la documentazione
    private JButton helpButton;
    //aboutButton: pulsante per ricevere informazioni sull'applicazione
    private JButton aboutButton;
    
    //distributionCombo: quarto campo della tabella; scelta della distribuzione
    private JComboBox distributionCombo;
    
    //Etichette
    private JLabel repetitionsLabel;
    private JLabel activitiesNumLabel;
    private JLabel activitiesLabel;
    private JLabel descriptionLabel;
    private JLabel precedencesLabel;
    private JLabel distributionLabel;
    private JLabel parametersLabel;
    private JLabel cpnLabel;
    private JLabel maxTotalDurLabel;
    private JLabel sdLabel;
    private JLabel meanLabel;
    private JLabel timeSpentLabel;
    private JLabel pleaseWaitLabel;
    
    //repetitionsField: input per il numero di ripetizioni della simulazione
    private JTextField repetitionsField;
    //activitiesNumField: mostra il numero delle attività correnti
    private JTextField activitiesNumField;
    //activitiesField: primo campo della tabella; ID dell'attività
    private JTextField activitiesField;
    //descriptionField: secondo campo della tabella; descrizione dell'attività
    private JTextField descriptionField;
    //precedencesField: terzo campo della tabella; precedenze dell'attività
    private JTextField precedencesField;
    //parametersField: quinto campo della tabella; parametri della distribuzione
    private JTextField[] parametersField;
    //cpnField: sesto campo della tabella; critical path node
    private JTextField cpnField;
    //numActField: numero di attività da inserire/rimuovere
    private JTextField numActField;
    //maxTotalDurField: risultato del Monte Carlo Simulation
    private JTextField maxTotalDurField;
    //meanField: result mean
    private JTextField meanField;
    //sdField: deviazione standard dei risultati ottenuti
    private JTextField sdField;
    //timeSpentField: tempo trascorso per il calcolo
    private JTextField timeSpentField;
    
    //activitiesArray: array che memorizza i campi di ogni attività
    //Posizione 0: Activities
    //Posizione 1: Description
    //Posizione 2: Precedences
    //Posizione 3: Distribution
    //Posizione 4: Parameters
    //Posizione 5: Critical Path Node
    private ArrayList<ArrayList<Object>> activitiesArray;

    //tableScroll: JScrollPane contenente il pannello tableSuperPanel
    private JScrollPane tableScroll;
    
    private File lastFolder = null;

    //*************************************************
    //*** Costruttore MainPanel() *********************
    //*************************************************  
    public MainPanel(boolean s, File f){
        
        //---------------------------------------------------------------------
        //Impostazione MainPanel
        //---------------------------------------------------------------------
        this.setLayout(new BorderLayout());
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili varie
        //---------------------------------------------------------------------
        verbose = s;
        limTable = new GridBagConstraints();
        limRepetitions = new GridBagConstraints();
        activitiesNumber = 1;
        startSimulations = 0;
        stopSimulations = 0;
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili ArrayList
        //---------------------------------------------------------------------
        activitiesArray = new ArrayList<ArrayList<Object>>();
        
        
        //---------------------------------------------------------------------
        //Inizializzaione variabili JPanel ed impostazioni dei Layout
        //---------------------------------------------------------------------
        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.setBorder(new TitledBorder(new MatteBorder(2, 2, 2, 2, Color.black), "User Panel"));
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new TitledBorder(new MatteBorder(2, 2, 2, 2, Color.black), "Activities Chart"));
        
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(new TitledBorder(new MatteBorder(2, 2, 2, 2, Color.black), "Results Panel"));
        
        tablePanel = new JPanel();
        tablePanel.setLayout(new GridBagLayout()); 
        
        tableSuperPanel = new JPanel();
        tableSuperPanel.setLayout(new BorderLayout());
        
        repetitionsPanel = new JPanel();
        repetitionsPanel.setLayout(new GridBagLayout());
        repetitionsPanel.setBackground(new Color(80, 172, 236));
        
        menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new FlowLayout());
        menuButtonPanel.setBackground(new Color(108, 128, 214));

        activityButtPanel = new JPanel();
        activityButtPanel.setLayout(new FlowLayout());
        
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(4, 2));
        resultsPanel.setBackground(new Color(80, 172, 236));
        
        
        //---------------------------------------------------------------------
        //Inizializzaione variabili JScrollPane
        //---------------------------------------------------------------------
        tableScroll = new JScrollPane(tableSuperPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroll.setPreferredSize(new Dimension (770,300));
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JButton ed impostazioni
        //---------------------------------------------------------------------
        addActivityButton = new JButton("Add Activities");
        remActivityButton = new JButton("Remove Activities");
        resetButton = new JButton("Reset Fields");
        computeButton = new JButton("Compute");
        computeButton.setForeground(new Color(174, 0, 0));
        newButton = new JButton("New");
        newButton.setBackground(new Color(108, 128, 214));
        openButton = new JButton("Open");
        openButton.setBackground(new Color(108, 128, 214));
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(108, 128, 214));
        helpButton = new JButton("Help");
        helpButton.setBackground(new Color(108, 128, 214));
        aboutButton = new JButton("About");
        aboutButton.setBackground(new Color(108, 128, 214));
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JLabel/JTextField relative al JPanel
        //repetitionsPanel ed impostazioni/aggiunta al GridBagLayout
        //---------------------------------------------------------------------
        repetitionsLabel = new JLabel("Number of Repetitions of the Simulation: ");
        limRepetitions.fill = GridBagConstraints.NONE;
        limRepetitions.anchor = GridBagConstraints.LINE_END;
        limRepetitions.weightx = 0.0;
        limRepetitions.weighty = 0.0;
	limRepetitions.gridx = 0;
	limRepetitions.gridy = 0;
        repetitionsPanel.add(repetitionsLabel, limRepetitions);
        
        repetitionsField = new JTextField("1", 10);
        repetitionsField.setEditable(true);
        repetitionsField.setHorizontalAlignment(JTextField.LEFT);
        limRepetitions.fill = GridBagConstraints.NONE;
        limRepetitions.weightx = 0.0;
        limRepetitions.weighty = 0.0;
	limRepetitions.gridx = 1;
	limRepetitions.gridy = 0;
        repetitionsPanel.add(repetitionsField, limRepetitions);
        
        activitiesNumLabel = new JLabel("Number of Current Activities: ");
        limRepetitions.fill = GridBagConstraints.NONE;
        limRepetitions.anchor = GridBagConstraints.LINE_END;
        limRepetitions.weightx = 0.0;
        limRepetitions.weighty = 0.0;
	limRepetitions.gridx = 0;
	limRepetitions.gridy = 1;
        repetitionsPanel.add(activitiesNumLabel, limRepetitions);
        
        activitiesNumField = new JTextField(""+activitiesNumber, 10);
        activitiesNumField.setEditable(false);
        activitiesNumField.setHorizontalAlignment(JTextField.LEFT);
        limRepetitions.fill = GridBagConstraints.NONE;
        limRepetitions.weightx = 0.0;
        limRepetitions.weighty = 0.0;
	limRepetitions.gridx = 1;
	limRepetitions.gridy = 1;
        repetitionsPanel.add(activitiesNumField, limRepetitions);
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili Filler
        //---------------------------------------------------------------------
        fillerPanel = new JPanel();
        fillerPanel.setBackground(new Color(80, 172, 236));        

        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JLabel relative alla tabella
        //ed impostazioni/aggiunta al GridBagLayout
        //---------------------------------------------------------------------
        activitiesLabel = new JLabel("Activities", JLabel.CENTER);
        activitiesLabel.setOpaque(true);
        activitiesLabel.setBackground(new Color(25, 255, 225));
        activitiesLabel.setPreferredSize(new Dimension(80,30));
        activitiesLabel.setMinimumSize(new Dimension(80,30));        
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 0;
	limTable.gridy = 0;
        tablePanel.add(activitiesLabel, limTable);
        
        descriptionLabel = new JLabel("Description", JLabel.CENTER);
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(new Color(25, 225, 200));
        descriptionLabel.setPreferredSize(new Dimension(200,30));
        descriptionLabel.setMinimumSize(new Dimension(200,30));
        limTable.fill = GridBagConstraints.HORIZONTAL;
	limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 1;
	limTable.gridy = 0;
        tablePanel.add(descriptionLabel, limTable);
        
        precedencesLabel = new JLabel("Precedences", JLabel.CENTER);
        precedencesLabel.setOpaque(true);
        precedencesLabel.setBackground(new Color(25, 255, 225));
        precedencesLabel.setPreferredSize(new Dimension(130,30));
        precedencesLabel.setMinimumSize(new Dimension(130,30));
        limTable.fill = GridBagConstraints.HORIZONTAL;
	limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 2;
	limTable.gridy = 0;
        tablePanel.add(precedencesLabel, limTable);
        
        distributionLabel = new JLabel("Distributions", JLabel.CENTER);
        distributionLabel.setOpaque(true);
        distributionLabel.setBackground(new Color(25, 225, 200));
        distributionLabel.setPreferredSize(new Dimension(120,30));
        distributionLabel.setMinimumSize(new Dimension(120,30));
        limTable.fill = GridBagConstraints.HORIZONTAL;
	limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 3;
	limTable.gridy = 0;
        tablePanel.add(distributionLabel, limTable);
        
        parametersLabel = new JLabel("Parameters", JLabel.CENTER);
        parametersLabel.setOpaque(true);
        parametersLabel.setBackground(new Color(25, 255, 225));
        parametersLabel.setPreferredSize(new Dimension(120,30));
        parametersLabel.setMinimumSize(new Dimension(120,30));
        limTable.fill = GridBagConstraints.HORIZONTAL;
	limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 4;
	limTable.gridy = 0;
        tablePanel.add(parametersLabel, limTable);
        
        cpnLabel = new JLabel ("Critical Path Node", JLabel.CENTER);
        cpnLabel.setOpaque(true);
        cpnLabel.setBackground(new Color(25, 225, 200));
        cpnLabel.setPreferredSize(new Dimension(130,30));
        cpnLabel.setMinimumSize(new Dimension(130,30));
        limTable.fill = GridBagConstraints.HORIZONTAL;
	limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
	limTable.gridx = 5;
	limTable.gridy = 0;
        tablePanel.add(cpnLabel, limTable);
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JTextField e JComboBox relative alla tabella
        //ed impostazioni/aggiunta al GridBagLayout per la prima riga di Activities
        //---------------------------------------------------------------------
        activitiesArray.add(new ArrayList<Object>());
        
        activitiesField = new JTextField(""+(activitiesNumber));
        activitiesField.setEditable(false);
        activitiesField.setHorizontalAlignment(JTextField.CENTER);
        activitiesField.setPreferredSize(new Dimension(80,23));
        activitiesField.setMinimumSize(new Dimension(80,23));
        activitiesArray.get(0).add(activitiesField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 0;
        limTable.gridy = 1;
        tablePanel.add((JTextField)activitiesArray.get(0).get(0), limTable);
        
        descriptionField = new JTextField();
        descriptionField.setEditable(true);
        descriptionField.setHorizontalAlignment(JTextField.CENTER);
        descriptionField.setPreferredSize(new Dimension(200,23));
        descriptionField.setMinimumSize(new Dimension(200,23));
        activitiesArray.get(0).add(descriptionField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 1;
        limTable.gridy = 1;
        tablePanel.add((JTextField)activitiesArray.get(0).get(1), limTable);
        
        precedencesField = new JTextField();
        precedencesField.setEditable(true);
        precedencesField.setHorizontalAlignment(JTextField.CENTER);
        precedencesField.setPreferredSize(new Dimension(130,23));
        precedencesField.setMinimumSize(new Dimension(130,23));
        activitiesArray.get(0).add(precedencesField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 2;
        limTable.gridy = 1;
        tablePanel.add((JTextField)activitiesArray.get(0).get(2), limTable);
        
        distributionCombo = new JComboBox();

        // ToDo (bit-man) replace using Distribution enum (check warnings!)
        distributionCombo.addItem("Uniform");
        distributionCombo.addItem("Triangular");
        distributionCombo.addItem("Beta");
        distributionCombo.addItem("Gaussian");
        distributionCombo.addItem("Exponential");
        distributionCombo.setPreferredSize(new Dimension(120,23));
        distributionCombo.setMinimumSize(new Dimension(120,23));
        activitiesArray.get(0).add(distributionCombo);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 3;
        limTable.gridy = 1;
        tablePanel.add((JComboBox)activitiesArray.get(0).get(3), limTable);
        
        //Associazione elemento JComboBox al DistributionListener
        ((JComboBox)activitiesArray.get(0).get(3)).addItemListener(new DistributionListener());
        
        parametersPanel = new JPanel(new GridLayout(1, 2));
        parametersField = new JTextField[2];
        parametersField[0] = new JTextField();
        parametersField[0].setEditable(true);
        parametersField[0].setHorizontalAlignment(JTextField.CENTER);
        parametersField[0].setToolTipText("Parameter a");
        parametersField[1] = new JTextField();
        parametersField[1].setEditable(true);
        parametersField[1].setHorizontalAlignment(JTextField.CENTER);
        parametersField[1].setToolTipText("Parameter b");
        parametersPanel.add(parametersField[0]);
        parametersPanel.add(parametersField[1]);
        activitiesArray.get(0).add(parametersPanel);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 4;
        limTable.gridy = 1;
        tablePanel.add((JPanel)activitiesArray.get(0).get(4), limTable);
        
        cpnField = new JTextField();
        cpnField.setEditable(false);
        cpnField.setHorizontalAlignment(JTextField.CENTER);
        cpnField.setPreferredSize(new Dimension(130,23));
        cpnField.setMinimumSize(new Dimension(130,23));
        activitiesArray.get(0).add(cpnField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 5;
        limTable.gridy = 1;
        tablePanel.add((JTextField)activitiesArray.get(0).get(5), limTable);
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JTextField relative all'activityButtPanel
        //---------------------------------------------------------------------
        numActField = new JTextField("1", 3);
        numActField.setEditable(true);
        numActField.setHorizontalAlignment(JTextField.CENTER);
        
        
        //---------------------------------------------------------------------
        //Inizializzazione variabili JLabel/JTextField relative al southPanel
        //---------------------------------------------------------------------
        maxTotalDurLabel = new JLabel("Max Total Duration (days): ", JLabel.RIGHT);
        sdLabel = new JLabel("Standard Deviation (days): ", JLabel.RIGHT);
        meanLabel = new JLabel("Mean (days): ",  JLabel.RIGHT);
        timeSpentLabel = new JLabel("Time Spent (sec): ", JLabel.RIGHT);
        pleaseWaitLabel = new JLabel("...Please Wait...", JLabel.CENTER);
        pleaseWaitLabel.setForeground(new Color(255,0,0));
        pleaseWaitLabel.setVisible(false);
        maxTotalDurField = new JTextField(10);
        maxTotalDurField.setEditable(false);
        sdField = new JTextField(10);
        sdField.setEditable(false);
        meanField = new JTextField(10);
        meanField.setEditable(false);
        timeSpentField = new JTextField(""+0.0, 10);
        timeSpentField.setEditable(false);
                        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi menuButtonPanel
        //---------------------------------------------------------------------
        menuButtonPanel.add(newButton);
        menuButtonPanel.add(openButton);
        menuButtonPanel.add(saveButton);
        menuButtonPanel.add(helpButton);
        menuButtonPanel.add(aboutButton);
        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi activityButtPanel
        //---------------------------------------------------------------------
        activityButtPanel.add(numActField);
        activityButtPanel.add(addActivityButton);
        activityButtPanel.add(remActivityButton);
        activityButtPanel.add(resetButton);
        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi resultsPanel
        //---------------------------------------------------------------------
        resultsPanel.add(maxTotalDurLabel);
        resultsPanel.add(maxTotalDurField);
        resultsPanel.add(sdLabel);
        resultsPanel.add(sdField);
        resultsPanel.add(meanLabel);
        resultsPanel.add(meanField);
        resultsPanel.add(timeSpentLabel);
        resultsPanel.add(timeSpentField);        
        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi tableSuperPanel
        //---------------------------------------------------------------------
        tableSuperPanel.add(tablePanel, BorderLayout.NORTH);
        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi northPanel
        //---------------------------------------------------------------------
        northPanel.add(repetitionsPanel, BorderLayout.WEST);
        northPanel.add(fillerPanel, BorderLayout.CENTER);
        northPanel.add(menuButtonPanel, BorderLayout.EAST);
        
        //---------------------------------------------------------------------
        //Aggiunta elementi centerPanel
        //---------------------------------------------------------------------
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(activityButtPanel, BorderLayout.SOUTH);

        
        //---------------------------------------------------------------------
        //Aggiunta elementi southPanel
        //---------------------------------------------------------------------
        southPanel.add(resultsPanel, BorderLayout.WEST);
        southPanel.add(pleaseWaitLabel, BorderLayout.SOUTH);
        southPanel.add(computeButton, BorderLayout.EAST);
        
        
        //---------------------------------------------------------------------
        //Aggiunta elementi MainPanel
        //---------------------------------------------------------------------
        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
        
        
        //---------------------------------------------------------------------
        //Associazione elementi al MainListener
        //---------------------------------------------------------------------
        addActivityButton.addActionListener(new MainListener());
        remActivityButton.addActionListener(new MainListener());
        resetButton.addActionListener(new MainListener());
        newButton.addActionListener(new MainListener());
        openButton.addActionListener(new MainListener());
        saveButton.addActionListener(new MainListener());
        helpButton.addActionListener(new MainListener());
        aboutButton.addActionListener(new MainListener());
        computeButton.addActionListener(new MainListener());
        
        if ( f != null )
            openProject(f);
    }
    
    
    
    
    //*************************************************
    //*** METODO addActivity() ************************
    //*************************************************
    public void addActivity() {
                
        activitiesArray.add(new ArrayList<Object>());
        activitiesNumber++;
        
        activitiesField = new JTextField(""+(activitiesNumber));
        activitiesField.setEditable(false);
        activitiesField.setHorizontalAlignment(JTextField.CENTER);
        activitiesField.setPreferredSize(new Dimension(80,23));
        activitiesField.setMinimumSize(new Dimension(80,23));
        activitiesArray.get(activitiesNumber-1).add(activitiesField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 0;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JTextField)activitiesArray.get(activitiesNumber-1).get(0), limTable);
        
        descriptionField = new JTextField();
        descriptionField.setEditable(true);
        descriptionField.setHorizontalAlignment(JTextField.CENTER);
        descriptionField.setPreferredSize(new Dimension(200,23));
        descriptionField.setMinimumSize(new Dimension(200,23));
        activitiesArray.get(activitiesNumber-1).add(descriptionField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 1;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JTextField)activitiesArray.get(activitiesNumber-1).get(1), limTable);
        
        precedencesField = new JTextField();
        precedencesField.setEditable(true);
        precedencesField.setHorizontalAlignment(JTextField.CENTER);
        precedencesField.setPreferredSize(new Dimension(130,23));
        precedencesField.setMinimumSize(new Dimension(130,23));
        activitiesArray.get(activitiesNumber-1).add(precedencesField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 2;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JTextField)activitiesArray.get(activitiesNumber-1).get(2), limTable);
        
        distributionCombo = new JComboBox();

        // ToDo (bit-man) replace using Distribution enum (check warnings!)
        distributionCombo.addItem("Uniform");
        distributionCombo.addItem("Triangular");
        distributionCombo.addItem("Beta");
        distributionCombo.addItem("Gaussian");
        distributionCombo.addItem("Exponential");
        distributionCombo.setPreferredSize(new Dimension(120,23));
        distributionCombo.setMinimumSize(new Dimension(120,23));
        activitiesArray.get(activitiesNumber-1).add(distributionCombo);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 3;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JComboBox)activitiesArray.get(activitiesNumber-1).get(3), limTable);
        
        //Associazione elemento JComboBox al DistributionListener
        ((JComboBox)activitiesArray.get(activitiesNumber-1).get(3)).addItemListener(new DistributionListener());
        
        parametersPanel = new JPanel(new GridLayout(1, 2));
        parametersField = new JTextField[2];
        parametersField[0] = new JTextField();
        parametersField[0].setEditable(true);
        parametersField[0].setHorizontalAlignment(JTextField.CENTER);
        parametersField[0].setToolTipText("Parameter a");
        parametersField[1] = new JTextField();
        parametersField[1].setEditable(true);
        parametersField[1].setHorizontalAlignment(JTextField.CENTER);
        parametersField[1].setToolTipText("Parameter b");
        parametersPanel.add(parametersField[0]);
        parametersPanel.add(parametersField[1]);
        activitiesArray.get(activitiesNumber-1).add(parametersPanel);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 4;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JPanel)activitiesArray.get(activitiesNumber-1).get(4), limTable);
        
        cpnField = new JTextField();
        cpnField.setEditable(false);
        cpnField.setHorizontalAlignment(JTextField.CENTER);
        cpnField.setPreferredSize(new Dimension(130,23));
        cpnField.setMinimumSize(new Dimension(130,23));
        activitiesArray.get(activitiesNumber-1).add(cpnField);
        limTable.fill = GridBagConstraints.HORIZONTAL;
        limTable.weightx = 0.16666;
        limTable.weighty = 0.0;
        limTable.gridx = 5;
        limTable.gridy = activitiesNumber;
        tablePanel.add((JTextField)activitiesArray.get(activitiesNumber-1).get(5), limTable);
        
    }
    
    
    //*************************************************
    //*** METODO remActivity() ************************
    //*************************************************
    public void remActivity() {
        
        if(activitiesNumber != 1) {
            
            tablePanel.remove((JTextField)activitiesArray.get(activitiesNumber-1).get(0));        
            tablePanel.remove((JTextField)activitiesArray.get(activitiesNumber-1).get(1));        
            tablePanel.remove((JTextField)activitiesArray.get(activitiesNumber-1).get(2));
            tablePanel.remove((JComboBox)activitiesArray.get(activitiesNumber-1).get(3));        
            tablePanel.remove((JPanel)activitiesArray.get(activitiesNumber-1).get(4));        
            tablePanel.remove((JTextField)activitiesArray.get(activitiesNumber-1).get(5));
            
            activitiesArray.remove(activitiesNumber-1);
            
            activitiesNumber--;
        
        } else JOptionPane.showMessageDialog(this, "There must be at least one activity!", "Information", 
                                            JOptionPane.ERROR_MESSAGE);
        
    }
    
    
    //*************************************************
    //*** METODO resetFields() ************************
    //*************************************************
    public void resetFields() {
        
        for(int i=0; i<activitiesNumber; i++) {
            
            ((JTextField)activitiesArray.get(i).get(1)).setText("");
            ((JTextField)activitiesArray.get(i).get(2)).setText("");
            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Uniform");
            //Dato che alla JComboBox è associato il listener per il cambio dei
            //parametri, di conseguenza è sufficiente settare l'item su "Uniform"
            //ed il metodo paramChanging() ridisegnerà i parametri "a" e "b"
            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setText("");
            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setText("");
            ((JTextField)activitiesArray.get(i).get(5)).setText("");
            
        }
        
        maxTotalDurField.setText("");
        sdField.setText("");
        meanField.setText("");
        timeSpentField.setText(""+0.0);
    }
    
    
    //*************************************************
    //*** METODO paramChanging() **********************
    //*************************************************
    public void paramChanging(int indexActivity, String itemDistribution) {
        
        tablePanel.remove((JPanel)activitiesArray.get(indexActivity).get(4));
        activitiesArray.get(indexActivity).remove(4);
        
        if(itemDistribution.equals("Uniform")) {
        
            parametersPanel = new JPanel(new GridLayout(1, 2));
            parametersField = new JTextField[2];
            parametersField[0] = new JTextField();
            parametersField[0].setEditable(true);
            parametersField[0].setHorizontalAlignment(JTextField.CENTER);
            parametersField[0].setToolTipText("Parameter a");
            parametersField[1] = new JTextField();
            parametersField[1].setEditable(true);
            parametersField[1].setHorizontalAlignment(JTextField.CENTER);
            parametersField[1].setToolTipText("Parameter b");
            parametersPanel.add(parametersField[0]);
            parametersPanel.add(parametersField[1]);
            activitiesArray.get(indexActivity).add(4, parametersPanel);
            limTable.fill = GridBagConstraints.HORIZONTAL;
            limTable.weightx = 0.16666;
            limTable.weighty = 0.0;
            limTable.gridx = 4;
            limTable.gridy = indexActivity+1;
            tablePanel.add((JPanel)activitiesArray.get(indexActivity).get(4), limTable);

        } else
        
        
        if(itemDistribution.equals("Triangular")) {
            
            parametersPanel = new JPanel(new GridLayout(1, 3));
            parametersField = new JTextField[3];
            parametersField[0] = new JTextField();
            parametersField[0].setEditable(true);
            parametersField[0].setHorizontalAlignment(JTextField.CENTER);
            parametersField[0].setToolTipText("Parameter Min");
            parametersField[1] = new JTextField();
            parametersField[1].setEditable(true);
            parametersField[1].setHorizontalAlignment(JTextField.CENTER);
            parametersField[1].setPreferredSize(new Dimension(37,23));
            parametersField[1].setMinimumSize(new Dimension(37,23));
            parametersField[1].setToolTipText("Parameter Mode");
            parametersField[2] = new JTextField();
            parametersField[2].setEditable(true);
            parametersField[2].setHorizontalAlignment(JTextField.CENTER);
            parametersField[2].setToolTipText("Parameter Max");
            parametersPanel.add(parametersField[0]);
            parametersPanel.add(parametersField[1]);
            parametersPanel.add(parametersField[2]);
            activitiesArray.get(indexActivity).add(4, parametersPanel);
            limTable.fill = GridBagConstraints.HORIZONTAL;
            limTable.weightx = 0.16666;
            limTable.weighty = 0.0;
            limTable.gridx = 4;
            limTable.gridy = indexActivity+1;
            tablePanel.add((JPanel)activitiesArray.get(indexActivity).get(4), limTable);
            
        } else
        
        
        if(itemDistribution.equals("Beta")) {
            
            parametersPanel = new JPanel(new GridLayout(1, 2));

            // ToDo (bit-man) perform same code usage in remaining distributions
            int numGUIParams = Distribution.BETA.getNumGUIParams();
            parametersField = new JTextField[numGUIParams];
            List<String> paramNamesGUI = Distribution.BETA.getParamNamesGUI();
            for( int i = 0; i < numGUIParams; i++) {
                parametersField[i] = new JTextField();
                parametersField[i].setEditable(true);
                parametersField[i].setHorizontalAlignment(JTextField.CENTER);
                parametersField[i].setToolTipText(paramNamesGUI.get(i));
                parametersPanel.add(parametersField[i]);
            }

            activitiesArray.get(indexActivity).add(4, parametersPanel);
            limTable.fill = GridBagConstraints.HORIZONTAL;
            limTable.weightx = 0.16666;
            limTable.weighty = 0.0;
            limTable.gridx = 4;
            limTable.gridy = indexActivity+1;
            tablePanel.add((JPanel)activitiesArray.get(indexActivity).get(4), limTable);
            
        } else
        
        
        if(itemDistribution.equals("Gaussian")) {
            
            parametersPanel = new JPanel(new GridLayout(1, 2));
            parametersField = new JTextField[2];
            parametersField[0] = new JTextField();
            parametersField[0].setEditable(true);
            parametersField[0].setHorizontalAlignment(JTextField.CENTER);
            parametersField[0].setToolTipText("Parameter mean");
            parametersField[1] = new JTextField();
            parametersField[1].setEditable(true);
            parametersField[1].setHorizontalAlignment(JTextField.CENTER);
            parametersField[1].setToolTipText("Parameter sd");
            parametersPanel.add(parametersField[0]);
            parametersPanel.add(parametersField[1]);
            activitiesArray.get(indexActivity).add(4, parametersPanel);
            limTable.fill = GridBagConstraints.HORIZONTAL;
            limTable.weightx = 0.16666;
            limTable.weighty = 0.0;
            limTable.gridx = 4;
            limTable.gridy = indexActivity+1;
            tablePanel.add((JPanel)activitiesArray.get(indexActivity).get(4), limTable);
            
        } else
        
        
        if(itemDistribution.equals("Exponential")) {
            
            parametersPanel = new JPanel(new GridLayout(1, 1));
            parametersField = new JTextField[1];
            parametersField[0] = new JTextField();
            parametersField[0].setEditable(true);
            parametersField[0].setHorizontalAlignment(JTextField.CENTER);
            parametersField[0].setToolTipText("Parameter mean");
            parametersPanel.add(parametersField[0]);
            activitiesArray.get(indexActivity).add(4, parametersPanel);
            limTable.fill = GridBagConstraints.HORIZONTAL;
            limTable.weightx = 0.16666;
            limTable.weighty = 0.0;
            limTable.gridx = 4;
            limTable.gridy = indexActivity+1;
            tablePanel.add((JPanel)activitiesArray.get(indexActivity).get(4), limTable);
            
        }
        
    }

    private List<List<Object>> getDataAct() {
    	List<Integer> activityId = new ArrayList<Integer>(activitiesNumber);
    	List<String> activityDesc = new ArrayList<String>(activitiesNumber);
    	List<String> activityPrecedence = new ArrayList<String>(activitiesNumber);
    	List<String> activityDistribution = new ArrayList<String>(activitiesNumber);
    	List<List<Integer>> activityDistributionParam = new ArrayList<List<Integer>>(activitiesNumber);
    	
    	for( int i = 0; i < activitiesNumber; i++) {
    		activityId.add( Integer.parseInt(((JTextField)activitiesArray.get(i).get(0)).getText()) );
    		activityDesc.add( ((JTextField)activitiesArray.get(i).get(1)).getText() );
    		activityPrecedence.add( ((JTextField)activitiesArray.get(i).get(2)).getText() );
    		
    		String distS = ((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().toString();
    		activityDistribution.add( distS );
    		
    		Distribution dist = 
    			Distribution.valueOf(
    				distS.toUpperCase() );
    		
    		List<Integer> paramL = new ArrayList<Integer>(dist.getNumGUIParams());
    		
    		for( int p = 0; p < dist.getNumGUIParams(); p++ ) {
    			paramL.add( Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(p))).getText()) );
    		}
    		
    		activityDistributionParam.add( paramL );
    	}
    	
    	DataActResponse resp = Tools.getDataAct(activitiesNumber, activityId,
    											activityDesc, activityPrecedence,
                                                activityDistribution, activityDistributionParam);
        
        	
    	for( int i = 0; i < activitiesNumber; i++) {
    		// Update precedences field
    		updatePrecedencesField((int[]) resp.getDataActivities().get(i).get(2), i, activitiesArray);
        }
 
        repaint();
        
        //----------------------------------------------------------
        //Controllo errori e restituzione dataActArray o errActArray
        //----------------------------------------------------------
        if (resp.getParamExceptions().size() == 0 && resp.getPrecErrors().size() == 0 && resp.getCyclicErrors().size() == 0 && resp.getParamErrors().size() == 0) {

            return resp.getDataActivities();

        } else {

            // OUTPUT ECCEZIONI ED ERRORI
            if (verbose) {
                System.out.println("Exceptions of Parameters: " + resp.getParamExceptions().size());
                System.out.println("Error of Parameters: " + resp.getParamErrors().size());
                System.out.println("Errors of Precedences: " + resp.getPrecErrors().size());
                System.out.println("Errors of cyclic Activities: " + resp.getCyclicErrors().size());
            }


            //Restituzione numero di errori e le attività che li contengono
            if (resp.getPrecErrors().size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < resp.getPrecErrors().size(); i++) {
                    if (i != resp.getPrecErrors().size() - 1) {
                        strBuff.append(resp.getPrecErrors().get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(resp.getPrecErrors().get(i) + 1);
                }

                if (verbose) {
                    System.out.println("ERROR: Precedences out of Bounds in the following Activities: " + strBuff.toString());
                }
                
				JOptionPane.showMessageDialog(null,
						"<html><body>Precedences out of Bounds in the following Activities:<br>" +
								"" + strBuff.toString() + "</body></html>", "Error",
						JOptionPane.ERROR_MESSAGE);
				
            }

            if (resp.getParamExceptions().size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < resp.getParamExceptions().size(); i++) {
                    if (i != resp.getParamExceptions().size() - 1) {
                        strBuff.append(resp.getParamExceptions().get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(resp.getParamExceptions().get(i) + 1);
                }


                if (verbose) {
                    System.out.println("ERROR: The following Activities contain parameters with an invalid format (it must be an integer): " + strBuff.toString());
                }
			 
				JOptionPane.showMessageDialog(null,
						"<html><body>The following Activities contain parameters<br>" +
								"with an invalid format (it must be an integer):<br>" +
								"" + strBuff.toString() + "</body></html>", "Error",
						JOptionPane.ERROR_MESSAGE);
            }

            if (resp.getParamErrors().size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < resp.getParamErrors().size(); i++) {
                    if (i != resp.getParamErrors().size() - 1) {
                        strBuff.append(resp.getParamErrors().get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(resp.getParamErrors().get(i) + 1);
                }


                if (verbose) {
                    System.out.println("ERROR: The following Activities contain incorrect parameters: " + strBuff.toString());
                }
                
				JOptionPane.showMessageDialog(null,
						"<html><body>The following Activities contain incorrect parameters:<br>" +
								"" + strBuff.toString() + "</body></html>", "Error",
						JOptionPane.ERROR_MESSAGE);

            }

            if (resp.getCyclicErrors().size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < resp.getCyclicErrors().size(); i++) {
                    if (i != resp.getCyclicErrors().size() - 1) {
                        strBuff.append(resp.getCyclicErrors().get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(resp.getCyclicErrors().get(i) + 1);
                }


                if (verbose) {
                    System.out.println("ERROR: The following Activities are cyclical: " + strBuff.toString());
                }

				JOptionPane.showMessageDialog(null,
						"<html><body>The following Activities are cyclical:<br>" +
								"" + strBuff.toString() + "</body></html>", "Error",
						JOptionPane.ERROR_MESSAGE);

            }

            //errActArray: array fittizio per il controllo nel listener
            List<List<Object>> errActArray = new ArrayList<List<Object>>();
            errActArray.add(new ArrayList<Object>());
            errActArray.get(0).add(-1);

            return errActArray;
        }

    }
    
    
    private void updatePrecedencesField(int[] precArray, int currentActivity, ArrayList<ArrayList<Object>> activitiesArray1) {

        if (precArray[0] != -1) {
            StringBuffer strBuff = new StringBuffer();

            for (int i = 0; i < precArray.length; i++) {
                if (i != precArray.length - 1) {
                    strBuff.append(precArray[i]);
                    strBuff.append("; ");
                } else {
                    strBuff.append(precArray[i]);
                }
            }

            ((JTextField) activitiesArray1.get(currentActivity).get(2)).setText(strBuff.toString());
        } else
            ((JTextField) activitiesArray1.get(currentActivity).get(2)).setText("");

    }

        
	private ArrayList<ArrayList<Object>> getDataActOLD() {
        //occurrences: occorrence di numeri uguali in extractIntArray
        int occurrences = 0;
        
        //precOutOfBounds: contatore per precErrors
        int precOutOfBounds = 0;
        
        //dataActArray: array che memorizza i dati di ogni attività
        //Posizione 0: Activities
        //Posizione 1: Description
        //Posizione 2: Precedences
        //Posizione 3: Distribution
        //Posizione 4: Parameters
        ArrayList<ArrayList<Object>> dataActArray = new ArrayList<ArrayList<Object>>();
        
        //precErrors: array che memorizza le attività contenenti errori
        //nella stringa delle precedences riguardo il numero delle precedenze.
        ArrayList<Integer> precErrors = new ArrayList<Integer>();
        
        //cyclicErrors: array che memorizza le attività contenenti errori
        //nella stringa delle precedences riguardo i cicli
        ArrayList<Integer> cyclicErrors = new ArrayList<Integer>();
        
        //paramErrors: array che memorizza le attività contenenti errori
        //nei campi parameters con condizioni sbagliate.
        ArrayList<Integer> paramErrors = new ArrayList<Integer>();
        
        //paramExceptions: array che memorizza le attività che hanno lanciato
        //eccezioni dovute ai campi parameters.
        ArrayList<Integer> paramExceptions = new ArrayList<Integer>();
        
        
        for(int i=0; i<activitiesNumber; i++) {
           
            dataActArray.add(new ArrayList<Object>());
           
            try {
                
                //------------------------------------------
                //Inserimento elemento 0: activities
                //------------------------------------------
                dataActArray.get(i).add(Integer.parseInt(((JTextField)activitiesArray.get(i).get(0)).getText()));
                
                //------------------------------------------
                //Inserimento elemento 1: description
                //------------------------------------------
                dataActArray.get(i).add(((JTextField)activitiesArray.get(i).get(1)).getText());
                
                //---------------------------------------------------------
                //Controllo stringa delle precedenze prima dell'inserimento
                //---------------------------------------------------------
                String precedencesString = ((JTextField)activitiesArray.get(i).get(2)).getText();
                //Estrazione numeri dalla stringa del campo (JTextField) precedences
                ArrayList<Integer> extractIntArray = Tools.extractingNumbers(precedencesString);
                
                //Controllo di numeri doppioni in extractIntArray e copia
                //del risultato in precedencesIntArray
                ArrayList<Integer> precedencesIntArray = new ArrayList<Integer>();
                for(int k=0; k<extractIntArray.size(); k++) {
                    if(k==0) {
                        precedencesIntArray.add(extractIntArray.get(0));
                    } else {
                        for(int z=0; z<precedencesIntArray.size(); z++) {
                            if(precedencesIntArray.get(z) == extractIntArray.get(k)) {
                                occurrences = 1;
                            }
                        }
                        if(occurrences == 0) {
                            precedencesIntArray.add(extractIntArray.get(k));
                        } else occurrences = 0;
                    }
                }
                
                //Allocazione dimensione per l'array statico
                int[] precedencesInt = new int[precedencesIntArray.size()];
                //Copia degli elementi da precedencesIntArray a precedencesInt
                for(int j=0; j<precedencesIntArray.size(); j++) {
                    precedencesInt[j] = precedencesIntArray.get(j);
                }
                //Controllo che le precedenze scritte non siano pari a 0,
                //maggiori delle attività totali o uguali all'attività corrente
                for(int j=0; j<precedencesInt.length; j++) {
                    if(precedencesInt[j] == i+1 || precedencesInt[j] == 0 || precedencesInt[j] > activitiesNumber)
                        precOutOfBounds++;
                }
                //Se l'if precedente è soddisfatto incremento precOutOfBounds
                //e memorizzo in precErrors il numero dell'attività con l'errore
                if (precOutOfBounds != 0) {
                    precErrors.add(i);
                    precOutOfBounds = 0;
                }

                //Aggiornamento campi (JTextField) precedences
                updatePrecedencesField(precedencesInt, i, activitiesArray);
                
                //------------------------------------------
                //Inserimento elemento 2: precedences
                //------------------------------------------
                dataActArray.get(i).add(precedencesInt);
                
                //------------------------------------------
                //Inserimento elemento 3: distribution
                //------------------------------------------
                dataActArray.get(i).add(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().toString());

                //------------------------------------------
                //Inserimento elemento 4: parameters
                //------------------------------------------
                if(dataActArray.get(i).get(3).equals("Uniform")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).getText());
                    paramArray[1] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).getText());
                    //Controllo delle condizioni sui parametri
                    if(paramArray[1] > paramArray[0])
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);
                    
                } else

                if(dataActArray.get(i).get(3).equals("Triangular")) {

                    int[] paramArray = new int[3];
                    paramArray[0] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).getText());
                    paramArray[1] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).getText());
                    paramArray[2] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(2))).getText());
                    //Controllo delle condizioni sui parametri
                    if((paramArray[0] <= paramArray[1]) && (paramArray[1] <= paramArray[2]))
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else

                if(dataActArray.get(i).get(3).equals("Beta")) {

                    // ToDo (bit-man) replicate this change in remaining distributions
                    int numParams = Distribution.BETA.getNumGUIParams();
                    int[] paramArray = new int[numParams];
                    for( int j = 0; j < numParams; j++ )
                        paramArray[j] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(j))).getText());

                    //Controllo delle condizioni sui parametri
                    if(paramArray[0] > 0 && paramArray[1] > 0)
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else

                if(dataActArray.get(i).get(3).equals("Gaussian")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).getText());
                    paramArray[1] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).getText());
                    //Controllo delle condizioni sui parametri
                    if(paramArray[1] > 0)
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else

                if(dataActArray.get(i).get(3).equals("Exponential")) {

                    int[] paramArray = new int[1];
                    paramArray[0] = Integer.parseInt(((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).getText());
                    //Controllo delle condizioni sui parametri
                    if(paramArray[0] > 0)
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                }
            
            }
            
            catch(NumberFormatException exception) {
                
                paramExceptions.add(i);
                
            }
               
        }
        
        //Refresh MainPanel
        repaint();
        
        //----------------------------------
        //Controllo ciclicità nelle attività
        //----------------------------------
        if(paramExceptions.size() == 0 && precErrors.size() == 0 && paramErrors.size() == 0) {
            
            ArrayList<Integer> duplicateCyclicErrors = new ArrayList<Integer>();
            
            int compareActivity = 0;
            
            for(int i=0; i<dataActArray.size(); i++) {
                for(int k=0; k<((int[])dataActArray.get(i).get(2)).length; k++) {
                    if(((int[])dataActArray.get(i).get(2))[k] != -1) {
                        compareActivity = ((int[])dataActArray.get(i).get(2))[k]-1;
                        for(int z=0; z<((int[])dataActArray.get(compareActivity).get(2)).length; z++) {
                            if(((int[])dataActArray.get(compareActivity).get(2))[z] != -1) {
                                if(((int[])dataActArray.get(compareActivity).get(2))[z] == i+1) {

                                    duplicateCyclicErrors.add(i);
                                }
                            }
                        }
                    }
                }
            }
            
            //------------------------------------------------------
            //Controllo di numeri doppioni in duplicateCyclicErrors
            //e copia del risultato in cyclicErrors
            //------------------------------------------------------
            for(int k=0; k<duplicateCyclicErrors.size(); k++) {
                if(k==0) {
                    cyclicErrors.add(duplicateCyclicErrors.get(0));
                } else {
                    for(int z=0; z<cyclicErrors.size(); z++) {
                        if(cyclicErrors.get(z) == duplicateCyclicErrors.get(k)) {
                            occurrences = 1;
                        }
                    }
                    if(occurrences == 0) {
                        cyclicErrors.add(duplicateCyclicErrors.get(k));
                    } else occurrences = 0;
                }
            }
            
        }
        
        //----------------------------------------------------------
        //Controllo errori e restituzione dataActArray o errActArray
        //----------------------------------------------------------
        if(paramExceptions.size() == 0 && precErrors.size() == 0 && cyclicErrors.size() == 0 && paramErrors.size() == 0) {
            
            return dataActArray;
            
        } else {
            
            // OUTPUT ECCEZIONI ED ERRORI
            if(verbose) {
                System.out.println("Exceptions of Parameters: "+paramExceptions.size());
                System.out.println("Error of Parameters: "+paramErrors.size());
                System.out.println("Errors of Precedences: "+precErrors.size());
                System.out.println("Errors of cyclic Activities: "+cyclicErrors.size());
            }
            
            
            //Restituzione numero di errori e le attività che li contengono
            if(precErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for(int i=0; i<precErrors.size(); i++) {
                    if(i != precErrors.size()-1) {
                        strBuff.append(precErrors.get(i)+1);
                        strBuff.append("; ");
                    } else strBuff.append(precErrors.get(i)+1);
                }
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>Precedences out of Bounds in the following Activities:<br>" +
                    ""+strBuff.toString()+"</body></html>", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                
            }
            
            if(paramExceptions.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for(int i=0; i<paramExceptions.size(); i++) {
                    if(i != paramExceptions.size()-1) {
                        strBuff.append(paramExceptions.get(i)+1);
                        strBuff.append("; ");
                    } else strBuff.append(paramExceptions.get(i)+1);
                }
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>The following Activities contain parameters<br>" +
                    "with an invalid format (it must be an integer):<br>" +
                    ""+strBuff.toString()+"</body></html>", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            if(paramErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for(int i=0; i<paramErrors.size(); i++) {
                    if(i != paramErrors.size()-1) {
                        strBuff.append(paramErrors.get(i)+1);
                        strBuff.append("; ");
                    } else strBuff.append(paramErrors.get(i)+1);
                }
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>The following Activities contain incorrect parameters:<br>" +
                    ""+strBuff.toString()+"</body></html>", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                
            }
            
            if(cyclicErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for(int i=0; i<cyclicErrors.size(); i++) {
                    if(i != cyclicErrors.size()-1) {
                        strBuff.append(cyclicErrors.get(i)+1);
                        strBuff.append("; ");
                    } else strBuff.append(cyclicErrors.get(i)+1);
                }
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>The following Activities are cyclical:<br>" +
                    ""+strBuff.toString()+"</body></html>", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                
            }
            
            //errActArray: array fittizio per il controllo nel listener
            ArrayList<ArrayList<Object>> errActArray = new ArrayList<ArrayList<Object>>();
            errActArray.add(new ArrayList<Object>());
            errActArray.get(0).add(-1);
            
            return errActArray;
            
        }
       
    }
    
    
    //*************************************************
    //*** METODO getRepetitions() *********************
    //*************************************************
    public int getRepetitions() {

        return Integer.parseInt(repetitionsField.getText());
        
    }
    
    
    //*************************************************
    //*** METODO saveProject() ************************
    //*************************************************
    public void saveProject() {
        
        List<List<Object>> saveActArray = getDataAct();
        
        if(saveActArray.get(0).size() > 1) {
            JFileChooser chooser = new JFileChooser();
            
            /** Extension Filter for Java 6
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Monte Carlo Project", "mcp");
            chooser.setFileFilter(filter);
            * oppure
            chooser.addChoosableFileFilter(filter);
            **/

            // Extension Filter for Java 5
            String[] mcp = new String[] {"mcp"};
            chooser.addChoosableFileFilter(new ExtFilter(mcp, "Monte Carlo Project (*.mcp)"));
            chooser.setCurrentDirectory( lastFolder );
            
            if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                	lastFolder = chooser.getCurrentDirectory();
                    File selectedFile = chooser.getSelectedFile();
                    Tools.saveProject(selectedFile, saveActArray);
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,
                        ""+e.toString(), "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
    
    //*************************************************
    //*** METODO openProject() ************************
    //*************************************************
    @SuppressWarnings("unchecked")
    //Warning inevitabile nella chiamata in.readObject();
    public void openProject(File f) {
        
        boolean hasFile = (f != null);
        JFileChooser chooser = new JFileChooser();
        boolean exception_error = false;
        
        /** Extension Filter for Java 6
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Monte Carlo Project", "mcp");
        chooser.setFileFilter(filter);
        * oppure
        chooser.addChoosableFileFilter(filter);
        **/
        
        
        if ( ! hasFile) {
            // Extension Filter for Java 5
            String[] mcp = new String[] {"mcp"};
            chooser.addChoosableFileFilter(new ExtFilter(mcp, "Monte Carlo Project (*.mcp)"));
        }
        
        chooser.setCurrentDirectory( lastFolder );
        
        if( hasFile || chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            
        	lastFolder = chooser.getCurrentDirectory();
        
            ArrayList<ArrayList<Object>> openActArray = null;
            
            try {
                File selectedFile = hasFile ? f : chooser.getSelectedFile();
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(selectedFile));
                openActArray = (ArrayList<ArrayList<Object>>)in.readObject();
                in.close();
            }
            catch(Exception e) {
                exception_error = true;
                JOptionPane.showMessageDialog(null,
                    "The selected file is not valid or is corrupted!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            if(exception_error == false)
                if(openActArray.size() > 0) {

                    //Rimozione attività presenti nella schermata
                    for(int i=activitiesNumber; i>1; i--)
                        remActivity();

                    resetFields();

                    //Aggiornamento prima attività
                    ((JTextField)activitiesArray.get(0).get(1)).setText((String)openActArray.get(0).get(1));
                    updatePrecedencesField((int[]) openActArray.get(0).get(2), 0, activitiesArray);

                    if(((String)openActArray.get(0).get(3)).equals("Uniform")) {

                        ((JComboBox)activitiesArray.get(0).get(3)).setSelectedItem("Uniform");
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[0]));
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[1]));

                    } else


                    if(((String)openActArray.get(0).get(3)).equals("Triangular")) {

                        ((JComboBox)activitiesArray.get(0).get(3)).setSelectedItem("Triangular");
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[0]));
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[1]));
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(2))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[2]));

                    } else


                    if(((String)openActArray.get(0).get(3)).equals("Beta")) {

                        ((JComboBox)activitiesArray.get(0).get(3)).setSelectedItem("Beta");

                        // ToDo (bit-man) perform same replacement for remaining distributions
                        for( int j = 0; j < Distribution.BETA.getNumGUIParams(); j++)
                            ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(j))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[j]));

                    } else


                    if(((String)openActArray.get(0).get(3)).equals("Gaussian")) {

                        ((JComboBox)activitiesArray.get(0).get(3)).setSelectedItem("Gaussian");
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[0]));
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[1]));

                    } else


                    if(((String)openActArray.get(0).get(3)).equals("Exponential")) {

                        ((JComboBox)activitiesArray.get(0).get(3)).setSelectedItem("Exponential");
                        ((JTextField)(((JPanel)activitiesArray.get(0).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(0).get(4))[0]));
                    }


                    //Aggiornamento della attività 2 in poi
                    for(int i=1; i<openActArray.size(); i++) {

                        addActivity();

                        ((JTextField)activitiesArray.get(i).get(1)).setText((String)openActArray.get(i).get(1));
                        updatePrecedencesField((int[]) openActArray.get(i).get(2), i, activitiesArray);

                        if(((String)openActArray.get(i).get(3)).equals("Uniform")) {

                            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Uniform");
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[0]));
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[1]));

                        } else


                        if(((String)openActArray.get(i).get(3)).equals("Triangular")) {

                            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Triangular");
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[0]));
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[1]));
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(2))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[2]));

                        } else


                        if(((String)openActArray.get(i).get(3)).equals("Beta")) {

                            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Beta");

                            // ToDo (bit-man) perform same replacement for remaining distributions
                            for( int j = 0; j < Distribution.BETA.getNumGUIParams(); j++)
                                ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(j))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[j]));

                        } else


                        if(((String)openActArray.get(i).get(3)).equals("Gaussian")) {

                            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Gaussian");
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[0]));
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[1]));

                        } else


                        if(((String)openActArray.get(i).get(3)).equals("Exponential")) {

                            ((JComboBox)activitiesArray.get(i).get(3)).setSelectedItem("Exponential");
                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setText(Integer.toString(((int[])openActArray.get(i).get(4))[0]));

                        }

                    }

                    //Aggiornamento campo del numero delle attività
                    activitiesNumField.setText(""+activitiesNumber);

                }
        }
        
    }
    
    
    
    
    //*************************************************
    //*** LISTENER MainListener ***********************
    //*************************************************   
    class MainListener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            
            if(e.getSource() == addActivityButton) {
                
                try {
                    
                    int numAct = Integer.parseInt(numActField.getText());
                    
                    if (numAct > 0) {
                        
                        int totalAct = numAct+activitiesNumber;
                        int permittedAct = 500-activitiesNumber;
                
                        if(totalAct <= 500) {
                            for(int i=0; i<numAct; i++)
                                addActivity();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "<html><body>Maximum number of permitted activities: 500<br>" +
                                "Please Waiting... Inclusion of "+permittedAct+" activities</body></html>", "Error", 
                                JOptionPane.ERROR_MESSAGE);
                            for(int i=0; i<permittedAct; i++)
                                addActivity();
                        }
                
                        activitiesNumField.setText(""+activitiesNumber);
                
                        //Refresh MainPanel
                        revalidate();
                        
                    } else {
                        
                        JOptionPane.showMessageDialog(null,
                            "<html><body>The format is not valid!<br>" +
                            "Please enter a positive integer.</body></html>", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        
                    }
                
                }
                
                catch(NumberFormatException exception) {
                
                    JOptionPane.showMessageDialog(null,
                            "<html><body>The format is not valid!<br>" +
                            "Please enter a positive integer.</body></html>", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
                
                
            } else
                
                
            if(e.getSource() == remActivityButton) {
                
                try {
                
                    int numAct = Integer.parseInt(numActField.getText());
                
                    if (numAct > 0) {
                        
                        if(numAct < activitiesNumber) {
                            for(int i=numAct; i>0; i--)
                                remActivity();
                        } else {
                            for(int i=activitiesNumber; i>0; i--)
                                remActivity();
                        }
                
                        activitiesNumField.setText(""+activitiesNumber);
                
                        //Refresh MainPanel
                        repaint();
                    
                    } else {
                        
                        JOptionPane.showMessageDialog(null,
                            "<html><body>The format is not valid!<br>" +
                            "Please enter a positive integer.</body></html>", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        
                    }
                
                }
                
                catch(NumberFormatException exception) {
                    
                    JOptionPane.showMessageDialog(null,
                        "<html><body>The format is not valid!<br>" +
                        "Please enter a positive integer.</body></html>", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    
                }
                
                
            } else
            
            
            if(e.getSource() == resetButton) {
                
                resetFields();
                
                //Refresh MainPanel
                repaint();
                
            } else
                
                
            if(e.getSource() == newButton) {
                
                for(int i=activitiesNumber; i>1; i--)
                    remActivity();
                
                resetFields();
                activitiesNumField.setText(""+activitiesNumber);
                
                //Refresh MainPanel
                repaint();
                
            } else
            
            
            if(e.getSource() == openButton) {
                
                openProject(null);
                
                //Refresh MainPanel
                revalidate();
                repaint();
                
            } else
                
            
            if(e.getSource() == saveButton) {
                
                saveProject();
                
            } else   
            
            
            if(e.getSource() == helpButton) {
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>To receive information about<br>" +
                    "Monte Carlo Simulations, please<br>" +
                    "read the user manual.<br></body><html>", "Help", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } else
            
                
            if(e.getSource() == aboutButton) {
                
                JOptionPane.showMessageDialog(null,
                    "<html><body>Monte Carlo Simulations<br>" +
                    "Copyright (C) 2008  Francesco Ficarola<br>" +
                    "E-Mail: francesco.ficarola@gmail.com<br>" +
                    "<br>" +
                    "Special thanks for collaborations to:<br>" +
                    "- Luigi Laura<br>" +
                    "- Stefania Tattoni<br>" +
                    "- Community of Freemodding.it</body><html>", "About", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } else
            
                
            if(e.getSource() == computeButton) {
                
                Thread algorithmThread = new Thread() {
                    
                    @Override
                    public void run() 
                    {
                        Runnable hideAndUpdate = new Runnable() {
                            public void run() 
                            {
                                pleaseWaitLabel.setVisible(false);
                                repetitionsField.setEnabled(true);
                                activitiesNumField.setEnabled(true);
                                numActField.setEnabled(true);
                                maxTotalDurField.setEnabled(true);
                                sdField.setEnabled(true);
                                meanField.setEnabled(true);
                                timeSpentField.setEnabled(true);
                                addActivityButton.setEnabled(true);
                                remActivityButton.setEnabled(true);
                                resetButton.setEnabled(true);
                                newButton.setEnabled(true);
                                openButton.setEnabled(true);
                                saveButton.setEnabled(true);
                                aboutButton.setEnabled(true);
                                helpButton.setEnabled(true);
                                computeButton.setEnabled(true);
                                for(int i=0; i<activitiesNumber; i++) {
                                    ((JTextField)activitiesArray.get(i).get(1)).setEnabled(true);
                                    ((JTextField)activitiesArray.get(i).get(2)).setEnabled(true);
                                    ((JComboBox)activitiesArray.get(i).get(3)).setEnabled(true);
                                    ((JTextField)activitiesArray.get(i).get(5)).setEnabled(true);

                                    if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Uniform")) {
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(true);
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(true);
                                    } else

                                    if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Triangular")) {
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(true);
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(true);
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(2))).setEnabled(true);
                                    } else

                                    if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Beta")) {
                                        // ToDo (bit-man) perform same replacement for remaining distributions
                                        for( int j = 0; j < Distribution.BETA.getNumGUIParams(); j++)
                                            ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(j))).setEnabled(true);
                                    } else

                                    if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Gaussian")) {
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(true);
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(true);
                                    } else

                                    if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Exponential")) {
                                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(true);
                                    }

                                }
                                
                                //Refresh MainPanel
                                repaint();
                            }
                        };

                        int numRepetitions = -1;
                        int acyclic = 0;

                        try {

                            if (getRepetitions() > 0 && getRepetitions() <= 100000000)
                                numRepetitions = getRepetitions();
                            else
                            JOptionPane.showMessageDialog(null,
                                "<html><body>The format is not valid!<br>" +
                                "Please enter a positive integer<br>" +
                                "and less equal than 100000000.</body></html>", "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }

                        catch(NumberFormatException exception) {

                            JOptionPane.showMessageDialog(null,
                                "<html><body>The format is not valid!<br>" +
                                "Please enter a positive integer<br>" +
                                "and less equal than 100000000.</body></html>", "Error", 
                                JOptionPane.ERROR_MESSAGE);

                        }

                        if(numRepetitions > 0) {

                            List<List<Object>> dataActArray = new ArrayList<List<Object>>();
                            dataActArray = getDataAct();

                            if(dataActArray.get(0).size() > 1) {

                                // OUTPUT dataActArray
                                if(verbose) {
                                    System.out.println();
                                    System.out.println("ACTIVITIES CHART:");
                                    for(int i=0; i<activitiesNumber; i++) {

                                        System.out.print(dataActArray.get(i).get(0)+" ");
                                        System.out.print(dataActArray.get(i).get(1)+" ");
                                        for (int k=0; k<((int[])dataActArray.get(i).get(2)).length; k++) {
                                            System.out.print(((int[])dataActArray.get(i).get(2))[k]+" ");
                                        }
                                        System.out.print(dataActArray.get(i).get(3)+" ");
                                        if(dataActArray.get(i).get(3).equals("Uniform")){
                                            System.out.print(((int[])dataActArray.get(i).get(4))[0]+" ");
                                            System.out.println(((int[])dataActArray.get(i).get(4))[1]);
                                        } else

                                        if(dataActArray.get(i).get(3).equals("Triangular")){
                                            System.out.print(((int[])dataActArray.get(i).get(4))[0]+" ");
                                            System.out.print(((int[])dataActArray.get(i).get(4))[1]+" ");
                                            System.out.println(((int[])dataActArray.get(i).get(4))[2]);
                                        } else

                                        if(dataActArray.get(i).get(3).equals("Beta")){
                                            // ToDo (bit-man) perform same replacement for remaining distributions
                                            for( int j=0; j < Distribution.BETA.getNumGUIParams(); j++)
                                                System.out.print(((int[])dataActArray.get(i).get(4))[j]+" ");

                                            System.out.println();
                                        } else

                                        if(dataActArray.get(i).get(3).equals("Gaussian")){
                                            System.out.print(((int[])dataActArray.get(i).get(4))[0]+" ");
                                            System.out.println(((int[])dataActArray.get(i).get(4))[1]);
                                        } else

                                        if(dataActArray.get(i).get(3).equals("Exponential")){
                                            System.out.println(((int[])dataActArray.get(i).get(4))[0]);
                                        }
                                    }
                                    System.out.println();
                                }

                                startSimulations = System.currentTimeMillis();

                                MCSimulation mcs = new MCSimulation(verbose, numRepetitions, dataActArray);
                                acyclic = mcs.topologicalSort();

                                if(acyclic == 1) {

                                    // OUTPUT ARRAY topologicalArray
                                    if( verbose ) {
                                        System.out.println("Prima colonna: Array ordinato topologicamente.");
                                        System.out.println("Seconda colonna in poi: nodi successori (outNodes):");
                                        mcs.printSortArray();
                                    }
                                    
                                    
                                    mcs.makeInNodes();
                                    
                                    // OUTPUT ARRAY inDegree/outDegree e MATRICE inNodes
                                    if( verbose ) {
                                        System.out.println();
                                        System.out.println("Matrice inNodes:");
                                        mcs.printInNodes();
                                        System.out.println();
                                        System.out.println("Array inDegree:");
                                        mcs.printInDegree();
                                        System.out.println();
                                        System.out.println();
                                        System.out.println("Array outDegree:");
                                        mcs.printOutDegree();
                                        System.out.println();
                                        System.out.println();
                                    }

                                    boolean errorOnSimulation = false;
                                    String errorMessage = "";
                                    results = null;
                                    try {
                                        results = mcs.results();
                                    } catch(RuntimeException e1) {
                                        e1.printStackTrace();
                                        errorOnSimulation = true;
                                        errorMessage = e1.getMessage();
                                    }
                                    catch (Exception e1) {
                                        e1.printStackTrace();
                                        errorOnSimulation = true;
                                        errorMessage = e1.getMessage();
                                    }
									catch(OutOfMemoryError e) {
										JOptionPane.showMessageDialog(null,
										"<html><body>For repetitions more than 7 million and<br>" +
										"less than 100 million please restart the<br>" +
										"application with the following java parameters:<br><br>" +
										"7~10 million: -Xmx128m (RAM 128MB+)<br>" +
										"10~60 million: -Xmx512m (RAM 512MB+)<br>" +
										"60~100 million: -Xmx1024m (RAM 1024MB+)</body></html>", "Error", 
										JOptionPane.ERROR_MESSAGE);
									}
                                    finally {
                                        stopSimulations = System.currentTimeMillis();
                                    }

                                    if ( results != null ) {
                                        DecimalFormatSymbols symb = new DecimalFormatSymbols();
                                        symb.setDecimalSeparator('.');
                                        DecimalFormat df = new DecimalFormat("0.###");
                                        df.setDecimalFormatSymbols(symb);

                                        timeSpentField.setText( "" + ((double)(stopSimulations-startSimulations)/1000) );
                                        maxTotalDurField.setText( "" + df.format(results.getMaxDuration()) );
                                        sdField.setText( "" + df.format(results.getSD()) );
                                        meanField.setText( "" + df.format(results.getMean()) );
                                        for(int i=0; i<activitiesNumber; i++) {
                                            final int criticalPathNode = ((int[]) results.getCPN())[i];
                                            ((JTextField)activitiesArray.get(i).get(5)).setText(""+ criticalPathNode);
                                        }
                                    }

                                    if (errorOnSimulation)
                                        JOptionPane.showMessageDialog(null,
                                                "<html><body>"+errorMessage+"</body></html>", "Error",
                                                JOptionPane.ERROR_MESSAGE);

                                } else {

                                    stopSimulations = System.currentTimeMillis();
                                    JOptionPane.showMessageDialog(null,
                                        "<html><body>The following activities make a cyclical project!!!<br>" +
                                        "Please check all precedences in the activities!!!</body></html>", "Error", 
                                        JOptionPane.ERROR_MESSAGE);

                                }

                            } // OUTPUT AVVISO ECCEZIONI
                            else {
                                if( verbose ) {
                                    System.out.println("Error: ci sono state delle eccezioni!");
                                }
                            }

                        }

                        SwingUtilities.invokeLater(hideAndUpdate);
                    }
                };
            
            pleaseWaitLabel.setVisible(true);
            repetitionsField.setEnabled(false);
            activitiesNumField.setEnabled(false);
            numActField.setEnabled(false);
            maxTotalDurField.setEnabled(false);
            sdField.setEnabled(false);
            timeSpentField.setEnabled(false);
            addActivityButton.setEnabled(false);
            remActivityButton.setEnabled(false);
            resetButton.setEnabled(false);
            newButton.setEnabled(false);
            openButton.setEnabled(false);
            saveButton.setEnabled(false);
            aboutButton.setEnabled(false);
            helpButton.setEnabled(false);
            computeButton.setEnabled(false);
            for(int i=0; i<activitiesNumber; i++) {
                ((JTextField)activitiesArray.get(i).get(1)).setEnabled(false);
                ((JTextField)activitiesArray.get(i).get(2)).setEnabled(false);
                ((JComboBox)activitiesArray.get(i).get(3)).setEnabled(false);
                ((JTextField)activitiesArray.get(i).get(5)).setEnabled(false);
                
                if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Uniform")) {
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(false);
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(false);
                } else
                
                if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Triangular")) {
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(false);
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(false);
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(2))).setEnabled(false);
                } else
                
                if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Beta")) {

                    // ToDo (bit-man) perform same replacement for remaining distributions
                    for( int j = 0; j < Distribution.BETA.getNumGUIParams(); j++)
                        ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(j))).setEnabled(false);
                } else
                
                if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Gaussian")) {
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(false);
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(1))).setEnabled(false);
                } else
                
                if(((JComboBox)activitiesArray.get(i).get(3)).getSelectedItem().equals("Exponential")) {
                    ((JTextField)(((JPanel)activitiesArray.get(i).get(4)).getComponent(0))).setEnabled(false);
                }
                
            }
                
            //Refresh MainPanel
            repaint();
            
            algorithmThread.start();
            
            }
        }
    }
    
    
    //*************************************************
    //*** LISTENER DistributionListener ***************
    //*************************************************     
    class DistributionListener implements ItemListener {
        
        public void itemStateChanged (ItemEvent item) {
                        
            int index = -1;
            String itemSelected = (item.getItem()).toString();
            
            for(int i=0; i<activitiesNumber;i++) {
                
                if((JComboBox)activitiesArray.get(i).get(3) == (JComboBox)item.getSource()) {
                        index = i;
                        break;
                }
                
            }
            
            paramChanging(index, itemSelected);
            
            //Refresh MainPanel
            repaint();
            revalidate();
            
        }
        
    }
 
    
}
