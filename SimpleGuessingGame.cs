using System;

namespace SimpleGuessingGame
{
    class Program
    {
        static void Main(string[] args)
        {
            // Tạo đối tượng Random để sinh số ngẫu nhiên.
            Random random = new Random();
            int secretNumber = random.Next(1, 101); // Số bí mật từ 1 đến 100.
            int userGuess = 0;
            int attempts = 0; // Biến đếm số lần đoán.

            Console.WriteLine("Chào mừng đến với Trò Chơi Giải Đoán Số!");
            Console.WriteLine("Tôi đã chọn một số từ 1 đến 100. Hãy thử đoán xem số đó là gì!");

            // Vòng lặp sẽ tiếp tục cho đến khi người chơi đoán đúng số bí mật.
            while (userGuess != secretNumber)
            {
                Console.Write("Nhập số đoán của bạn: ");
                string input = Console.ReadLine();

                // Kiểm tra xem đầu vào có phải là số hay không.
                if (!int.TryParse(input, out userGuess))
                {
                    Console.WriteLine("Vui lòng nhập một số hợp lệ.");
                    continue;
                }

                attempts++;

                if (userGuess < secretNumber)
                {
                    Console.WriteLine("Số của bạn nhỏ quá. Hãy tăng lên!");
                }
                else if (userGuess > secretNumber)
                {
                    Console.WriteLine("Số của bạn lớn quá. Hãy giảm xuống!");
                }
                else
                {
                    Console.WriteLine($"Chúc mừng! Bạn đã đoán đúng số sau {attempts} lần đoán.");
                }
            }

            Console.WriteLine("Trò chơi kết thúc. Nhấn phím bất kỳ để thoát.");
            Console.ReadKey();
        }
    }
}
