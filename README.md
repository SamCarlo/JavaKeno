# JavaKeno: A Java-Based Keno Game

## What is this? 
JavaKeno is a betting game that is played on a graphical user interface. It was written using the JSwing library and was the hardest assignment in my ICS 211 class in Fall 2025. 

The game works by selecting cards/icons to bet on, setting your bet amount, and receiving a payout or loss based on the number of winning cards you selected. After a few rounds, your final results will be shown. A .txt file will be produced in the working directory that shows you the history of your gameplay. 

## How do I run it? 
1) Make sure java is installed on your device. 

**MacOS:**
'''bash
brew install openjdk
'''

**Windows:**
1. Download the installer from [adoptium.net](https://adoptium.net)
2. Run the `.msi` and follow the prompts
3. Open a new terminal and verify:

'''cmd
```cmd
java -version
```
'''

**Arch Linux:**
'''bash
sudo pacman -S jdk-openjdk
'''

**Linux distros using apt:**
'''bash
sudo apt update
sudo apt install default-jdk
'''

2) Compile the .java files. With terminal set to the project folder, enter the following command:

'''bash
javac *.java
'''

3) Run
'''bash
java KenoApp
'''

## Features
- Winning and losing tiles are highlighted with colors at the end of each round
- .txt file of final results is produced at the end. 

