# Battlefield
### A small confrontation simulator

## Building
Maven and java compiler are required.
Go to **Battlefield/** directory and run
```
mvn package
```
#### Additionally
The dependent project is [JogAmp](https://jogamp.org/) (modules GlueGen and JOGL), which uses native libraries. The program is prepared for assembly for:
* linux-amd64
* linux-i586
* macosx-universal
* solaris-amd64
* solaris-i586
* windows-amd64
* windows-i586

***Note:*** tested on **Linux Mint 20 x86_64** only with **openjdk-8**, **openjdk-11** and **amazon-corretto-11**

Dependencies that are not required can be removed from the **pom.xml** file if `v2.3.2` is used, **or** removed from the **pom.xml** and **src/assembly/src-ja-2.3.1.xml** files if `v2.3.1` is used.

If you need to change the version of JogAmp, change the contents of the jogamp.version tag. Unfortunately versions `v2.3.2` and `v2.3.1` are packaged differently. Separate assembly files are provided for them. See **src/assembly/** folder. Older versions will probably pack like `v2.3.1`.

## Running
#### Linux
Go to **Battlefield/target/** directory and run
```
java -jar battelefield-0.2.0-fat.jar
```
or give permission to run with `chmod` and run
```
./battelefield-0.2.0-fat.jar
```
or just double click on this .jar in your file manager.

***Note:*** to avoid [issues](https://stackoverflow.com/questions/22564780/debugging-ld-inconsistency-detected-by-ld-so) with JogAmp recommend do not using **openjre-11**.

## Using
### Rules
* Each team consists of squads.
* Each squad consists of units.
* All units of one team are opponents for units of the other team.
* All units of the same squad have common stats: **damage**, **defence** and **velocity**.
* Each unit has its own **health**.
* Each unit looks for a path to the opponent, approaches him and tries to destroy him.
* As soon as a unit has no health left, it disappears.
* Each iteration, each of the units in a random sequence takes a step or strikes. If, after taking a step, the unit is next to the enemy, then it immediately inflicts increased damage.
* If there are several opponents nearby, then the damage is dealt to a random.
* There is no beginning of a battle or an end of a battle. You decide for yourself what and how.
* You can change the stats of the squad, add and remove units, squads or teams at any time!
* The stats damage, defense and velocity of all units of the squad change immediately.
* The initial health applies only to those units of the squad that were added to the battlefield after the value was changed.

### Interface and control
* First, create a field.
* The window is divided into two parts. On the right - the battle field is displayed. On the left - there are tables with lists of teams and squads.
* To add a squad - select a team from the table.
* To add units - select a specific squad and draw their location on the field by holding the **LMB**.
* To remove units - select a specific squad and swipe over its units on the field, holding **RMB**.
* To change the name or color of a squad or team - click on a cell in the table.
* To switch the color of teams/squads - use the menu in which you created the field earlier.
* Press **spacebar** for iteration.
