#!/usr/bin/env bash
set -euo pipefail

# Thư mục hiện tại là thư mục mẹ
parent_dir="."

cd "$parent_dir" || { echo "Không thể vào thư mục $parent_dir"; exit 1; }

# Duyệt tất cả thư mục chapter (*)
while IFS= read -r -d '' folder; do
  base=$(basename "$folder")

  # Lấy tất cả ảnh .jpg/.jpeg (không phân biệt hoa thường)
  find "$folder" -maxdepth 1 -type f \( -iname "*.jpg" -o -iname "*.jpeg" \) -print0 |
  while IFS= read -r -d '' img; do
    filename=$(basename "$img")
    target="./$filename"

    # Nếu file chưa tồn tại ở thư mục mẹ → move
    if [ ! -e "$target" ]; then
      mv -- "$img" "$target"
      echo "✓ Moved: $filename"
    else
      echo "⚠️  Skipped (trùng tên): $filename"
    fi
  done
done < <(find . -maxdepth 1 -type d -name "chapter (*)" -print0)

