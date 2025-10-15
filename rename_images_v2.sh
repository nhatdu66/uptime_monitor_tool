#!/usr/bin/env bash
set -euo pipefail

# Làm việc trong thư mục hiện tại
parent_dir="."

cd "$parent_dir" || { echo "Không thể vào thư mục $parent_dir"; exit 1; }

# Duyệt các thư mục con
while IFS= read -r -d '' folder; do
  base=$(basename "$folder")

  # Kiểm tra tên dạng: chapter (N)
  if [[ "$base" =~ ^chapter\ \(([0-9]+)\)$ ]]; then
    chap_num="${BASH_REMATCH[1]}"
  else
    continue
  fi

  # Format số chương thành 2 chữ số: 01, 02, ...
  chap_label=$(printf "chapter_%02d" "$chap_num")

  # Lấy danh sách file ảnh trong thư mục, sort tự nhiên
  mapfile -t files < <(
    find "$folder" -maxdepth 1 -type f \( -iname "*.jpg" -o -iname "*.jpeg" \) -print0 \
    | sort -z -V \
    | xargs -0 -n1 echo
  )

  [ "${#files[@]}" -gt 0 ] || continue

  i=1
  for img in "${files[@]}"; do
    num=$(printf "%03d" "$i")
    ext="${img##*.}"
    ext="${ext,,}"  # chuyển phần mở rộng về chữ thường

    newname="${folder}/${chap_label}_${num}.${ext}"

    if [ "$img" != "$newname" ]; then
      mv -n -- "$img" "$newname"
    fi

    ((i++))
  done

  echo "✓ Đã xử lý: $base  (${#files[@]} ảnh)"
done < <(find . -maxdepth 1 -mindepth 1 -type d -print0)

