# TURKON

TURKON is a Turkish-based toy programming language designed for educational purposes. It supports variable declarations, conditionals, loops, expressions, and console output—all using Turkish keywords.

---

## 🔤 Key Features

- ✅ Turkish keywords: `değişken`, `eğer`, `iken`, `yazdır`, `aksi takdirde`
- ✅ Arithmetic & logical operations with operator precedence
- ✅ Custom BNF grammar & recursive descent parsing
- ✅ Full support for Turkish characters in identifiers

---

## 📁 Files

- `TURKONLexer.java` – Tokenizer
- `TURKONParser.java` – Parser & interpreter
- `TURKONMain.java` – Main entry
- `example1-4.tkn` – Test programs
- `build.xml`, `manifest.mf` – NetBeans support

---

## ▶️ Usage

```bash
javac TURKONLexer.java TURKONParser.java TURKONMain.java
java TURKONMain
