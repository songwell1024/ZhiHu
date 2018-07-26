# ZhiHu
SpringBoot2.0+SpringMVC+Mybatis 仿写知乎问答的网站
#实现的功能：
登陆注册，增删改查，提问，评论，敏感词过滤，站内信，点赞，新鲜事，邮件通知，solr搜索等
  1.框架：SpringBoot + Mybatis
  2.存储：数据库使用MySQL
  3.缓存：Redis
  4.模板引擎：freemarker
  5.搜索引擎：solr
#技术：
  1.基于redis的缓存
  2.基于前缀树的敏感词过滤
  3.异步框架的设计，底层使用的是Redis的异步队列，使用异步队列实现站内信，点赞，邮件等功能
  4.solr结合IKAnalyzer自定义中文分词，实现搜索引擎
  5.timeline推拉模式结合的时间轴，渲染新鲜事，实现内容的推送
