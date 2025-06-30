#!/usr/bin/env bash
ext="${0##*.}"
if [[ "$ext" == "js" ]]; then
  # Bóc tách và chạy JS
  sed -n '/^JS_START$/,/^JS_END$/p' "$0" | sed '1d;$d' | node
  exit
elif [[ "$ext" == "java" ]]; then
  # Bóc tách và chạy Java
  sed -n '/^JAVA_START$/,/^JAVA_END$/p' "$0" \
    | sed '1d;$d' > Poly.java \
    && javac Poly.java && java Poly
  exit
fi

# ----- Đoạn JavaScript -----
JS_START
console.log("Hello from JavaScript!");
JS_END

# ----- Đoạn Java -----
JAVA_START
class Poly {
    public static void main(String[] args) {
        System.out.println("Hello from Java!");
    }
}
JAVA_END
