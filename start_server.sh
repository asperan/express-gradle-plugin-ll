#!/bin/bash
# An error fails the whole script
set -e

# Regenerate code with gradle
./gradlew generateCode

# Compile Typescript
npm run build

# Start server
npm start
