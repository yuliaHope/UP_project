var flag = false;
var editElement;
var userName = 'Anonymous';

var uniqueId = function() {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};

var newMessage = function(name, text, removed, changed, who) {
    return {
        author: name,
        messageText: text,
        timestamp: '5:15 PM',
        removed: !!removed,
        changed: !!changed,
        myMessage: !!who,
        id: uniqueId()
    };
};

var messageHistory = [];

function run() {
    document.getElementById('Rename').addEventListener('click', delegateEvent);
    document.getElementsByClassName('messageHistory')[0].addEventListener('click', delegateEvent);
    document.getElementById('addMessage').addEventListener('click', delegateEvent);
    document.getElementById('inputMessage').addEventListener('keydown', delegateEvent);
    document.getElementById('NewName').addEventListener('keydown', delegateEvent);

    var messageList = loadMessages();

    if(messageList != 0) {
        userName = messageList[messageList.length - 1].author;
        document.getElementById("userName").innerText = userName;
        render(messageList);
    }

}

function reName(){
    var name = document.getElementById('NewName');
    userName = name.value;
    var user = document.getElementById("userName");
    user.innerText = userName;
    name.value = '';
}

function delegateEvent(event) {
    if (event.target.classList.contains('editMsg')
        || event.target.classList.contains('editText')) {
        editMsg(event.target);
        return;
    }
    if (event.target.classList.contains('deleteMsg')
        || event.target.classList.contains('delText')) {
        delMsg(event.target);
        return;
    }
    if(event.target.classList.contains('addMessage')
        && flag == true){
        onEditButtonClick();
        return;
    }
    if(event.target.classList.contains('Rename')
        || (event.target.classList.contains('NewName')
        && event.type === 'keydown' && event.keyCode == 13)){
        reName();
        return;
    }
    if(event.target.classList.contains('addMessage')
        || event.type === 'keydown' && event.keyCode == 13){
        onAddButtonClick();
        return;
    }
}

function delMsg(element) {
    while (element != this) {
        if (element.classList.contains('Message')
            || element.classList.contains('editMessage')) {
            break;
        }
        element = element.parentNode;
    }

    var index = indexByElement(element, messageHistory);
    var message = messageHistory[index];
    message.removed = !message.removed;
    message.messageText = 'This message has been removed.';

    renderMessageState(element, message);
    messageHistory.splice(index, 1);
    saveMessages(messageHistory);
}

function indexByElement(element){
    var id = element.attributes['message-id'].value;
    for(var i = 0; i < messageHistory.length; i++)
        if(messageHistory[i].id === id)
            return i;
}

function editMsg(element) {
    while (element != this) {
        if (element.classList.contains('Message') || element.classList.contains('editMessage')) {
            break;
        }
        element = element.parentNode;
    }
    var messageText = document.getElementById('inputMessage');
    var valueMessageText = element.getElementsByClassName('messageText')[0].innerHTML;
    messageText.value = valueMessageText;
    editElement = element;

    flag = true;
}

function onEditButtonClick() {
    var messageText = document.getElementById('inputMessage');

    addEditMessage(messageText.value);

    messageText.value = '';
    flag = false;
}

function addEditMessage(text){
    if (!text) {
        return;
    }

    var index = indexByElement(editElement);
    var message = messageHistory[index];
    message.changed = !message.changed;
    message.messageText = text;

    renderMessageState(editElement, message);
    saveMessages(messageHistory);
}

function onAddButtonClick(){
    var messageText = document.getElementById('inputMessage');
    var message = newMessage(userName, messageText.value, false, false, false);

    if(messageText.value == '')
        return;

    messageText.value = '';
    render([message]);
    saveMessages(messageHistory);
}

function render(messages) {
    for(var i = 0; i < messages.length; i++) {
        renderMessage(messages[i]);
    }
    saveMessages(messageHistory);
}

function renderMessage(message){
    var items = document.getElementsByClassName('messageHistory')[0];
    var element = elementFromTemplate();
    renderMessageState(element, message);
    messageHistory.push(message);
    items.appendChild(element);
}

function renderMessageState(element, message){
    if(message.removed) {
        if(element.classList.contains('Message')){
            element.classList.remove('Message')
        } else if(element.classList.contains('editMessage')){
            element.classList.remove('editMessage')
        }
        element.classList.add('deleteMessage');
    } else if(message.changed){
        if(element.classList.contains('Message')){
            element.classList.remove('Message')
        }
        element.classList.add('editMessage');
    }
    element.setAttribute('message-id', message.id);
    element.getElementsByClassName('author')[0].innerText = message.author;
    element.getElementsByClassName('messageText')[0].innerText = message.messageText;
    element.getElementsByClassName('timestamp')[0].innerText = message.timestamp;
}

function elementFromTemplate() {
    var template = document.getElementById("message-template");
    return template.firstElementChild.cloneNode(true);
}

function saveMessages(listToSave) {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }
    localStorage.setItem("Messages List", JSON.stringify(listToSave));
}

function loadMessages() {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("Messages List");

    return item && JSON.parse(item);
}
