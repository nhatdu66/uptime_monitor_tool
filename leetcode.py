#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# BẢN ĐỒ KÝ TỰ CHUYỂN ĐỔI
LEET_MAP = {
    'A': '4', 'B': '8', 'C': '(', 'D': '|)',
    'E': '3', 'F': '|=', 'G': '6', 'H': '#',
    'I': '1', 'J': '_|', 'K': '|<', 'L': '1',
    'M': '/\\/\\', 'N': '|\\|', 'O': '0', 'P': '|D',
    'Q': '(,)', 'R': '|2', 'S': '5', 'T': '7',
    'U': '|_|', 'V': '\\/', 'W': '\\/\\/', 'X': '><',
    'Y': '`/', 'Z': '2'
}

# TẠO BẢN ĐỒ NGƯỢC ĐỂ GIẢI MÃ
REVERSE_MAP = {v: k for k, v in LEET_MAP.items()}

def encode_leet(text: str) -> str:
    """
    Mã hóa text thường sang leet.
    Với ký tự không nằm trong LEET_MAP, giữ nguyên.
    """
    result = []
    for ch in text.upper():
        result.append(LEET_MAP.get(ch, ch))
    return ''.join(result)

def decode_leet(leet: str) -> str:
    """
    Giải mã Leet ngược lại text thường.
    Duyệt từ trái qua phải, cố gắng match chuỗi dài nhất trước.
    """
    result = []
    i = 0
    tokens = sorted(REVERSE_MAP.keys(), key=len, reverse=True)
    
    while i < len(leet):
        match = None
        for token in tokens:
            if leet[i:i+len(token)] == token:
                match = token
                break
        if match:
            result.append(REVERSE_MAP[match])
            i += len(match)
        else:
            result.append(leet[i])
            i += 1
    return ''.join(result)

def main():
    while True:
        mode = input("\nChọn chế độ (e=encode, d=decode, q=quit): ").strip().lower()
        if mode == 'q':
            print("Thoát chương trình. Bye!")
            break
        text = input("Nhập văn bản: ").strip()
        if mode == 'e':
            print("Kết quả mã hóa:", encode_leet(text))
        elif mode == 'd':
            print("Kết quả giải mã:", decode_leet(text))
        else:
            print("Lựa chọn không hợp lệ. Thử lại!")

if __name__ == "__main__":
    main()
