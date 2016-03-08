var iconMoreInfo = 'https://lh3.googleusercontent.com/-bsIfzHbokTPWlSUtSqq-s2nCyjBIpufVNndHAX0RJcJlTbN8UlbQd5M3JbycNEZVYKy=w1107-h623-rw-no';
var iconEdit = 'https://lh3.googleusercontent.com/l6tkRklJ12kAfG21Aecae5QOuFn36bgQZzHArURg1m6QRrcJdu-2n3JSiI_CSTOt40f1=w1107-h623-rw-no';
var iconDelete = 'https://lh3.googleusercontent.com/2Pe-HtXrCB-yzQXGJ0cmzAgbuf_ipJGy7Q2TpAaBK9zC8bdSt8ZkVMyCCs0LxjLcg-21=w1107-h623-rw-no';
var iconEditMsg = 'https://lh3.googleusercontent.com/l6tkRklJ12kAfG21Aecae5QOuFn36bgQZzHArURg1m6QRrcJdu-2n3JSiI_CSTOt40f1=w1107-h623-rw-no';
var flag = false;
var editTarget;
var userName;


function run() {
    replaceName();
    document.getElementsByClassName('messageHistory')[0].addEventListener('click', delegateEvent);
    document.getElementById('addMessage').addEventListener('click', delegateEvent);
}

function renameClick(){
    document.getElementById('Rename').addEventListener('click', reName);
}

function reName(){
    var username = document.getElementById('NewName');
    document.location.href = 'ConversationM.html?' + username.value;
}

function replaceName(){
    var pItem = document.getElementById('userName');
    var name = document.location.href.split ('?') [1];
    pItem.innerHTML = name;
}

function delegateEvent(event) {
    var target = event.target;
    if (event.target.classList.contains('editMsg') || event.target.classList.contains('editText')) {
        editMsg(target);
        return;
    }
    if (event.target.classList.contains('deleteMsg') || event.target.classList.contains('delText')) {
        delMsg(target);
        return;
    }
    if(event.target.classList.contains('addMessage') && flag == true){
        onEditButtonClick();
        return;
    }
    if(event.target.classList.contains('addMessage')){
        onAddButtonClick();
        return;
    }
}

function delMsg(target) {
    var item = createItemBody('This message has been removed.');
    item.classList.add('deleteMessage');
    while (target != this) {
        if (target.classList.contains('Message') || target.classList.contains('editMessage')) {
            break;
        }
        target = target.parentNode;
    }
    var items = document.getElementsByClassName('messageHistory')[0];

    items.replaceChild(item, target);
}

function editMsg(target) {
    while (target != this) {
        if (target.classList.contains('Message')) {
            break;
        }
        target = target.parentNode;
    }
    var messageText = document.getElementById('inputMessage');
    var valueMessageText = target.getElementsByClassName('messageText')[0].innerHTML;
    messageText.value = valueMessageText;
    editTarget = target;

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

    var imgEdit = document.createElement('img');
    var divItem = document.createElement('div');
    var item = createItem(text);

    imgEdit.classList.add('editIcon');
    imgEdit.setAttribute('alt', 'edit message');
    imgEdit.setAttribute('src', iconEditMsg);

    divItem.appendChild(imgEdit);
    item.appendChild(divItem);

    item.classList.add('editMessage');

    var items = document.getElementsByClassName('messageHistory')[0];

    items.replaceChild(item, editTarget);
}

function logIn() {
    document.getElementById('newUser').addEventListener('click', newUser);
    document.getElementById('addUser').addEventListener('click', logInChat);
}

function newUser() {
    var name = document.getElementById('name');
    document.location.href = 'Conversation.html?' + name.value;
}

function logInChat() {
    var name = document.getElementById('name');
    document.location.href = 'ConversationM.html?' + name.value;
}

function setName(){
    var pItem = document.getElementById('userName');
    var name = document.location.href.split ('?') [1];
    pItem.innerHTML = name;
}

function onAddButtonClick() {
    var messageText = document.getElementById('inputMessage');

    addMessage(messageText.value);
    messageText.value = '';
}

function addMessage(value) {
    if (!value) {
        return;
    }

    var item = createItem(value);
    var items = document.getElementsByClassName('messageHistory')[0];

    items.appendChild(item);
}

function createItem(text) {
    var divItem = createItemBody(text);
    var menu = createItemNav();

    divItem.appendChild(menu);

    return divItem;
}

function createItemBody(text) {
    var divItem = document.createElement('li');
    var divAuthor = document.createElement('div');
    var strongAuthor = document.createElement('strong');
    var divMsgText = document.createElement('div');
    var divTimestamp = document.createElement('div');


    divItem.classList.add('Message');
    divAuthor.classList.add('author');
    divMsgText.classList.add('messageText');
    divTimestamp.classList.add('timestamp');
    strongAuthor.innerHTML = document.getElementById('userName').innerHTML;
    divTimestamp.appendChild(document.createTextNode('7:30 p.m.'));

    divAuthor.appendChild(strongAuthor);
    divItem.appendChild(divAuthor);
    divMsgText.appendChild(document.createTextNode(text));
    divItem.appendChild(divMsgText);
    divItem.appendChild(divTimestamp);

    return divItem;
}

function createItemNav() {
    var item = document.createElement('ul');
    var moreInfo = document.createElement('li');
    var menu = document.createElement('ul');
    var edit = document.createElement('li');
    var deleteMsg = document.createElement('li');
    var imgMoreInfo = document.createElement('img');
    var imgEdit = document.createElement('img');
    var imgDelete = document.createElement('img');
    var spanItemEdit = document.createElement('span');
    var spanItemDel = document.createElement('span');

    item.classList.add('nav');
    edit.classList.add('editMsg');
    deleteMsg.classList.add('deleteMsg');
    imgDelete.classList.add('icon');
    imgEdit.classList.add('icon');
    imgMoreInfo.classList.add('icon');
    spanItemEdit.classList.add('editText');
    spanItemDel.classList.add('delText');

    setAtributsNav(imgMoreInfo, imgEdit, imgDelete);

    deleteMsg.appendChild(imgDelete);
    edit.appendChild(imgEdit);
    spanItemEdit.appendChild(document.createTextNode('edit message'));
    edit.appendChild(spanItemEdit);
    spanItemDel.appendChild(document.createTextNode('delete message'));
    deleteMsg.appendChild(spanItemDel);
    menu.appendChild(edit);
    menu.appendChild(deleteMsg);
    moreInfo.appendChild(imgMoreInfo);
    moreInfo.appendChild(menu);
    item.appendChild(moreInfo);

    return item;
}

function setAtributsNav(imgMoreInfo, imgEdit, imgDelete){
    imgMoreInfo.setAttribute('src', iconMoreInfo);
    imgMoreInfo.setAttribute('alt', 'More actions');
    imgMoreInfo.setAttribute('title', 'More');

    imgEdit.setAttribute('src', iconEdit);
    imgEdit.setAttribute('alt', 'edit message');
    imgEdit.setAttribute('title', 'Edit');

    imgDelete.setAttribute('src', iconDelete);
    imgDelete.setAttribute('alt', 'delete message');
    imgDelete.setAttribute('title', 'Delete');

}