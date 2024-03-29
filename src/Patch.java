/**
 * Patches are the basis of the world. They store all the information about a ground,
 * which contains the daisy, temperature and how to calculate the local temperature in each tick.
 */
public class Patch {
    //the temperature of this patch
    private double temperature;
    //the soilQuality of this patch
    private double soilQuality;
    //the daisy on this patch
    private Daisy daisy;

    public Patch(){
        temperature = 0;
        daisy = null;
        soilQuality = 1;
    }

    /**
     * Calculate the temperature of this patch after absorbing sun light.
     * @param solarLuminosity Solar luminosity
     * @param albedoOfSurface Albedo of surface
     */
    public void calTemp(double solarLuminosity, double albedoOfSurface) {
        // the absorbed luminosity by this patch
        double absorbedLuminosity = 0;
        double localHeating = 0;
        // if there is no daisy on this patch, the absorbed luminosity
        // is calculated by (1 - albedo of surface) * solar luminosity
        if (daisy == null) {
            absorbedLuminosity = (1 - albedoOfSurface) * solarLuminosity;
        }
        // else the absorbed luminosity is calculated by
        // (1 - albedo of daisy on this patch) * solar luminosity
        else {
            absorbedLuminosity = (1 - daisy.getAlbedo()) * solarLuminosity;
        }
        // calculate the local heating complying with the NetLogo code
        if (absorbedLuminosity > 0) {
            localHeating = 72 * Math.log(absorbedLuminosity) + 80;
        }
        else {
            localHeating = 80;
        }
        // set the temperature of this patch to the average
        // number of the current temperature and the local heating
        temperature = (temperature + localHeating) / 2;
    }

    public Daisy getDaisy() {
        return daisy;
    }

    public void setDaisy(Daisy daisy) {
        this.daisy = daisy;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getSoilQuality() {
        return soilQuality;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setSoilQuality(double soilQuality) {
        this.soilQuality = soilQuality;
    }
}
