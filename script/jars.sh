# build the project and copy all of the JAR files to the ./build/ directory
# this script requires you to provide a single parameter, which is the
# version of the JAR file to copy

base_name="core"
version=$1
artifacts=("geometry" "kinematics" "core" "frc" "OdometryCore" "commands")

echo "removing build directory"
rm -r -f "build"
echo "creating build/libs directory"
mkdir "build"
mkdir "build/libs"

echo "building project..."
./gradlew build
echo "finished build!"

echo "removing empty artifacts..."
rm "build/libs/Pathfinder2-v$version.jar"
rm "build/libs/Pathfinder2-v$version-sources.jar"
rm "build/libs/Pathfinder2-v$version-javadoc.jar"

copy_jar() {
    echo "processing archive '$base_name'"
    local jar="pathfinder2-${base_name}-v${version}.jar"
    local sources="pathfinder2-${base_name}-v${version}-sources.jar"
    local javadoc="pathfinder2-${base_name}-v${version}-javadoc.jar"

    local src="pathfinder2-${base_name}/build/libs"
    local dst="build/libs"

    local src_jar="$src/$jar"
    local src_sources="$src/$sources"
    local src_javadoc="$src/$javadoc"

    local dst_jar="$dst/$jar"
    local dst_sources="$dst/$sources"
    local dst_javadoc="$dst/$javadoc"

    printf "copying artifact 'jar'...\n- src: $src_jar\n- dst: $dst_jar\n"
    cp $src_jar $dst_jar
    printf "copying artifact 'sources'...\n- src: $src_sources\n- dst: $dst_sources\n"
    cp $src_sources $dst_sources
    printf "copying artifact 'javadoc'...\n- src: $src_javadoc\n- dst: $dst_javadoc\n"
    cp $src_javadoc $dst_javadoc
}

for artifact in ${artifacts[@]}; do
    base_name=$artifact
    copy_jar
done
