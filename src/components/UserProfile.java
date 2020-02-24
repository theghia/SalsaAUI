package components;

import org.apache.commons.math3.util.CombinatoricsUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * UserProfile Class used to hold information on all of the possible combinations of instruments and tempo that
 * the simulation could possibly enact to test the user's ability in identifying the correct timing.
 *
 * @author Gareth Iguasnia
 * @date 16/02/2020
 */
public class UserProfile {
    // oooo have the ability to export this data into a csv? a public method that requires a string -> username.

    // Key - Unique String ID where the single digits represent an instrument and the alphabetical character
    // represents a tempo. Value - The State object that holds the combination detailed by the ID
    private Map<String, State> states = new HashMap<String, State>();

    // Fields just to access information quickly when setting up the UserProfile object
    private int numOfInstruments;
    private int numOfTempos;

    /**
     * Constructor for the UserProfile object. Here, all of the States will be initialised with their respective
     * neighbours. If more Instrument and BPM objects are added to their enum class, this will still adapt accordingly
     * and create more states. There can only ever be a maximum of 10 instruments being used in this application.
     */
    public UserProfile() {
        initStates();
        addNeighbours();
    }

    /**
     * Method returns the HashMap used to store all of the different States that the simulation can go through
     *
     * @return Map object where the key is a String ID where the single digit pertains to an instrument and the last
     * alphabetical character pertains to a certain tempo.
     */
    public Map<String, State> getStates() {
        return states;
    }

    /* Helper method creates an ID to identify the State.
     * The first characters will be numbers representing the different instruments
     * The last character represents the tempo. */
    public String createID(BPM bpm, ArrayList<Instrument> instruments) {
        String id = "";
        for (Instrument instrument: instruments) {
            id += Integer.toString(instrument.getValue());
        }
        id += bpm.getId();
        return id;
    }

    private void addNeighbours() {
        // This loop is so that we can add the neighbours for each of the states found in the HashMap
        for (Map.Entry<String, State> entry : states.entrySet()) {
            // The ID of the State in this application
            String id = entry.getKey();

            // Splitting the ID into two parts that represent what instruments and tempo that State has
            String instruments = id.substring(0, id.length() - 1);
            String tempo = id.substring(id.length() - 1);

            // Adding the neighbouring states that have one more instrument
            addOneMoreInstrumentNeighbour(instruments, tempo, entry.getValue());

            // Adding the neighbouring states that have a faster/slower tempo
            addTempoNeighbour(instruments, tempo, entry.getValue());

            // Adding the neighbouring states that have a swapped instrument
            addSwappedInstrumentNeighbour(instruments, tempo, entry.getValue());

            // Adding the neighbouring states that have one less instrument than the current combination
            addOneLessInstrumentNeighbour(instruments, tempo, entry.getValue());
        }
    }

    /* Helper method adds the neighbouring states that have one less instrument than the current combination */
    private void addOneLessInstrumentNeighbour(String instruments, String tempo, State state) {
        // Cannot remove an instrument if only one is currently present in the state
        if (instruments.length() > 1) {
            // Removing one instrument from the current combination at each index bar the final one
            for (int i = 0; i < instruments.length() - 1; i++) {
                String oneLess = instruments.substring(0,i) + instruments.substring(i+1);
                state.getNeighbours().add(this.states.get(oneLess + tempo));
            }
            // Removing the final character from the instrument ID
            String oneLess = instruments.substring(0, instruments.length() - 1);
            state.getNeighbours().add(this.states.get(oneLess + tempo));
        }
    }

    /* Helper method that adds the neighbours that have an instrument changed in its current combination */
    private void addSwappedInstrumentNeighbour(String instruments, String tempo, State state) {
        // All of the possible instruments
        List<Integer> range = IntStream.rangeClosed(0, this.numOfInstruments - 1)
                .boxed().collect(Collectors.toList());

        for (int num: range) {
            String conNum = Integer.toString(num);
            // If there is an instrument not in the current combination, then it will be used for swapping
            if (!instruments.contains(conNum)) {
                // If only one instrument, we just replace it
                if (instruments.length() == 1) {
                    state.getNeighbours().add(this.states.get(conNum+tempo));
                }
                // If several instruments, we replace one at a time
                else if (instruments.length() > 1) {
                    for (int i = 0; i < instruments.length() - 1; i++) {
                        // Swapping out a current instrument for a new one
                        String swappedInstrument = instruments.substring(0, i) + conNum + instruments.substring(i+1);

                        // Sorting the ID and adding the state neighbour
                        sortIDAndAddNeighbour(swappedInstrument, tempo, state);
                    }
                    // Doing a final swap for the last character
                    String swappedInstrument = instruments.substring(0, instruments.length() - 1) + conNum;
                    sortIDAndAddNeighbour(swappedInstrument, tempo, state);
                }
            }
        }
    }

    /* Helper method that sorts the characters in the ID in ascending order. Numbers first and then characters */
    private void sortIDAndAddNeighbour(String newCombo, String tempo, State state) {
        // Sorting the array as the ID goes in ascending order
        char[] toSort = newCombo.toCharArray();
        Arrays.sort(toSort);
        newCombo = new String(toSort);

        // Adding the ID for the tempo
        newCombo += tempo;

        // Adding the neighbour to the State
        state.getNeighbours().add(this.states.get(newCombo));
    }

