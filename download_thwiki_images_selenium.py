import os
import time
import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from urllib.parse import urlparse, unquote

SAVE_DIR = "downloaded_images"
VALID_EXTENSIONS = (".png", ".jpg", ".jpeg", ".gif", ".webp")

def convert_to_original_url(thumb_url):
    """Chuyển từ link thumbnail sang ảnh gốc"""
    if "/thumb/" in thumb_url:
        parts = thumb_url.split("/thumb/")
        path = parts[1].split("/")
        filename = path[1]
        original_url = f"{parts[0]}/{path[0]}/{filename}"
        return original_url
    return thumb_url

def download_image(url, folder):
    """Tải ảnh về thư mục chỉ định"""
    os.makedirs(folder, exist_ok=True)
    parsed = urlparse(url)
    filename = unquote(os.path.basename(parsed.path))
    filepath = os.path.join(folder, filename)

    if os.path.exists(filepath):
        print(f"[=] Bỏ qua (đã tồn tại): {filename}")
        return True

    try:
        r = requests.get(url, timeout=5)
        r.raise_for_status()
        with open(filepath, "wb") as f:
            f.write(r.content)
        print(f"[✓] Đã tải: {filename}")
        return True
    except Exception as e:
        print(f"[!] Lỗi khi tải {url}: {e}")
        return False

def get_html_with_selenium(url):
    """Mở trang bằng Selenium để lấy đầy đủ HTML"""
    print(f"[i] Mở trình duyệt để tải trang: {url}")
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--window-size=1920,1080")

    driver = webdriver.Chrome(options=chrome_options)
    driver.get(url)
    time.sleep(3)  # đợi tải trang
    html = driver.page_source
    driver.quit()
    return html

def main():
    url = input("📎 Nhập URL trang wiki (THWiki): ").strip()
    if not url.startswith("http"):
        print("⚠️ URL không hợp lệ.")
        return

    html = get_html_with_selenium(url)
    soup = BeautifulSoup(html, "html.parser")

    img_tags = soup.find_all("img")
    image_urls = []

    for tag in img_tags:
        src = tag.get("src", "")
        if "upload.thbwiki.cc" not in src:
            continue
        if not any(ext in src.lower() for ext in VALID_EXTENSIONS):
            continue

        if src.startswith("//"):
            src = "https:" + src
        elif src.startswith("/"):
            src = "https://upload.thbwiki.cc" + src

        image_urls.append(convert_to_original_url(src))

    print(f"[i] Tìm thấy {len(image_urls)} ảnh hợp lệ")

    count = 0
    for url in image_urls:
        if download_image(url, SAVE_DIR):
            count += 1

    print(f"[🏁] Hoàn tất! Đã tải {count}/{len(image_urls)} ảnh.")

if __name__ == "__main__":
    main()
