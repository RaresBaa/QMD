#!/usr/bin/env python3
"""Rewrite QMD's own nc.* references to the bundled classes in lach_01298.qmd.nco."""
import glob, os, re, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import vendor_nco as v
classes = {os.path.relpath(f, v.QMD)[len("lach_01298/qmd/nco/"):-5].replace("/", ".")
           for f in glob.glob(v.QMD + "lach_01298/qmd/nco/**/*.java", recursive=True)}
classes = {"nc." + c for c in classes}
for f in glob.glob(v.QMD + "lach_01298/qmd/**/*.java", recursive=True):
    if "/qmd/nco/" in f:
        continue
    s = open(f, errors="replace").read()
    n = v.relocate(s, classes)
    if n != s:
        open(f, "w").write(n)
