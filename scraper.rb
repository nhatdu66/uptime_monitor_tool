require 'nokogiri'
require 'httparty'

def scrape_titles(url)
  # Gửi yêu cầu HTTP đến trang web
  response = HTTParty.get(url)
  
  # Kiểm tra nếu request thành công (HTTP Code: 200)
  if response.code == 200
    # Dùng Nokogiri để phân tích HTML
    doc = Nokogiri::HTML(response.body)
    
    # Lấy danh sách tiêu đề bài viết (tùy chỉnh theo cấu trúc trang web)
    titles = doc.css('h2, h3, .post-title') # Thay đổi selector nếu cần
    
    puts "Danh sách tiêu đề:"
    titles.each_with_index do |title, index|
      puts "#{index + 1}. #{title.text.strip}"
    end
  else
    puts "Lỗi khi tải trang (HTTP Code: #{response.code})"
  end
end

# Nhập URL cần scrape
puts "Nhập URL của website cần thu thập:"
url = gets.chomp

# Gọi hàm scrape_titles
scrape_titles(url)
