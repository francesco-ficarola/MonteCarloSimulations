package mcsimulations;

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

    public static void saveProject(File file, List<List<Object>> saveActArray) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file + ".mcp"));
        out.writeObject(saveActArray);
        out.close();
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

    /**
     * obtains data activities
     *
     * @param activitiesNumber Number of activities
     * @param activityId       Activities number (id ?)
     * @param activityDesc     activites descriptions
     * @return activities data
     */
    public static DataActResponse getDataAct(int activitiesNumber, 
    										 List<Integer> activityId, 
    										 List<String> activityDesc, 
    										 List<String> activityPrecedence,
											 List<String> activityDistribution, 
											 List<List<Integer>> activityDistributionParam ) {

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
        DataActResponse resp = new DataActResponse();

        for (int i = 0; i < activitiesNumber; i++) {

        	List<Object> currentActivity = new ArrayList<Object>();
            resp.getDataActivities().add(currentActivity);

            try {

                //------------------------------------------
                //Inserimento elemento 0: activities
                //------------------------------------------
                currentActivity.add(activityId.get(i));

                //------------------------------------------
                //Inserimento elemento 1: description
                //------------------------------------------
                currentActivity.add(activityDesc.get(i));

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
                    resp.getPrecErrors().add(i);
                    precOutOfBounds = 0;
                }

                //------------------------------------------
                //Inserimento elemento 2: precedences
                //------------------------------------------
                currentActivity.add(precedencesInt);

                //------------------------------------------
                //Inserimento elemento 3: distribution
                //------------------------------------------
                currentActivity.add(activityDistribution.get(i));

                //------------------------------------------
                //Inserimento elemento 4: parameters
                //------------------------------------------
                if (currentActivity.get(3).equals("Uniform")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[1] > paramArray[0])
                        currentActivity.add(paramArray);
                    else resp.getParamErrors().add(i);

                } else if (currentActivity.get(3).equals("Triangular")) {

                    int[] paramArray = new int[3];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    paramArray[2] = activityDistributionParam.get(i).get(2);
                    //Controllo delle condizioni sui parametri
                    if ((paramArray[0] <= paramArray[1]) && (paramArray[1] <= paramArray[2]))
                        currentActivity.add(paramArray);
                    else resp.getParamErrors().add(i);

                } else if (currentActivity.get(3).equals("Beta")) {

                    // ToDo (bit-man) replicate this change in remaining distributions
                    int numParams = Distribution.BETA.getNumGUIParams();
                    int[] paramArray = new int[numParams];
                    for( int j = 0; j < numParams; j++ )
                        paramArray[j] = activityDistributionParam.get(i).get(j);

                    //Controllo delle condizioni sui parametri
                    if(paramArray[0] > 0 && paramArray[1] > 0)
                        currentActivity.add(paramArray);
                    else resp.getParamErrors().add(i);

                } else if (currentActivity.get(3).equals("Gaussian")) {

                    int[] paramArray = new int[2];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    paramArray[1] = activityDistributionParam.get(i).get(1);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[1] > 0)
                        currentActivity.add(paramArray);
                    else {
                        System.out.println("Std Dev must be greater than zero (activity " + i +")");
                        resp.getParamErrors().add(i);
                    }

                } else if (currentActivity.get(3).equals("Exponential")) {

                    int[] paramArray = new int[1];
                    paramArray[0] = activityDistributionParam.get(i).get(0);
                    //Controllo delle condizioni sui parametri
                    if (paramArray[0] > 0)
                        currentActivity.add(paramArray);
                    else resp.getParamErrors().add(i);

                }

            } catch (NumberFormatException exception) {

                resp.getParamExceptions().add(i);

            }

        }

        //----------------------------------
        //Controllo ciclicità nelle attività
        //----------------------------------
        if (resp.getParamExceptions().size() == 0 && resp.getPrecErrors().size() == 0 && resp.getParamErrors().size() == 0) {

            ArrayList<Integer> duplicateCyclicErrors = new ArrayList<Integer>();

            int compareActivity = 0;

            for (int i = 0; i < resp.getDataActivities().size(); i++) {
            	List<Object> currentActivity = resp.getDataActivities().get(i);
                for (int k = 0; k < ((int[]) currentActivity.get(2)).length; k++) {
                    if (((int[]) currentActivity.get(2))[k] != -1) {
                        compareActivity = ((int[]) currentActivity.get(2))[k] - 1;
                        for (int z = 0; z < ((int[]) resp.getDataActivities().get(compareActivity).get(2)).length; z++) {
                            if (((int[]) resp.getDataActivities().get(compareActivity).get(2))[z] != -1) {
                                if (((int[]) resp.getDataActivities().get(compareActivity).get(2))[z] == i + 1) {

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
                    resp.getCyclicErrors().add(duplicateCyclicErrors.get(0));
                } else {
                    for (int z = 0; z < resp.getCyclicErrors().size(); z++) {
                        if (resp.getCyclicErrors().get(z) == duplicateCyclicErrors.get(k)) {
                            occurrences = 1;
                        }
                    }
                    if (occurrences == 0) {
                        resp.getCyclicErrors().add(duplicateCyclicErrors.get(k));
                    } else occurrences = 0;
                }
            }

        }

        
        return resp;

    }

}
