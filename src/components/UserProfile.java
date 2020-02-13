package components;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class UserProfile {
    //Here we will need to add neighbours to the states and make a connected graph
    // oooo have the ability to export this data into a csv? a public method that requires a string -> username.
    //

    // So get the total number of instruments and then use the combinatorics to get an array of numbers
    // and then use this to label the states with the instruments (since they have their own assigned values)

    private HashMap<String, State> states;

    public UserProfile() {
        initStates();
        addNeighbours();
    }

    private void addNeighbours() {

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

        return null;
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
        System.out.println(ed3 + ni);

    }
}
