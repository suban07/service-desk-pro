const ticketForm = document.getElementById("ticketForm");
const messageDiv = document.getElementById("message");
const contentArea = document.getElementById("contentArea");

function showMessage(message, type) {
    messageDiv.innerHTML = message;
    messageDiv.style.display = "block";

    if (type === "success") {
        messageDiv.style.color = "green";
        messageDiv.style.backgroundColor = "#dcfce7";
    } else {
        messageDiv.style.color = "red";
        messageDiv.style.backgroundColor = "#fee2e2";
    }
}

function formatDate(dateValue) {
    if (!dateValue) {
        return "N/A";
    }

    return new Date(dateValue).toLocaleString();
}

function getStatusBadge(status) {
    return `<span class="badge status-${status}">${status}</span>`;
}

function getPriorityBadge(priority) {
    return `<span class="badge priority-${priority}">${priority}</span>`;
}

function ticketTable(tickets) {
    let rows = "";

    tickets.forEach(ticket => {
        rows += `
            <tr>
                <td>${ticket.id}</td>
                <td>${ticket.title}</td>
                <td>${ticket.description}</td>
                <td>${getPriorityBadge(ticket.priority)}</td>
                <td>${getStatusBadge(ticket.status)}</td>
                <td>${ticket.createdById}</td>
                <td>${ticket.assignedToId || "N/A"}</td>
            </tr>
        `;
    });

    return `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Customer ID</th>
                    <th>Engineer ID</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

function ticketTableWithDates(tickets) {
    let rows = "";

    tickets.forEach(ticket => {
        rows += `
            <tr>
                <td>${ticket.id}</td>
                <td>${ticket.title}</td>
                <td>${ticket.description}</td>
                <td>${getPriorityBadge(ticket.priority)}</td>
                <td>${getStatusBadge(ticket.status)}</td>
                <td>${ticket.createdById}</td>
                <td>${ticket.assignedToId || "N/A"}</td>
                <td>${formatDate(ticket.createdAt)}</td>
                <td>${formatDate(ticket.updatedAt)}</td>
            </tr>
        `;
    });

    return `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Customer ID</th>
                    <th>Engineer ID</th>
                    <th>Created At</th>
                    <th>Updated At</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

ticketForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const ticket = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        priority: document.getElementById("priority").value,
        createdById: document.getElementById("createdById").value
    };

    const response = await fetch(`${API_BASE_URL}/api/tickets`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticket)
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("Ticket created successfully", "success");
    ticketForm.reset();
});

async function showAllTickets() {
    const response = await fetch(`${API_BASE_URL}/api/tickets`);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const tickets = await response.json();

    contentArea.innerHTML = `
        <h2 style="text-align:center;">All Tickets</h2>
        ${ticketTable(tickets)}
    `;
}

function showAssignForm() {
    contentArea.innerHTML = `
        <form id="assignTicketForm">
            <h3>Assign Ticket</h3>

            <input type="number" id="assignTicketId" placeholder="Ticket ID" required>
            <input type="number" id="engineerId" placeholder="Support Engineer ID" required>

            <button type="submit" class="btn-assign">Assign Ticket</button>
        </form>
    `;

    document.getElementById("assignTicketForm")
        .addEventListener("submit", assignTicket);
}

async function assignTicket(event) {
    event.preventDefault();

    const ticketId = document.getElementById("assignTicketId").value;
    const engineerId = document.getElementById("engineerId").value;

    const response = await fetch(
        `${API_BASE_URL}/api/tickets/${ticketId}/assign/${engineerId}`,
        {
            method: "PUT"
        }
    );

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("Ticket assigned successfully", "success");
    showAllTickets();
}

function showStatusForm() {
    contentArea.innerHTML = `
        <form id="statusTicketForm">
            <h3>Update Ticket Status</h3>

            <input type="number" id="statusTicketId" placeholder="Ticket ID" required>
            <input type="number" id="updatedById" placeholder="Assigned Engineer ID" required>

            <select id="status" required>
                <option value="" selected disabled>Select Status</option>
                <option value="ASSIGNED">ASSIGNED</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="RESOLVED">RESOLVED</option>
                <option value="CLOSED">CLOSED</option>
            </select>

            <button type="submit" class="btn-update">Update Status</button>
        </form>
    `;

    document.getElementById("statusTicketForm")
        .addEventListener("submit", updateTicketStatus);
}

