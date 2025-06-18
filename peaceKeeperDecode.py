import base64
import binascii
import urllib.parse
import codecs
import string

#############################################
# 1. Reverse
#############################################
def decode_reverse(s):
    """Đảo ngược chuỗi"""
    return s[::-1]

#############################################
# 2. Atbash cipher
#############################################
def decode_atbash(s):
    """Giải mã Atbash: đổi ký tự A <-> Z, B <-> Y, v.v.
       Áp dụng cho chữ cái Hoa và Thường.
    """
    def atbash_char(ch):
        if ch.isalpha():
            if ch.isupper():
                return chr(ord('Z') - (ord(ch) - ord('A')))
            else:
                return chr(ord('z') - (ord(ch) - ord('a')))
        return ch

    return ''.join(atbash_char(c) for c in s)

#############################################
# 3. Caesar cipher với shift = 7
#############################################
def decode_caesar(s, shift=7):
    """Giải mã Caesar: mỗi chữ cái được dịch lùi shift chữ.
       Ví dụ với shift=7: 'H' -> 'A'
    """
    result = []

    for ch in s:
        if ch.isalpha():
            if ch.isupper():
                result.append(chr((ord(ch) - ord('A') - shift) % 26 + ord('A')))
            else:
                result.append(chr((ord(ch) - ord('a') - shift) % 26 + ord('a')))
        else:
            result.append(ch)
    return ''.join(result)

#############################################
# 4. Reversed rail fence cipher (5 rails, offset 0)
#############################################
def decode_rail_fence(cipher, num_rails=5):
    """
    Giải mã Rail Fence Cipher theo thuật toán:
    - Xác định vị trí mẫu cho mỗi ký tự với số hàng là num_rails.
    - Phân chia ciphertext theo số lượng ký tự ở mỗi rail.
    - Đọc lại theo thứ tự di chuyển sóng.
    """
    # Tính độ dài của chuỗi
    n = len(cipher)
  
    # Lấy mẫu vị trí của rail cho từng ký tự
    # đoạn pattern vận hành theo "diều" lên xuống
    pattern = list(range(num_rails)) + list(range(num_rails-2, 0, -1))
    rail_pos = [pattern[i % len(pattern)] for i in range(n)]
  
    # Số ký tự cần cho mỗi rail
    rails_count = [rail_pos.count(r) for r in range(num_rails)]
  
    # Lấy các phần tương ứng từ ciphertext
    rails = {}
    index = 0
    for r in range(num_rails):
        rails[r] = list(cipher[index: index + rails_count[r]])
        index += rails_count[r]
  
    # Đọc lại dùng mẫu ban đầu
    result = []
    for pos in rail_pos:
        result.append(rails[pos].pop(0))
    return ''.join(result)

def decode_reversed_rail_fence(s):
    """
    Để giải mã "reversed rail fence cipher":
    - Đảo ngược chuỗi trước.
    - Giải mã theo rail fence với 5 rail.
    """
    reversed_string = decode_reverse(s)
    return decode_rail_fence(reversed_string, num_rails=5)

#############################################
# 5. Kết hợp 2 cipher: từ một chuỗi chỉ có E và T
#    - Thay E -> A, T -> B.
#    - Gom các nhóm 5 ký tự, mỗi nhóm là một mã Bacon.
#    - Áp dụng giải mã Bacon: chuyển A->0, B->1, sau đó chuyển từ số (0->25) sang chữ (0=A,...)
#    - Cuối cùng giải mã kết quả bằng Atbash cipher.
#############################################
def decode_bacon(text):
    """
    Giải mã Bacon cipher hiện đại:
    - Mỗi nhóm 5 ký tự (A và B) được chuyển thành số nhị phân,
      sau đó số đó tương ứng với chữ (0 -> A, 1 -> B, …, 25 -> Z).
    Nếu số không nằm trong [0,25] thì trả về dấu hỏi.
    """
    result = []
    # Chia thành các nhóm 5 ký tự
    groups = [text[i:i+5] for i in range(0, len(text), 5)]
    for group in groups:
        if len(group) < 5:
            continue  # bỏ qua nhóm không đủ 5 ký tự
        # Chuyển A/B thành chuỗi bit (A:0, B:1)
        try:
            num = int(''.join('0' if ch.upper()=='A' else '1' for ch in group))
        except Exception:
            result.append('?')
            continue
        # Nhóm 5 ký tự biểu diễn một số nhị phân.
        # Tuy nhiên vì có thể số này vượt 25, ta kiểm tra:
        if num < 0 or num > 25:
            result.append('?')
        else:
            result.append(chr(num + ord('A')))
    return ''.join(result)

