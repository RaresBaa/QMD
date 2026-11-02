#!/usr/bin/env bash
# Dev server: a real linac fires protons into a target chamber holding beryllium, and the alpha output runs down a beamline into a beam dump. Needs a world whose spawn is at 0,0 (see testblocks.sh).
# Plain NC has no power source or hydrogen supply this large, so power, hydrogen and water are loaded through the multiblock data on each lowest-corner casing.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
c() { echo "$1" >&3; sleep 0.1; }
c 'fill 18 118 15 40 126 26 minecraft:air'

# Linac along X: 20..34, 120..124, 20..24; the beam runs at y 122, z 22
acc='multiblockData:{isNew:1b,energyStorage:{energy:4600000L,capacity:4680000L,maxTransfer:4680000},tanks0:{capacity:1000000,FluidName:"water",Amount:500000},tanks2:{capacity:1000000,FluidName:"hydrogen",Amount:500000}}'
c "setblock 20 120 20 qmd:accelerator_casing 0 replace {$acc}"
c 'fill 20 120 20 34 124 24 qmd:accelerator_casing 0 keep'
c 'fill 21 121 21 33 123 23 minecraft:air'
c 'fill 21 122 22 33 122 22 qmd:accelerator_beam'
# cavity <x>: the 8 blocks around the beam
ring() { for p in "123 21" "123 22" "123 23" "122 21" "122 23" "121 21" "121 22" "121 23"; do c "setblock $1 ${p% *} ${p#* } qmd:$2 $3"; done; }
quad() {
  for p in "$1 123 22" "$1 121 22" "$1 122 21" "$1 122 23"; do c "setblock $p qmd:accelerator_magnet 0"; done
  # Redstone coolers need a cavity and a magnet beside them
  for p in "$1 123 23" "$1 123 21" "$1 121 23" "$1 121 21"; do c "setblock $p qmd:accelerator_cooler1 2"; done
}
for x in 22 24 26 28 30 32; do ring $x accelerator_cavity 0; done
for x in 21 27 33; do quad $x; done
# Water coolers need one cavity beside them
for x in 23 25 29 31; do ring $x accelerator_cooler1 0; done
c 'setblock 20 122 22 qmd:accelerator_source 5'
c 'setblock 34 122 22 qmd:accelerator_beam_port 1 replace {mode:1,setting:1}'
c 'setblock 23 122 20 qmd:accelerator_energy_port'
c 'setblock 25 122 20 qmd:accelerator_vent 2'
c 'setblock 29 122 20 qmd:accelerator_vent 10'
c 'setblock 25 122 19 nuclearcraft:water_source'
c 'setblock 27 122 20 qmd:linear_accelerator_controller 2'

# Target chamber: 35..37, 121..123, 21..23
cha='multiblockData:{energyStorage:{energy:1000000L,capacity:1080000L,maxTransfer:1080000}}'
c "setblock 35 121 21 qmd:particle_chamber_casing 0 replace {$cha}"
c 'fill 35 121 21 37 123 23 qmd:particle_chamber_casing 0 keep'
c 'setblock 36 122 22 qmd:particle_chamber'
c 'setblock 35 122 22 qmd:particle_chamber_beam_port 0'
c 'setblock 36 122 21 qmd:particle_chamber_beam_port 1 replace {mode:1}'
c 'setblock 37 122 22 qmd:particle_chamber_energy_port'
c 'setblock 36 123 22 qmd:target_chamber_controller 1 replace {Items:[{Slot:0b,id:"nuclearcraft:ingot",Count:64b,Damage:9s}]}'

c 'setblock 36 122 20 qmd:beamline 2'

# Beam dump: 35..37, 121..123, 17..19
c "setblock 35 121 17 qmd:particle_chamber_casing 0 replace {$cha}"
c 'fill 35 121 17 37 123 19 qmd:particle_chamber_casing 0 keep'
c 'setblock 36 122 18 qmd:particle_chamber'
c 'setblock 36 122 19 qmd:particle_chamber_beam_port 0'
c 'setblock 36 122 17 qmd:particle_chamber_energy_port'
c 'setblock 35 122 18 qmd:particle_chamber_fluid_port 12'
c 'setblock 37 122 18 qmd:beam_dump_controller 5'
sleep 3

dump() { c "blockdata 20 120 20 {}"; c "blockdata 35 121 21 {}"; c "blockdata 36 123 22 {}"; c "blockdata 35 121 17 {}"; c "blockdata 36 122 20 {}"; }
c 'say FORMED'; dump
c 'setblock 27 122 19 minecraft:redstone_block'
sleep 10
c 'say RUN1'; dump
sleep 10
c 'say RUN2'; dump
sleep 1; echo stop >&3; wait $pid; echo gradle exit $?
