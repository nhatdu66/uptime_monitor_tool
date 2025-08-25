import os
from PIL import Image

# Đường dẫn đến thư mục chứa các ảnh .jpg
folder_path = r"D:\exhentai\can"

# Duyệt qua tất cả các file trong thư mục
for filename in os.listdir(folder_path):
    if filename.lower().endswith(".jpg"):
        # Đường dẫn đầy đủ của ảnh
        input_path = os.path.join(folder_path, filename)

        # Mở ảnh
        with Image.open(input_path) as img:
            # Kiểm tra kích thước của ảnh
            width, height = img.size

            # Nếu chiều ngang lớn hơn chiều cao, xoay ảnh
            if width > height:
                # Xoay ảnh 90 độ
                img = img.rotate(90, expand=True)

                # Lưu ảnh đã xoay
                output_path = os.path.join(folder_path, f"rotated_{filename}")
                img.save(output_path)
                print(f"Đã xoay: {input_path} -> {output_path}")

                # Xóa ảnh gốc
                os.remove(input_path)
                print(f"Đã xóa ảnh gốc: {input_path}")
            else:
                print(f"Không cần xoay: {input_path}")
