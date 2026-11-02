#!/usr/bin/env bash
# Dev server: a real nucleosynthesis chamber runs hydrogen + deuterium into helium-3 on a creative muon beam, then its casing is broken with plasma inside. Needs a world whose spawn is at 0,0 (see testblocks.sh).
# Power, cold magnets and all fluids are loaded through the multiblock data on the lowest-corner casing.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
c() { echo "$1" >&3; sleep 0.1; }
c 'fill 18 128 18 32 138 26 minecraft:air'

# Chamber: 20..30, 130..136, 20..24. 90 K sits below the parts' 110 K limit and above liquid nitrogen's 77 K.
tank() { echo "tanks$1:{capacity:$2,FluidName:\"$3\",Amount:$4}"; }
data="multiblockData:{cold:1b,heatBuffer:{heatStored:2165625L,heatCapacity:9625000L},energyStorage:{energy:15000000L,capacity:15400000L,maxTransfer:15400000},$(tank 0 3850 liquid_nitrogen 3800),$(tank 2 100000 hot_mercury 90000),$(tank 4 10000 hydrogen 1000),$(tank 5 10000 deuterium 1000)}"
c "setblock 20 130 20 qmd:containment_casing 0 replace {$data}"
c 'fill 20 130 20 30 136 24 qmd:containment_casing 0 keep'
c 'fill 21 131 21 29 135 23 minecraft:air'
c 'fill 21 135 22 29 135 22 qmd:vacuum_chamber_beam'
c 'fill 21 132 22 21 134 22 qmd:vacuum_chamber_beam'
c 'fill 29 132 22 29 134 22 qmd:vacuum_chamber_beam'
c 'setblock 22 132 22 qmd:vacuum_chamber_plasma_nozzle 0'
c 'setblock 28 132 22 qmd:vacuum_chamber_plasma_nozzle 0'
c 'fill 23 131 22 27 131 22 qmd:vacuum_chamber_plasma_glass'
c 'fill 23 133 22 27 133 22 qmd:vacuum_chamber_plasma_glass'
c 'fill 23 132 21 27 132 21 qmd:vacuum_chamber_plasma_glass'
c 'fill 23 132 23 27 132 23 qmd:vacuum_chamber_plasma_glass'
# Iron heaters need one casing beside them, redstone heaters one beam. The redstone heater also joins the top glass row to the rest.
c 'fill 23 131 21 26 131 21 qmd:vacuum_chamber_heater 0'
c 'fill 23 131 23 26 131 23 qmd:vacuum_chamber_heater 0'
c 'setblock 25 134 22 qmd:vacuum_chamber_heater 1'
c 'setblock 20 132 22 qmd:containment_beam_port 0'
c 'setblock 20 133 22 qmd:vacuum_chamber_fluid_port 4'
c 'setblock 20 135 22 qmd:vacuum_chamber_fluid_port 4'
c 'setblock 30 133 22 qmd:vacuum_chamber_fluid_port 13'
c 'setblock 30 135 22 qmd:vacuum_chamber_fluid_port 13'
c 'setblock 22 133 20 qmd:containment_energy_port'
c 'setblock 23 133 20 qmd:containment_vent 2'
c 'setblock 25 133 20 qmd:containment_vent 10'
c 'setblock 27 133 20 qmd:vacuum_chamber_heater_vent 2'
c 'setblock 29 133 20 qmd:vacuum_chamber_heater_vent 10'
c 'setblock 25 133 24 qmd:nucleosynthesis_chamber_controller 3'
sleep 2
dump() { c 'blockdata 20 130 20 {}'; c 'testforblock 25 132 22 minecraft:air'; }
c 'say FORMED'; dump
c 'setblock 19 132 22 qmd:creative_particle_source 0 replace {ParticleStorage0:{particle_stack:{particle:"muon",amount:1000,meanEnergy:500L,focus:2.0d},maxEnergy:2147483647L,minEnergy:0L,capacity:2147483647}}'
sleep "${NUCLEO_WAIT:-10}"
c 'say RUN1'; dump
c 'setblock 24 136 20 minecraft:air'
sleep 3
c 'say BROKEN'; dump; c 'testforblock 25 133 22 minecraft:air'; c 'testforblock 21 135 22 minecraft:air'
sleep 1; echo stop >&3; wait $pid; echo gradle exit $?