async function updateTicketStatus(event) {
    event.preventDefault();

    const ticketId = document.getElementById("statusTicketId").value;

    const request = {
        status: document.getElementById("status").value,
        updatedById: document.getElementById("updatedById").value
    };

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/status`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("Ticket status updated successfully", "success");
    showAllTickets();
}

function showCloseForm() {
    contentArea.innerHTML = `
        <form id="closeTicketForm">
            <h3>Close Ticket</h3>

            <input type="number" id="closeTicketId" placeholder="Ticket ID" required>

           <button type="submit" class="btn-close">Close Ticket</button>
        </form>
    `;

    document.getElementById("closeTicketForm")
        .addEventListener("submit", closeTicket);
}

async function closeTicket(event) {
    event.preventDefault();

    const ticketId = document.getElementById("closeTicketId").value;

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/close`, {
        method: "PUT"
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("Ticket closed successfully", "success");
    showAllTickets();
}

function showFilterOptions() {
    contentArea.innerHTML = `
        <form id="filterForm">
            <h3>Filter Tickets</h3>

            <select id="filterType" required>
                <option value="" selected disabled>Select Filter Type</option>
                <option value="status">Status</option>
                <option value="priority">Priority</option>
                <option value="assigned">Assigned Engineer ID</option>
                <option value="customer">Customer ID</option>
            </select>

            <div id="filterValueContainer">
                <input type="text"
                       id="filterValue"
                       placeholder="Enter filter value"
                       required>
            </div>

          <button type="submit" class="btn-view">Apply Filter</button>
        </form>
    `;

    document.getElementById("filterForm")
        .addEventListener("submit", filterTickets);
}

document.addEventListener("change", function (event) {
    if (event.target.id !== "filterType") {
        return;
    }

    const filterType = event.target.value;
    const container = document.getElementById("filterValueContainer");

    if (!container) {
        return;
    }

    if (filterType === "status") {
        container.innerHTML = `
            <select id="filterValue" required>
                <option value="" selected disabled>Select Status</option>
                <option value="OPEN">OPEN</option>
                <option value="ASSIGNED">ASSIGNED</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="RESOLVED">RESOLVED</option>
                <option value="CLOSED">CLOSED</option>
            </select>
        `;
    } else if (filterType === "priority") {
        container.innerHTML = `
            <select id="filterValue" required>
                <option value="" selected disabled>Select Priority</option>
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
            </select>
        `;
    } else {
        container.innerHTML = `
            <input type="number"
                   id="filterValue"
                   placeholder="Enter ID"
                   required>
        `;
    }
});

async function filterTickets(event) {
    event.preventDefault();

    const filterType = document.getElementById("filterType").value;
    const filterValue = document.getElementById("filterValue").value;

    let url = "";

    if (filterType === "status") {
        url = `${API_BASE_URL}/api/tickets/status/${filterValue}`;
    } else if (filterType === "priority") {
        url = `${API_BASE_URL}/api/tickets/priority/${filterValue}`;
    } else if (filterType === "assigned") {
        url = `${API_BASE_URL}/api/tickets/assigned/${filterValue}`;
    } else if (filterType === "customer") {
        url = `${API_BASE_URL}/api/tickets/customer/${filterValue}`;
    }

    const response = await fetch(url);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const tickets = await response.json();

    contentArea.innerHTML = `
        <h2 style="text-align:center;">Filtered Tickets</h2>
        ${ticketTable(tickets)}
    `;
}

function showPaginationForm() {
    contentArea.innerHTML = `
        <form id="paginationForm">
            <h3>Pagination & Sorting</h3>

            <input type="number"
                   id="page"
                   placeholder="Page Number"
                   min="0"
                   required>

            <input type="number"
                   id="size"
                   placeholder="Page Size"
                   min="1"
                   required>

            <select id="sortBy">
                <option value="">No Sorting</option>
                <option value="id">ID</option>
                <option value="title">Title</option>
                <option value="createdAt">Created At</option>
                <option value="updatedAt">Updated At</option>
            </select>

            <select id="direction">
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>

            <button type="submit" class="btn-view">Get Tickets</button>
        </form>
    `;

    document.getElementById("paginationForm")
        .addEventListener("submit", getPaginatedTickets);
}

async function getPaginatedTickets(event) {
    event.preventDefault();

    const page = document.getElementById("page").value - 1;
    const size = document.getElementById("size").value;
    const sortBy = document.getElementById("sortBy").value;
    const direction = document.getElementById("direction").value;

    let url = "";

    if (sortBy === "") {
        url = `${API_BASE_URL}/api/tickets/page?page=${page}&size=${size}`;
    } else {
        url = `${API_BASE_URL}/api/tickets/page-sort?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`;
    }

    const response = await fetch(url);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const data = await response.json();

    const sortLabel = sortBy === "" ? "No Sorting" : sortBy;
    const directionLabel = sortBy === "" ? "N/A" : direction.toUpperCase();

    contentArea.innerHTML = `
        <h2 style="text-align:center;">Paginated Tickets</h2>

        <p style="text-align:center;">
            Page ${data.number + 1} of ${data.totalPages}
        </p>

        <p style="text-align:center;">
            Total Tickets: ${data.totalElements}
        </p>

        <p style="text-align:center;">
            Sorted By: ${sortLabel} | Direction: ${directionLabel}
        </p>

        ${ticketTableWithDates(data.content)}
    `;
}