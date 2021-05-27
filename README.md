# osw-dev 

This repo contains submodules for *csw*, *esw*, *sequencer-scripts* and *esw-ocs-eng-ui*.
Goal of this repo is to maintain version compatibility between these repos.

## Initialize

First time users should run following commands, this will clone the repo and fetch submodules

```bash 
git clone https://github.com/tmtsoftware/osw-dev.git
cd osw-dev
git submodule update --init --recursive
```

## Overview

This repository contains `TMTRunner` application which supports following three commands

1. **init** - Initializes all the submodules. Run this command only once when you clone this repo for the first time.
   Ignore this command if you have followed [Initialize](#Initialize) section. 
1. **update-submodules** - Updates all the projects with correct compatible versions
1. **start** - Starts CSW and ESW services. Internally this executes following two commands:
    1. `sbt csw-services/run start -c`
    1. `sbt esw-services/run start-eng-ui-services`
1. **print-versions** - Prints version compatibility table for `sequencer-scripts`, `esw` and `csw` repo

## Dev Workflow

1. Execute `git pull --rebase` command to fetch the latest changes of `osw-dev`
1. Execute `sbt` command to enter into sbt console
1. Execute `run update-submodules` command to update project
1. Execute `run start` command to start CSW and ESW services

## TODOS

1. Automatically detect `INTERFACE_NAME` and `AAS_INTERFACE_NAME`. Currently, it defaults to `en0` if not set explicitly by user. 