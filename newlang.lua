-- simple_http_server.lua
-- Yêu cầu: cài đặt LuaSocket (luarocks install luasocket)
--
-- Chạy tool:
--   lua simple_http_server.lua
-- Truy cập: http://localhost:8080 trong trình duyệt của bạn

local socket = require("socket")

-- Tạo server lắng nghe trên tất cả các interface, cổng 8080
local server = assert(socket.bind("*", 8080))
local ip, port = server:getsockname()
print("HTTP Server đang chạy tại: http://" .. ip .. ":" .. port)

while true do
  -- Chấp nhận liên kết từ client
  local client = server:accept()
  client:settimeout(1)
  
  -- Nhận dòng đầu tiên của request (chứa method và url)
  local request, err = client:receive("*l")
  if request then
    print("Yêu cầu nhận được: " .. request)
  else
    print("Lỗi nhận request: " .. tostring(err))
  end

  -- Đưa ra phản hồi với một trang HTML đơn giản
  local response = table.concat({
    "HTTP/1.1 200 OK\r\n",
    "Content-Type: text/html; charset=UTF-8\r\n",
    "Connection: close\r\n",
    "\r\n",
    "<html>",
    "<head><title>Lua HTTP Server</title></head>",
    "<body>",
    "<h1>Chào mừng đến với Lua HTTP Server!</h1>",
    "<p>Đây là một ví dụ đơn giản về server web được viết bằng Lua.</p>",
    "</body>",
    "</html>"
  })
  
  client:send(response)
  client:close()
end
