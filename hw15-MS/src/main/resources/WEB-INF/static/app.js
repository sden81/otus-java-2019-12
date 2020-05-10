let stompClient = null;
let receivedUsersList = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('end-point'));
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/response', (greeting) => showGreeting(JSON.parse(greeting.body).messageStr));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

const showGreeting = (messageStr) => {
    receivedUsersList = JSON.parse(messageStr);
    let allUsersTable = showAllUsers(receivedUsersList);
    $("#allUsersResult").html('').append(allUsersTable);
    console.log(receivedUsersList);
}


const showAllUsers = function (allUsers) {
    if (!Array.isArray(allUsers.value) || !allUsers.value.length) {
        return "Empty result";
    }

    let content = '';
    Array.prototype.forEach.call(allUsers.value, function (user) {
        content += '<tr>\n' +
            '<td>' + user.id + '</td>\n' +
            '<td>' + user.name + '</td>\n' +
            '<td>' + user.login + '</td>\n' +
            '<td>' + user.password + '</td>\n' +
            '<td><a href="user-api?id=' + user.id + '">get json</a></td>\n' +
            '</tr>';
    });

    return '<table>\n' +
        '    <thead>\n' +
        '    <tr>\n' +
        '        <th>ID</th>\n' +
        '        <th>Name</th>\n' +
        '        <th>Login</th>\n' +
        '        <th>Password</th>\n' +
        '        <th>Json</th>\n' +
        '    </tr>\n' +
        '    </thead>\n' +
        '    <tbody>\n' +
        content +
        '    </tbody>\n' +
        '</table>';
}

const getAllUsers = () => {
    stompClient.send("/app/message.getAllUsers", {}, JSON.stringify({'messageStr': 111}));
}

function createUser() {
    const userName = document.getElementById('createUserName');
    const userLogin = document.getElementById('createUserLogin');
    const userPassword = document.getElementById('createUserPassword');

    if (!userName.value || !userLogin.value || !userPassword.value) {
        alert("Заполнены не все поля");
    }

    let user = {
        name: userName.value,
        login: userLogin.value,
        password: userPassword.value
    };

    stompClient.send("/app/message.addNewUser", {}, JSON.stringify({'messageStr': btoa(JSON.stringify(user))}));
}

$('document').ready(function () {
    connect();
    setTimeout(() => {
        getAllUsers();
    }, 500);
});