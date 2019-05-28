/**
 * Parameter class for model settings. One can adjust the value of parameters
 * in this class to observe different model behaviour.
 */
public class Params {
    // initial percentage of black daisies
    public static double START_BLACK = 0.2;
    // initial percentage of white daisies
    public static double START_WHITE = 0.2;
    // Albedo for white daisies
    public static double ALBEDO_OF_WHITE = 0.75;
    // Albedo for black daisies
    public static double ALBEDO_OF_BLACk = 0.25;
    // Albedo for earth surface
    public static double ALBEDO_Of_SURFACE = 0.4;

    // the max age of daisies
    public static int MAX_AGE = 25;

    // the solar luminosity in different scenarios
    public static double SOLAR_LUMINOSITY = 1.0;

    // max ticks of the daisy world
    public static int TICKS = 1000;

    // edge length of daisy world
    public static int EDGE = 29;

    // Diffusion ratio
    public static double DIFFUSION_RATIO = 0.5;

    /* Extension switch and parameters */

    //Main switch of extension, off = 0; on = 1.
    public static int QUALITY_SWITCH = 0;

    //Change base, the change will dependent on product of base and current non-perfect degree.
    public static double CHANGE_BASE = .1;

    // At the beginning the best quality of soil among patches. For a common environment should be
    // ~ 1 and the mathematical expect would be .5.
    public static double INITIAL_MAX_QUALITY = 1;

    // If the quality is below Death line it will become sand and never go back.
    // This should be very small.
    public static double DEATH_LINE = .02;
}
