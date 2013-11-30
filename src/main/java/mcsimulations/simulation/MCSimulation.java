package mcsimulations.simulation; /**
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

import jsc.distributions.Beta;
import jsc.distributions.Uniform;
import jsc.distributions.Normal;
import jsc.distributions.Exponential;
import umontreal.iro.lecuyer.randvar.TriangularGen;
import umontreal.iro.lecuyer.rng.LFSR113;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.io.File;

public class MCSimulation {
    
    //Stringa per la modalità verbose
    private String verbose;
    
    //Numero ripetizioni per la simulazione
    private int repetitions;
    
    //Tempo totale massimo di fine progetto tra tutte le ripetizioni
    private double maxTotalEnding;
    
    //ArrayList delle attività
    private ArrayList<ArrayList<Object>> dataActArray;
    
    //Matrice d'adiacenza
    private int[][] matrix;
    
    //Matrice nella quale son contenuti i nodi precedenti rispetto al valore
    //rappresentato dalla riga i-esima del topologicalArray
    private int[][] inNodes;
        
    //Matrice di ArrayList d'appoggio per inNodes. Gli elementi di inNodesArray
    //vengono compiati nella matrice di array statici innodes.
    private ArrayList<ArrayList<Integer>> inNodesArray;
    
    //Array inDegree nel quale sono memorizzati il numero di archi entranti
    //(ovvero il numero di precedenze) per ogni nodo (attività) seguendo
    //sempre l'ordinamento topologico.
    private int[] inDegree;
    
    //Array outDegree nel quale sono memorizzati il numero di archi uscenti
    //(ovvero il numero di successori) per ogni nodo (attività) seguendo
    //sempre l'ordinamento topologico.
    private int[] outDegree;
    
    //Array delle attività ordinate topologicamente
    private ArrayList<ArrayList<Integer>> topologicalArray;
    
    //Indice dell'array topologico
    int index_topologicalArray = 0;
    
    //Numero nodi (attività)
    private int n;
    
    //Array delle durate singole per ogni nodo
    private double[] durations;
    
    //Array delle durate finali per ogni nodo
    private double[] ending;
    
    //Array che memorizza quante volte ogni nodo viene richiamato dai suoi
    //successori durante la scansione del topologicalArray
    private int[] cpnCount;
    
    private int[] cpnResult;
    
    //Array delle durate finali per ogni ripetizione
    private double[] totalDurations;
    
    
    public MCSimulation(String s, int repet, ArrayList<ArrayList<Object>> actArray) {
        
        verbose = s;
        repetitions = repet;
        dataActArray = actArray;
        
        //Inizializzazione maxTotalEnding
        maxTotalEnding = 0;
        
        //Inizializzazione numero nodi
        n = dataActArray.size();
        
        //Inizializzazione array topologico
        topologicalArray = new ArrayList<ArrayList<Integer>>();
        
        //Inizializzazione array inNodesArray
        inNodesArray = new ArrayList<ArrayList<Integer>>();
        
        //Inizializzazione array inDegree
        inDegree = new int[n];
        
        //Inizializzazione array outDegree
        outDegree = new int[n];
        
        //Inizializzazione array cpnResult
        cpnResult = new int[n];
        
        //Inizializzazione matrice d'adiacenza
        matrix = new int[n][n];
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                matrix[i][j] = 0;
        
        //Verifico se tra i nodi ci sono delle precedenze (e dunque collegati
        //da archi). In caso affermativo imposto nella matrice il valore 1.
        int[] temp;
        for(int i=0; i<n; i++) {
            //Elaboro solo i nodi che hanno precedenze e quindi il primo
            //elemento dell'array precedences diverso da -1.
            if(((int[])dataActArray.get(i).get(2))[0] != -1) {
                //Alloco ed inizializzo l'array temp corrispondente
                //all'array precedences di ciascuna attività.
                temp = new int[((int[])dataActArray.get(i).get(2)).length];
                temp = (int[])dataActArray.get(i).get(2);
                //Scorro l'array appena copiato
                for(int j=0; j<temp.length; j++) {
                    //Imposto 1 nella cella [temp[j]-1][i], ovvero la riga
                    //rappresentata dal valore contenuto in temp[j]-1 e la
                    //colonna i-esima. Sono stati invertiti gli indici di
                    //riga e colonna perchè nell'array delle attività sono
                    //presenti le precedenze, mentre nella matrice d'adiacenza
                    //bisogna impostare 1 se il nodo ha dei successori.
                    matrix[temp[j]-1][i] = 1;
                }
            }
        }
        
        
        
    }
    
    
    public int topologicalSort() {
        
        int occurrences = 0;
        int no_occurrences = 0;
        int result = 0;
        
        for(int j=0; j<n; j++) {
            for(int i=0; i<n; i++) {
                if(matrix[i][j] == 0) {
                    occurrences++;
                }
            }
            
            //Condizione che verifica se nella colonna j-esima sono presenti
            //zeri in tutte le righe. Se dovesse esser così allora il nodo
            //j-esimo non ha archi entranti (e dunque precedenze) e può essere
            //inserito nell'array ordinato.
            if(occurrences == n) {
                occurrences = 0;
                
                //Aggiungo un subarray (forma matriciale) dove nella prima
                //colonna saranno memorizzati i nodi dell'ordine topologico,
                //mentre nelle successive i corrispettivi nodi successori.
                topologicalArray.add(new ArrayList<Integer>());
                topologicalArray.get(index_topologicalArray).add(j+1);
                for(int i=0; i<n; i++) {
                    if(matrix[j][i] == 1)
                        topologicalArray.get(index_topologicalArray).add(i+1);
                }
                index_topologicalArray++;
                
                //Azzero tutti gli elementi della riga j-esima
                //in modo da annullare tutte le precedenze con
                //l'elemento (rappresentato dalla colonna j)
                //appena inserito nell'array ordinato
                for(int i=0; i<n; i++) {
                    if(matrix[j][i] == 1)
                        matrix[j][i] = 0;
                }
                
                //Pongo tutti gli elementi della colonna j-esima
                //uguali a -1 per indicare che quell'elemento j
                //è stato estratto ed inserito nell'array ordinato
                for(int i=0; i<n; i++) {
                    matrix[i][j] = -1;
                }
                
            } else {
                occurrences = 0;
                no_occurrences++;
            }
        }
        
        //Controllo che tutti i nodi siano stati elaborati
        //(tutti gli elementi della matrice = -1)
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++ ) {
                if(matrix[i][j] == -1)
                    result++;
            }
        }
        
        //Se tutti gli elementi della matrice sono pari a -1 allora l'algoritmo
        //può terminare e restituire 1.
        if(result == n*n) {
            index_topologicalArray = 0;
            return 1;
        }
        else {
            //Se no_occurrences è diversa da n significa che c'è stata qualche
            //colonna con tutti zeri, dunque è possibile continuare l'algoritmo.
            if(no_occurrences != n) {
                return topologicalSort();
            } else {
                //Se invece no_occurrences è uguale a n allora non è possibile
                //andare avanti dato che il progetto è ciclico.
                index_topologicalArray = 0;
                return 0;
            }
        }
        
    }
    
    
    public double computeSD(double[] totalDurs) {
        
        double totMean = 0;
        double totVar = 0;
        double variance = 0;
        
        SimulationResults result = new SimulationResults();
        
        if (isVerbose() ) {
              System.out.println("Histogram");
              System.out.println("---------");
        }
          
        for(int i=0; i<repetitions; i++) {
            result.addDuration(totalDurs[i]);
            totMean = totMean + totalDurs[i];
            if( isVerbose() )
                System.out.println(totalDurs[i]);     
        }
        
        result.setMean( totMean /  result.getNumDurations() );
        
        for( Double d : result.getDurations() ) {
            System.out.println("d : " + d );
            System.out.println("mean : " + result.getMean() );
            totVar = totVar + Math.pow(d - result.getMean() , 2);
        }
        
        variance = totVar / result.getNumDurations();
        
        result.setSD( Math.pow(variance, (double)1/2) );
        
        // OUTPUT MEDIA/VARIANZA/DEVIAZIONE STANDARD
        if( isVerbose() ) {
            System.out.println("Media: " + result.getMean());
            System.out.println("Varianza: " + variance);
            System.out.println("Deviazione standard: " + result.getSD());
            System.out.println("---------------------------------");
        }
        
        
        return result.getSD();
        
    }
    
    public boolean isVerbose() {
        return verbose.equals("-v") || verbose.equals("--verbose");
    }
    
    
    public void computeDurations() throws Exception {
        
        durations = new double[n];
                
        for(int i=0; i<n; i++) {
            
            if(dataActArray.get(topologicalArray.get(i).get(0)-1).get(3) == "Uniform") {
                
                Uniform uniform = new Uniform(((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[0],
                                             ((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[1]);
                durations[i] = uniform.random();
                if(durations[i]<0)
                    durations[i] = 0;
                
            } else
            
            if(dataActArray.get(topologicalArray.get(i).get(0)-1).get(3) == "Triangular") {
                
                durations[i] = TriangularGen.nextDouble(new LFSR113(),
                        ((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[0],
                        ((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[2],
                        ((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[1]);
                if(durations[i]<0)
                    durations[i] = 0;
                
            } else
            
            if(dataActArray.get(topologicalArray.get(i).get(0)-1).get(3) == "Beta") {

                durations[i] = calcBetaDuration(i);

            } else
            
            if(dataActArray.get(topologicalArray.get(i).get(0)-1).get(3) == "Gaussian") {
                
                Normal normal = new Normal(((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[0], 
                                           ((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[1]);
                durations[i] = normal.random();
                if(durations[i]<0)
                    durations[i] = 0;
                
            } else
            
            if(dataActArray.get(topologicalArray.get(i).get(0)-1).get(3) == "Exponential") {
                
                Exponential exponential = new Exponential(((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(4))[0]);
                durations[i] = exponential.random();
                if(durations[i]<0)
                    durations[i] = 0;
                
            }
        }
        
        // OUTPUT ARRAY durations
        if(isVerbose()) {
            for(int i=0; i<n; i++)
                System.out.println("Durata nodo "+topologicalArray.get(i).get(0)+": "+durations[i]);
            System.out.println();
        }
        
        
    }

    private double calcBetaDuration(int i) throws ComputeException {

        try {
            BetaCalculator betaCalculator = new BetaCalculator(((int[]) dataActArray.get(topologicalArray.get(i).get(0) - 1).get(4))[0],
                    ((int[]) dataActArray.get(topologicalArray.get(i).get(0) - 1).get(4))[1],
                    ((int[]) dataActArray.get(topologicalArray.get(i).get(0) - 1).get(4))[2]);

            Beta beta = new Beta(betaCalculator.getAlpha(),
                    betaCalculator.getBeta());
            return betaCalculator.scaleValue(beta.random());
        } catch (ComputeException e) {
            throw new ComputeException(e.getMessage() + ", Activity : " + i , e);
        }
    }


    public void makeInDegree() {
        
        for(int i=0; i<n; i++) {
            if(inNodesArray.get(i).get(0) != 0) {
                inDegree[i] = inNodesArray.get(i).size();
            } else {
                inDegree[0] = 0;
            }
        }
    }
    
    
    public void makeOutDegree() {
        
        for(int i=0; i<n; i++) {
            outDegree[i] = topologicalArray.get(i).size()-1;
        }
        
    }
    
    
    public void makeInNodes() {
        int maxNumPrecedences = 0;
        
        for(int i=0; i<topologicalArray.size(); i++) {
            //Se nella posizione restituita da topologicalArray.get(i).get(0)-1
            //(num. attività - 1) di dataActArray sono presenti precedenze
            //allora le inserisco in inNodesArray, altrimenti inserisco -1.
            if(((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(2))[0] != -1) {
                inNodesArray.add(new ArrayList<Integer>());
                for(int k=0; k<((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(2)).length; k++) {
                    inNodesArray.get(i).add(((int[])dataActArray.get(topologicalArray.get(i).get(0)-1).get(2))[k]);
                }
            } else {
                inNodesArray.add(new ArrayList<Integer>());
                inNodesArray.get(i).add(0);
            }
        }
        
        //Calcolo il massimo numero di precedenze tra tutte le attività
        //in modo da allocare una matrice a dimensione fissa (inNodes).
        for(int i=0; i<inNodesArray.size(); i++) {
            if(inNodesArray.get(i).size() > maxNumPrecedences)
                maxNumPrecedences = inNodesArray.get(i).size();
        }
        
        //Inizializzazione matrice inNodes
        inNodes = new int[n][maxNumPrecedences];
        for(int i=0; i<n; i++) {
            for(int j=0; j<maxNumPrecedences; j++) {
                inNodes[i][j] = 0;
            }
        }
        
        //Copia degli elementi da inNodesArray alla matrice inNodes
        for(int i=0; i<inNodesArray.size(); i++) {
            for(int j=0; j<inNodesArray.get(i).size(); j++) {
                inNodes[i][j] = inNodesArray.get(i).get(j);
            }
        }
        
        makeInDegree();
        makeOutDegree();
    }
    
        
    public double computePERT_CPN() {
        
        int colCheck = 0;
        int index_array = 0;
        int finalNode = 0;
        int occurrences = 0;
        int[] predSelect = new int[n];
        double max = 0.0;
        double maxending = 0.0;
        ending = new double[n];
        cpnCount = new int[n];
        ArrayList<ArrayList<Object>> durFinalNodes = new ArrayList<ArrayList<Object>>();
        
        for(int i=0; i<n; i++) {
            if(inDegree[i] > 0) {
                //Se il nodo i (ordine topologico) ha archi entranti allora
                //memorizzo in max il valore finale (ending) del nodo numero
                //inNodes[i][0]-1
                max = ending[inNodes[i][0]-1];
                for(int j=0; j<inDegree[i]; j++){
                    
                    // OUTPUT DURATE FINALI RELATIVE ALLE POSIZIONI RESTITUITE DA inNodes
                    if(isVerbose()) {
                        System.out.println("ending[inNodes"+"["+i+"]"+"["+j+"]"+"]: "+ending[inNodes[i][j]-1]);
                    }
                    
                    //Controllo tutti i nodi entranti verso il nodo che si sta
                    //verificando e memorizzo in max il valore di ending più
                    //elevato tra tutti quelli scansionati.
                    if(ending[inNodes[i][j]-1] > max) {
                        //Se ending[inNodes[i][j]-1] è maggiore di max,
                        //allora aggiorno quest'ultima con il nuovo valore.
                        max = ending[inNodes[i][j]-1];
                        //Memorizzo in colCheck la colonna j-esima (serve per
                        //il controllo successivo nell'if).
                        colCheck = j;
                    }                    
                }
                
                //Se colCheck è diverso da zero incremento il valore di cpnCount
                //(ordine numerico) in posizione inNodes[i][colCheck]-1, 
                //altrimenti in quella con j=0, ovvero inNodes[i][0]-1.
                //Inoltre se colCheck è diverso da zero aggiungo in predSelect
                //il predecessore scelto inNodes[i][colCheck], altrimenti
                //aggiungo quello con colCheck = 0: inNodes[i][0]
                if(colCheck != 0) {
                    cpnCount[inNodes[i][colCheck]-1]++;
                    predSelect[topologicalArray.get(i).get(0)-1] = inNodes[i][colCheck];
                    colCheck = 0;
                } else {
                    cpnCount[inNodes[i][0]-1]++;
                    predSelect[topologicalArray.get(i).get(0)-1] = inNodes[i][0];
                }
                
                // OUTPUT VARIABILE Max
                if(isVerbose()) {
                    System.out.println("Max: "+max);
                }
                
                //Alla fine del ciclo memorizzo in ending (ordine numerico e non
                //topologico) le durate per arrivare a ciascun nodo.
                ending[topologicalArray.get(i).get(0)-1] = durations[i] + max;
                
                //Se il nodo considerato ha outDegree pari a zero (pozzo)
                //allora lo memorizzo, insieme alla sua durata finale, nello
                //array durFinalNodes (serve per il controllo successivo
                //sull'incremento dei nodi finali).
                if(outDegree[i] == 0) {
                    durFinalNodes.add(new ArrayList<Object>());
                    durFinalNodes.get(index_array).add(topologicalArray.get(i).get(0));
                    durFinalNodes.get(index_array).add(ending[topologicalArray.get(i).get(0)-1]);
                    index_array++;
                }
            } 
            //Se al contrario il nodo non ha archi entranti memorizzo
            //nella rispettiva posizione di ending solo la sua durata di
            //distribuzione di probabilità scelta.
            else {
                ending[topologicalArray.get(i).get(0)-1] = durations[i];
                predSelect[topologicalArray.get(i).get(0)-1] = 0;
            }
            
            // OUTPUT ARRAY ending
            if(isVerbose()) {
                System.out.println("Durata per arrivare al nodo "+topologicalArray.get(i).get(0)+": "+ending[topologicalArray.get(i).get(0)-1]);
                System.out.println();
            }
            
            
            if (ending[topologicalArray.get(i).get(0)-1] > maxending)
                maxending = ending[topologicalArray.get(i).get(0)-1];
                
        }
        
        //Incremento per i nodi finali: quello con durata pari a maxending
        //fa parte del critical path, di conseguenza incremento il suo valore.
        if(durFinalNodes.size() > 0) {
            for(int z=0; z<durFinalNodes.size(); z++) {
                if((Double)durFinalNodes.get(z).get(1) == maxending)
                    finalNode = (Integer)durFinalNodes.get(z).get(0);
            }

            if(finalNode != 0)
                cpnCount[finalNode-1]++;
        }
        
        //Se il nodo i-esimo ha tutti successori con cpnCount pari a zero
        //(occurrences = topologicalArray.get(i).size()-1) oppure i suoi
        //successori durante la scansione non lo hanno scelto come predecessore
        //(perchè magari hanno trovato un max maggiore in un altro predecessore), 
        //allora pongo uguale a zero anche il cpnCount del nodo esaminato.
        for(int i=n-1; i>=0; i--) {
            if(outDegree[i] > 0) {
                for(int j=1; j<topologicalArray.get(i).size(); j++) {
                    if(cpnCount[topologicalArray.get(i).get(j)-1] == 0 ||
                       predSelect[topologicalArray.get(i).get(j)-1] != topologicalArray.get(i).get(0))
                        occurrences++;
                }
                
                if(occurrences == topologicalArray.get(i).size()-1)
                    cpnCount[topologicalArray.get(i).get(0)-1] = 0;
                
                occurrences = 0;
            }
        }
        
        //Incremento degli elementi i-esimi di cpnResult se i corrispondenti
        //elementi di cpnCount sono maggiori di zero.
        for(int i=0; i<n; i++) {
            if(cpnCount[i] > 0)
                cpnResult[i]++;
        }
        
        return maxending;
        
    }
    
    
    public void computeMaxTotalDuration() throws Exception {
        
        for(int i=0; i<repetitions; i++) {

            computeDurations();
            totalDurations[i] = computePERT_CPN();

            // OUTPUT ARRAY totalDurations
            if(isVerbose()) {
                System.out.println("Total duration: "+totalDurations[i]);
                System.out.println();
            }

            if(totalDurations[i] > maxTotalEnding)
                maxTotalEnding = totalDurations[i];

        }

        // OUTPUT VARIABILE maxTotalEnding
        if(isVerbose()) {
            System.out.println("Tempo massimo di progetto: "+maxTotalEnding);
        }
            
    }
    
    
    public ArrayList<Object> simulations() throws Exception {
        
        boolean exception = false;
        ArrayList<Object> results = new ArrayList<Object>();
                
        try {
            totalDurations = new double[repetitions];
        }
        catch(OutOfMemoryError e) {
            exception = true;
            JOptionPane.showMessageDialog(null,
                "<html><body>For repetitions more than 7 million and<br>" +
                "less than 100 million please restart the<br>" +
                "application with the following java parameters:<br><br>" +
                "7~10 million: -Xmx128m (RAM 128MB+)<br>" +
                "10~60 million: -Xmx512m (RAM 512MB+)<br>" +
                "60~100 million: -Xmx1024m (RAM 1024MB+)</body></html>", "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        if(exception == false) {
            
            computeMaxTotalDuration();
            double sd = computeSD(totalDurations);
            results.add(maxTotalEnding);
            results.add(sd);
            results.add(cpnResult);

        } else results.add(-1);
        
        return results;
        
    }
    
    // METODI DI STAMPA PER LA VERIFICA DELL'ESATEZZA DEI RISULTATI
    public void printMatrix() {
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                if(j<n-1)
                    System.out.print(matrix[i][j]+" ");
                else
                    System.out.println(matrix[i][j]);
    }
    
    
    public void printSortArray() {
        for(int i=0; i<topologicalArray.size(); i++)
            for(int j=0; j<topologicalArray.get(i).size(); j++)
                if(j < topologicalArray.get(i).size()-1)
                    System.out.print(topologicalArray.get(i).get(j)+" ");
                else
                    System.out.println(topologicalArray.get(i).get(j)+" ");
    }
    
    
    public void printInNodes() {
        for(int i=0; i<inNodes.length; i++)
            for(int j=0; j<inNodes[i].length; j++)
                if(j<inNodes[i].length-1)
                    System.out.print(inNodes[i][j]+" ");
                else
                    System.out.println(inNodes[i][j]);
    }
    
    
    public void printInDegree() {
        for(int i=0; i<inDegree.length; i++)
            System.out.print(inDegree[i]+" ");
    }
    
    
    public void printOutDegree() {
        for(int i=0; i<outDegree.length; i++)
            System.out.print(outDegree[i]+" ");
    }

}
