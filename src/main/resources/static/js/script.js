function buildTaskStatusUrl(status) {
    var baseUrl = /*[[@{/task-by-status}]]*/ '';
    var urlWithParam = baseUrl + '?status=' + status;
    return urlWithParam;
}

function loadTaskByStatus(status) {
    var url = buildTaskStatusUrl(status);
    var target = '#task-container';

    hx.get(url, { 'hx-swap': 'outerHTML', 'hx-target': target });
}