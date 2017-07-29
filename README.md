# ObjBlock
Minecraft 1.7.10 : 3D model block MOD

## コンテンツパック概要  
コンテンツパックは各ユーザが独自のブロックを追加するためのパックで  
3Dモデル、画像、ブロック設定ファイルを集めたものである。  

MODを導入した状態で起動すると ObjBlock というフォルダがmodsと同じ階層に作成されるので、  
そこにパックを入れて再度起動するとブロックが追加される。

マルチプレイではサーバ、クライアントともにすべて同じパックを使用しなければならない。  

## コンテンツパック構成  
コンテンツパックは以下の構成でなければならない。  
ファイルやフォルダ名に日本語やスペースは使用できない。  
_ または - 以外の記号も推奨しない。

フォルダは解凍していても、zip に圧縮していても読み込む。  
パックの作成中は解凍したままで、配布するときはzipに圧縮して配布するとよい。  
★注意 zip で配布する場合は zip内の一番上の階層のフォルダは assets でなければならない。  

	sample.zip
	  +---assets
	    +---objblock
	    +---blocks
	    |    sample.json  ... ブロック設定ファイル
	    |
	    +---lang
	    |    en_US.lang   ... ブロック名言語設定ファイル
	    |    ja_JP.lang   ... ブロック名言語設定ファイル
	    |
	    +---models
	    |    sample.mqo   ... ブロックモデルファイル mqo/obj (mqo形式を強く推奨する)
	    |
	    +---textures
	        +---blocks
	            ample.png  ... ブロックの描画に使うテクスチャ

## ブロック設定ファイル
追加するブロックの設定で、json フォーマットで記載する。  
assets/objblock/blocks/???.json

### 例

	{
	  "Hardness"  : 1.0,
	  "Material"  : "iron",
	  "ColorARGB" : "80FF0000"
	}

### 詳細

	"Hardness": 1.0,	ブロックの硬さ 0.0 ～。参考として石が1.5  
	"LightLevel" : 1.0,	光源の強さ 0.0 ～ 1.0  
	"Material" : "iron",	ブロックの性質  
	  "Material"で使用可能な値 :  
	    "air" "grass" "ground" "wood" "rock" "iron" "anvil" "water" "lava" "leaves" "web" "cake"  
	    "plants" "vine" "sponge" "cloth" "fire" "sand" "circuits" "carpet" "glass" "redstoneLight"  
	    "tnt" "coral" "ice" "packedIce" "snow" "craftedSnow" "cactus" "clay" "gourd" "dragonEgg" "portal"  
	"Unbreakable" : false,	ブロックが破壊可能かどうか trueにすると岩盤のように破壊できなくなる。
	"CollidedDamage" : 4,  　ブロックに触れた時のダメージ。0未満にすると回復する。  
	"ColorARGB" : "808000FF",  　ブロックの色 16進数で 透明度,赤,緑,青  
	ブロックの色 ColorARGBと同様だが、16進数ではなく 0.0 ～ 1.0 で表現できる  
	"ColorAlpha" : 1.0,  
	"ColorRed"   : 1.0,  
	"ColorGreen" : 1.0,  
	"ColorBlue"  : 1.0,  
	"Ladder" : true,  　trueにすると梯子として登れるようにする  

