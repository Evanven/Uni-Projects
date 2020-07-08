#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd $DIR

gcc -Wall -pthread -o p3180019-p3170034-pizza1 p3180019-p3170034-pizza1.c
./p3180019-p3170034-pizza1.out 100 1000

$SHELL