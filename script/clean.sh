artifacts=("geometry" "kinematics" "core" "frc" "OdometryCore")

for artifact in ${artifacts[@]}; do
    path="pathfinder2-$artifact"

    rm -f -r "$path/build"
    rm -f -r "$path/bin"
done
