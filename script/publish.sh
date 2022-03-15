# 1. clean the project
# 2. publish JARs to mavenLocal
# 3. copy JARs

script/clean.sh
./gradlew publishToMavenLocal
script/jars.sh $1
