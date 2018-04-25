#!/bin/bash

# Remove previous file
rm *.css
rm *.html
rm *.txt

# Build
ant

# Run locally
java -jar fc.jar -a 8080
