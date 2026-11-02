#!/usr/bin/env python3
"""Copy the nuclearcraft: assets and lang keys QMD refers to from NCO, when plain NC doesn't have them. Plain NC's own files are never replaced."""
import glob, json, os, re, shutil
ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
NCO = ROOT + "/sources/nco/src/main/resources/assets/nuclearcraft/"
PLAIN = ROOT + "/sources/nc-plain/src/main/resources/assets/nuclearcraft/"
DEST = ROOT + "/src/main/resources/assets/nuclearcraft/"
QMD_RES = ROOT + "/src/main/resources/assets/qmd/"
QMD_JAVA = ROOT + "/src/main/java/lach_01298/qmd/"
seen, copied, missing = set(), [], []


def want(rel):
    if rel in seen:
        return
    seen.add(rel)
    if os.path.exists(PLAIN + rel):
        return
    if not os.path.exists(NCO + rel):
        missing.append(rel)
        return
    os.makedirs(os.path.dirname(DEST + rel), exist_ok=True)
    shutil.copy(NCO + rel, DEST + rel)
    copied.append(rel)
    if rel.endswith(".png") and os.path.exists(NCO + rel + ".mcmeta"):
        shutil.copy(NCO + rel + ".mcmeta", DEST + rel + ".mcmeta")
    if rel.endswith(".json"):
        scan(open(NCO + rel, errors="replace").read(), "models/")


def scan(text, default_dir):
    for ref in re.findall(r"nuclearcraft:([a-zA-Z0-9_/.]+)", text):
        if ref.startswith(("blocks/", "items/")):
            want("textures/" + ref + ".png")
        elif ref.startswith("textures/"):
            want(ref if ref.endswith(".png") else ref + ".png")
        elif ref.startswith("item/") or ref.startswith("block/") or "/" not in ref:
            for candidate in (ref, "block/" + ref, "item/" + ref):
                if os.path.exists(NCO + "models/" + candidate + ".json") or os.path.exists(PLAIN + "models/" + candidate + ".json"):
                    want("models/" + candidate + ".json")
                    break
            else:
                missing.append("models/" + ref)


for f in glob.glob(QMD_RES + "**/*.json", recursive=True):
    scan(open(f, errors="replace").read(), "models/")
for f in glob.glob(QMD_JAVA + "**/*.java", recursive=True):
    s = open(f, errors="replace").read()
    scan(s, "")
    for rel in re.findall(r'Global\.MOD_ID \+ ":(textures/[a-zA-Z0-9_/.]+)"', s):
        want(rel)
    for rel in re.findall(r'"nuclearcraft:(textures/[a-zA-Z0-9_/.]+)"', s):
        want(rel)
print("copied", len(copied)); print("\n".join(sorted(copied)[:60]))
print("not found in NCO:", sorted(set(missing))[:40])
# lang
def keys(p):
    out = {}
    for l in open(p, errors="replace"):
        if "=" in l and not l.startswith("#"):
            k, v = l.rstrip("\n").split("=", 1)
            out[k] = v
    return out
nco, plain = keys(NCO + "lang/en_us.lang"), keys(PLAIN + "lang/en_us.lang")
qmd = keys(QMD_RES + "lang/en_us.lang")
src = "".join(open(f, errors="replace").read() for f in glob.glob(QMD_JAVA + "**/*.java", recursive=True))
add = {}
for k, v in nco.items():
    if k in plain or k in qmd:
        continue
    if k.startswith(("nuclearcraft.multiblock_validation", "zerocore.api", "gui.nc.", "nc.sf.", "nc.gui", "info.nuclearcraft", "jei.nuclearcraft.heat", "tile.nuclearcraft.heat_exchanger")) or k in src:
        add[k] = v
with open(QMD_RES + "lang/en_us.lang", "a") as f:
    f.write("\n# From NCO, for the bundled classes\n")
    for k, v in sorted(add.items()):
        f.write(f"{k}={v}\n")
print("lang keys added", len(add))
