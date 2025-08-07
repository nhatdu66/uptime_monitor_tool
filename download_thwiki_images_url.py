import os
import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse, unquote

SAVE_DIR = "downloaded_images"

def convert_to_original_url(thumb_url):
    if "/thumb/" in thumb_url:
        parts = thumb_url.split("/thumb/")
        path = parts[1].split("/")
        filename = path[1]
        original_url = f"{parts[0]}/{path[0]}/{filename}"
        return original_url
    return thumb_url

def download_image(url, folder):
    os.makedirs(folder, exist_ok=True)
    parsed = urlparse(url)
    filename = os.path.basename(parsed.path)
    filename = unquote(filename)
    filepath = os.path.join(folder, filename)

    if os.path.exists(filepath):
        print(f"[=] Bỏ qua (đã tồn tại): {filename}")
        return

    try:
        r = requests.get(url, timeout=10)
        r.raise_for_status()
        with open(filepath, "wb") as f:
            f.write(r.content)
        print(f"[✓] Đã tải: {filename}")
    except Exception as e:
        print(f"[!] Lỗi khi tải {url}: {e}")

def main():
    page_url = input("📎 Nhập URL trang wiki (THWiki): ").strip()
    if not page_url.startswith("http"):
        print("⚠️ URL không hợp lệ.")
        return

    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36"
    }

    print(f"[i] Đang tải trang: {page_url}")
    try:
        res = requests.get(page_url, headers=headers)
        res.raise_for_status()
    except Exception as e:
        print(f"[!] Không thể tải trang: {e}")
        return

    soup = BeautifulSoup(res.text, "html.parser")
    img_tags = soup.find_all("img")
    image_urls = []

    for tag in img_tags:
        src = tag.get("src", "")
        if "upload.thbwiki.cc" in src:
            if src.startswith("//"):
                src = "https:" + src
            elif src.startswith("/"):
                src = "https://upload.thbwiki.cc" + src
            image_urls.append(convert_to_original_url(src))

    print(f"[i] Tìm thấy {len(image_urls)} ảnh")
    for url in image_urls:
        download_image(url, SAVE_DIR)

if __name__ == "__main__":
    main()
