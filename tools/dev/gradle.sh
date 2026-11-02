#!/usr/bin/env bash
# Runs the wrapper with the JDKs from sources/. Azul 8 is what the buildscript's toolchain asks for.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"
export JAVA_HOME="$(readlink -f "$here/jdk17")"
unset JDK_JAVA_OPTIONS
cd "$here/.." && exec bash gradlew --no-daemon \
  -Porg.gradle.java.installations.auto-download=false \
  -Porg.gradle.java.installations.auto-detect=false \
  -Porg.gradle.java.installations.paths="$(readlink -f "$here/jdk8-azul"),$(readlink -f "$here/jdk17-azul"),$(readlink -f "$here/jdk21-azul"),$JAVA_HOME" "$@"
