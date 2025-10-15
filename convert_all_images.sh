#!/usr/bin/env bash
set -euo pipefail

mkdir -p converted

for img in *.jpg *.jpeg; do
  # Bỏ qua nếu không có file phù hợp
  [ -e "$img" ] || continue
  
  base=$(basename "$img")
  # Chuyển ảnh về chuẩn jpeg, giảm thiểu lỗi
  convert "$img" -strip -interlace Plane -quality 90 "converted/$base"
  echo "✔ Converted: $img"
done

