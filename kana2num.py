# -*- coding: utf-8 -*-
"""
Công cụ chuyển chuỗi romaji (kana latin) thành số với:
- Mỗi lần chạy, nếu key trùng mapping thì chọn ngẫu nhiên 50/50.
- Kết quả có số chữ số đầu ra nhỏ nhất (minimize tổng độ dài chuỗi số).
"""

import random

# 1. Định nghĩa bảng romaji → list các chuỗi số
mapping = {
    # 0
    "maru": ["0"], "ma": ["0"], "wa": ["0"], "rei": ["0"],
    "re": ["0"], "o": ["0"], "ou": ["0"], "oo": ["0"],
    "zero": ["0"], "ze": ["0"],

    # 1
    "hitotsu": ["1"], "hito": ["1"], "hi": ["1"],
    "ichi": ["1"], "itsu": ["1", "5"], "i": ["1", "5"],
    "wan": ["1"],

    # 2
    "futatsu": ["2"], "fu": ["2"], "futa": ["2"],
    "ha": ["2", "8"], "ni": ["2"], "ji": ["2", "10"],
    "aru": ["2"], "tsui": ["2"], "tsuu": ["2"],
    "tsu": ["2"], "tu": ["2"], "tui": ["2"], "tuu": ["2"], "bu": ["2"],

    # 3
    "mittsu": ["3"], "mi": ["3"], "san": ["3"],
    "sa": ["3"], "za": ["3"], "su": ["3"],
    "suri": ["3"], "surii": ["3"],

    # 4
    "yon": ["4"], "yo": ["4"], "yottsu": ["4"],
    "shi": ["4"], "fo": ["4"], "foo": ["4"],
    "fou": ["4"], "fa": ["4"], "faa": ["4"],
    "ho": ["4"], "hou": ["4"], "hoo": ["4"],

    # 5
    "itsutsu": ["5"], "itsu": ["1", "5"], "i": ["1", "5"],
    "go": ["5"], "ko": ["5", "9"], "ga": ["5"],
    "ka": ["5"], "faibu": ["5"], "faivu": ["5"],

    # 6
    "muttsu": ["6"], "mu": ["6"], "roku": ["6"],
    "ro": ["6"], "ri": ["6"], "ra": ["6"],
    "ru": ["6"], "ryu": ["6"], "ryuu": ["6"],
    "ryui": ["6"], "shikkusu": ["6"],

    # 7
    "nana": ["7"], "nanatsu": ["7"], "na": ["7"],
    "shichi": ["7"], "sebun": ["7"], "sevun": ["7"],

    # 8
    "yattsu": ["8"], "ya": ["8"], "hachi": ["8"],
    "ha": ["2", "8"], "ba": ["8"], "pa": ["8"],
    "eito": ["8"],

    # 9
    "kokonotsu": ["9"], "ko": ["5", "9"], "kyu": ["9"],
    "kyui": ["9"], "kyuu": ["9"], "ku": ["9"],
    "gu": ["9"], "nain": ["9"],

    # 10
    "tou": ["10"], "too": ["10"], "to": ["10"],
    "ta": ["10"], "ju": ["10"], "juu": ["10"],
    "jui": ["10"], "ji": ["2", "10"], "te": ["10"],
    "ten": ["10"],
}

# 2. Sắp xếp danh sách key giảm dần theo độ dài để dễ kiểm tra match
keys_sorted = sorted(mapping.keys(), key=len, reverse=True)

def encode_min_digits(romaji: str) -> str:
    """
    Chuyển romaji thành chuỗi số sao cho:
    - Tổng độ dài chuỗi số (số chữ số) là nhỏ nhất.
    - Với mỗi key có nhiều mapping, chọn ngẫu nhiên 50/50.
    """
    s = romaji.lower().strip()
    n = len(s)

    # dp[i] = minimal tổng độ dài chuỗi số khi encode s[i:]
    dp = [float('inf')] * (n + 1)
    # next_choice[i] = (key_matched, next_index)
    next_choice = [None] * (n + 1)

    dp[n] = 0  # hết chuỗi thì cost = 0

    # 3. DP từ cuối về đầu
    for i in range(n - 1, -1, -1):
        for key in keys_sorted:
            if s.startswith(key, i):
                # lấy chiều dài nhỏ nhất trong các mapping[key]
                min_len = min(len(num) for num in mapping[key])
                j = i + len(key)
                cost = min_len + dp[j]
                # cập nhật nếu tốt hơn
                if cost < dp[i]:
                    dp[i] = cost
                    next_choice[i] = (key, j)

    # Nếu dp[0] vẫn vô cực, nghĩa là không thể encode
    if dp[0] == float('inf'):
        raise ValueError(f"Không thể chuyển chuỗi '{romaji}' thành số.")

    # 4. Xây kết quả theo next_choice, random mapping khi cần
    i = 0
    result_parts = []
    while i < n:
        key, j = next_choice[i]
        choices = mapping[key]
        # chọn ngẫu nhiên 1 mapping trong list
        num = random.choice(choices)
        result_parts.append(num)
        i = j

    # 5. Ghép các phần lại
    return "".join(result_parts)


if __name__ == "__main__":
    inp = input("Nhập romaji cần chuyển: ")
    try:
        out = encode_min_digits(inp)
        print("→ Kết quả số:", out)
    except ValueError as err:
        print("Lỗi:", err)
