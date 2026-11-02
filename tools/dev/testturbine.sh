#!/usr/bin/env bash
# Dev server: a real plain NC turbine runs on QMD's high pressure mercury recipe. Needs a world whose spawn is at 0,0 (see testblocks.sh).
# Plain NC has no block that supplies this fluid, so a small linac's coolant outlet holds the mercury and pushes it into the inlet. Tile entities stop ticking 300 ticks after boot with no players, so the build has to be quick.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
c() { echo "$1" >&3; sleep 0.1; }
c 'fill 12 138 17 27 148 26 minecraft:air'

# Turbine along X: 20..25, 140..144, 20..24; the shaft runs at y 142, z 22
c 'fill 20 140 20 25 144 24 nuclearcraft:turbine_frame 0 keep'
c 'fill 21 141 21 24 143 23 minecraft:air'
c 'fill 21 141 20 24 143 20 nuclearcraft:turbine_wall'
c 'fill 21 141 24 24 143 24 nuclearcraft:turbine_wall'
c 'fill 21 140 21 24 140 23 nuclearcraft:turbine_wall'
c 'fill 21 144 21 24 144 23 nuclearcraft:turbine_wall'
c 'fill 20 141 21 20 143 23 nuclearcraft:turbine_wall'
c 'fill 25 141 21 25 143 23 nuclearcraft:turbine_wall'
c 'fill 21 142 22 24 142 22 nuclearcraft:turbine_rotor_shaft'
c 'fill 21 143 22 24 143 22 nuclearcraft:turbine_rotor_blade_steel'
c 'fill 21 141 22 24 141 22 nuclearcraft:turbine_rotor_blade_steel'
c 'fill 21 142 21 24 142 21 nuclearcraft:turbine_rotor_blade_steel'
c 'fill 21 142 23 24 142 23 nuclearcraft:turbine_rotor_blade_steel'
c 'setblock 20 142 22 nuclearcraft:turbine_rotor_bearing'
c 'setblock 25 142 22 nuclearcraft:turbine_rotor_bearing'
c 'setblock 20 143 21 nuclearcraft:turbine_inlet'
c 'setblock 25 143 21 nuclearcraft:turbine_outlet'
c 'setblock 20 143 22 nuclearcraft:turbine_dynamo_coil 0'
c 'setblock 25 143 22 nuclearcraft:turbine_dynamo_coil 0'
c 'setblock 22 142 20 nuclearcraft:turbine_controller 2'
c 'setblock 22 142 19 minecraft:redstone_block'

# Supply linac: 14..19, 142..146, 19..23. Its outlet at 19 143 21 touches the inlet at 20 143 21.
c 'setblock 14 142 19 qmd:accelerator_casing 0 replace {multiblockData:{isNew:1b,tanks1:{capacity:115200,FluidName:"high_pressure_mercury",Amount:115200}}}'
c 'fill 14 142 19 19 146 23 qmd:accelerator_casing 0 keep'
c 'fill 15 143 20 18 145 22 minecraft:air'
c 'fill 15 144 21 18 144 21 qmd:accelerator_beam'
c 'setblock 14 144 21 qmd:accelerator_source 5'
c 'setblock 19 144 21 qmd:accelerator_beam_port 1 replace {mode:1,setting:1}'
c 'setblock 17 146 21 qmd:accelerator_energy_port'
c 'setblock 16 146 21 qmd:accelerator_vent 1'
out=''; for i in 0 1 2 3 4 5; do out="$out,fluidConnection$i:{sorption0:3,sorption1:1}"; done
c "setblock 19 143 21 qmd:accelerator_vent 13 replace {${out#,}}"
c 'setblock 15 146 21 qmd:linear_accelerator_controller 1'
dump() { c 'blockdata 20 140 20 {}'; }
c 'say FORMED'; dump
for i in $(seq 1 30); do dump; done
sleep 5
c 'say RUN1'; dump
sleep 1; echo stop >&3; wait $pid; echo gradle exit $?
