#!/usr/bin/env php
<?php
/**
 * ssl_expiry.php – Kiểm tra SSL expiry trên CLI
 * Usage: php ssl_expiry.php example.com
 */

$host = $argv[1] ?? exit("Usage: php {$argv[0]} <domain>\n");
$port = 443;
$ctx = stream_context_create(["ssl"=>["capture_peer_cert"=>true]]);
$client = @stream_socket_client("ssl://{$host}:{$port}", $errno, $errstr, 5, STREAM_CLIENT_CONNECT, $ctx);
if (!$client) {
    die("[!] Không kết nối được: $errstr ($errno)\n");
}
$cert = stream_context_get_params($client)["options"]["ssl"]["peer_certificate"];
$info = openssl_x509_parse($cert);
$expire = $info['validTo_time_t'];
$days = ($expire - time())/86400;
printf("[*] %s expires in %.1f days (%s)\n", $host, $days, date("Y-m-d H:i:s", $expire));
