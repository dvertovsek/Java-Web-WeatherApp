package org.foi.nwtis.dvertovs.konfiguracije;

/**
 *
 * @author dare
 */
public class NeispravnaKonfiguracija extends Exception {

    /**
     * Creates a new instance of <code>NeispravnaKonfiguracija</code> without
     * detail message.
     */
    public NeispravnaKonfiguracija() {
    }

    /**
     * Constructs an instance of <code>NeispravnaKonfiguracija</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NeispravnaKonfiguracija(String msg) {
        super(msg);
    }
}
