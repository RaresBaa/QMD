#!/usr/bin/env python3
"""Copy the heat exchanger blocks, tiles and assets the Liquefier is built from out of NCO, as qmd:liquefier_* blocks. Plain NC's own files are never replaced."""
import glob, os, re, shutil
ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
NCO_JAVA = ROOT + "/sources/nco/src/main/java/nc/"
NCO_RES = ROOT + "/sources/nco/src/main/resources/assets/nuclearcraft/"
JAVA = ROOT + "/src/main/java/lach_01298/qmd/nco/"
RES = ROOT + "/src/main/resources/assets/"
BUNDLED = r"nc\.(block|tile|multiblock|util)\b"


def java(rel):
    text = open(NCO_JAVA + rel).read()
    text = re.sub(BUNDLED, lambda m: "lach_01298.qmd.nco." + m.group(1), text)
    os.makedirs(os.path.dirname(JAVA + rel), exist_ok=True)
    open(JAVA + rel, "w").write(text)


def rewrite(text):
    text = text.replace("nuclearcraft:block/heat_exchanger_tube", "qmd:block/liquefier_tube")
    text = text.replace("nuclearcraft:heat_exchanger_tube", "qmd:liquefier_tube")
    return text


def asset(src, dest):
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    open(dest, "w").write(rewrite(open(src).read()))


for part in "Casing Glass Baffle Inlet Outlet Tube".split():
    java("block/hx/BlockHeatExchanger%s.java" % part)
for part in "Casing Glass".split():
    java("tile/hx/TileHeatExchanger%s.java" % part)
java("block/property/PropertySidedEnum.java")

for name in "casing glass baffle inlet outlet".split():
    asset(NCO_RES + "blockstates/heat_exchanger_%s.json" % name, RES + "qmd/blockstates/liquefier_%s.json" % name)
for tube in "copper hard_carbon thermoconducting".split():
    asset(NCO_RES + "blockstates/heat_exchanger_tube_%s.json" % tube, RES + "qmd/blockstates/liquefier_tube_%s.json" % tube)
    asset(NCO_RES + "models/item/heat_exchanger_tube_%s.json" % tube, RES + "qmd/models/item/liquefier_tube_%s.json" % tube)
for path in glob.glob(NCO_RES + "models/block/heat_exchanger_tube*.json"):
    asset(path, RES + "qmd/models/block/liquefier_tube" + os.path.basename(path)[len("heat_exchanger_tube"):])
for path in glob.glob(NCO_RES + "models/block/heat_exchanger_tube/*.json"):
    asset(path, RES + "qmd/models/block/liquefier_tube/" + os.path.basename(path))

# The textures keep their nuclearcraft: paths, as liquefier_nozzle and liquefier_compressor already use them. Plain NC has none at these paths.
tex = "textures/blocks/heat_exchanger/"
for root, _, files in os.walk(NCO_RES + tex):
    for f in files:
        rel = os.path.relpath(os.path.join(root, f), NCO_RES)
        dest = RES + "nuclearcraft/" + rel
        if "condenser" in rel or "controller" in rel or "redstone_port" in rel or "computer_port" in rel:
            continue
        os.makedirs(os.path.dirname(dest), exist_ok=True)
        shutil.copy(os.path.join(root, f), dest)
