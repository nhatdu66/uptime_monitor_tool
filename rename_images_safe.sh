#!/usr/bin/env bash
set -euo pipefail

# Chạy từ thư mục chứa các thư mục chapter, hoặc thay "." bằng đường dẫn cụ thể
parent_dir="."

cd "$parent_dir" || { echo "Không tìm thấy $parent_dir"; exit 1; }

# Duyệt các thư mục (dùng find để tránh lỗi với ngoặc)
while IFS= read -r -d '' folder; do
  # folder: đường dẫn như "chapter (1)"
  base=$(basename "$folder")

  # Kiểm tra tên dạng: chapter (N)
  if [[ $base =~ ^chapter\ \(([0-9]+)\)$ ]]; then
    chap="${BASH_REMATCH[1]}"
  else
    continue
  fi

  label="chapter_${chap}"

  # Lấy danh sách file .jpg/.jpeg (case-insensitive), sort theo thứ tự "tự nhiên"
  mapfile -t files < <(
    find "$folder" -maxdepth 1 -type f \( -iname '*.jpg' -o -iname '*.jpeg' \) -print0 \
    | while IFS= read -r -d '' f; do printf '%s\n' "$f"; done \
    | sort -V
  )

  # Nếu không có file thì bỏ qua
  [ "${#files[@]}" -gt 0 ] || continue

  i=1
  for img in "${files[@]}"; do
    # định dạng số 3 chữ số
    num=$(printf "%03d" "$i")
    ext="${img##*.}"
    # phần mở rộng chữ thường (yêu cầu bash >=4)
    ext="${ext,,}"
    newpath="${folder}/${label}_${num}.${ext}"

    # Nếu cùng đường dẫn thì bỏ qua, else đổi tên (không ghi đè)
    if [ "$img" != "$newpath" ]; then
      # nếu muốn dry-run, đổi echo thành mv
      mv -n -- "$img" "$newpath"
    fi
    ((i++))
  done

  echo "Đã xử lý: $base  (đổi ${#files[@]} file)"
done < <(find . -maxdepth 1 -mindepth 1 -type d -print0)

