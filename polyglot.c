#if !CS
// ==== C & C++ branch ====
#include <stdio.h>

int main(void) {
    printf("Hello from C/C++!\n");
    return 0;
}

#else
// ==== C# branch ====
using System;

class Program {
    static void Main() {
        Console.WriteLine("Hello from C#!");
    }
}
#endif
