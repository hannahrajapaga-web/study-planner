let user = JSON.parse(localStorage.getItem("user"));

if(!user){
window.location = "index.html";
}

document.getElementById("profileName").innerText = user.name;
document.getElementById("profileEmail").innerText = user.email;

const API = "http://localhost:9090/tasks";

fetch(API + "/" + user.id)
.then(res => res.json())
.then(tasks => {

let total = tasks.length;
let completed = tasks.filter(t => t.completed).length;
let pending = total - completed;

document.getElementById("totalTasks").innerText = total;
document.getElementById("completedTasks").innerText = completed;
document.getElementById("pendingTasks").innerText = pending;

});

function logout(){
localStorage.removeItem("user");
window.location = "index.html";
}
function goPlanner(){
window.location = "dashboard.html";
}

function goDashboard(){
window.location = "main-dashboard.html";
}

function logout(){
localStorage.removeItem("user");
window.location = "index.html";
}