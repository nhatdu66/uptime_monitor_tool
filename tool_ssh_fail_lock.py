#!/usr/bin/env python3
# tool_ssh_fail_lock.py

import argparse
import time
import re
import subprocess
import threading
from collections import defaultdict, deque

# Regex để bắt “Failed password” và trích IP
FAIL_REGEX = re.compile(r"Failed password.*from\s+(\d+\.\d+\.\d+\.\d+)")

def ban_ip(ip):
    """Chặn IP bằng iptables."""
    subprocess.run(
        ["iptables", "-I", "INPUT", "-s", ip, "-j", "DROP"],
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    print(f"[!] Banned {ip}")

def unban_ip(ip):
    """Gỡ bỏ chặn IP."""
    subprocess.run(
        ["iptables", "-D", "INPUT", "-s", ip, "-j", "DROP"],
        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    print(f"[i] Unbanned {ip}")

def monitor_log(logfile, threshold, window, ban_time):
    fail_times = defaultdict(deque)  # ip -> deque[timestamp]
    banned = {}  # ip -> unban_timestamp

    # Mở file và seek về cuối
    with open(logfile, "r") as f:
        f.seek(0, 2)

        while True:
            line = f.readline()
            if not line:
                time.sleep(0.5)
            else:
                m = FAIL_REGEX.search(line)
                if m:
                    ip = m.group(1)
                    now = time.time()

                    # Nếu IP đang bị banned, bỏ qua
                    if ip in banned:
                        continue

                    # Ghi timestamp, xoá những entry đã quá window
                    dq = fail_times[ip]
                    dq.append(now)
                    while dq and now - dq[0] > window:
                        dq.popleft()

                    # Nếu vượt threshold -> ban
                    if len(dq) >= threshold:
                        ban_ip(ip)
                        banned[ip] = now + ban_time
                        del fail_times[ip]

            # Kiểm tra unban
            for ip, t_unban in list(banned.items()):
                if time.time() >= t_unban:
                    unban_ip(ip)
                    del banned[ip]

def main():
    parser = argparse.ArgumentParser(
        description="SSH Fail‐Lock: chặn IP đăng nhập SSH thất bại quá mức"
    )
    parser.add_argument(
        "--log-file", default="/var/log/auth.log",
        help="Đường dẫn log SSH (mặc định /var/log/auth.log)"
    )
    parser.add_argument(
        "--threshold", type=int, default=5,
        help="Số lần thất bại để trigger block (mặc định 5)"
    )
    parser.add_argument(
        "--window", type=int, default=300,
        help="Khoảng thời gian (giây) tính threshold (mặc định 300s)"
    )
    parser.add_argument(
        "--ban-time", type=int, default=600,
        help="Thời gian chặn (giây) trước khi tự unban (mặc định 600s)"
    )
    args = parser.parse_args()

    print(f"[*] Monitoring {args.log_file}, threshold={args.threshold}/{args.window}s, ban_time={args.ban_time}s")
    try:
        monitor_log(args.log_file, args.threshold, args.window, args.ban_time)
    except PermissionError:
        print("[!] Bạn cần chạy với quyền root để đọc log và thao tác iptables.")
    except KeyboardInterrupt:
        print("\n[i] Đã dừng tool.")

if __name__ == "__main__":
    main()
