#!/usr/bin/env python3
"""Copy the NuclearCraft Overhauled classes QMD needs into QMD under a new package.

Run with `--list` to print the class list without writing. The copied classes live in
lach_01298.qmd.nco and are MIT licensed by tomdodd4598 (see NCO-LICENSE.md). Classes in KEEP
stay as references to the real nc.* classes of plain NuclearCraft 2.19c.

Usage: vendor_nco.py [--list]
"""
import collections
import glob
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
NCO = ROOT + "/sources/nco/src/main/java/"
QMD = ROOT + "/src/main/java/"
PKG = "lach_01298.qmd.nco"

# Shared with the running NuclearCraft mod: state or content that must not be duplicated.
KEEP = re.compile(r"^nc\.(Global|ModCheck|NuclearCraft|config\.NCConfig|init\.\w+|tab\.\w+|recipe\.NCRecipes"
                  r"|capability\.radiation\..*|radiation\..*)$")
# Features that plain NuclearCraft has no counterpart for, or QMD doesn't need. Anything that still
# refers to these has to be cut by hand.
CUT = re.compile(r"^nc\.(worldgen\..*|integration\.(tconstruct|crafttweaker|gtce|hwylaprovider|groovyscript|projecte|tis3d"
                 r"|opencomputers|mekanism|drawers|hwyla)\b.*|tile\.(fission|turbine|quantum|rtg|battery|distributor|machine)\..*"
                 r"|multiblock\.(fission|turbine|quantum|rtg|battery|distributor|machine)\..*"
                 r"|block\.(fission|turbine|quantum|battery|rtg)\..*|command\..*|proxy\..*|model\..*|block\.fluid\..*|fluid\..*"
                 r"|integration\.jei\.(NCJEI|category\.(?!JEIRecipeCategory\b|.*Base\b).*)"
                 r"|(gui|container)\.multiblock\.(controller|port)\.\w*(Fission|Salt|Solid|Pebble|Turbine|DecayPool|Distiller|Electrolyzer"
                 r"|Infiltrator|Machine)\w*|(gui|container)\.multiblock\.\w*(Fission|Salt|Solid|Pebble|Turbine)\w*"
                 r"|network\.(tile\.multiblock|multiblock)\.\w*(Fission|Salt|Solid|Pebble|Turbine|Machine|Infiltrator|Electrolyzer|Distiller|Battery|RTG|Quantum)\w*"
                 r"|recipe\.(multiblock|processor|generator|other|vanilla)\..*|handler\.(TileInfoHandlerStub))$")


def path(c):
    return NCO + c.replace(".", "/") + ".java"


def resolve(ref):
    parts = ref.split(".")
    for i in range(len(parts), 1, -1):
        q = ".".join(parts[:i])
        if os.path.exists(path(q)):
            return q
    return None


def references(c):
    src = open(path(c), errors="replace").read()
    pkg = c.rsplit(".", 1)[0]
    refs = set()
    for star, name in re.findall(r"^import\s+(static\s+)?(nc\.[\w.*]+);", src, re.M):
        if name.endswith(".*"):
            base = name[:-2]
            d = NCO + base.replace(".", "/")
            if os.path.isdir(d):  # a package: only the classes it names
                refs |= {base + "." + w for w in set(re.findall(r"\b[A-Z]\w+\b", src)) if os.path.exists(path(base + "." + w))}
            else:
                refs.add(base)
        else:
            refs.add(name)
    refs |= {pkg + "." + w for w in set(re.findall(r"\b[A-Z]\w+\b", src)) if os.path.exists(path(pkg + "." + w))}
    return {t for t in map(resolve, refs) if t}


def seeds():
    out = set()
    for f in glob.glob(QMD + "lach_01298/qmd/**/*.java", recursive=True):
        if "/qmd/nco/" in f:
            continue
        for name in re.findall(r"^import\s+(?:static\s+)?(nc\.[\w.]+)(?:\.\*)?;", open(f, errors="replace").read(), re.M):
            t = resolve(name)
            if t:
                out.add(t)
    return out


def closure():
    seen, cut, stack = set(), collections.defaultdict(set), [s for s in seeds() if not KEEP.match(s)]
    while stack:
        c = stack.pop()
        if c in seen:
            continue
        if CUT.match(c):
            cut[c].add("(seed)")
            continue
        seen.add(c)
        for r in references(c):
            if KEEP.match(r):
                continue
            if CUT.match(r):
                cut[r].add(c)
            elif r not in seen:
                stack.append(r)
    return seen, cut


def relocate(src, classes):
    def sub(m):
        ref = m.group(0)
        t = resolve(ref)
        if t in classes or (t is None and ref.rstrip(".") in packages):
            return PKG + ref[2:]
        return ref
    packages = {c.rsplit(".", 1)[0] for c in classes}
    return re.sub(r"\bnc\.[a-zA-Z_][\w.]*", sub, src)


def write(classes):
    for c in sorted(classes):
        dest = QMD + (PKG + c[2:]).replace(".", "/") + ".java"
        os.makedirs(os.path.dirname(dest), exist_ok=True)
        with open(dest, "w") as f:
            f.write(relocate(open(path(c), errors="replace").read(), classes))


if __name__ == "__main__":
    classes, cut = closure()
    lines = sum(open(path(c), errors="replace").read().count("\n") for c in classes)
    print(len(classes), "classes,", lines, "lines")
    print(collections.Counter(".".join(c.split(".")[:3]) for c in classes).most_common(60))
    print("CUT:")
    for c, by in sorted(cut.items()):
        print(" ", c, "<-", sorted(by)[:4], len(by))
    if "--list" not in sys.argv:
        write(classes)
