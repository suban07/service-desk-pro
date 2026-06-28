const userForm = document.getElementById("userForm");
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

userForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        role: document.getElementById("role").value
    };

    const response = await fetch(`${API_BASE_URL}/api/users`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("User created successfully", "success");
    userForm.reset();
});

async function showAllUsers() {
    contentArea.innerHTML = `
        <h2 style="text-align:center;">All Users</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody id="userTableBody"></tbody>
        </table>
    `;

    const response = await fetch(`${API_BASE_URL}/api/users`);
    const users = await response.json();

    const userTableBody = document.getElementById("userTableBody");
    userTableBody.innerHTML = "";

    users.forEach(user => {
        userTableBody.innerHTML += `
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
            </tr>
        `;
    });
}

function showUpdateForm() {
    contentArea.innerHTML = `
        <form id="updateUserForm">
            <h3>Update User</h3>

            <input type="number" id="updateUserId" placeholder="User ID" required>
            <input type="text" id="updateName" placeholder="New Name" required>
            <input type="email" id="updateEmail" placeholder="New Email" required>

            <select id="updateRole">
                <option value="CUSTOMER">CUSTOMER</option>
                <option value="SUPPORT_ENGINEER">SUPPORT_ENGINEER</option>
                <option value="MANAGER">MANAGER</option>
            </select>

            <button type="submit" class="btn-update">
            Update User
            </button>
        </form>
    `;

    document.getElementById("updateUserForm")
        .addEventListener("submit", updateUser);
}

async function updateUser(event) {
    event.preventDefault();

    const userId = document.getElementById("updateUserId").value;

    const updatedUser = {
        name: document.getElementById("updateName").value,
        email: document.getElementById("updateEmail").value,
        role: document.getElementById("updateRole").value
    };

    const response = await fetch(`${API_BASE_URL}/api/users/${userId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(updatedUser)
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    showMessage("User updated successfully", "success");
    showAllUsers();
}

function showDeleteForm() {
    contentArea.innerHTML = `
        <form id="deleteUserForm">
            <h3>Delete User</h3>

            <input type="number" id="deleteUserId" placeholder="User ID" required>

            <button type="submit" class="btn-delete">
            Delete User
            </button>
        </form>
    `;

    document.getElementById("deleteUserForm")
        .addEventListener("submit", deleteUser);
}

async function deleteUser(event) {
    event.preventDefault();

    const userId = document.getElementById("deleteUserId").value;

    const response = await fetch(`${API_BASE_URL}/api/users/${userId}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const message = await response.text();
    showMessage(message, "success");
    contentArea.innerHTML = "";
}

function showUserByIdForm() {
    contentArea.innerHTML = `
        <form id="getUserByIdForm">
            <h3>Get User By ID</h3>

            <input type="number"
                   id="getUserId"
                   placeholder="User ID"
                   required>

            <button type="submit" class="btn-view">
                Get User
            </button>
        </form>
    `;

    document.getElementById("getUserByIdForm")
        .addEventListener("submit", getUserById);
}

async function getUserById(event) {
    event.preventDefault();

    const userId = document.getElementById("getUserId").value;

    const response = await fetch(`${API_BASE_URL}/api/users/${userId}`);

    if (!response.ok) {
        const error = await response.text();
        showMessage(error, "error");
        return;
    }

    const user = await response.json();

    contentArea.innerHTML = `
        <h2 style="text-align:center;">User Details</h2>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>

            <tbody>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                </tr>
            </tbody>
        </table>
    `;
}