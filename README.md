# TMT-Mono

This repo contains submodules for *csw*, *esw*, *sequencer-scripts* and *esw-ocs-eng-ui*.
Goal of this repo is to maintain version compatibility between these repos.

## Initialize

First time users should run following commands, this will clone the repo and fetch submodules

```bash 
git clone https://github.com/kpritam/tmt-mono.git
cd tmt-mono
git submodule update --init --recursive
```

## Overview

This repository contains following three applications

1. **GitUpdateSubmodules** - Updates all the projects with correct compatible versions
1. **TMTRunner** - Starts CSW and ESW services. Internally this executes following two commands:
    1. `bash csw-services/run start -c`
    1. `bash esw-services/run start-eng-ui-services`
1. **VersionGenerator** - Prints version compatibility table for `sequencer-scripts`, `esw` and `csw` repo

## Dev Workflow

1. Execute `sbt` command to enter into sbt console
1. Execute `runMain dev.apps.GitUpdateSubmodules` command to update project
1. Execute `runMain dev.apps.TMTRunner` command to start CSW and ESW services
