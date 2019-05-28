# SWEN90004 Assignment 2
## Run the DaisyWorld Model
Compile the java program by:
```
javac DaisyWorld.java
```
And run the model by:
```
java DaisyWorld
```
After running the model, the program will print the daisy world at the final tick of the simulation.
The world is represented as a grid of patches, each of which may or may not a daisy.
A solid circle means a black daisy. An empty circle means a white daisy. No circle means no daisy.
A number in each patch indicates the local temperature.

## Change Model Parameters
For simplicity and clarity, all model parameters are written in class Params in `Params.java`.
One can change the parameter value in the file and then recompile `DaisyWorld.java` to observe
different model behaviour.
### Turn on Extension
An extension of adding the soil quality attribute to the environment can be turned on by setting
variable `QUALITY_SWITCH` to `1` in file `Params.java`.

## Collect the Results
Apart from a world visualization of the final tick, key model variables of each tick is recorded
and written to a file `data.csv` that can be opened by Excel.
The first line of the file records the tick number (from 0 to the specified tick). 
The second line records the global temperature for each tick.
The third line records the black population for each tick.
The forth line records the white population for each tick.
The fifth line records the global soil quality for each tick.

## Plot using MATLAB
Beside a csv file that can be opened by Excel, a MATLAB script is provided for nicer plotting.
After running the model, run `plot.m` in MATLAB in the same directory with the java program.
MATLAB will generate three plots. First plots the global temperature against ticks.
Second plots the black and white population against ticks.
Third plots the global soil quality against ticks.
