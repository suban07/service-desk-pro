const totalUsersCount = document.getElementById("totalUsersCount");
const totalTicketsCount = document.getElementById("totalTicketsCount");
const openTicketsCount = document.getElementById("openTicketsCount");
const resolvedTicketsCount = document.getElementById("resolvedTicketsCount");

async function loadDashboardStats() {
    try {
        const usersResponse = await fetch(`${API_BASE_URL}/api/users`);
        const ticketsResponse = await fetch(`${API_BASE_URL}/api/tickets`);

        const users = await usersResponse.json();
        const tickets = await ticketsResponse.json();

        totalUsersCount.innerText = users.length;
        totalTicketsCount.innerText = tickets.length;

        openTicketsCount.innerText = tickets.filter(ticket =>
            ticket.status === "OPEN"
        ).length;

        resolvedTicketsCount.innerText = tickets.filter(ticket =>
            ticket.status === "RESOLVED"
        ).length;

    } catch (error) {
        totalUsersCount.innerText = "0";
        totalTicketsCount.innerText = "0";
        openTicketsCount.innerText = "0";
        resolvedTicketsCount.innerText = "0";
    }
}

loadDashboardStats();