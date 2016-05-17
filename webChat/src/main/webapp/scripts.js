'use strict';

var isEdit = false;
var idEditElement;
var newName;

var uniqueId = function () {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};

var User = {
    userName: '###',
    userID: '#'
};

var Application = {
    mainUrl: 'http://localhost:8080/conversation',
    messageHistory: [],
    token: 'TN11EN'
};

function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function newMessage(name, text, removed, changed) {
    return {
        author: name,
        text: text,
        timestamp: new Date().getTime(),
        removed: !!removed,
        changed: !!changed,
        userId: User.userID,
        id: uniqueId()
    };
};


function run() {
    loadUser();
    document.getElementsByClassName('messageHistory')[0].addEventListener('click', delegateEvent);
    document.getElementById('addMessage').addEventListener('click', delegateEvent);
    document.getElementById('inputMessage').addEventListener('keydown', delegateEvent);

    doPolling();
}

function delegateEvent(event) {
    if (event.target.classList.contains('editMsg')
        || event.target.classList.contains('editText')) {
        prepareMessageForEdit(event.target);
        return;
    }
    if (event.target.classList.contains('deleteMsg')
        || event.target.classList.contains('delText')) {
        deleteIconClick(event.target);
        return;
    }
    if ((event.target.classList.contains('addMessage')
        || (event.target.classList.contains('inputMessage')
        && event.type === 'keydown' && event.keyCode == 13))
        && isEdit == true) {
        onEditIconClick();
        return;
    }
    if (event.target.classList.contains('addMessage')
        || event.type === 'keydown' && event.keyCode == 13) {
        onAddButtonClick();
        return;
    }
}

function searchRootElement(element) {
    while (element != this) {
        if (element.classList.contains('Message')
            || element.classList.contains('editMessage')) {
            break;
        }
        element = element.parentNode;
    }
    return element;
}

function deleteIconClick(element) {
    element = searchRootElement(element);
    var id = idFromElement(element);

    deleteMessage(id, function () {
        render(Application);
    });
}

function deleteMessage(id, done) {
    var index = indexById(Application.messageHistory, id);
    var messageToDelete = Application.messageHistory[index];
    messageToDelete.text = 'This message has been removed.';
    messageToDelete.removed = !messageToDelete.removed;
    messageToDelete.author = User.userName;
    messageToDelete.timestamp = new Date().getTime();

    var url = Application.mainUrl + '?msgId=' + id;

    ajax('DELETE', url, null, function () {
        Application.messageHistory.push(messageToDelete);
        done();
    });
}


function prepareMessageForEdit(element) {
    element = searchRootElement(element);

    textToEdit(element);

    idEditElement = idFromElement(element);

    isEdit = true;
}

function textToEdit(element) {
    var text = document.getElementById('inputMessage');
    var valueMessageText = element.getElementsByClassName('messageText')[0].innerHTML;
    text.value = valueMessageText;
}

function onEditIconClick() {
    addEditMessage(idEditElement, function () {
        render(Application);
    });

    isEdit = false;
}

function addEditMessage(id, done) {
    var index = indexById(Application.messageHistory, id);
    var messageToPut = Application.messageHistory[index];
    messageToPut.changed = !messageToPut.changed;
    messageToPut.text = textValue();
    messageToPut.timestamp = new Date().getTime();
    messageToPut.author = User.userName;

    ajax('PUT', Application.mainUrl, JSON.stringify(messageToPut), function () {
        Application.messageHistory.splice(index, 1);
        Application.messageHistory.push(messageToPut);
        done();
    });
}

function onAddButtonClick() {
    var text = textValue();

    addMessage(text, function () {
        render(Application);
    });
}

function addMessage(text, done) {
    if (text == '' || text == null)
        return;

    var message = newMessage(User.userName, text, false, false, true);

    ajax('POST', Application.mainUrl, JSON.stringify(message), function () {
        Application.messageHistory.push(message);
        done();
    });
}

function render(root) {
    visibleServerErr('hidden');
    var messages = document.getElementsByClassName('messageHistory')[0];
    var messagesMap = root.messageHistory.reduce(function (accumulator, message) {
        accumulator[message.id] = message;

        return accumulator;
    }, {});
    var notFound = updateList(messages, messagesMap);
    removeFromList(messages, notFound);
    appendToList(messages, root.messageHistory, messagesMap);
}

