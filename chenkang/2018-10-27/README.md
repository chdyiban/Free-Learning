#今日进度
1. 修改solr配置文件,使返回的搜索建议个数减少至2个;
2. 修改搜索应用，对外部隐藏“文档集合”这一复杂的概念。
3. 搜索模块：改进搜索效果，修改了Solr搜索的权重因子分配，把原来不合理的标题:内容 = 2:1 改为 新闻标题:新闻内容:其它（如作者） = 3:3:1
4. 在2.和3.变动下修改API文档