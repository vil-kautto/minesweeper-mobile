# Rhinesweeper
Rhinesweeper is a minesweeper game developed natively for android, written by using java programming language

## About
The application works just like any other minesweeper game, clear the game board without triggering a mine.

Features:
- Multiple difficulty modes
  - Easy
  - Medium
  - Hard
  - Extreme
  - Custom
- High Scores
- Settings for sound, vibration and debugging


## Setup

The test the application, download the latest release to your android device and execute the file to install it. Alternatively you can install the application's old version through the google app store.

### Development environment setup

Clone the repository and open the project root directory via Android Studio

## Development directory structure

```
minesweeper-mobile
├───app
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───fi
│       │           └───tuni
│       │               └───minesweeper
│       ├───main
│       │   ├───java
│       │   │   └───fi
│       │   │       └───tuni
│       │   │           └───minesweeper
│       │   └───res
│       │       ├───anim
│       │       ├───drawable
│       │       ├───drawable-v24
│       │       ├───font
│       │       ├───layout
│       │       ├───layout-land
│       │       ├───mipmap-anydpi-v26
│       │       ├───mipmap-hdpi
│       │       ├───mipmap-mdpi
│       │       ├───mipmap-xhdpi
│       │       ├───mipmap-xxhdpi
│       │       ├───mipmap-xxxhdpi
│       │       ├───raw
│       │       ├───values
│       │       └───xml
│       └───test
│           └───java
│               └───fi
│                   └───tuni
│                       └───minesweeper

```

Notable directories:
- root/app/src/main/java - contains all the functional components of the app
- root/app/src/main/res - contains all the resource files of the app (sounds images, values, scene layouts)
- root/app/src/main/androidManifest.xml - contains the main settings for the application 
