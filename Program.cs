// File: Program.cs
using System;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Concurrent;

class Program
{
    static async Task Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Usage: dotnet run <base_domain> <wordlist.txt> [maxConcurrency]");
            return;
        }

        string baseDomain    = args[0].Trim().TrimEnd('.');
        string wordlistPath  = args[1];
        int maxConcurrency   = args.Length >= 3 && int.TryParse(args[2], out var mc) ? mc : 50;

        if (!File.Exists(wordlistPath))
        {
            Console.WriteLine($"[!] Wordlist file not found: {wordlistPath}");
            return;
        }

        var words = File.ReadAllLines(wordlistPath)
                        .Select(w => w.Trim())
                        .Where(w => !string.IsNullOrWhiteSpace(w))
                        .ToArray();

        var found = new ConcurrentBag<(string fqdn, IPAddress[] addrs)>();
        using var sem = new SemaphoreSlim(maxConcurrency);

        var tasks = words.Select(async word =>
        {
            await sem.WaitAsync();
            try
            {
                string fqdn = $"{word}.{baseDomain}";
                try
                {
                    var addrs = await Dns.GetHostAddressesAsync(fqdn);
                    if (addrs.Length > 0)
                    {
                        found.Add((fqdn, addrs));
                        Console.WriteLine($"[+] {fqdn} → {string.Join(", ", addrs)}");
                    }
                }
                catch (SocketException)
                {
                    // no such host
                }
            }
            finally
            {
                sem.Release();
            }
        });

        await Task.WhenAll(tasks);

        Console.WriteLine("\n=== Scan completed ===");
        if (found.IsEmpty)
            Console.WriteLine("No subdomains found.");
        else
            Console.WriteLine($"Total found: {found.Count}");
    }
}
