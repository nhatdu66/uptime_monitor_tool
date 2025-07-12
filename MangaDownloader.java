import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

public class MangaDownloader {

    // Đường dẫn tới file chứa danh sách URL
    private static final String URL_FILE = "url.txt";
    // Thư mục gốc để lưu ảnh
    private static final String OUTPUT_DIR = "manga_downloads";

    public static void main(String[] args) {
        try {
            List<String> urls = Files.readAllLines(Paths.get(URL_FILE));
            int chapterIndex = 1;

            for (String chapterUrl : urls) {
                if (chapterUrl.trim().isEmpty()) continue;

                // Tạo thư mục cho từng chương
                String chapterFolder = String.format("%s/chapter_%02d", OUTPUT_DIR, chapterIndex++);
                Files.createDirectories(Paths.get(chapterFolder));

                System.out.println("Đang tải chương từ: " + chapterUrl);
                downloadChapterImages(chapterUrl, chapterFolder);
            }

            System.out.println("Hoàn thành tải truyện!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file url.txt hoặc tạo thư mục: " + e.getMessage());
        }
    }

    private static void downloadChapterImages(String pageUrl, String saveFolder) {
        try {
            // Kết nối và parse HTML
            Document doc = Jsoup.connect(pageUrl)
                                .userAgent("Mozilla/5.0")
                                .timeout(10_000)
                                .get();

            // Lấy tất cả thẻ <img> có src kết thúc bằng .jpg
            Elements imgs = doc.select("img[src$=.jpg]");

            int count = 1;
            for (Element img : imgs) {
                String imgUrl = img.absUrl("src");
                String fileName = String.format("%03d.jpg", count++);
                downloadFile(imgUrl, saveFolder + "/" + fileName);
                System.out.println("  Đã tải: " + fileName);
            }

        } catch (IOException e) {
            System.err.println("  Lỗi tải chương: " + e.getMessage());
        }
    }

    private static void downloadFile(String fileURL, String savePath) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.connect();

        try (InputStream in = conn.getInputStream();
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(savePath)))) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } finally {
            conn.disconnect();
        }
    }
}
