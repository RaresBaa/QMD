#!/usr/bin/env bash
# Dev server: run plain NC condenser tubes on the mercury and condensate recipes QMD adds, and dump their progress. Needs a world whose spawn is at 0,0 (see testblocks.sh).
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
c() { echo "$1" >&3; sleep 0.2; }
# build <x> <fluid>: a 5x3x3 exchanger, its middle a condenser tube holding <fluid>, with water on two sides
build() {
  x=$1; a=$((x+1)); b=$((x+2)); d=$((x+3)); e=$((x+4))
  c "fill $x 100 20 $e 102 22 nuclearcraft:heat_exchanger_frame"
  c "fill $a 100 21 $d 100 21 nuclearcraft:heat_exchanger_wall"
  c "fill $a 102 21 $d 102 21 nuclearcraft:heat_exchanger_wall"
  c "fill $a 101 20 $d 101 20 nuclearcraft:heat_exchanger_wall"
  c "fill $a 101 22 $d 101 22 nuclearcraft:heat_exchanger_wall"
  c "setblock $x 101 21 nuclearcraft:heat_exchanger_wall"
  c "setblock $e 101 21 nuclearcraft:heat_exchanger_wall"
  c "setblock $a 101 21 minecraft:water"
  c "setblock $d 101 21 minecraft:water"
  c "setblock $b 101 21 nuclearcraft:heat_exchanger_condenser_tube_thermoconducting 0 replace {tank0:{fluidName:\"$2\",fluidAmount:64000}}"
  c "setblock $b 101 19 minecraft:redstone_block"
  c "setblock $b 101 20 nuclearcraft:heat_exchanger_controller"
}
c 'fill 18 99 18 50 103 24 minecraft:air'
build 20 exhaust_mercury
build 28 hot_mercury
build 36 condensate_water
sleep 12
c 'blockdata 22 101 21 {}'
c 'blockdata 30 101 21 {}'
c 'blockdata 38 101 21 {}'
sleep 1; echo stop >&3; wait $pid; echo gradle exit $?
