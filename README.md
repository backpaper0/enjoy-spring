# Enjoy Spring!!

## プロジェクトの作成

Spring Initializrを使うと便利。

* https://start.spring.io/

## [REST APIでHello, world!](https://github.com/backpaper0/enjoy-spring/tree/helloworld)

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

## [Spring Data JPA](https://github.com/backpaper0/enjoy-spring/tree/jpa)

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

## [Thymeleaf](https://github.com/backpaper0/enjoy-spring/tree/thymeleaf)

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

## [ログイン](https://github.com/backpaper0/enjoy-spring/tree/login)

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

## [メトリクス](https://github.com/backpaper0/enjoy-spring/tree/metrics)

"Dependencies"で`Actuator`を追加で選択して"Generate Project"。

アプリケーションを一旦停止して`pom.xml`を差し替えた後に再起動する。

通常だと見られる情報が限られるので`application.properties`に次の設定を加える。

```
management.endpoints.web.exposure.include=*
```

これでメトリクスを取得したり、Springが管理しているコンポーネントを一覧したり、環境値を一覧できる。

|エンドポイントの種類|URL|
|---|---|
|ヘルスチェック|http://localhost:8080/actuator/health|
|メトリクス（CPU使用量）|http://localhost:8080/actuator/metrics/process.cpu.usage|
|メトリクス（メモリ使用量）|http://localhost:8080/actuator/metrics/jvm.memory.used|
|メトリクス（ロードされたクラス数）|http://localhost:8080/actuator/metrics/jvm.classes.loaded|
|メトリクス（ライブスレッド数）|http://localhost:8080/actuator/metrics/jvm.threads.live|
|コンポーネント一覧|http://localhost:8080/actuator/beans|
|環境値一覧|http://localhost:8080/actuator/env|
|Spring MVCのエンドポイント一覧|http://localhost:8080/actuator/mappings|

## [データベースをPostgreSQLに変更する](https://github.com/backpaper0/enjoy-spring/tree/postgres)

"Dependencies"で`H2`を削除して、`PostgreSQL`を追加で選択して"Generate Project"。

アプリケーションを一旦停止して`pom.xml`を差し替える。

PostgreSQLを起動する。

ここではDockerでの方法を記載する。
次のコマンドでPostgreSQLが起動する。

```
docker run -d -p 5432:5432 --name=db -e POSTGRES_USER=demo postgres:10.3
```

接続情報は次の通り。

|項目|値|
|---|---|
|ホスト|`localhost`|
|ポート|`5432`|
|データベース名|`demo`|
|ユーザー名|`demo`|
|パスワード|`demo`|

Spring Data JPAはデフォルトだと組み込みH2なら勝手にDDLを発行してくれるが、PostgreSQLだと勝手には発行しなくなる。
手動でテーブルの準備をする。

今回はPostgreSQLクライアントもDockerから使用する。
次のコマンドでpsqlを起動する。

```
docker run -it --rm --link db postgres:10.3 psql -h db demo demo
```

`message`テーブルを作成する。

```sql
CREATE TABLE message (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    content VARCHAR(1000),
    PRIMARY KEY (id)
);
```

動作確認のためレコードを登録しておく。

```sql
INSERT INTO message (content) VALUES ('Hello, world!');
```

このPostgreSQLと繋ぐためJDBC接続情報を`application.properties`へ追加する。

```
spring.datasource.url=jdbc:postgresql://localhost:5432/demo
spring.datasource.username=demo
spring.datasource.password=demo
```

アプリケーションを起動して http://localhost:8080/messages をブラウザで開く。

## [JARで実行する](https://github.com/backpaper0/enjoy-spring/tree/jar)

Spring Bootでは、WARを作ってTomcatなどにデプロイするのではなく、JARを作って`java -jar`で実行する。

まずJARを作る。
`mvnw`というコマンドが付いてくるので、それを使う。
`mvnw`はMavenを実行するスクリプトで、必要に応じてMaven自体のダウンロードも行ってくれる。

企業内ネットワークなど、プロキシ環境にある場合は環境変数`MAVEN_OPTS`を設定する必要がある。
例えばプロキシサーバーのホストが`example.com`、ポートが`3128`とすると次のように`MAVEN_OPTS`を設定する。

```sh
MAVEN_OPTS=-Dhttp.proxyHost=example.com -Dhttp.proxyPort=3128 -Dhttps.proxyHost=example.com -Dhttps.proxyPort=3128
```

`package`ゴールでJARを作る。

```sh
mvnw package
```

作ったJARを実行する。

```sh
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Spring Bootアプリケーションはいくつかの手段で環境毎に設定値を変更できる。


```sh
# コマンドライン引数
java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8765
# システムプロパティ
java -Dserver.port=8765 -jar target/demo-0.0.1-SNAPSHOT.jar
# 環境変数 (cf. https://12factor.net/ja/config )
SERVER_PORT=8765 java -jar target/demo-0.0.1-SNAPSHOT.jar
```

