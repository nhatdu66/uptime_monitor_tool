#!/usr/bin/env ruby
require 'nokogiri'
require 'open-uri'
require 'uri'
require 'fileutils'

# Hàm tải một ảnh từ URL về thư mục được chỉ định
def download_image(url, dest_folder = '.')
  # Lấy tên file từ phần cuối của path URL.
  filename = File.basename(URI.parse(url).path)
  # Nếu không lấy được tên hợp lệ thì tạo tên ngẫu nhiên.
  if filename.nil? || filename.strip == "" || filename == "/"
    filename = "image_#{rand(10000)}.jpg"
  end
  file_path = File.join(dest_folder, filename)

  begin
    URI.open(url) do |image|
      File.open(file_path, 'wb') do |file|
        file.write(image.read)
      end
    end
    puts "Downloaded: #{url} -> #{file_path}"
  rescue => e
    puts "Error downloading #{url}: #{e.message}"
  end
end

# Kiểm tra tham số dòng lệnh
if ARGV.size < 1
  puts "Usage: ruby #{__FILE__} <URL>"
  exit
end

page_url = ARGV[0]

begin
  # Lấy nội dung HTML của trang web
  html = URI.open(page_url).read
  doc = Nokogiri::HTML(html)
rescue => e
  abort "Error opening #{page_url}: #{e.message}"
end

# Tạo thư mục chứa ảnh. Ở đây đặt tên là downloaded_images.
dest_folder = "downloaded_images"
FileUtils.mkdir_p(dest_folder)

# Duyệt qua tất cả các thẻ <img> và tải ảnh về.
doc.css('img').each do |img|
  src = img['src']
  # Bỏ qua nếu không có src
  next unless src
  # Xử lý URL tương đối: chuyển thành URL tuyệt đối dựa trên page_url.
  begin
    image_url = URI.join(page_url, src).to_s
  rescue => e
    puts "Error joining URL for src: #{src} : #{e.message}"
    next
  end
  download_image(image_url, dest_folder)
end
