package com.expertofordering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")

    public String test() {
        return """
        <!DOCTYPE html>
        <html>
        <body>
        <h3>WebSocket AI Chat Test</h3>

        <input id="msg" placeholder="메시지 입력"/>
        <button onclick="send()">Send</button>

        <pre id="log"></pre>

        <script>
          const log = document.getElementById("log");
          const ws = new WebSocket("ws://127.0.0.1:8081/ws/chat");

          ws.onopen = () => log.innerText += "✅ CONNECTED\\n";
          ws.onmessage = e => log.innerText += "🤖 " + e.data + "\\n";
          ws.onclose = () => log.innerText += "❌ CLOSED\\n";

          function send() {
            const v = document.getElementById("msg").value;
            log.innerText += "🧑 " + v + "\\n";
            ws.send(v);
          }
        </script>
        </body>
        </html>
        """;
    }
}
