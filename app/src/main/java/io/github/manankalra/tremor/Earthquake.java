/**
 * This class contains information related to a single earthquake.
 *
 * @author Manan Kalra
 * @version 2.0
 */

package io.github.manankalra.tremor;

public class Earthquake {
    private String magnitude; //Magnitude of the quake.
    private String location; //Location of the quake.
    private String date; //Date on which it occurred.
    private String time; //Time at which it occurred.
    private String url; //Additional details on the earthquake.

    /**
     * Constructor
     *
     * @param mag is the magnitude of the quake.
     * @param loc is it's location.
     * @param dat is the date on which it occurred.
     * @param tim is the time on whihc it occurred.
     */
    public Earthquake(String mag, String loc, String dat, String tim, String resource) {
        magnitude = mag;
        location = loc;
        date = dat;
        time = tim;
        url = resource;
    }

    //Return functions
    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

}
