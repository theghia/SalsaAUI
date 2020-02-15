package components;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;

public class UserProfile {
    //Here we will need to add neighbours to the states and make a connected graph
    // oooo have the ability to export this data into a csv? a public method that requires a string -> username.
    //

    // So get the total number of instruments and then use the combinatorics to get an array of numbers
    // and then use this to label the states with the instruments (since they have their own assigned values)

    private Map<String, State> states = new HashMap<String, State>();

    public UserProfile() {
        initStates();
        addNeighbours();
    }

    private void addNeighbours() {

        for (Map.Entry<String, State> entry : states.entrySet()) {

            //System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    private ArrayList<State> getNeigbours(String id) {
        String instruments = id.substring(0, id.length() -1);
        String tempo = id.substring(id.length() - 1);

        // ADDING AN INSTRUMENT
        // Convert instruments into int[] + 1 to add an extra instrument
        // Then create a {1,2,...} and loop through this. Use asArray to use the contains!
        // if bool comes false, then we can add that instrument
        // Sort that int[] and convert back into a string and add the tempo back on

        // CHANGING TEMPO
        // Loop through {a, b, c...} and find the index of the tempo on that list. Now -1 and +1 but index
        // cannot be less than 0 and more than {}.length - 1

        // REMOVING ONE INSTRUMENT
        // Loop through the String instruments and remove one and add to tempo

        // SWAP OUT ONE INSTRUMENT
        // First we need to get an Int[] of instruments we currently do not have (can create a function for this
        // to use with ADDING...)
        // Then we loop through the int[] of instruments we do have and swap each out with one of the instruments
        // we do not have, add the tempo, and then find the state
        return null;
    }

    private void initStates() {
        ArrayList<int[]> combinations = getCombinations();
        BPM aBPM = BPM.SLOW;
        BPM[] possibleBPM = aBPM.getDeclaringClass().getEnumConstants();

        for(int[] combo: combinations) {
            for(BPM bpm: possibleBPM) {
                ArrayList<Instrument> instrumentCombo = convertIntListToInstrument(combo);
                State state = new State(bpm, instrumentCombo);
                String id = createID(bpm, instrumentCombo);
                states.put(id, state);
            }
        }
    }

    private String createID(BPM bpm, ArrayList<Instrument> instruments) {
        String id = null;
        for (Instrument instrument: instruments) {
            id += Integer.toString(instrument.getValue());
        }
        id += bpm.getId();
        return id;
    }

    private ArrayList<Instrument> convertIntListToInstrument(int[] combo) {

        ArrayList<Instrument> combination = new ArrayList<Instrument>();

        for(int value: combo) {
            combination.add(convertIntToInstrument(value));
        }

        return combination;
    }

    private Instrument convertIntToInstrument(int value) {
        Instrument anInstrument = Instrument.PIANO;
        Instrument[] possibleInstruments = anInstrument.getDeclaringClass().getEnumConstants();

        Instrument converted = null;

        for(Instrument instrument: possibleInstruments) {
            if (instrument.getValue() == value) {
                converted = instrument;
            }
        }
        return converted;
    }

    private ArrayList<int[]> getCombinations() {
        // All of the possible combinations
        ArrayList<int[]> combinations = new ArrayList<int[]>();

        // The number of instruments being used in the MVC application
        Instrument aInstrument = Instrument.PIANO;
        Instrument[] possibleValues = aInstrument.getDeclaringClass().getEnumConstants();
        int numOfInstruments = possibleValues.length;

        // Getting all of the combinations with:
        // n = number of instruments
        // k = 1, ... , n
        for(int i = 1; i < numOfInstruments + 1; i ++) {
            Iterator<int[]> combo = CombinatoricsUtils.combinationsIterator(numOfInstruments, i);
            while(combo.hasNext()) {
                combinations.add(combo.next());
            }
        }
        return combinations;
    }

    public static void main(String[] args) {

        ArrayList<int[]> test;

        Iterator<int[]> testing = CombinatoricsUtils.combinationsIterator(4,2);
        while(testing.hasNext()) {
            //Object element = testing.next();
            int[] element = testing.next();

            System.out.print(Arrays.toString(element));
        }

        Instrument hu = Instrument.BASS;

        Object[] possibleValues = hu.getDeclaringClass().getEnumConstants();
        Instrument[] possVal = hu.getDeclaringClass().getEnumConstants();

        for (Object obj: possibleValues) {
            System.out.println(obj);
            Instrument tesst = (Instrument) obj;
            Instrument fr = Instrument.PIANO;
            //int tet =
            //System.out.println(fr.value);
        }
        System.out.println(possibleValues.length);

        ArrayList<Instrument> in = new ArrayList<Instrument>();
        in.add(Instrument.CLAVE);
        in.add(Instrument.PIANO);
        BPM inn = BPM.SLOW;

        Object[] getting = {in, inn};

        ArrayList<Instrument> in2 = new ArrayList<Instrument>();
        in2.add(Instrument.CLAVE);
        in2.add(Instrument.PIANO);

        BPM inn2 = BPM.MEDIUM;
        Object[] getting2 = {in2, inn2};

        System.out.println(getting.equals(getting2));
        System.out.println(in.equals(inn2));
        String ed = "hi";
        String ed2 ="hi";
        String ed3 = ed+ed2;
        System.out.println(ed3);

        char ni = 'a';
        String ed4 = ed3 + ni;
        System.out.println(ed4);

        System.out.println(ed4.substring(ed4.length() - 1));
        System.out.println('b' > 'a');
        System.out.println(ed4.substring(0, ed4.length() - 1));
    }
}
