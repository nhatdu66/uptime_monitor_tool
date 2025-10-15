#!/bin/bash

# Thư mục chứa các chapter
parent_dir="."

# Duyệt qua các thư mục chapter (*)
for folder in "$parent_dir"/chapter\ (*); do
    # Lấy số chapter từ tên folder
    chap_num=$(echo "$folder" | sed -n 's/.*chapter (\([0-9]\+\)).*/\1/p')
    chap_label="chapter_${chap_num}"

    # Kiểm tra xem thư mục có tồn tại không
    [ -d "$folder" ] || continue

    # Lấy danh sách file ảnh .jpg và sắp xếp
    files=("$folder"/*.jpg)
    sorted_files=($(ls "$folder"/*.jpg 2>/dev/null | sort))

    # Đếm và đổi tên
    i=1
    for img in "${sorted_files[@]}"; do
        ext="${img##*.}"
        newname=$(printf "%s/%s_%03d.%s" "$folder" "$chap_label" "$i" "$ext")
        mv -n "$img" "$newname"
        ((i++))
    done
done

echo "Đã đổi tên xong các ảnh!"

