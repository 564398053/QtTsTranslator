# QtTsTranslator

A Qt ts translator written in java to auto translate qt files

## 原理

打开文件目录下的ts文件.
解析文件. 解析出当前文件的语言，针对一个个未翻译的单词通过翻译API发起请求，获取到值后，填充到对应的值. 保存.

## 其他

### Qt项目中的ts文件生成

1. pro文件中添加 `translations += projectname_languageName.ts projectname_languageName1.ts`
      e.g. `translations += demo_fr.ts demo_de.ts demo_en.ts`

2. 命令行执行 `lupdate projectname.pro` 将项目代码中的 `tr("xxx")`  等提取出来生成ts文件

3. 命令行`lrelease projectname.pro` 将ts文件生成压缩文件 `*.qm`

## 开发进度

目前实现的Java读取xml文件， 后续待开发
