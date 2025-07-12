import os
import requests
from bs4 import BeautifulSoup
from pathlib import Path

URL_FILE    = "url.txt"
OUTPUT_DIR  = Path("manga_downloads")
HEADERS     = {"User-Agent": "Mozilla/5.0"}

def read_urls(file_path):
    with open(file_path, encoding="utf-8") as f:
        return [line.strip() for line in f if line.strip()]

def download_image(url, save_path):
    resp = requests.get(url, headers=HEADERS, stream=True, timeout=10)
    resp.raise_for_status()
    with open(save_path, "wb") as f:
        for chunk in resp.iter_content(1024):
            f.write(chunk)

def download_chapter(chapter_url, chapter_index):
    folder = OUTPUT_DIR / f"chapter_{chapter_index:02d}"
    folder.mkdir(parents=True, exist_ok=True)
    print(f"Đang tải chương #{chapter_index}: {chapter_url}")

    resp = requests.get(chapter_url, headers=HEADERS, timeout=10)
    resp.raise_for_status()
    soup = BeautifulSoup(resp.text, "html.parser")

    imgs = soup.find_all("img", src=lambda x: x and x.lower().endswith(".jpg"))
    for idx, img in enumerate(imgs, start=1):
        img_url  = img.get("src") if img.get("src").startswith("http") else chapter_url + img.get("src")
        filename = folder / f"{idx:03d}.jpg"
        try:
            download_image(img_url, filename)
            print(f"  + Tải xong: {filename.name}")
        except Exception as e:
            print(f"  ! Lỗi tải {img_url}: {e}")

def main():
    OUTPUT_DIR.mkdir(exist_ok=True)
    urls = read_urls(URL_FILE)

    for i, url in enumerate(urls, start=1):
        download_chapter(url, i)

    print("Hoàn thành tải truyện!")

if __name__ == "__main__":
    main()
