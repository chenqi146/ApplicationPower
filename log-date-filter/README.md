# Log date filter
项目用于处理日志中的时间，代码源自[logstash-filter-date](https://github.com/logstash-plugins/logstash-filter-date)插件。
可以使用该项目将ISO8601规范的时间准换成UTC时间，
例如从日志中提取时间来覆盖elk日志采集的@timestamp字段值。

# 常用组件日志格式
traefix访问日志： dd/MMM/yyyy:H:m:s Z