    /* Helper method adds the neighbours that have the same combination of instruments
    * but with a slower and faster tempo if possible */
    private void addTempoNeighbour(String instruments, String tempo, State state) {
        // Ordered list from SLOW to FAST tempo
        BPM aBPM = BPM.SLOW;
        BPM[] possibleBPM = aBPM.getDeclaringClass().getEnumConstants();

        for (int i = 0; i < possibleBPM.length; i++) {
            char charTempo = tempo.charAt(0);

            // Checking what speed the state's current tempo belongs to
            if (charTempo == possibleBPM[i].getId()) {
                // Neighbour with a slower tempo (if it exists)
                if (i - 1 >= 0) {
                    String slowID = instruments + possibleBPM[i-1].getId();
                    state.getNeighbours().add(this.states.get(slowID));
                }

                // Neighbour with a faster tempo (if it exists)
                if (i + 1 <= possibleBPM.length - 1) {
                    String fastID = instruments + possibleBPM[i+1].getId();
                    state.getNeighbours().add(this.states.get(fastID));
                }
                break;
            }
        }
    }

    /* Helper method adds the neighbours that have one extra instrument than the current state */
    private void addOneMoreInstrumentNeighbour(String instruments, String tempo, State state) {
        // Creating an int array of all instruments
        List<Integer> range = IntStream.rangeClosed(0, this.numOfInstruments - 1)
                .boxed().collect(Collectors.toList());

        for (int num: range) {
            String conNum = Integer.toString(num);
            if (!instruments.contains(conNum)) {
                // Adding the extra instrument
                String newCombo = instruments + conNum;

                // Sorting the array as the ID goes in ascending order
                char[] toSort = newCombo.toCharArray();
                Arrays.sort(toSort);
                newCombo = new String(toSort);

                // Adding the ID for the the tempo
                newCombo += tempo;

                // Adding the neighbour to the State
                state.getNeighbours().add(this.states.get(newCombo));
            }
        }
    }

    /* Helper method initialises every State object that will be used in the application */
    private void initStates() {
        // Getting all of the possible instrument combinations
        ArrayList<int[]> combinations = getCombinations();

        // Getting the amount of Tempos being used in this application
        BPM aBPM = BPM.SLOW;
        BPM[] possibleBPM = aBPM.getDeclaringClass().getEnumConstants();

        // Adding the number of tempos being used in this application to the UserProfile
        this.numOfTempos = possibleBPM.length;

        for(int[] combo: combinations) {
            for(BPM bpm: possibleBPM) {
                ArrayList<Instrument> instrumentCombo = convertIntListToInstrument(combo);
                State state = new State(bpm, instrumentCombo);
                String id = createID(bpm, instrumentCombo);
                states.put(id, state);
            }
        }
    }

    /* Helper method to convert an int array into an ArrayList of Instrument objects */
    private ArrayList<Instrument> convertIntListToInstrument(int[] combo) {

        ArrayList<Instrument> combination = new ArrayList<Instrument>();

        for(int value: combo) {
            combination.add(convertIntToInstrument(value));
        }

        return combination;
    }

    /* Helper method identifies what the Instrument object pertains to the integer value  */
    private Instrument convertIntToInstrument(int value) {
        // Getting an Instrument array of all of the instruments that are being used in the application
        Instrument anInstrument = Instrument.PIANO;
        Instrument[] possibleInstruments = anInstrument.getDeclaringClass().getEnumConstants();

        Instrument converted = null;

        // Checking the value associated to the Instrument object and comparing that to the int value
        for(Instrument instrument: possibleInstruments) {
            if (instrument.getValue() == value) {
                converted = instrument;
            }
        }
        return converted;
    }

    /* Helper method gets all possible combinations of integers using 3rd party module */
    private ArrayList<int[]> getCombinations() {
        // All of the possible combinations
        ArrayList<int[]> combinations = new ArrayList<int[]>();

        // The number of instruments being used in the MVC application
        Instrument aInstrument = Instrument.PIANO;
        Instrument[] possibleValues = aInstrument.getDeclaringClass().getEnumConstants();
        int numOfInstruments = possibleValues.length;

        // Adding the information on the number of instruments to the UserProfile object
        this.numOfInstruments = numOfInstruments;

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

    /*public static void main(String[] args) {

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

        List<Integer> range = IntStream.rangeClosed(0, 3)
                .boxed().collect(Collectors.toList());
        System.out.println(range);

        String con = "hello1";
        System.out.println(con.contains("1"));
        char[] waitUp = con.toCharArray();
        Arrays.sort(waitUp);
        System.out.println(waitUp);
        char huhu = 'a';
        char hihi = 'a';
        System.out.println(huhu == hihi);
        System.out.println(con + huhu);
        System.out.println(con.substring(0,1));
        System.out.println(con.substring(2));

        UserProfile up = new UserProfile();

        for (Map.Entry<String, State> entry : up.getStates().entrySet()) {
            System.out.println("The combination is: " + entry.getKey());
            System.out.println(entry.getValue().getInstruments());
            System.out.println(entry.getValue().getBpm());
            System.out.println("The neighbours are:");
            for (State state: entry.getValue().getNeighbours()) {
                System.out.println(state.getInstruments());
                System.out.println(state.getBpm());
            }
            System.out.println();
        }

    }*/
}
