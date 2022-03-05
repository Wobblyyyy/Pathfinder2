artifacts=("geometry" "kinematics" "core" "frc" "OdometryCore")

for artifact in ${artifacts[@]}; do
    rm -r "pathfinder2-$artifact/build"
done
