#!/usr/bin/env python3
"""Bundle more NCO classes, with whatever else they need, and repoint all of QMD at them. Usage: add_nco.py <nc.class> ..."""
import glob, os, re, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import vendor_nco as v
NCO_DIR = v.QMD + "lach_01298/qmd/nco/"
have = {"nc." + os.path.relpath(f, NCO_DIR)[:-5].replace("/", ".") for f in glob.glob(NCO_DIR + "**/*.java", recursive=True) if "/nco/legacy/" not in f and not f.endswith("NCOConfig.java")}
SKIP = re.compile(r"^nc\.(worldgen\..*|integration\.(tconstruct|gtce|hwylaprovider|groovyscript|projecte|tis3d|opencomputers|mekanism|drawers|hwyla)\b.*|tile\.(fission|turbine|quantum|rtg|battery|distributor|machine)\..*|multiblock\.(fission|turbine|quantum|rtg|battery|distributor|machine)\..*|block\.(fission|turbine|quantum|battery|rtg)\..*|command\..*|proxy\..*|model\..*|init\..*|handler\.\w+|recipe\.NCRecipes|recipe\.(multiblock|processor|generator|other)\..*|enumm\.MetaEnums|integration\.jei\..*)$")
stack, new = list(sys.argv[1:]), set()
while stack:
    c = stack.pop()
    if c in have or c in new or v.KEEP.match(c) or SKIP.match(c) or not os.path.exists(v.path(c)):
        continue
    new.add(c)
    stack.extend(v.references(c))
print(len(new), "added:", sorted(new))
allc = have | new
for c in new:
    dest = v.QMD + (v.PKG + c[2:]).replace(".", "/") + ".java"
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    open(dest, "w").write(open(v.path(c), errors="replace").read())
pat_pkg = {c.rsplit(".", 1)[0] for c in allc}
for f in glob.glob(v.QMD + "lach_01298/qmd/**/*.java", recursive=True):
    if f.endswith("NCBridge.java"):  # refers to the real NC classes on purpose
        continue
    s = open(f, errors="replace").read()
    def sub(m):
        ref = m.group(0).rstrip(".")
        t = v.resolve(ref)
        if t in allc or (t is None and ref in pat_pkg):
            return v.PKG + m.group(0)[2:]
        return m.group(0)
    n = re.sub(r"(?<![\w.])nc\.[a-zA-Z_][\w.]*", sub, s)
    if n != s:
        open(f, "w").write(n)
