#!/usr/bin/env bash
# Dev server: run real irradiators on a Plutonium-Beryllium source and check their output slots. Needs a world whose spawn is at 0,0 (see testblocks.sh).
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
fuel='{Slot:1b,id:"qmd:source",Count:1b,Damage:5s,tag:{particle_storage:{particle_amount:1000000,particle_capacity:1000000}}}'
for pos in "1 101 1" "4 101 1" "1 102 1" "4 102 1"; do echo "setblock $pos minecraft:air" >&3; done; sleep 1
# Inputs arrive through hoppers because /setblock NBT skips the refresh that a real insert triggers
echo "setblock 1 101 1 qmd:irradiator 0 replace {Items:[$fuel]}" >&3
echo "setblock 4 101 1 qmd:irradiator 0 replace {Items:[$fuel]}" >&3
sleep 1
echo 'setblock 1 102 1 minecraft:hopper 0 replace {Items:[{Slot:0b,id:"qmd:semiconductor",Count:4b,Damage:3s}]}' >&3
echo "setblock 4 102 1 minecraft:hopper 0 replace {Items:[{Slot:0b,id:\"qmd:ingot\",Count:1b,Damage:${COBALT_META:-4}s}]}" >&3
sleep 25
dump() { echo "blockdata $1 {}" >&3; sleep 1; }
dump "1 101 1"; dump "1 102 1"
sleep "${COBALT_WAIT:-170}"
dump "4 101 1"; dump "4 102 1"; dump "1 101 1"
sleep 2; echo stop >&3; wait $pid; echo gradle exit $?
