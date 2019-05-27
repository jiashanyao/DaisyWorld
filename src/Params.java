public class Params {
    // initial percentage of black daisies
    public static double START_BLACK = 0.2;
    // initial percentage of white daisies
    public static double START_WHITE = 0.2;

    public static double ALBEDO_OF_WHITE = 0.75;
    public static double ALBEDO_OF_BLACk = 0.25;
    public static double ALBEDO_Of_SURFACE = 0.4;

    // the max age of daisies
    public static int MAX_AGE = 25;

    // the solar luminosity in different scenarios
    public static Double SOLAR_LUMINOSITY = 1.0;

    // max ticks of the daisy world
    public static int TICKS = 400;

    // edge length of daisy world
    public static int EDGE = 29;

    // Diffusion ratio
    public static double DIFFUSION_RATIO = 0.5;

    //Extension switch and parameters

    //Main switch, off = 0; on = 1.
    public static int QUALITY_SWITCH = 0;
    //Change base, the change will dependent on product of base and current non-perfect degree.
    public static double CHANGE_BASE = .15;
}
