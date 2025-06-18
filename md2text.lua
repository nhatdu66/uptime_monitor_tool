-- md2text.lua
-- Cách sử dụng:
--   lua md2text.lua file.md

-- Kiểm tra tham số dòng lệnh
if not arg[1] then
  print("Usage: lua md2text.lua file.md")
  os.exit(1)
end

local filename = arg[1]

-- Mở file Markdown
local file, err = io.open(filename, "r")
if not file then
  print("Lỗi mở file: " .. err)
  os.exit(1)
end

-- Đọc toàn bộ nội dung file
local text = file:read("*a")
file:close()

-- Biến đổi cơ bản để làm cho văn bản dễ đọc hơn

-- Loại bỏ khối code (``` ... ```) và inline code (`...`)
text = text:gsub("```[%w%s]*\n", ""):gsub("```", ""):gsub("`", "")

-- Xử lý tiêu đề: chuyển dòng bắt đầu bằng một hay nhiều ký tự '#' thành nội dung in hoa
text = text:gsub("^(#+)%s*(.-)$", function(h, content)
  return content:upper() 
end)

-- Loại bỏ ký tự đánh dấu cho in đậm, in nghiêng (chẳng hạn **, *, __, _)
text = text:gsub("%*%*", ""):gsub("%*", "")
text = text:gsub("__", ""):gsub("%_", "")

-- Chuyển đổi liên kết dạng [text](url) thành chỉ text
text = text:gsub("%[(.-)%]%((.-)%)", "%1")

-- Loại bỏ dấu cảm thán trước ảnh ![alt](url) và chỉ giữ lại alt text
text = text:gsub("!%[(.-)%]%((.-)%)", "%1")

-- Xử lý dấu đầu dòng cho các danh sách: chuyển các dấu -, * hoặc + ở đầu dòng thành dấu gạch ngang
text = text:gsub("^[%s]*[-*+]%s+", "- ")

-- Hiển thị nội dung đã xử lý
print(text)
