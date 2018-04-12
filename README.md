# Enjoy Spring!

## プロジェクトの作成

Spring Initializrを使うと便利。

* https://start.spring.io/

## REST APIでHello, world!

Spring Initializrを開く。

"Dependencies"で`Web`, `DevTools`を選んで"Generate Project"。

ダウンロードした`demo.zip`を任意の場所に解凍。

`src/main/java`へ`com.example.demo.HelloController`クラスを作る。

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello, world!";
    }
}
```

IDEから`com.example.demo.DemoApplication`を実行して http://localhost:8080/hello をブラウザで開く。
