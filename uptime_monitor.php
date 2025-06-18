<?php
/**
 * Uptime and Performance Monitoring Tool - PHP Edition
 * Phiên bản nâng cao này kiểm tra:
 *  - HTTP Status Code
 *  - Thời gian phản hồi (ms)
 *  - Kích thước tải về (bytes)
 *  - Kích thước header (bytes)
 *  - Nội dung (phần tóm tắt)
 *
 * Bạn có thể mở rộng thêm các chi tiết khác như DNS resolution, SSL info, v.v.
 */

/**
 * Hàm kiểm tra trạng thái website và thu thập các thông số chi tiết.
 *
 * @param string $url URL của website cần kiểm tra.
 * @return array Mảng kết quả chứa thông tin chi tiết.
 */
function checkWebsiteStatus($url) {
    // Khởi tạo cURL
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    
    // Lấy cả header lẫn body
    curl_setopt($ch, CURLOPT_HEADER, true);  
    curl_setopt($ch, CURLOPT_NOBODY, false); // Để lấy cả nội dung (body)
    
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); // Theo dõi redirection nếu có
    curl_setopt($ch, CURLOPT_TIMEOUT, 10); // Thời gian timeout: 10 giây
    
    // Bắt đầu đo thời gian phản hồi
    $startTime = microtime(true);
    $result = curl_exec($ch);
    $endTime = microtime(true);
    
    // Nếu có lỗi cURL
    if (curl_errno($ch)) {
        $errorMsg = curl_error($ch);
        curl_close($ch);
        return [
            "status"  => "error",
            "message" => "Lỗi cURL: {$errorMsg}"
        ];
    }
    
    // Lấy các thông số từ cURL
    $httpCode   = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $totalTime  = round(($endTime - $startTime) * 1000, 2); // Thời gian tính bằng milliseconds
    $downloadSize = curl_getinfo($ch, CURLINFO_SIZE_DOWNLOAD); // Kích thước tải về (bytes)
    $headerSize = curl_getinfo($ch, CURLINFO_HEADER_SIZE); // Kích thước header (bytes)
    
    // Tách header và nội dung từ kết quả trả về
    $header = substr($result, 0, $headerSize);
    $body   = substr($result, $headerSize);
    
    curl_close($ch);
    
    // Xác định trạng thái website dựa vào mã HTTP
    $siteStatus = ($httpCode >= 200 && $httpCode < 300) ? "up" : "down";
    
    return [
        "status"         => $siteStatus,
        "httpCode"       => $httpCode,
        "totalTime_ms"   => $totalTime,
        "downloadSize"   => $downloadSize,
        "headerSize"     => $headerSize,
        "headerPreview"  => substr($header, 0, 300),   // preview header (300 ký tự)
        "bodyPreview"    => substr($body, 0, 200)        // preview body (200 ký tự)
    ];
}

// URL của website cần kiểm tra (bạn có thể thay đổi hoặc lấy từ tham số truyền vào)
$url = "https://nhatdu66.github.io/";

// Thực hiện kiểm tra
$result = checkWebsiteStatus($url);

// In kết quả
echo "=== Kết quả kiểm tra website ===" . PHP_EOL;
echo "URL: {$url}" . PHP_EOL;
echo "Trạng thái: " . $result['status'] . PHP_EOL;
echo "HTTP Code: " . $result['httpCode'] . PHP_EOL;
echo "Thời gian phản hồi: " . $result['totalTime_ms'] . " ms" . PHP_EOL;
echo "Kích thước tải về: " . $result['downloadSize'] . " bytes" . PHP_EOL;
echo "Kích thước header: " . $result['headerSize'] . " bytes" . PHP_EOL;

echo PHP_EOL . "--- Preview Header ---" . PHP_EOL;
echo $result['headerPreview'] . PHP_EOL;

echo PHP_EOL . "--- Preview Nội dung ---" . PHP_EOL;
echo $result['bodyPreview'] . PHP_EOL;
?>
