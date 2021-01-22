function deleteRow() {
    var accountIdToDelete = prompt("Введите ID записи, которую нужно удалить.");
    // строка с параметрами для отправки
    var parameters = "accountId=" + accountIdToDelete;

    var request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8081/PSUTI_JAVA_WEB_APP/DeleteAccountFromDatabase");
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    request.send(parameters);
    location.reload()
}