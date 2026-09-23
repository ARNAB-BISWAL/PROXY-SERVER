# PROXY-SERVER
A multithreaded proxy server that acts as an intermediary between clients and the internet, supporting HTTP/HTTPS request forwarding, response caching, access control, and traffic logging. Demonstrates core networking concepts such as concurrent connections, HTTP communication, caching, request filtering, and proxy-based traffic management.
HTTP Proxy Server
Course: CS-31 (Computer Networks)
Team Size: 4 Members

📌 Project Overview
This project is a lightweight HTTP Proxy Server built to process, inspect, and manage client web traffic. The server sits between a client (such as a web browser) and target remote servers to perform HTTP request interception, caching, domain filtering, and concurrent request handling.

👥 Team Members
Name	Roll / Student ID
[Tanaya Dalabehera]	[2405314]
[B Arnab Kumar Biswal]	[24155918]
[Vidushi Mishra]	[2405320]
[Swastik Banra]	[24156032]
⚡ Key Features
Request Interception & Forwarding: Intercepts incoming HTTP requests, parses headers, forwards requests to destination servers, and relays responses back to the client.

In-Memory Caching: Saves recent response objects locally to reduce response latency and save bandwidth for repeated requests.

Domain Blacklisting / Filtering: Restricts access to specific hostnames or URLs using a configurable blacklist.

Multithreading / Concurrency: Handles multiple simultaneous client connections efficiently.

Activity Logging: Records client IP addresses, requested URLs, status codes, and timestamps to the console/file.

🤖 Declaration of AI Usage
Artificial intelligence tools (such as ChatGPT/Gemini) will be used strictly as an assistant for:

Debugging edge cases, runtime exceptions, and syntax errors.

Understanding low-level socket API options and HTTP header syntax.

Assisting in code documentation and formatting.

Note: AI tools will not be used to auto-generate the complete codebase. Core networking logic, socket operations, and implementations will be developed and committed progressively by team members.
