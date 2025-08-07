import os
import re
import requests

def get_image_links_from_chapter(url):
    print(f"\n🔍 Đang lấy ảnh từ: {url}")
    headers = {"User-Agent": "Mozilla/5.0"}
    response = requests.get(url, headers=headers)
    response.raise_for_status()
    html = response.text

    match = re.search(r'var chapter\s*=\s*(\{.*?\});', html, re.DOTALL)
    if not match:
        print("⚠️ Không tìm thấy ảnh trong chương.")
        return []

    chapter_raw = match.group(1)
    image_urls = re.findall(r'"image"\s*:\s*"(.*?)"', chapter_raw)

    full_urls = []
    for url in image_urls:
        clean_url = url.replace("\\/", "/")
        if clean_url.startswith("//"):
            clean_url = "https:" + clean_url
        full_urls.append(clean_url)

    return full_urls

def download_images(chapter_url, output_dir):
    image_urls = get_image_links_from_chapter(chapter_url)
    if not image_urls:
        return

    os.makedirs(output_dir, exist_ok=True)
    print(f"📥 Đang tải {len(image_urls)} ảnh vào: {output_dir}")

    for i, img_url in enumerate(image_urls, 1):
        try:
            response = requests.get(img_url, headers={"User-Agent": "Mozilla/5.0"})
            if response.status_code == 200:
                filename = os.path.join(output_dir, f"{i:03}.jpg")
                with open(filename, "wb") as f:
                    f.write(response.content)
                print(f"  ✅ {filename}")
            else:
                print(f"  ⚠️ Lỗi {response.status_code} khi tải: {img_url}")
        except Exception as e:
            print(f"  ❌ Exception khi tải ảnh: {e}")

def main():
    list_file = "list_chapters.txt"
    if not os.path.exists(list_file):
        print(f"❌ Không tìm thấy file: {list_file}")
        return

    with open(list_file, "r", encoding="utf-8") as f:
        urls = [line.strip() for line in f if line.strip()]

    for url in urls:
        # Đặt tên thư mục theo đoạn cuối của URL
        folder_name = url.strip("/").split("/")[-1]
        download_images(url, output_dir=folder_name)

    print("\n🎉 Hoàn tất tải toàn bộ chương!")

if __name__ == "__main__":
    main()
