var scroller = document.querySelector('#scroller');
var anchor = document.querySelector('#anchor');

var stompClient = null;

var payments;

$(function () {
    function connect() {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }

    $.ajax({
        url: '/api/payments',
        type: 'GET',
        dataType: 'html',
        success: function (payload) {
            payments = JSON.parse(payload)

            alertLoop(0);
        }
    });

    var alertLoop = function (i) {
        if (payments[i]) {
            append(payments[i]);
            setTimeout(function () {
                alertLoop(i + 1);
            }, 90);
        }
    }

    function append(payment) {
        console.log(payment);
        var msg = document.createElement('div');
        msg.className = 'message';
        msg.innerText = payment.dateTime + ", Xrapid Transaction spoted : " + payment.amount + " XRP from " + payment.source + "(" + payment.sourceAddress + ")" + " to " + payment.destination + ", TrxHash : " + payment.transactionHash +  ", Destination Fiat : " + payment.destinationCurrencry;
        scroller.insertBefore(msg, anchor);
    }

    function onError(event) {
    }

    function onConnected() {
        stompClient.subscribe('/topic/payments', onMessageReceived);

    }

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        console.log(message);
        var msg = document.createElement('div');
        msg.className = 'message';
        msg.innerText = message.dateTime + ", Xrapid Transaction spoted : " + message.amount + " XRP from " + message.source + "(" + message.sourceAddress + ")" + " to " + message.destination + ", TrxHash : " + message.transactionHash +  ", Destination Fiat : " + message.destinationCurrencry;
        scroller.insertBefore(msg, anchor);
    }

    connect();
});
