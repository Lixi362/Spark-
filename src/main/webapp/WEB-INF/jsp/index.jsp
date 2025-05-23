<%@page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="UTF-8">
    <title>商品销售额实时展示</title>
</head>
<body style="height: 100%;margin:0">
    <div id="container" style="height: 30%;width: 30%"></div>
    <div id="message"></div>
    <script src="js/echarts.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.8.3.min.js"></script>
    <script type="text/javascript">

        var myChart=echarts.init(document.getElementById('container'));
        myChart.setOption({
            title:{
                text:'商品销售额汇总',
                subtext : '数据来自模拟'
            },
            tooltip:{
                trigger:'axis',
                axisPoint:{
                    type:'shadow'
                }
            },
            legend:{
                data:['2025年5月22日']
            },
            grid:{
                left:'3%',
                right:'4%',
                bottom:'3%',
                containLabel:true
            },
            xAxis:{
                type:'value',
                boundaryGap:[0,0.01]
            },
            yAxis:{
                type:'category',
                data:[]
            },
            series:[
                {
                name:'2025年5月22日',
                type:'bar',
                data:[]
                }
            ]
        });
        // 隐藏加载动画
        myChart.hideLoading();
        var websocket=null;
    //     判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window){
            websocket=new WebSocket("ws://localhost:8080/RealtimeUi-1.0-SNAPSHOT/uiwebSocket");
        }else {
            alert("当前浏览器不支持 websocket")
        }
    //     连接发生错误的回调方法
        websocket.onerror=function (){
            setMessageInnerHTML("WebSocket连接发生错误");
        };
    //     连接成功建立的回调方法
        websocket.onopen=function (){
            setMessageInnerHTML("WebSocket连接成功");
        }
    //     收到消息的回调方法
        websocket.onmessage=function (event){
            jsonbean=JSON.parse(event.data);
            alert(jsonbean);
        //填充数据
        myChart.setOption({
            yAxis: {
                data:jsonbean.produceId
            },
            series:[{
                data:jsonbean.producetSumPrice
            }]
        })
        setMessageInnerHTML(event.data);
    }
    //     连接关闭的回调方法
        websocket.onclose=function (){
            setMessageInnerHTML("WebSocket连接关闭");
        }
    //     监听窗口关闭事件，当窗口关闭时，主动关闭WebSocket连接，
    //     为防止连接还没断开就关闭窗口，server端会抛出异常
        window.onbeforeunload=function () {
            closeWebSocket();
        }
    //     将消息显示在网页上
        function setMessageInnerHTML(innerHTML){
            // document.getElementById('message').innerText = message;
            document.getElementById("message").innerHTML+=innerHTML+'<br/>'
        }
        // 关闭websocket连接
        function closeWebSocket(){
            websocket.close();
        }

    </script>
</body>
</html>