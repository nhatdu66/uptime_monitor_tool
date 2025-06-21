#!/usr/bin/env php
<?php
/**
 * tool_dir_enum.php
 *
 * PHP CLI Directory Enumerator (HEAD requests, multi-cURL)
 *
 * Usage:
 *   php tool_dir_enum.php <base_url> <wordlist.txt> [concurrency]
 *
 * Example:
 *   php tool_dir_enum.php https://example.com dirs.txt 20
 */

if ($argc < 3) {
    fwrite(STDERR, "Usage: php {$argv[0]} <base_url> <wordlist.txt> [concurrency]\n");
    exit(1);
}

$base   = rtrim($argv[1], "/");
$list   = $argv[2];
$maxCh  = isset($argv[3]) ? (int)$argv[3] : 10;

if (!filter_var($base, FILTER_VALIDATE_URL)) {
    fwrite(STDERR, "[!] Invalid URL: $base\n");
    exit(1);
}
if (!is_readable($list)) {
    fwrite(STDERR, "[!] Cannot read wordlist: $list\n");
    exit(1);
}

// Load wordlist
$paths = file($list, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);

// cURL multi init
$mh = curl_multi_init();
$handles = [];
$results = [];

// Function to enqueue a new handle
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
    $handles[$id] = $ch;
    curl_multi_add_handle($mh, $ch);
}

// Seed initial batch
$next = 0;
$total = count($paths);
while ($next < $maxCh && $next < $total) {
    enqueue($mh, $handles, $next, $base . '/' . ltrim($paths[$next], '/'));
    $next++;
}

// Process queue
do {
    while (($status = curl_multi_exec($mh, $running)) === CURLM_CALL_MULTI_PERFORM) {}
    // Read any completed handles
    while ($info = curl_multi_info_read($mh)) {
        $ch = $info['handle'];
        $idx = array_search($ch, $handles, true);
        $url = $base . '/' . $paths[$idx];

        if ($info['result'] === CURLE_OK) {
            $http = curl_getinfo($ch, CURLINFO_RESPONSE_CODE);
            if (in_array($http, [200,301,302])) {
                echo "[+] Found ({$http}): $url\n";
            }
        } else {
            // silent on error
        }

        // remove
        curl_multi_remove_handle($mh, $ch);
        curl_close($ch);
        unset($handles[$idx]);

        // enqueue next
        if ($next < $total) {
            enqueue($mh, $handles, $next, $base . '/' . ltrim($paths[$next], '/'));
            $next++;
        }
    }
    // wait for activity
    if ($running) {
        curl_multi_select($mh, 1.0);
    }
} while ($running || !empty($handles));

curl_multi_close($mh);
