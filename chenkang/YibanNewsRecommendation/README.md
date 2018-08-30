# NewsRecommendation
长安大学校内新闻搜索 & 推荐系统。

### 架构 & 实现要点
1. 使用IKAnalyzer分词器对新闻进行分词；
2. 使用R按照分词结果对新闻按照关键词聚类聚类的结果存储在本地数据库（因为没有学校数据库的写权限）；
3. 使用Solr对文档集合（新闻总和）进行倒排索引。

### 系统架构
![Image text](https://github.com/OURCKTheCoder/NewsRecommendation/blob/master/src/main/resources/SysStructure.png)
