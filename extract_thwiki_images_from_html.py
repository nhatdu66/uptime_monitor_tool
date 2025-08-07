import os
import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse, unquote

HTML_FILE = "scriptth.txt"
SAVE_DIR = "images"
VALID_EXT = (".png", ".jpg", ".jpeg", ".gif", ".webp")

def is_valid_image_url(url):
    parsed = urlparse(url)
    ext = os.path.splitext(parsed.path)[-1].lower()
    return ext in VALID_EXT and "upload.thbwiki.cc" in parsed.netloc

def download(url):
    os.makedirs(SAVE_DIR, exist_ok=True)
    parsed = urlparse(url)
    filename = unquote(os.path.basename(parsed.path))
    filepath = os.path.join(SAVE_DIR, filename)

    if os.path.exists(filepath):
        print(f"[=] Bỏ qua: {filename}")
        return

    try:
        r = requests.get(url, timeout=10)
        r.raise_for_status()
        with open(filepath, "wb") as f:
            f.write(r.content)
        print(f"[✓] {filename}")
    except Exception as e:
        print(f"[!] Lỗi {url}: {e}")

def main():
    with open(HTML_FILE, "r", encoding="utf-8") as f:
        soup = BeautifulSoup(f.read(), "html.parser")

    imgs = soup.find_all("img")
    urls = []

    for tag in imgs:
        # Ưu tiên lấy tất cả src/data-src/data-original
        src = tag.get("src") or tag.get("data-src") or tag.get("data-original")

        if not src:
            continue

        if src.startswith("//"):
            src = "https:" + src
        elif src.startswith("/"):
            src = "https://upload.thbwiki.cc" + src

        if is_valid_image_url(src):
            urls.append(src)

    print(f"[i] Tìm thấy {len(urls)} ảnh để tải")
    for url in urls:
        download(url)

if __name__ == "__main__":
    main()