function updateList(element, messagesMap) {
    var children = element.children;
    var notFound = [];

    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        var id = child.attributes['message-id'].value;
        var message = messagesMap[id];

        if (message == null) {
            notFound.push(child);
            //continue;
        }

        renderMessageState(child, message);
        messagesMap[id] = null;
    }

    return notFound;
}

function removeFromList(element, children) {
    for (var i = 0; i < children.length; i++) {
        element.removeChild(children[i]);
    }
}

function appendToList(element, messages, messageMap) {
    for (var i = 0; i < messages.length; i++) {
        var message = messages[i];

        if (messageMap[message.id] == null) {
            continue;
        }
        messageMap[message.id] = null;

        var child = elementFromTemplate();

        renderMessageState(child, message);
        element.appendChild(child);
    }
}

function dateParse(millisecondsTime) {
    var hours = millisecondsTime.getHours();
    var minutes = millisecondsTime.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    if (hours < 10) {
        hours = '0' + hours;
    }
    var formatedDate = hours + ':' + minutes;
    return formatedDate;
}

function renderMessageState(element, message) {
    if (message.removed) {
        element.classList.remove('Message');
        element.classList.add('deleteMessage');
    } else if (message.changed) {
        element.classList.remove('Message');
        element.classList.add('editMessage');
    }
    if (message.userId !== User.userID) {
        element.classList.add('MyMessage');
    } else if (element.classList.contains('MyMessage')) {
        element.classList.remove('MyMessage');
    }
    element.setAttribute('message-id', message.id);
    element.getElementsByClassName('author')[0].innerText = message.author;
    var date = new Date(message.timestamp);
    element.getElementsByClassName('messageText')[0].innerText = message.text;
    element.getElementsByClassName('timestamp')[0].innerText = dateParse(date);
}

function elementFromTemplate() {
    var template = document.getElementById("message-template");
    return template.firstElementChild.cloneNode(true);
}

function idFromElement(element) {
    return element.attributes['message-id'].value;
}

function indexById(list, id) {
    for (var i = 0; i < list.length; i++)
        if (list[i].id === id)
            return i;
}

function textValue() {
    var messageTextElement = document.getElementById('inputMessage');
    var messageText = messageTextElement.value;

    messageTextElement.value = '';

    return messageText;
}

function defaultErrorHandler(message) {
    console.error(message);
}

function isError(text) {
    if (text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch (ex) {
        return true;
    }

    return !!obj.error;
}

function visibleServerErr(state) {
    if (state === 'visible') {
        if (document.getElementById('server').classList.contains('hiddenServer')) {
            document.getElementById('server').classList.remove('hiddenServer');
            document.getElementById('server').classList.add('displayServer');
        }
    } else if (state === 'hidden') {
        if (document.getElementById('server').classList.contains('displayServer')) {
            document.getElementById('server').classList.remove('displayServer');
            document.getElementById('server').classList.add('hiddenServer');
        }
    }
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4) {
            visibleServerErr('visible');
            return;
        }

        if (xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if (isError(xhr.responseText)) {
            visibleServerErr('visible');
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
    };

    xhr.ontimeout = function () {
        visibleServerErr('visible');
        ontinueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
        visibleServerErr('visible');
        var errMsg = 'Server connection error !\n' +
            '\n' +
            'Check if \n' +
            '- server is active\n' +
            '- server sends header "Access-Control-Allow-Origin:*"\n' +
            '- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

function doPolling() {
    function loop() {
        var url = Application.mainUrl + '?token=' +
            Application.token;
        ajax('GET', url, null, function (responseText) {
            var response = JSON.parse(responseText);
            Application.token = response.token;
            updateHistory(response.messages);
            render(Application);
            setTimeout(loop, 1000);
        }, function (error) {
            defaultErrorHandler(error);
            setTimeout(loop, 1000);
        });
    }

    loop();
}

function updateHistory(messageList) {
    for (var i = 0; i < messageList.length; ++i) {
        Application.messageHistory.push(messageList[i]);
    }
}

function loadUser() {
    User.userName = getCookie("login");
    User.userID = getCookie("pass");

    var user = document.getElementById("userName");
    user.innerText = User.userName;

}


