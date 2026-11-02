#!/usr/bin/env bash
# Starts the dev server with only the implementation dependencies, waits for it to finish loading, then stops it.
here="$(cd "$(dirname "$0")/../../sources" && pwd)"; cd "$here/.."
mkdir -p run; echo 'eula=true' > run/eula.txt
log="$here/server.log"; fifo="$here/server.in"; rm -f "$fifo"; mkfifo "$fifo"
exec 3<>"$fifo"
echo n >&3  # the online-mode prompt
"$here/../tools/dev/gradle.sh" --console=plain runServer <&3 > "$log" 2>&1 &
pid=$!
for i in $(seq 1 ${1:-300}); do
  if grep -qE 'Done \([0-9.]+s\)!' "$log"; then echo READY after ${i}s; sleep 15; echo stop >&3; break; fi
  kill -0 $pid 2>/dev/null || { echo EXITED early after ${i}s; break; }
  sleep 2
done
wait $pid; echo gradle exit $?
