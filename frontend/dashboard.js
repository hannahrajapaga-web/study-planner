let user = JSON.parse(localStorage.getItem("user"));
if(localStorage.getItem("darkMode") === "true"){
document.body.classList.add("dark");
}

if(!user){
    window.location = "index.html";
}

document.getElementById("welcome").innerText = "👋 Welcome, " + user.name;

const API = "https://study-planner-467z.onrender.com/tasks";
// LOAD DASHBOARD DATA
function loadDashboard(){

    fetch(API + "/" + user.id)
    .then(res => res.json())
    .then(data => {

        let total = data.length;
        let completed = data.filter(t => t.completed).length;
        let pending = total - completed;

        document.getElementById("total").innerText = total;
        document.getElementById("completed").innerText = completed;
        document.getElementById("pending").innerText = pending;

        // progress
        let percent = total ? (completed/total)*100 : 0;
        document.getElementById("progressFill").style.width = percent + "%";
        document.getElementById("progressText").innerText =
            `Completed ${completed} of ${total}`;

        // TODAY TASKS
        let todayDiv = document.getElementById("todayTasks");
        todayDiv.innerHTML = "";

        let today = new Date().toISOString().split("T")[0];

        let todayTasks = data.filter(t => t.deadline === today);

        if(todayTasks.length === 0){
            todayDiv.innerHTML = "<p>No tasks today 🎉</p>";
        }

        todayTasks.forEach(t=>{
            let div = document.createElement("div");
            div.className = "taskItem";
            div.innerHTML = `
                ${t.subject} - ${t.task}
                ${t.completed ? "✅" : ""}
            `;
            todayDiv.appendChild(div);
        });

    });
}

// NAVIGATION
function goPlanner(){
    window.location = "dashboard.html";
}

function logout(){
    localStorage.removeItem("user");
    window.location = "index.html";
}
function goProfile(){
window.location = "profile.html";
}

window.onload = loadDashboard;
function toggleDarkMode(){

document.body.classList.toggle("dark");

if(document.body.classList.contains("dark")){
localStorage.setItem("darkMode","true");
}
else{
localStorage.setItem("darkMode","false");
}

}