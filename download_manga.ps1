# download_manga.ps1
# ------------------
# Đọc url.txt, với mỗi URL: tạo folder chapter_##, tìm mọi link .jpg và tải về.

# 1. Đọc danh sách URL (bỏ dòng trống)
$urls = Get-Content .\url.txt |
        Where-Object { -not [string]::IsNullOrWhiteSpace($_) }

# 2. Thư mục gốc để lưu mọi chương
$outputRoot = ".\manga_downloads"
New-Item -ItemType Directory -Path $outputRoot -Force | Out-Null

# 3. Regex pattern (protocol-relative hoặc http(s)) 
$pattern = @'
(?:https?://|//)[^\s]+?\.jpg
'@

$chapterIndex = 1

foreach ($url in $urls) {
    # tạo folder riêng cho chương
    $chapterName   = "chapter_{0:D2}" -f $chapterIndex
    $chapterFolder = Join-Path $outputRoot $chapterName
    New-Item -ItemType Directory -Path $chapterFolder -Force | Out-Null

    Write-Host "`nDownloading chapter $chapterIndex from $url"
    try {
        $resp = Invoke-WebRequest `
            -Uri $url `
            -Headers @{ 'User-Agent' = 'Mozilla/5.0' } `
            -ErrorAction Stop
        $html = $resp.Content
    }
    catch {
        Write-Warning "  Cannot download page: $url"
        $chapterIndex++
        continue
    }

    # tìm tất cả URL kết thúc .jpg
    $matches = [regex]::Matches(
        $html,
        $pattern,
        [System.Text.RegularExpressions.RegexOptions]::IgnoreCase
    )
    Write-Host "  Found $($matches.Count) .jpg links"

    if ($matches.Count -gt 0) {
        $seen       = @{}
        $imageIndex = 1

        foreach ($m in $matches) {
            $rawUrl = $m.Value

            # với //domain…
            if ($rawUrl.StartsWith("//")) {
                $rawUrl = "https:$rawUrl"
            }

            # cắt bỏ ký tự đóng ngoặc, chấm phẩy nếu dính kèm
            $rawUrl = $rawUrl.TrimEnd(')', ';', '"', "'")

            if ($seen.ContainsKey($rawUrl)) { continue }
            $seen[$rawUrl] = $true

            $filename = "{0:D3}.jpg" -f $imageIndex
            $savePath = Join-Path $chapterFolder $filename

            try {
                Invoke-WebRequest `
                    -Uri $rawUrl `
                    -OutFile $savePath `
                    -Headers @{ 'User-Agent' = 'Mozilla/5.0' } `
                    -ErrorAction Stop
                Write-Host "    Saved $filename"
            }
            catch {
                Write-Warning "    Failed to download $rawUrl"
            }

            $imageIndex++
        }
    }

    $chapterIndex++
}

Write-Host "`nAll chapters downloaded into $outputRoot"
