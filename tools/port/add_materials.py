#!/usr/bin/env python3
"""Add the materials QMD gets from NCO but plain NC lacks to dust2 and ingot2: enum, blockstate, texture, lang and ore dictionary."""
import json, re, struct, zlib
QMD = "src/main/java/lach_01298/qmd/"
ASSETS = "src/main/resources/assets/qmd/"
DUSTS = {"bismuth": 0xC4B5B8, "molybdenum": 0xBCC5E4, "palladium": 0xD8D8CE, "polonium": 0x9C9E7E, "radium": 0xB8E0B0, "dysprosium": 0x9FB0A6,
         "gadolinium": 0xB9BCC4, "holmium": 0xC9B58F, "terbium": 0x5BA694, "ruthenium": 0xA8A8B0, "ruthenium_106": 0xA3A3A3, "strontium_90": 0xB8BE88,
         "caesium_137": 0xADADAD, "europium_155": 0x74664A, "promethium_147": 0x96C199, "protactinium_233": 0x7E8C86}
INGOTS = {"dysprosium": 0x9FB0A6, "gadolinium": 0xB9BCC4, "holmium": 0xC9B58F, "palladium": 0xD8D8CE, "samarium": 0xA4D95F}


def ore_name(name):
    return "".join(p.capitalize() for p in name.split("_"))


def title(name):
    return " ".join(p.capitalize() for p in name.split("_"))


def tint(src, dest, color):
    d = open(src, "rb").read()
    w, h, chunks, i = 0, 0, b"", 8
    while i < len(d):
        n = struct.unpack(">I", d[i:i + 4])[0]
        t = d[i + 4:i + 8]
        if t == b"IHDR":
            w, h, bits, ctype = struct.unpack(">IIBB", d[i + 8:i + 18])
            assert bits == 8 and ctype == 6
        elif t == b"IDAT":
            chunks += d[i + 8:i + 8 + n]
        i += 12 + n
    raw, out, prev = zlib.decompress(chunks), bytearray(), bytearray(w * 4)
    stride = w * 4 + 1
    r0, g0, b0 = color >> 16 & 255, color >> 8 & 255, color & 255
    for y in range(h):
        ftype, line = raw[y * stride], bytearray(raw[y * stride + 1:(y + 1) * stride])
        for x in range(w * 4):
            a = line[x - 4] if x >= 4 else 0
            b = prev[x]
            c = prev[x - 4] if x >= 4 else 0
            if ftype == 1: line[x] = (line[x] + a) & 255
            elif ftype == 2: line[x] = (line[x] + b) & 255
            elif ftype == 3: line[x] = (line[x] + (a + b) // 2) & 255
            elif ftype == 4:
                p = a + b - c
                pa, pb, pc = abs(p - a), abs(p - b), abs(p - c)
                line[x] = (line[x] + (a if pa <= pb and pa <= pc else b if pb <= pc else c)) & 255
        prev = line
        row = bytearray(line)
        for x in range(0, w * 4, 4):
            v = (row[x] * 30 + row[x + 1] * 59 + row[x + 2] * 11) / 100 / 170
            row[x], row[x + 1], row[x + 2] = min(255, int(r0 * v)), min(255, int(g0 * v)), min(255, int(b0 * v))
        out += b"\0" + row
    def chunk(t, c):
        return struct.pack(">I", len(c)) + t + c + struct.pack(">I", zlib.crc32(t + c))
    open(dest, "wb").write(b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0)) + chunk(b"IDAT", zlib.compress(bytes(out))) + chunk(b"IEND", b""))


def add(enum, item, folder, prefix, names, label):
    p = QMD + "enums/MaterialTypes.java"
    s = open(p).read()
    m = re.search(r"enum " + enum + r"\b[^{]*\{(.*?);\s*\n\s*private String name", s, re.S)
    body = m.group(1)
    next_id = max(int(x) for x in re.findall(r'",\s*(\d+)\)', body)) + 1
    new = body.rstrip()
    for n in names:
        new += f',\n\t\t{n.upper()}("{n}", {next_id})'
        next_id += 1
    s = s[:m.start(1)] + new + s[m.end(1):]
    open(p, "w").write(s)
    bs = ASSETS + f"blockstates/items/{item}.json"
    j = json.load(open(bs))
    lang = ASSETS + "lang/en_us.lang"
    with open(lang, "a") as f:
        f.write("\n")
        for n, color in names.items():
            j["variants"]["type"][n] = {"textures": {"layer0": f"qmd:items/materials/{folder}/{n}"}}
            f.write(f"item.qmd.{item}.{n}.name={title(n)} {label}\n")
            tint(ASSETS + f"textures/items/materials/{folder}/yttrium.png", ASSETS + f"textures/items/materials/{folder}/{n}.png", color)
    json.dump(j, open(bs, "w"), indent=4)
    d = QMD + "QMDOreDictionary.java"
    t = open(d).read()
    lines = "".join(f'\t\tOreDictionary.registerOre("{prefix}{ore_name(n)}", new ItemStack(QMDItems.{item}, 1, {enum}.{n.upper()}.getID()));\n' for n in names)
    anchor = '\t\tOreDictionary.registerOre("blockStrontium90"'
    open(d, "w").write(t.replace(anchor, lines + anchor, 1))


add("DustType2", "dust2", "dust", "dust", DUSTS, "Dust")
add("IngotType2", "ingot2", "ingot", "ingot", INGOTS, "Ingot")
