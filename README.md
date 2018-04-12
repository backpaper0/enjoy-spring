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

## Spring Data JPA

"Dependencies"で`JPA`, `H2`を追加で選択して"Generate Project"。

アプリケーションを一旦停止して`pom.xml`を差し替えた後に再起動する。

`Message`エンティティクラスを作る。

```java
package com.example.demo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

`MessageRepository`インターフェースを作る。

```java
package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
```

動作確認用の`MessageController`クラスを作る。

```java
package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Message> getAllMessage() {
        return repository.findAll();
    }

    @PostMapping
    public Message post(@RequestParam String content) {
        final Message entity = new Message();
        entity.setContent(content);
        repository.save(entity);
        return entity;
    }
}
```

次の`curl`コマンドでデータの取得ができる。

```sh
curl localhost:8080/messages
```

次の`curl`コマンドでデータの登録ができる。

```sh
curl localhost:8080/messages -X POST -d "content=My message"
```

## Thymeleaf

"Dependencies"で`Thymeleaf`を追加で選択して"Generate Project"。

アプリケーションを一旦停止して`pom.xml`を差し替えた後に再起動する。

`MessageController`クラスを修正する。

```java
package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ModelAndView getAllMessage() {
        final String viewName = "message-page";
        final String modelName = "messages";
        final List<Message> modelObject = repository.findAll();
        return new ModelAndView(viewName, modelName, modelObject);
    }

    @PostMapping
    public String post(@RequestParam String content) {
        final Message entity = new Message();
        entity.setContent(content);
        repository.save(entity);
        return "redirect:/messages";
    }
}
```

`src/main/resources/templates`へ`message-page.html`を作成する。

```html
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Messages</title>
</head>
<body>
    <h1>Messages</h1>
    <form data-th-action="@{/messages}" method="post">
        <input type="text" name="content" autofocus>
        <button>Submit</button>
    </form>
    <p data-th-each="message : ${messages}"
        data-th-text="${message.id + ': ' + message.content}">Dummy message</p>
</body>
</html>
```

http://localhost:8080/messages をブラウザで開く。

## ログイン

"Dependencies"で`Security`を追加で選択して"Generate Project"。

アプリケーションを一旦停止して`pom.xml`を差し替えた後に再起動する。

http://localhost:8080/messages をブラウザで開く。

ユーザー名は`user`、パスワードはアプリケーション起動時にコンソールに出力されるUUID。

ユーザー名とパスワードを変更する場合は`src/main/resources/`にある`application.properties`を変更する。
例えばユーザー名を`uragami`、パスワードを`secret`に変更する場合は`application.properties`に次の記述を足す。

```
spring.security.user.name=uragami
spring.security.user.password=secret
```
