#!/bin/bash

count=0
folder_index=1

# Tạo danh sách ảnh (sắp xếp theo tên, phân biệt định dạng)
find . -maxdepth 1 -type f \( -iname "*.jpg" -o -iname "*.jpeg" -o -iname "*.png" -o -iname "*.webp" -o -iname "*.avif" \) \
    | sort | while read file; do

    # Nếu count chia hết cho 80, tạo thư mục mới
    if (( count % 80 == 0 )); then
        folder_name="LW_$folder_index"
        mkdir -p "$folder_name"
        ((folder_index++))
    fi

    mv "$file" "$folder_name"
    ((count++))
done

