<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker测试</title>
</head>
<body>
${name}---${msg}
<br>
<hr>
<br>
assign指令<br>
<#-- assign指令使用-->
<#assign linkman="黑马"/>
${linkman}<br>

<#assign info={"mobile":"13333333333", "address":"吉山村"} />
mobile = ${info.mobile}；address = ${info.address}

<br>
<hr>
<br>
include引入其他模版<br>
<#include "header.ftl"/>

<br>
<hr>
<br>
if条件控制语句<br>
<#assign bool=true>
<#if bool>
    bool的值为true
<#else>
    bool的值为false
</#if>

<br>
<hr>
<br>
list循环控制语句<br>
<#list goodsList as goods>
    ${goods_index}--${goods.name}--${goods.price}<br>
</#list>
总有${goodsList?size}条记录。

<br>
<hr>
<br>
eval内建函数，可以将Json字符串转换为对象<br>
<#assign jsonStr='{"id":123,"name":"浩洋"}'/>
<#assign jsonObj=jsonStr?eval/>
${jsonObj.id}--${jsonObj.name}

<br>
<br>
日期格式化：<br>
.now 表示当前日期时间：${.now}<br>
today的日期时间：${today?datetime}<br>
today的日期：${today?date}<br>
today的时间：${today?time}<br>
today的格式化显示：${today?string("yyyy年MM月dd日 HH:mm:ss")}<br>

<br><br>
number数值默认显示 = ${number}；可以使用?c方式进行格式化为字符串显示而不会出现千分位上使用,的方式：${number?c}

<br>
<hr>
<br>

<br>
<hr>
<br>
</body>
</html>