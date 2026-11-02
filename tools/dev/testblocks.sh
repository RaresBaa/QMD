#!/usr/bin/env bash
# Dev server: place every QMD block with /setblock, let it tick, then stop.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"; echo n >&3
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 200); do grep -qE 'Done \([0-9.]+s\)!' "$log" && break; kill -0 $pid 2>/dev/null || { echo EXITED early; wait $pid; exit 1; }; sleep 2; done
echo 'setworldspawn 0 100 0' >&3; sleep 3
n=0
while read -r b; do
  x=$(( (n % 12) * 3 )); z=$(( (n / 12) * 3 )); n=$((n+1))
  echo "setblock $x 100 $z qmd:$b" >&3
  sleep 0.1
done < "$here/blocks.txt"
echo "placed $n"; sleep 25
echo stop >&3; wait $pid; echo gradle exit $?
