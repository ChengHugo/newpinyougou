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


<br>
<hr>
<br>

<br>
<hr>
<br>

<br>
<hr>
<br>
</body>
</html>