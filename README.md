# entity-with-hash
ホットスポット回避のためのhashを持つEntityを実装してみたサンプル

## how to use
※実機のSpannerを使用することを想定している

### 1. 環境変数を設定する
以下のSpanner用の3つの環境変数を設定する  
値は任意の値を設定する
- PROJECT_ID=project_id
- INSTANCE_ID=instance_id
- DATABASE_NAME=database_id

### 2. テーブルの作成
`src/resources/schema.sql`を実行してテーブルを作成する

### 3. テストを実行する
実行する
