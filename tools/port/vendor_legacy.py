#!/usr/bin/env python3
"""Recreate the nclegacy classes QMD uses from NCO's git history (MIT), at the 2o.6.6 commit they were split from."""
import os, re, subprocess, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import vendor_nco as v
import glob
COMMIT = "58090d49"
REPO = v.ROOT + "/sources/nc-repo"
PKG = v.PKG + ".legacy"
# new class -> (subpackage, old path under nc/)
CLASSES = {
    "ITileGuiLegacy": ("tile", "tile/ITileGui"),
    "IProcessorLegacy": ("tile", "tile/processor/IProcessor"),
    "IItemProcessorLegacy": ("tile", "tile/processor/IItemProcessor"),
    "IFluidProcessorLegacy": ("tile", "tile/processor/IFluidProcessor"),
    "IItemFluidProcessorLegacy": ("tile", "tile/processor/IItemFluidProcessor"),
    "IUpgradableLegacy": ("tile", "tile/processor/IUpgradable"),
    "TileItemFluidProcessorLegacy": ("tile", "tile/processor/TileItemFluidProcessor"),
    "TileItemFluidGeneratorLegacy": ("tile", "tile/generator/TileItemFluidGenerator"),
    "IGeneratorLegacy": ("tile", "tile/generator/IGenerator"),
    "IItemGeneratorLegacy": ("tile", "tile/generator/IItemGenerator"),
    "IFluidGeneratorLegacy": ("tile", "tile/generator/IFluidGenerator"),
    "IItemFluidGeneratorLegacy": ("tile", "tile/generator/IItemFluidGenerator"),
    "NCContainerLegacy": ("container", "container/NCContainer"),
    "ContainerTileLegacy": ("container", "container/ContainerTile"),
    "ContainerMachineConfigLegacy": ("container", "container/processor/ContainerMachineConfig"),
    "ContainerSorptionsLegacy": ("container", "container/processor/ContainerSorptions"),
    "ContainerItemFluidProcessorLegacy": ("container", "container/processor/ContainerItemFluidProcessor"),
    "NCGuiLegacy": ("gui", "gui/NCGui"),
    "GuiItemSorptionsLegacy": ("gui", "gui/processor/GuiItemSorptions"),
    "GuiFluidSorptionsLegacy": ("gui", "gui/processor/GuiFluidSorptions"),
    "ItemBlockMetaLegacy": ("block.item", "block/item/ItemBlockMeta"),
    "JEIBasicCategoryLegacy": ("jei", "integration/jei/JEIBasicCategory"),
    "JEIMachineCategoryLegacy": ("jei", "integration/jei/JEIMachineCategory"),
    "JEIBasicRecipeWrapperLegacy": ("jei", "integration/jei/JEIBasicRecipeWrapper"),
    "JEIMachineRecipeWrapperLegacy": ("jei", "integration/jei/JEIMachineRecipeWrapper"),
    "JEIHelperLegacy": ("jei", "integration/jei/JEIHelper"),
}
OLD_TO_NEW = {v_[1].replace("/", "."): k for k, v_ in CLASSES.items()}
SIMPLE = {p.rsplit("/", 1)[1]: k for k, (_, p) in CLASSES.items()}
bundled = {"nc." + os.path.relpath(f, v.QMD + "lach_01298/qmd/nco/")[:-5].replace("/", ".")
           for f in glob.glob(v.QMD + "lach_01298/qmd/nco/**/*.java", recursive=True) if "/nco/legacy/" not in f}
for new, (sub, old) in CLASSES.items():
    r = subprocess.run(["git", "-C", REPO, "show", f"{COMMIT}:src/main/java/nc/{old}.java"], capture_output=True, text=True, errors="replace")
    if r.returncode:
        print("missing", old); continue
    s = r.stdout
    s = re.sub(r"^package [\w.]+;", f"package {PKG}.{sub};", s, count=1, flags=re.M)
    def fqn(m):
        ref = m.group(0).rstrip(".")
        tail = ref[3:]
        if tail in OLD_TO_NEW:
            n = OLD_TO_NEW[tail]
            return f"{PKG}.{CLASSES[n][0]}.{n}"
        t = v.resolve(ref)
        if t in bundled or (t is None and ref in {c.rsplit('.', 1)[0] for c in bundled}):
            return v.PKG + m.group(0)[2:]
        return m.group(0)
    s = re.sub(r"\bnc\.[a-zA-Z_][\w.]*", fqn, s)
    for old_simple, new_simple in SIMPLE.items():
        s = re.sub(r"(?<![\w.])" + old_simple + r"\b", new_simple, s)
    s = re.sub(r"\bIJEIHandler\b", "IJEIHandlerLegacy", s).replace("NCJEI.IJEIHandlerLegacy", "IJEIHandlerLegacy")
    s = re.sub(r"^import nc\.integration\.jei\.NCJEI[^\n]*\n", "", s, flags=re.M)
    dest = v.QMD + f"{PKG}.{sub}".replace(".", "/") + f"/{new}.java"
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    open(dest, "w").write(s)
# QMD's own references
for f in glob.glob(v.QMD + "lach_01298/qmd/**/*.java", recursive=True):
    if "/qmd/nco/" in f: continue
    s = open(f, errors="replace").read()
    n = re.sub(r"\bnclegacy\.((?:\w+\.)*)", lambda m: PKG + "." + m.group(1), s)
    if n != s: open(f, "w").write(n)
