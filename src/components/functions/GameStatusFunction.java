package components.functions;

import components.State;

/**
 * GameStatusFunction interface that will be implemented by the game status function chosen to be used in this
 * MVC application. This makes the code extensible so that if changes need to be made in how the next State is chosen,
 * then it will not affect the application's overall behaviour
 *
 * @author Gareth Iguasnia
 * @date 16/02/2020
 */
public interface GameStatusFunction {

    /**
     * Method returns the next state that the simulation will move on to next based on the user's performance on
     * timing recognition in the current state.
     *
     * @param currentState A State object representing the current state that the user is being currently tested on
     * @return A State object representing the next state that the user will be tested on
     */
    State getNextState(State currentState);
}
