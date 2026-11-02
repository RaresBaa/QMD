#!/usr/bin/env bash
# Dev server: build a real Liquefier from one nozzle, one compressor and two tubes, feed it, and dump the controller. Needs a world whose spawn is at 0,0 (see testblocks.sh).
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
c() { echo "$1" >&3; sleep 0.2; }
c 'fill 8 99 8 16 106 16 minecraft:air'
c 'fill 10 100 10 14 105 14 qmd:liquefier_casing'
c 'fill 11 101 11 13 104 13 minecraft:air'
c 'fill 11 102 11 13 102 13 qmd:liquefier_baffle'
c 'setblock 12 102 12 qmd:liquefier_nozzle'
c 'setblock 12 105 12 qmd:liquefier_compressor 0'
# Settings run down, up, north, south, west, east; 1 is open
tube='{settings:[B;1b,1b,0b,0b,0b,0b]}'
c "setblock 12 103 12 qmd:liquefier_tube_copper 0 replace $tube"
c "setblock 12 104 12 qmd:liquefier_tube_copper 0 replace $tube"
c 'setblock 10 103 12 qmd:liquefier_inlet'
c 'setblock 14 104 12 qmd:liquefier_outlet'
c 'setblock 12 103 10 qmd:liquefier_fluid_port 2'
c 'setblock 12 104 14 qmd:liquefier_fluid_port 11'
c 'setblock 12 103 14 qmd:liquefier_energy_port'
c 'setblock 12 102 10 qmd:liquefier_controller 2'
sleep 3
# The casing at the lowest corner saves the multiblock's data; metadata 1 there means the frame formed
c 'testforblock 10 100 10 qmd:liquefier_casing 1'
c 'blockdata 10 100 10 {}'
c 'setblock 9 103 12 nuclearcraft:water_source'
c 'setblock 12 103 9 nuclearcraft:nitrogen_collector_dense'
c 'setblock 12 103 15 nuclearcraft:rtg_californium'
c 'setblock 12 102 9 minecraft:redstone_block'
sleep 10
c 'blockdata 10 100 10 {}'
sleep 1; echo stop >&3; wait $pid; echo gradle exit $?
