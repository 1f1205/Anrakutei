## 目次
* [ステージの追加](#stage)
* [インベーダーの追加](#invader)
* [インベーダーのビームの追加](#invaderBeam)
* [アイテムの追加](#item)

## [ステージの追加](id:stage)
### 関連するファイル
* assets/stage.xml
	* ステージ情報を管理しているファイル


### ステージ情報のXMLファイル構成
1. `assets/stage.xml`を開く
2. ステージに必要なプロパティは以下の通り
	* 特にinvTypeArrayプロパティが複雑になっている（直したい）

```
<Stage>
	<ID>ステージ番号</ID>
	<MaxInvader>インベーダーの総数</MaxInvader>
	<Boss>ボスの種類（今は使っていない）</Boss>
	<invTypeArray>表示するインベーダーの種類と数<invTypeArray>

```

### ステージの追加
1. `Id`にステージ番号を既に登録されている番号の連番になるように追加する
2. インベーダーの総数を`MaxInvader`プロパティに書く
3. `invTypeArray`にインベーダーの種類に対応した箇所に表示する数を記載する。
	* ここの数字は合算して`MaxInvader`と同じになるようにする
	* `Config.java`の`INV_XXXXXX`の値とinvTypeArrayの位置は対応している。
4. `invTypeArray`のそれぞれの値は以下のようになっている

```
<invTypeArray>パープル,イエロー,ブルー,オレンジ,グリーン,ボス</invTypeArray>

例)パープルを2体,ブルーを3体,ボスを1体
<invTypeArray>2,0,3,0,0,1</invTypeArray>
```


## [インベーダーの追加](id:invader)
### 関連するファイル
* res/drawable-ldpi/
	* インベーダーの画像を保存する
* src/com.Ichif1205.anrakutei.invader
	* インベーダーに関連したファイルがまとまったパッケージ
* BaseInvader.java
	* インベーダーの基底クラス
	* 新規にインベーダーを追加する時はこのクラスを継承する
* Config.java
	* ゲームの設定ファイル
* InvaderFactory.java
	* インベーダーを生成するクラス

### 1. 画像リソースを追加
1. `res/drawable-ldpi/`に画像を追加する

### 2. インベーダークラスの実装
1. `BaseInvader.java`を継承したクラスを`src/com.Ichif1205.anrakutei.invader`パッケージに保存
2. getInvaderResource()はインベーダー画像のIDを返すように実装する
3. move()にはインベーダーの動きを実装する。
4. `mPositionX`と`mPositionY`がインベーダーの座標になっているので、値を変化させれば移動する

```
public class ExampleInvader extends BaseInvader {

	public ExampleInvader(.....

	@Override
	protected int getInvaderResource() {
		return R.drawable.xxxxxxx(インベーダーの画像リソースID)
	}
	
	@Override
	public void move(float playerPosX) {
		インベーダーの動きを実装.....
	}

}
```

### 3. ファクトリークラスに追加
1. `Config.java`に追加するインベーダーの定数を既に定義されているものと連番になるように追加する。
2. `InvaderFactory.java`のcreate()に先ほど追加した定数の条件を追加する。
3. 上記の条件に合ったら`invader`変数に作成したインベーダークラスを入れるようにする。

### 4. ステージ情報を修正
1. `assets/stage.xml`を開く
2. `invTypeArray`に`Config.java`に定義した定数の値と同じ位置に表示する数を記載する
	* ステージを追加する際は[ステージの追加](#stage)を参照
	
## [インベーダーのビームの追加](id:invaderBeam)
### 関連するファイル
* src/com.Ichif1025.anrakutei.beam
	* ビームのクラスを管理するパッケージ
* BaseBeam.java
	* ビームの基底クラス
* BeamFactory.java
	* ビームを生成するファクトリークラス

### 1. ビームクラスの追加
1. `BaseBeam.java`を継承したビームクラスを`com.Ichif1205.anrakutei.beam`に追加する
2. コンストラクタは2種類追加する
3. `updatePosition()`メソッドにはビームの動きを実装する

### 2. Factoryクラスに追加
1. `com.Ichif1205.anrakutei.beam`の`BeamFactory.java`を開く
2. 追加したビームクラス用の定数を`BEAM_XXXXX`として定義する
3. `createBeam()`メソッドの条件文に定義した定数を追加し、作成したクラスを返すようにする。
4. ボスのビームにも動きを追加する場合は`createBossBeam()`メソッドに条件を追加する

## [アイテムの追加](id:item)
* com.Ichif1205.anrakutei.item
	* アイテムを管理するパッケージ
* BaseItem.java
	* アイテムの基底クラス
* ItemFactory.java
	* アイテムを生成するファクトリークラス
* ItemMediator.java
	* アイテムの効果を定義するクラス

### 1. アイテムクラスの追加
1. `BaseItem.java`を継承したクラスを`com.Ichif1205.anrakutei.item`に追加する
2. `getItemResource()`メソッドはアイテムの画像リソースを返すように実装する
3. `adjustEffective()`メソッドはMediatorクラス(後述)に実装したアイテムの効果を呼び出すようにする。

### 2. Mediatorにアイテムの効果を追加
1. `com.Ichif1205.anrakutei.item`の`ItemMediator.java`を開く
2. アイテムの効果にあった適切な名前でメソッドを作成し、取得時の処理を実装する
3. アイテムクラスの`adjustEffective()`メソッドでアイテムの効果を実装したメソッドを呼び出す


### 3. Factoryクラスに追加
1. `ItemFactory.java`を開く
2. `ITEM_PATTERN`の数字をインクリメント
3. `create`メソッドのswitch文に追加したアイテムのクラスを返すように処理を追加

