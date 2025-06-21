#!/usr/bin/env php
<?php
/**
 * tool_dir_enum_verbose.php
 *
 * PHP CLI Directory Enumerator (HEAD requests, multi-cURL, verbose)
 *
 * Usage:
 *   php tool_dir_enum_verbose.php <base_url> <wordlist.txt> [concurrency]
 *
 * Example:
 *   php tool_dir_enum_verbose.php https://example.com dirs.txt 20
 */

if ($argc < 3) {
    fwrite(STDERR, "Usage: php {$argv[0]} <base_url> <wordlist.txt> [concurrency]\n");
    exit(1);
}

$base   = rtrim($argv[1], "/");
$list   = $argv[2];
$maxCh  = isset($argv[3]) ? (int)$argv[3] : 10;

// Validate
if (!filter_var($base, FILTER_VALIDATE_URL)) {
    fwrite(STDERR, "[!] Invalid URL: $base\n");
    exit(1);
}
if (!is_readable($list)) {
    fwrite(STDERR, "[!] Cannot read wordlist: $list\n");
    exit(1);
}

// Load & clean wordlist
$raw = file($list, FILE_IGNORE_NEW_LINES);
$paths = [];
foreach ($raw as $line) {
    $p = trim($line, " \t\n\r\0\x0B");  // remove CR, LF, whitespace
    if ($p !== "") {
        $paths[] = ltrim($p, "/");
    }
}
$total = count($paths);
if ($total === 0) {
    fwrite(STDERR, "[!] Empty wordlist after trimming.\n");
    exit(1);
}

// Multi-cURL setup
$mh = curl_multi_init();
$handles = [];
$next = 0;

// Enqueue function
function enqueue(&$mh, &$handles, $id, $url) {
    $ch = curl_init();
    curl_setopt_array($ch, [
        CURLOPT_URL            => $url,
        CURLOPT_NOBODY         => true,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_TIMEOUT        => 5,
        CURLOPT_CONNECTTIMEOUT => 5,
        CURLOPT_USERAGENT      => 'PHP-DirEnum/1.0',
        CURLOPT_HEADER         => true,
        CURLOPT_RETURNTRANSFER => true,
    ]);
    $handles[$id] = ['ch'=>$ch, 'url'=>$url];
    curl_multi_add_handle($mh, $ch);
}

// Seed initial batch
while ($next < $maxCh && $next < $total) {
    enqueue($mh, $handles, $next, "$base/{$paths[$next]}");
    $next++;
}

// Process
do {
    while (curl_multi_exec($mh, $running) === CURLM_CALL_MULTI_PERFORM) {}
    while ($info = curl_multi_info_read($mh)) {
        $handle = $info['handle'];
        // find our meta
        $idx = null;
        foreach ($handles as $i=>$meta) {
            if ($meta['ch'] === $handle) { $idx = $i; break; }
        }
        $url = $handles[$idx]['url'];
        $code = curl_getinfo($handle, CURLINFO_RESPONSE_CODE);
        if ($info['result'] === CURLE_OK) {
            if ($code < 400) {
                echo "[+] FOUND  ({$code}): {$url}\n";
            } else {
                echo "[-] MISSING ({$code}): {$url}\n";
            }
        } else {
            echo "[!] ERROR   (".curl_strerror($info['result'])."): {$url}\n";
        }
        // cleanup
        curl_multi_remove_handle($mh, $handle);
        curl_close($handle);
        unset($handles[$idx]);

        // enqueue next
        if ($next < $total) {
            enqueue($mh, $handles, $next, "$base/{$paths[$next]}");
            $next++;
        }
    }
    if ($running) {
        curl_multi_select($mh, 1.0);
    }
} while ($running || !empty($handles));

curl_multi_close($mh);