def decode_complex_cipher(s):
    """
    Với cipher loại 5:
    - B1: Thay tất cả ký tự 'E' thành 'A' và 'T' thành 'B'.
    - B2: Giải mã Bacon: mỗi 5 ký tự.
    - B3: Áp dụng Atbash cipher lên kết quả Bacon.
    """
    # B1: chuyển đổi
    step1 = s.replace('E', 'A').replace('T', 'B')
    # Nếu chuỗi có khoảng trắng hoặc ký tự khác, loại bỏ chúng (giả sử chỉ xử lý A, B liên tiếp)
    step1 = ''.join(ch for ch in step1 if ch.upper() in ['A', 'B'])
  
    # B2: giải mã Bacon
    bacon_decoded = decode_bacon(step1)
  
    # B3: áp dụng Atbash trên kết quả Bacon
    result = decode_atbash(bacon_decoded)
    return result

#############################################
# 6. Vigenere cipher với passphrase "Edward"
#############################################
def decode_vigenere(ciphertext, key="EDWARD"):
    """
    Giải mã Vigenere cipher theo bảng chữ cái tiếng Anh.
    Công thức: P = (C - K) mod 26, áp dụng cho chữ hoa/thường.
    Không xử lý các ký tự không phải chữ cái.
    """
    key = key.upper()
    result = []
    key_index = 0
    for ch in ciphertext:
        if ch.isalpha():
            # Dùng bảng chữ cái thường là A-Z
            A = ord('A') if ch.isupper() else ord('a')
            c_val = ord(ch.upper()) - ord('A')
            k_val = ord(key[key_index % len(key)]) - ord('A')
            p_val = (c_val - k_val) % 26
            # Giữ nguyên định dạng chữ
            result.append(chr(p_val + A))
            key_index += 1
        else:
            result.append(ch)
    return ''.join(result)

#############################################
# 7. Vigenere autokey cipher với passphrase "George" và alphabet key "ZABCDEFGHIJKLMNOPQRSTUVWXY"
#############################################
def decode_vigenere_autokey(ciphertext, key_seed="GEORGE", custom_alph="ZABCDEFGHIJKLMNOPQRSTUVWXY"):
    """
    Giải mã Vigenere autokey:
    - Ban đầu key là key_seed (giá trị bất kỳ có thể dài < lời mã).
    - Sau đó key được mở rộng bằng chính plaintext vừa giải mã.
    - Sự khác biệt ở đây: bảng chữ cái không phải bình thường mà là custom_alph cho thứ tự ký tự.
    
    Công thức: plaintext = (cipher_index - key_index) mod 26;
    với key_index được xác định dựa trên vị trí trong custom_alph.
    """
    key = list(key_seed.upper())
    result = []
    alph = custom_alph.upper()
    
    for ch in ciphertext:
        if ch.upper() in alph:
            c_index = alph.index(ch.upper())
            # Nếu key hiện tại chưa đủ, ta lấy key từ seed
            k = key.pop(0)
            k_index = alph.index(k)
            # Giải mã
            p_index = (c_index - k_index) % 26
            p_letter = alph[p_index]
            result.append(p_letter)
            # Thêm plaintext vừa giải được vào key (autokey)
            key.append(p_letter)
        else:
            # Với ký tự không có trong bảng chữ cái, giữ nguyên.
            result.append(ch)
    return ''.join(result)

#############################################
# Tổng hợp tool: Thử từng phương pháp dựa vào input
#############################################
def decode_input(s):
    results = {}

    # 1. Reverse
    results["Reverse"] = decode_reverse(s)
  
    # 2. Atbash
    results["Atbash"] = decode_atbash(s)
  
    # 3. Caesar (shift = 7)
    results["Caesar (shift 7)"] = decode_caesar(s, shift=7)
  
    # 4. Reversed Rail Fence (5 rails, offset 0)
    results["Reversed Rail Fence (5 rails)"] = decode_reversed_rail_fence(s)
  
    # 5. Kết hợp 2 ciphers: E/T -> Bacon -> Atbash
    results["Dual cipher (E/T -> Bacon -> Atbash)"] = decode_complex_cipher(s)
  
    # 6. Vigenere (passphrase "Edward")
    results["Vigenere (passphrase 'Edward')"] = decode_vigenere(s, key="EDWARD")
  
    # 7. Vigenere autokey (passphrase "George", custom alph "ZABCDEFGHIJKLMNOPQRSTUVWXY")
    results["Vigenere Autokey (passphrase 'George')"] = decode_vigenere_autokey(s, key_seed="GEORGE", custom_alph="ZABCDEFGHIJKLMNOPQRSTUVWXY")
  
    return results

#############################################
# Hàm main để chạy tool
#############################################
def main():
    print("Nhập chuỗi mã hóa:")
    input_str = input().strip()
    outputs = decode_input(input_str)
    print("\nKết quả giải mã theo từng phương pháp:\n" + "="*50)
    for method, result in outputs.items():
        print(f"\n[{method}]:\n{result}\n{'-'*50}")

if __name__ == '__main__':
    main()
