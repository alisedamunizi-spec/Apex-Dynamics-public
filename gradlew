#!/usr/bin/env sh

################################################################################
##
##  Gradle start script for UN*X
##
################################################################################

# Attempt to set APP_HOME
# Resolve links instead of copying common problems
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
SAVED="`pwd`"
CDPATH=/
cd "`dirname \"$PRG\"`" >/dev/null
APP_HOME="`pwd`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM flags to this script.
DEFAULT_JVM_OPTS=""

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        # JDK installer for Windows places java.exe in C:\Windows\System32
        # but JAVAW.EXE is not there and executing java.exe from there fails.
        # Look for JAVA_HOME first.
        JAVACMD="$JAVA_HOME/bin/java"
    fi
fi

if [ -z "$JAVACMD" ] ; then
    JAVCMD="java"
    warn "JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $GRADLE_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
