package org.envirocar.server.core.exception;

/**
 * Exception that can be thrown if a requested fueling does not exist.
 *
 * @author Christian Autermann
 */
public class FuelingNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code FuelingNotFoundException} using the specified id for
     * the exception message.
     *
     * @param id the id
     */
    public FuelingNotFoundException(String id) {
        super(String.format("The fueling '%s' was not found", id));
    }

}
