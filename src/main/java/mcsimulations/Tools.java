package mcsimulations;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description : Tools
 * Date: 11/26/13
 * Time: 6:07 PM
 */
public class Tools {

    public static void saveProject(File file, ArrayList<ArrayList<Object>> saveActArray) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file + ".mcp"));
        out.writeObject(saveActArray);
        out.close();
    }



    //*************************************************
    //*** METODO updatePrecedencesField() *************
    //*************************************************
    public static void updatePrecedencesField(int[] precArray, int currentActivity, ArrayList<ArrayList<Object>> activitiesArray1) {

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


    //*************************************************
    //*** METODO extractingNumbers() ******************
    //*************************************************
    public static ArrayList<Integer> extractingNumbers(String s) {

        ArrayList<Integer> numbers = new ArrayList<Integer>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);

        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }

        if (numbers.size() > 0) {
            return numbers;
        } else {
            ArrayList<Integer> blankArray = new ArrayList<Integer>();
            blankArray.add(-1);
            return blankArray;
        }

    }


    // ToDo (Bit-man) make MainPanel uses this code too.
    /**
     * obtains data activities (original from fields in MainPanel)
     *
     * @param activitiesNumber Number of activities
     * @param activityId       Activities number (id ?)
     * @param activityDesc     activites descriptions
     * @param activitiesArray
     * @param panel
     * @param verbose
     * @return activities data
     */
    public static ArrayList<ArrayList<Object>> getDataAct(int activitiesNumber, List<Integer> activityId, List<String> activityDesc, List<String> activityPrecedence,
                                                          List<String> activityDistribution, List<List<Integer>> activityDistributionParam, ArrayList<ArrayList<Object>> activitiesArray, JPanel panel, boolean verbose) {

        boolean standaloneTool = (activitiesArray == null || panel == null);
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


        for (int i = 0; i < activitiesNumber; i++) {

            dataActArray.add(new ArrayList<Object>());

            try {

                //------------------------------------------
                //Inserimento elemento 0: activities
                //------------------------------------------
                dataActArray.get(i).add(activityId.get(i));

                //------------------------------------------
                //Inserimento elemento 1: description
                //------------------------------------------
                dataActArray.get(i).add(activityDesc.get(i));

                //---------------------------------------------------------
                //Controllo stringa delle precedenze prima dell'inserimento
                //---------------------------------------------------------
                String precedencesString = activityPrecedence.get(i);
                //Estrazione numeri dalla stringa del campo (JTextField) precedences
                ArrayList<Integer> extractIntArray = extractingNumbers(precedencesString);

                //Controllo di numeri doppioni in extractIntArray e copia
                //del risultato in precedencesIntArray
                ArrayList<Integer> precedencesIntArray = new ArrayList<Integer>();
                for (int k = 0; k < extractIntArray.size(); k++) {
                    if (k == 0) {
                        precedencesIntArray.add(extractIntArray.get(0));
                    } else {
                        for (int z = 0; z < precedencesIntArray.size(); z++) {
                            if (precedencesIntArray.get(z) == extractIntArray.get(k)) {
                                occurrences = 1;
                            }
                        }
                        if (occurrences == 0) {
                            precedencesIntArray.add(extractIntArray.get(k));
                        } else occurrences = 0;
                    }
                }

                //Allocazione dimensione per l'array statico
                int[] precedencesInt = new int[precedencesIntArray.size()];
                //Copia degli elementi da precedencesIntArray a precedencesInt
                for (int j = 0; j < precedencesIntArray.size(); j++) {
                    precedencesInt[j] = precedencesIntArray.get(j);
                }
                //Controllo che le precedenze scritte non siano pari a 0,
                //maggiori delle attività totali o uguali all'attività corrente
                for (int j = 0; j < precedencesInt.length; j++) {
                    if (precedencesInt[j] == i + 1 || precedencesInt[j] == 0 || precedencesInt[j] > activitiesNumber) {
                        if (precedencesInt[j] == i + 1)
                            System.out.println("precedencesInt[j] == i + 1:    j:" + j + ", i:" + i );
                        else if ( precedencesInt[j] == 0  )
                            System.out.println("precedencesInt[j] == 0:   j:" + j);
                        else if (precedencesInt[j] > activitiesNumber)
                            System.out.println("precedencesInt[j] > activitiesNumber:   j:" + j + ",   actNum:" + activitiesNumber);
                        precOutOfBounds++;
                    }
                }
                //Se l'if precedente è soddisfatto incremento precOutOfBounds
                //e memorizzo in precErrors il numero dell'attività con l'errore
                if (precOutOfBounds != 0) {
                    precErrors.add(i);
                    precOutOfBounds = 0;
                }

                //Aggiornamento campi (JTextField) precedences
                if (!standaloneTool)
                    updatePrecedencesField(precedencesInt, i, activitiesArray);

                //------------------------------------------
                //Inserimento elemento 2: precedences
                //------------------------------------------
                dataActArray.get(i).add(precedencesInt);

                //------------------------------------------
                //Inserimento elemento 3: distribution
                //------------------------------------------
                dataActArray.get(i).add(activityDistribution.get(i));

                //------------------------------------------
                //Inserimento elemento 4: parameters
                //------------------------------------------
                if (dataActArray.get(i).get(3).equals("Uniform")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[1] > paramArray[0])
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else if (dataActArray.get(i).get(3).equals("Triangular")) {

                    int[] paramArray = new int[3];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    paramArray[2] = activityDistributionParam.get(i).get(2);
                    //Controllo delle condizioni sui parametri
                    if ((paramArray[0] <= paramArray[1]) && (paramArray[1] <= paramArray[2]))
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else if (dataActArray.get(i).get(3).equals("Beta")) {

                    // ToDo (bit-man) replicate this change in remaining distributions
                    int numParams = Distribution.BETA.getNumGUIParams();
                    int[] paramArray = new int[numParams];
                    for( int j = 0; j < numParams; j++ )
                        paramArray[j] = activityDistributionParam.get(i).get(j);

                    //Controllo delle condizioni sui parametri
                    if(paramArray[0] > 0 && paramArray[1] > 0)
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                } else if (dataActArray.get(i).get(3).equals("Gaussian")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[1] > 0)
                        dataActArray.get(i).add(paramArray);
                    else {
                        System.out.println("Std Dev must be greater than zero (activity " + i +")");
                        paramErrors.add(i);
                    }

                } else if (dataActArray.get(i).get(3).equals("Exponential")) {

                    int[] paramArray = new int[1];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[0] > 0)
                        dataActArray.get(i).add(paramArray);
                    else paramErrors.add(i);

                }

            } catch (NumberFormatException exception) {

                paramExceptions.add(i);

            }

        }

        //Refresh MainPanel
        if (!standaloneTool)
            panel.repaint();

        //----------------------------------
        //Controllo ciclicità nelle attività
        //----------------------------------
        if (paramExceptions.size() == 0 && precErrors.size() == 0 && paramErrors.size() == 0) {

            ArrayList<Integer> duplicateCyclicErrors = new ArrayList<Integer>();

            int compareActivity = 0;

            for (int i = 0; i < dataActArray.size(); i++) {
                for (int k = 0; k < ((int[]) dataActArray.get(i).get(2)).length; k++) {
                    if (((int[]) dataActArray.get(i).get(2))[k] != -1) {
                        compareActivity = ((int[]) dataActArray.get(i).get(2))[k] - 1;
                        for (int z = 0; z < ((int[]) dataActArray.get(compareActivity).get(2)).length; z++) {
                            if (((int[]) dataActArray.get(compareActivity).get(2))[z] != -1) {
                                if (((int[]) dataActArray.get(compareActivity).get(2))[z] == i + 1) {

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
            for (int k = 0; k < duplicateCyclicErrors.size(); k++) {
                if (k == 0) {
                    cyclicErrors.add(duplicateCyclicErrors.get(0));
                } else {
                    for (int z = 0; z < cyclicErrors.size(); z++) {
                        if (cyclicErrors.get(z) == duplicateCyclicErrors.get(k)) {
                            occurrences = 1;
                        }
                    }
                    if (occurrences == 0) {
                        cyclicErrors.add(duplicateCyclicErrors.get(k));
                    } else occurrences = 0;
                }
            }

        }

        //----------------------------------------------------------
        //Controllo errori e restituzione dataActArray o errActArray
        //----------------------------------------------------------
        if (paramExceptions.size() == 0 && precErrors.size() == 0 && cyclicErrors.size() == 0 && paramErrors.size() == 0) {

            return dataActArray;

        } else {

            // OUTPUT ECCEZIONI ED ERRORI
            if (verbose) {
                System.out.println("Exceptions of Parameters: " + paramExceptions.size());
                System.out.println("Error of Parameters: " + paramErrors.size());
                System.out.println("Errors of Precedences: " + precErrors.size());
                System.out.println("Errors of cyclic Activities: " + cyclicErrors.size());
            }


            //Restituzione numero di errori e le attività che li contengono
            if (precErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < precErrors.size(); i++) {
                    if (i != precErrors.size() - 1) {
                        strBuff.append(precErrors.get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(precErrors.get(i) + 1);
                }

                if (standaloneTool)
                    System.out.println("ERROR: Precedences out of Bounds in the following Activities: " + strBuff.toString());
                else
                    JOptionPane.showMessageDialog(null,
                            "<html><body>Precedences out of Bounds in the following Activities:<br>" +
                                    "" + strBuff.toString() + "</body></html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }

            if (paramExceptions.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < paramExceptions.size(); i++) {
                    if (i != paramExceptions.size() - 1) {
                        strBuff.append(paramExceptions.get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(paramExceptions.get(i) + 1);
                }


                if (standaloneTool)
                    System.out.println("ERROR: The following Activities contain parameters with an invalid format (it must be an integer): " + strBuff.toString());
                else
                    JOptionPane.showMessageDialog(null,
                            "<html><body>The following Activities contain parameters<br>" +
                                    "with an invalid format (it must be an integer):<br>" +
                                    "" + strBuff.toString() + "</body></html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }

            if (paramErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < paramErrors.size(); i++) {
                    if (i != paramErrors.size() - 1) {
                        strBuff.append(paramErrors.get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(paramErrors.get(i) + 1);
                }


                if (standaloneTool)
                    System.out.println("ERROR: The following Activities contain incorrect parameters: " + strBuff.toString());
                else
                    JOptionPane.showMessageDialog(null,
                            "<html><body>The following Activities contain incorrect parameters:<br>" +
                                    "" + strBuff.toString() + "</body></html>", "Error",
                            JOptionPane.ERROR_MESSAGE);

            }

            if (cyclicErrors.size() > 0) {
                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < cyclicErrors.size(); i++) {
                    if (i != cyclicErrors.size() - 1) {
                        strBuff.append(cyclicErrors.get(i) + 1);
                        strBuff.append("; ");
                    } else strBuff.append(cyclicErrors.get(i) + 1);
                }


                if (standaloneTool)
                    System.out.println("ERROR: The following Activities are cyclical: " + strBuff.toString());
                else
                    JOptionPane.showMessageDialog(null,
                            "<html><body>The following Activities are cyclical:<br>" +
                                    "" + strBuff.toString() + "</body></html>", "Error",
                            JOptionPane.ERROR_MESSAGE);

            }

            //errActArray: array fittizio per il controllo nel listener
            ArrayList<ArrayList<Object>> errActArray = new ArrayList<ArrayList<Object>>();
            errActArray.add(new ArrayList<Object>());
            errActArray.get(0).add(-1);

            return errActArray;

        }

    }

}
