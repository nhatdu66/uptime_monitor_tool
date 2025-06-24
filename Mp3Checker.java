import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Mp3Checker {

    /**
     * Kiểm tra file có phải là file MP3 không.
     * 
     * @param file File cần kiểm tra
     * @return true nếu file là MP3, ngược lại trả về false
     * @throws IOException khi có lỗi xảy ra trong quá trình đọc file
     */
    public static boolean isMp3File(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        // Đọc 10 byte đầu tiên của file
        byte[] header = new byte[10];
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(header);
            if (bytesRead < 10) {
                return false;
            }
        }

        // Kiểm tra header ID3 (ID3v2)
        if (header[0] == 'I' && header[1] == 'D' && header[2] == '3') {
            return true;
        }

        // Nếu không có header ID3, kiểm tra MP3 frame sync:
        // Byte đầu tiên phải là 0xFF và 3 bit cao của byte thứ hai là 1 (tương đương với & 0xE0 = 0xE0)
        if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0) {
            return true;
        }

        // Nếu không thỏa điều kiện nào trên, file có khả năng không phải là MP3
        return false;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Sử dụng: java Mp3Checker <đường_dẫn_file>");
            return;
        }

        File file = new File(args[0]);
        try {
            if (isMp3File(file)) {
                System.out.println("File là file MP3.");
            } else {
                System.out.println("File không phải là file MP3.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }
}
