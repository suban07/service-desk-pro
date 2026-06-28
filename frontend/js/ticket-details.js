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

function showTicketByIdForm() {
    contentArea.innerHTML = `
        <form id="ticketByIdForm">
            <h3>Get Ticket By ID</h3>

            <input type="number"
                   id="ticketId"
                   placeholder="Ticket ID"
                   required>
        <button type="submit" class="btn-view">Get Ticket</button>
        </form>
    `;

    document.getElementById("ticketByIdForm")
        .addEventListener("submit", getTicketById);
}

showTicketByIdForm();

async function getTicketById(event) {
    event.preventDefault();

    const ticketId = document.getElementById("ticketId").value;

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}`);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const ticket = await response.json();

    contentArea.innerHTML = `
        <div class="details-card">
            <h3>Ticket Details</h3>

            <p><strong>ID:</strong> ${ticket.id}</p>
            <p><strong>Title:</strong> ${ticket.title}</p>
            <p><strong>Description:</strong> ${ticket.description}</p>
            <p><strong>Priority:</strong> ${ticket.priority}</p>
            <p><strong>Status:</strong> ${ticket.status}</p>
            <p><strong>Created At:</strong> ${formatDate(ticket.createdAt)}</p>
            <p><strong>Updated At:</strong> ${formatDate(ticket.updatedAt)}</p>
            <p><strong>Customer ID:</strong> ${ticket.createdById}</p>
            <p><strong>Customer:</strong> ${ticket.createdByName}</p>
            <p><strong>Engineer ID:</strong> ${ticket.assignedToId || "N/A"}</p>
            <p><strong>Engineer:</strong> ${ticket.assignedToName || "Not Assigned"}</p>
        </div>
    `;
}

function showAddCommentForm() {
    contentArea.innerHTML = `
        <form id="addCommentForm">
            <h3>Add Comment</h3>

            <input type="number"
                   id="commentTicketId"
                   placeholder="Ticket ID"
                   required>

            <input type="number"
                   id="commentedById"
                   placeholder="Commented By User ID"
                   required>

            <input type="text"
                   id="commentMessage"
                   placeholder="Comment Message"
                   required>

            <button type="submit" class="btn-update">Add Comment</button>
        </form>
    `;

    document.getElementById("addCommentForm")
        .addEventListener("submit", addComment);
}

async function addComment(event) {
    event.preventDefault();

    const ticketId = document.getElementById("commentTicketId").value;

    const request = {
        message: document.getElementById("commentMessage").value,
        commentedById: document.getElementById("commentedById").value
    };

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/comments`, {
        method: "POST",
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

    showMessage("Comment added successfully", "success");
}

function showCommentsForm() {
    contentArea.innerHTML = `
        <form id="commentsForm">
            <h3>Get Comments By Ticket</h3>

            <input type="number"
                   id="commentsTicketId"
                   placeholder="Ticket ID"
                   required>

            <button type="submit" class="btn-view">Get Comments</button>
        </form>
    `;

    document.getElementById("commentsForm")
        .addEventListener("submit", getCommentsByTicket);
}

async function getCommentsByTicket(event) {
    event.preventDefault();

    const ticketId = document.getElementById("commentsTicketId").value;

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/comments`);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const comments = await response.json();

    let rows = "";

    comments.forEach(comment => {
        rows += `
            <tr>
                <td>${comment.id}</td>
                <td>${comment.message}</td>
                <td>${comment.commentedById}</td>
                <td>${comment.commentedByName}</td>
                <td>${formatDate(comment.createdAt)}</td>
            </tr>
        `;
    });

    contentArea.innerHTML = `
        <h2 style="text-align:center;">Comments</h2>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Message</th>
                    <th>User ID</th>
                    <th>User</th>
                    <th>Created At</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

function showAddResolutionForm() {
    contentArea.innerHTML = `
        <form id="addResolutionForm">
            <h3>Add Resolution</h3>

            <input type="number"
                   id="resolutionTicketId"
                   placeholder="Ticket ID"
                   required>

            <input type="text"
                   id="rootCause"
                   placeholder="Root Cause"
                   required>

            <input type="text"
                   id="solution"
                   placeholder="Solution"
                   required>

            <input type="number"
                   id="resolvedById"
                   placeholder="Resolved By Engineer ID"
                   required>

            <button type="submit" class="btn-success">Add Resolution</button>
        </form>
    `;

    document.getElementById("addResolutionForm")
        .addEventListener("submit", addResolution);
}

async function addResolution(event) {
    event.preventDefault();

    const ticketId = document.getElementById("resolutionTicketId").value;

    const request = {
        rootCause: document.getElementById("rootCause").value,
        solution: document.getElementById("solution").value,
        resolvedById: document.getElementById("resolvedById").value
    };

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/resolution`, {
        method: "POST",
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

    showMessage("Resolution added successfully", "success");
}

function showResolutionForm() {
    contentArea.innerHTML = `
        <form id="getResolutionForm">
            <h3>Get Resolution By Ticket</h3>

            <input type="number"
                   id="getResolutionTicketId"
                   placeholder="Ticket ID"
                   required>

                 <button type="submit" class="btn-view">Get Resolution</button>
        </form>
    `;

    document.getElementById("getResolutionForm")
        .addEventListener("submit", getResolutionByTicket);
}

async function getResolutionByTicket(event) {
    event.preventDefault();

    const ticketId = document.getElementById("getResolutionTicketId").value;

    const response = await fetch(`${API_BASE_URL}/api/tickets/${ticketId}/resolution`);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const resolution = await response.json();

    contentArea.innerHTML = `
        <div class="details-card">
            <h3>Resolution Details</h3>

            <p><strong>ID:</strong> ${resolution.id}</p>
            <p><strong>Ticket ID:</strong> ${resolution.ticketId}</p>
            <p><strong>Root Cause:</strong> ${resolution.rootCause}</p>
            <p><strong>Solution:</strong> ${resolution.solution}</p>
            <p><strong>Resolved By ID:</strong> ${resolution.resolvedById}</p>
            <p><strong>Resolved By:</strong> ${resolution.resolvedByName}</p>
            <p><strong>Resolved At:</strong> ${formatDate(resolution.resolvedAt)}</p>
        </div>
    `;
}

