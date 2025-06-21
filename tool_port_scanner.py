#!/usr/bin/env python3
# tool_port_scanner.py

import argparse
import socket
import threading
import queue
import time

# Hàm quét một cổng đơn
def scan_port(target, port, timeout=1):
    """Scan một port, trả về True nếu mở, False nếu đóng/timeout."""
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.settimeout(timeout)
            result = s.connect_ex((target, port))
            return result == 0
    except Exception:
        return False

# Worker thread để lấy port từ queue và quét
def worker(thread_id, target, port_queue, open_ports, timeout):
    while True:
        try:
            port = port_queue.get_nowait()
        except queue.Empty:
            return
        if scan_port(target, port, timeout):
            open_ports.append(port)
        port_queue.task_done()

def main():
    parser = argparse.ArgumentParser(
        description="Simple Port Scanner (chạy hợp pháp trên hệ thống bạn được phép)."
    )
    parser.add_argument("target", help="IP hoặc hostname cần quét")
    parser.add_argument(
        "-p", "--ports", default="1-1024",
        help="Dải cổng, ví dụ 1-1024 hoặc 22,80,443"
    )
    parser.add_argument(
        "-t", "--threads", type=int, default=50,
        help="Số luồng đồng thời"
    )
    parser.add_argument(
        "-to", "--timeout", type=float, default=1.0,
        help="Timeout (giây) cho mỗi kết nối"
    )
    args = parser.parse_args()

    # Xử lý dải cổng
    port_list = []
    for part in args.ports.split(","):
        if "-" in part:
            start, end = map(int, part.split("-"))
            port_list.extend(range(start, end + 1))
        else:
            port_list.append(int(part))

    port_queue = queue.Queue()
    for port in port_list:
        port_queue.put(port)

    open_ports = []
    threads = []
    start_time = time.time()

    # Khởi chạy worker threads
    for i in range(args.threads):
        t = threading.Thread(
            target=worker,
            args=(i, args.target, port_queue, open_ports, args.timeout),
            daemon=True
        )
        t.start()
        threads.append(t)

    # Chờ tất cả xong
    port_queue.join()
    duration = time.time() - start_time

    # In kết quả
    print(f"\nScan hoàn tất sau {duration:.2f}s.")
    if open_ports:
        print("Các cổng mở:")
        for port in sorted(open_ports):
            print(f"  - {port}")
    else:
        print("Không tìm thấy cổng mở nào trong dải đã quét.")

if __name__ == "__main__":
    main()
